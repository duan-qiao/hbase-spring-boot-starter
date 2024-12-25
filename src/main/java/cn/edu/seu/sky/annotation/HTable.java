package cn.edu.seu.sky.annotation;

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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTable {
    /**
     * 命名空间
     */
    String namespace() default "";

    /**
     * hbase表名
     */
    String name() default "";

    /**
     * 列属性
     */
    String family() default "default";
}
