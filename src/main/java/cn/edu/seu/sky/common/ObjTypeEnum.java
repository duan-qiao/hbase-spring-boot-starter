package cn.edu.seu.sky.common;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaotian on 2022/4/17
 */
@Getter
@AllArgsConstructor
public enum ObjTypeEnum {
    /**
     * 数据类型枚举
     */
    UNKNOWN(Lists.newArrayList("unknown")) {

        @Override
        Object convert(String value) {
            return null;
        }
    },
    BYTE(Lists.newArrayList("java.lang.Byte", "byte")) {

        @Override
        Object convert(String value) {
            return Byte.valueOf(value);
        }
    },
    SHORT(Lists.newArrayList("java.lang.Short", "short")) {

        @Override
        Object convert(String value) {
            return Short.valueOf(value);
        }
    },
    INTEGER(Lists.newArrayList("java.lang.Integer", "int")) {

        @Override
        Object convert(String value) {
            return Integer.valueOf(value);
        }
    },
    DOUBLE(Lists.newArrayList("java.lang.Double", "double")) {

        @Override
        Object convert(String value) {
            return Double.valueOf(value);
        }
    },
    FLOAT(Lists.newArrayList("java.lang.Float", "float")) {

        @Override
        Object convert(String value) {
            return Float.valueOf(value);
        }
    },
    LONG(Lists.newArrayList("java.lang.Long", "long")) {

        @Override
        Object convert(String value) {
            return Long.valueOf(value);
        }
    },
    STRING(Lists.newArrayList("java.lang.String")) {

        @Override
        Object convert(String value) {
            return value;
        }
    },
    BOOLEAN(Lists.newArrayList("java.lang.Boolean", "boolean")) {

        @Override
        Object convert(String value) {
            return Boolean.valueOf(value);
        }
    };

    private final List<String> names;

    abstract Object convert(String value);

    public static Object typeConvert(Type type, String value) {
        return Arrays.stream(ObjTypeEnum.values())
                .filter(x -> x.getNames().contains(type.getTypeName()))
                .findFirst()
                .orElse(UNKNOWN)
                .convert(value);
    }
}
