package cn.edu.seu.sky.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaotian.zhou
 * @date 2020/11/5 11:32
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HConfig {
    /**
     * 配置名
     */
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
