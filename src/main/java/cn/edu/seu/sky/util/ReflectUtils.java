package cn.edu.seu.sky.util;

import cn.edu.seu.sky.exception.HbaseException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaotian on 2022/4/16
 */
public final class ReflectUtils {

    public static Object invokeGetMethod(Object object, String field) {
        try {
            String method = "get" + StringUtils.capitalize(field);
            return object.getClass().getMethod(method).invoke(object);

        } catch (Exception e) {
            throw new HbaseException("invokeGetMethod", e);
        }
    }

    public static List<Field> getAllClassFields(Class<?> clazz) {
        List<Field> fieldList = Lists.newArrayList();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }
}
