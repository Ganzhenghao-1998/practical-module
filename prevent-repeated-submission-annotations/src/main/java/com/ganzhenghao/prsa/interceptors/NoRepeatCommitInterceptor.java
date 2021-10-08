package com.ganzhenghao.prsa.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganzhenghao.prsa.annotation.NoRepeatCommit;
import com.ganzhenghao.prsa.config.NoRepeatCommitConfig;
import com.ganzhenghao.prsa.service.CacheService;
import com.ganzhenghao.prsa.service.impl.ConcurrentHashMapCacheImpl;
import com.ganzhenghao.prsa.service.impl.HutoolCacheImpl;
import com.ganzhenghao.prsa.util.CacheKeyThreadLocal;
import com.ganzhenghao.prsa.util.CacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 防重复提交的SpringMVC拦截器
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/8/24 11:57
 */
@ControllerAdvice
public class NoRepeatCommitInterceptor implements HandlerInterceptor {


    @Autowired(required = false)
    CacheService concurrentHashMapCache;

    @Autowired(required = false)
    private CacheService redisCache;

    @Autowired(required = false)
    private CacheService hutoolCache;

    @Autowired
    private ObjectMapper json;

    @Autowired
    private NoRepeatCommitConfig noRepeatCommitConfig;
    /**
     * 默认的注解 该对象的属性 全是注解默认值
     */
    @Autowired
    private NoRepeatCommit noRepeatCommit;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /*    private Logger logger = LoggerFactory.getLogger(this.getClass());*/


    /**
     * 拦截前端请求,获取id
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理程序
     * @return boolean
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        // 判断是否为HandlerMethod 不是则放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod methodHandler = (HandlerMethod) handler;


        switch (noRepeatCommitConfig.getNoRepeatCommitType()) {
            case Redis:
                return cacheImp(methodHandler, request, response, redisCache);
            case Internal_Hutool:
                return cacheImp(methodHandler, request, response, hutoolCache);
            case Internal_ConcurrentHashMap:
                return cacheImp(methodHandler, request, response, concurrentHashMapCache);

            default:
                break;
        }

        return true;
    }

    /**
     * 请求完成后,执行相关逻辑
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理程序
     * @param ex       异常
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // 只有当注解有@NoRepaetCommit时,并且还得判断是否是使用的缓存机制,加锁机制则不删缓存,才删除键值
        if (handler instanceof HandlerMethod) {
            HandlerMethod methodHandler = (HandlerMethod) handler;

            boolean result = methodHandler.hasMethodAnnotation(NoRepeatCommit.class);
            //如果没有 @NoRepeatCommit ,则不进行任何处理
            if (!result) {
                return;
            }

            switch (noRepeatCommitConfig.getNoRepeatCommitType()) {
                case Redis:
                    redisTemplate.delete(CacheKeyThreadLocal.get());
                    CacheKeyThreadLocal.remove();
                    break;
                case Internal_Hutool:
                    HutoolCacheImpl hutoolCacheImpl = (HutoolCacheImpl) this.hutoolCache;
                    hutoolCacheImpl.getTimedCache().remove(CacheKeyThreadLocal.get());
                    CacheKeyThreadLocal.remove();
                    break;
                case Internal_ConcurrentHashMap:
                    ConcurrentHashMapCacheImpl concurrentHashMapCacheImpl = (ConcurrentHashMapCacheImpl) this.concurrentHashMapCache;
                    concurrentHashMapCacheImpl.getCacheMap().remove(CacheKeyThreadLocal.get());
                    CacheKeyThreadLocal.remove();
                    break;
                default:
                    break;

            }


        }


    }

    /**
     * 封装响应并返回结果
     *
     * @param response 响应
     * @param status   状态
     * @param msg      消息
     * @throws IOException ioexception
     */
    private void encapsulateTheResponseResultAndReturn(HttpServletResponse response, Integer status, String msg) throws IOException {
        response.setStatus(status);
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("status", status);
        resultMap.put("message", msg);
        response.getWriter().write(json.writeValueAsString(resultMap));
    }

    /**
     * 缓存实现
     *
     * @param methodHandler 方法处理程序
     * @param request       请求
     * @param response      响应
     * @param cacheService  缓存服务
     * @return {@link Boolean}
     * @throws Exception 异常
     */
    private Boolean cacheImp(HandlerMethod methodHandler,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             CacheService cacheService) throws Exception {
        // 判断是否拥有@NoRepeatCommit注解 没有则放行,有则执行逻辑
        if (methodHandler.hasMethodAnnotation(NoRepeatCommit.class)) {

            NoRepeatCommit anno = methodHandler.getMethodAnnotation(NoRepeatCommit.class);
            //判断获取到的注解是否为空,为空则放行
            if (anno == null) {
                return true;
            }

            //以下五个 获取到的是全局值
            //获取请求头和返回的状态码
            String headerName = noRepeatCommitConfig.getHeaderName();
            Integer status = noRepeatCommitConfig.getStatus();
            //获取cacheKey前缀
            String cacheKeyPrefix = noRepeatCommitConfig.getCacheKeyPrefix();

            //获取到定义时间值 单位:分钟
            Long expireTime = noRepeatCommitConfig.getExpireTime();
            //获取时间单位
            TimeUnit timeUnit = noRepeatCommitConfig.getTimeUnit();

            // 局部header和默认header头不同,以局部header头为准
            // (如果局部的注解上的值和默认的值不同,那么使用局部的值,否则使用全局的值)
            if (!noRepeatCommit.headerName().equals(anno.headerName())) {
                headerName = anno.headerName();
            }

            //  局部status和默认status不同时 以局部status为准,否则使用全局的值
            if (noRepeatCommit.status() != anno.status()) {
                status = anno.status();
            }

            // 局部redisKeyPrefix和默认不同时,以局部为准,否则使用全局的值
            if (!noRepeatCommit.cacheKeyPrefix().equals(anno.cacheKeyPrefix())) {
                cacheKeyPrefix = anno.cacheKeyPrefix();
            }

            // 局部过期时间和默认不同时,以局部为准,否则使用全局的值
            if (noRepeatCommit.expireTime() != anno.expireTime()) {
                expireTime = anno.expireTime();
            }

            // 局部过期时间单位和默认不同时,以局部为准,否则使用全局的值
            if (noRepeatCommit.timeUnit() != anno.timeUnit()) {
                timeUnit = anno.timeUnit();
            }


            //获取请求头的id
            String id = request.getHeader(headerName);
            if (id == null || id.isEmpty()) {

/*                logger.warn("请求路径为 " + request.getRequestURI() + " 的请求头未携带" + headerName
                        + ",或者为空!");*/

                //如果请求被拦截 则返回状态码
                encapsulateTheResponseResultAndReturn(response, status, "The request header must carry " + headerName + " and not empty");

                return false;
            }


            // true --> 设置id成功  false --> 设置失败
            boolean result = cacheService.cache(id, cacheKeyPrefix, expireTime, timeUnit);

            if (result) {
                // 设置成功时,将cacheKey存入ThreadLocal中,最后在请求完成后删除该ThreadLocal
                String cacheKey = CacheKeyUtil.getCacheKey(cacheKeyPrefix, id);
                CacheKeyThreadLocal.set(cacheKey);

            } else {
                //如果请求被拦截 则返回状态码
                encapsulateTheResponseResultAndReturn(response, status, "The request was submitted repeatedly!");
            }
            return result;
        }
        return true;
    }


}
