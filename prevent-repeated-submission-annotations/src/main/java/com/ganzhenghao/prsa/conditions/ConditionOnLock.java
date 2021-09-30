package com.ganzhenghao.prsa.conditions;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ganzhenghao.prsa.annotation.ConditionalOnLock;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

/**
 * 自定义装载类,当使用的是锁类型的防重复提交方式时,才加载
 *
 * @author Ganzhenghao
 * @version 1.0
 * @date 2021/9/30 11:46
 */
public class ConditionOnLock implements Condition {


    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        MultiValueMap<String, Object> annotationAttributes = metadata.getAllAnnotationAttributes(ConditionalOnLock.class.getName());


        String prefix = "";
        if (ObjectUtil.isNotEmpty(annotationAttributes.get("prefix"))) {
            prefix = (String) annotationAttributes.get("prefix").get(0);
        }

        String name = "";
        if (ObjectUtil.isNotEmpty(annotationAttributes.get("name"))) {
            name = (String) annotationAttributes.get("name").get(0);
        }

        String[] havingValue = {};
        if (ObjectUtil.isNotEmpty(annotationAttributes.get("havingValue"))) {
            havingValue = (String[]) annotationAttributes.get("havingValue").get(0);
        }

        // 如果属性值不存在 是否加载
        boolean matchIfMissing = false;
        if (ObjectUtil.isNotEmpty(annotationAttributes.get("matchIfMissing"))) {
            matchIfMissing = (boolean) annotationAttributes.get("matchIfMissing").get(0);
        }

        String property = context.getEnvironment().getProperty(prefix + "." + name);

        if (StrUtil.isEmpty(property)) {
            // 当属性值不存在时 是否加载
            return matchIfMissing;
        }

        for (String value : havingValue) {
            if (StrUtil.equalsIgnoreCase(value, property)) {
                return true;
            }
        }


        return false;
    }


}
