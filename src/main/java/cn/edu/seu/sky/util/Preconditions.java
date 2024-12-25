package cn.edu.seu.sky.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author xiaotian on 2024/11/24
 */
public final class Preconditions {

    public static <T> void checkNotNull(T object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkNotBlank(String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkNotEmpty(List<?> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            throw new IllegalArgumentException();
        }
    }
}
