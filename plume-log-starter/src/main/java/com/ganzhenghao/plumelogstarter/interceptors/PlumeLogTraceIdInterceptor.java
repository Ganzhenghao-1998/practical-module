package com.ganzhenghao.plumelogstarter.interceptors;

import com.plumelog.core.TraceId;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/10/29 10:35
 */
@Component
public class PlumeLogTraceIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String traceId = request.getHeader("traceId");

        if (traceId != null && !traceId.equals("")) {
            TraceId.logTraceID.set(traceId);
        } else {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            traceId = uuid.substring(uuid.length() - 7);
            TraceId.logTraceID.set(traceId);

            // 通过反射设置 header的值
            Class<? extends HttpServletRequest> requestClass = request.getClass();
            try {
                // 获取字段 request
                Field requestClassDeclaredField = requestClass.getDeclaredField("request");
                requestClassDeclaredField.setAccessible(true);
                // 获取 字段 request 的值
                Object requestClassDeclaredFieldValue = requestClassDeclaredField.get(request);
                // 获取字段 coyoteRequest
                Field coyoteRequest = requestClassDeclaredFieldValue.getClass().getDeclaredField("coyoteRequest");
                coyoteRequest.setAccessible(true);
                // 获取字段 coyoteRequest的值
                Object coyoteRequestFieldValue = coyoteRequest.get(requestClassDeclaredFieldValue);
                // 获取header
                Field headers = coyoteRequestFieldValue.getClass().getDeclaredField("headers");
                headers.setAccessible(true);
                // 设置值
                MimeHeaders mimeHeaders = (MimeHeaders) headers.get(coyoteRequestFieldValue);
                mimeHeaders.removeHeader("traceId");
                mimeHeaders.addValue("traceId").setString(traceId);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return true;
    }
}
