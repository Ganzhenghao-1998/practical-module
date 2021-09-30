# 工程简介

原理:

原理一: 使用缓存机制或则数据库实现
![](https://g-picture.oss-cn-chengdu.aliyuncs.com/markdown/2021-7/1632902739031.png)
原理二: 直接加锁 通过aop 对对应请求

```pom
        <dependency>
            <groupId>com.ganzhenghao</groupId>
            <artifactId>prevent-repeated-submission-annotations</artifactId>
            <version>版本号</version>
        </dependency>
```

1. 配置文件详解
   ![](https://g-picture.oss-cn-chengdu.aliyuncs.com/markdown/2021-7/%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6.png)

```properties
# redis 默认值,使用redis实现缓存
# internal_concurrenthashmap 使用jdk的ConcurrentHashMap实现内置缓存,目前可能key过期时间会比设置的过期时间要长
# internal_hutool 使用jdk的ConcurrentHashMap实现内置缓存
# mysql todo 基于mysql的唯一索引实现
# lock todo 使用aop直接给带注解的方法加锁
# distributed_locks 加分布式锁 好看的
# distributed_locks_redis 基于redis实现加分布式锁
no.repeat.commit.no-repeat-commit-type=redis
# 是否开启id生成的Controller 默认为false
# 如果为true,则会开启一个控制器 请求路径为 /id/getId ,请求该路径会返回一个由雪花算法生成的分布式id
no.repeat.commit.open-id-controller=false
# 设置全局返回值 如果请求被拦截 那么会返回
# {"status" : 505, "message" : "拦截信息"} 
# 也会更改整体http的响应值status,把200改为505
# 默认值为505
# 可以通过设置注解对应的值,来使用局部的变量
no.repeat.commit.status=505
# 全局请求头 默认为 noRepeatId, 
# 前端请求必须携带对应的header头来进行请求
# 可以通过设置注解对应的值,来使用局部的变量
no.repeat.commit.header-name=noRepeatId
# 全局缓存key的前缀,存储时key的结构为 :
#       前缀:前端传入的防重复提交id
# 默认值为 NoRepeatCommit
# 可以通过设置注解对应的值,来使用局部的变量
no.repeat.commit.cache-key-prefix=NoRepeatCommit
# 可以通过设置注解对应的值,来使用局部的变量
# 每个缓存的键值存在的时间 默认为5
# 可以通过设置注解对应的值,来使用局部的变量
no.repeat.commit.expire-time=5
# 每个缓存的键值存在的时间单位 默认为 分钟
# 可以通过设置注解对应的值,来使用局部的变量
no.repeat.commit.time-unit=minutes
# 基于内置的缓存机制时
# 设置清理缓存的定时任务的时间
no.repeat.commit.clear-cache-time-interval=5
# 基于内置的缓存机制时
# 置清理缓存的定时任务的时间单位 默认为 分钟
no.repeat.commit.clear-cache-time-interval-time-unit=minutes




```
