package cn.edu.seu.sky.util;

import cn.edu.seu.sky.annotation.HColumn;
import cn.edu.seu.sky.annotation.HTable;
import cn.edu.seu.sky.model.HEntity;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiaotian.zhou
 * @email xiaotian.zhou@amh-group.com
 * @date 2020/11/5 11:32
 */
public final class HbaseUtils {

    public static <T> TableName getTableName(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(HTable.class)) {
            // 如果注解不存在，取类名作为表名
            return TableName.valueOf(clazz.getSimpleName());
        }
        HTable table = clazz.getAnnotation(HTable.class);
        return TableName.valueOf(table.namespace(), table.name());
    }

    public static <T> T mapRow(Result result, Class<T> clazz) {
        if (result == null || result.isEmpty()) {
            return null;
        }
        Field[] fields = getAllClassFields(clazz);
        JSONObject object = new JSONObject();
        for (Field field : fields) {
            Column column = getHbaseColumn(clazz, field);
            byte[] byteValue = result.getValue(column.getFamily(), column.getQualifier());
            if (byteValue != null && byteValue.length > 0) {
                object.put(field.getName(), Bytes.toString(byteValue));
            }
        }
        return object.toJavaObject(clazz);
    }

    public static <T> List<T> mapRows(Result[] results, Class<T> clazz) {
        return Arrays.stream(results)
                .map(result -> mapRow(result, clazz))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T> Put mapPut(String rowKey, T value) {
        Put put = new Put(Bytes.toBytes(rowKey));
        JSONObject valueMap = JSON.parseObject(JSON.toJSONString(value));
        Field[] fields = getAllClassFields(value.getClass());
        for (Field field : fields) {
            String columnValue = valueMap.getString(field.getName());
            if (StringUtils.isEmpty(columnValue)) {
                continue;
            }
            byte[] columns = Bytes.toBytes(columnValue);
            Column column = getHbaseColumn(value.getClass(), field);
            put.addColumn(column.getFamily(), column.getQualifier(), columns);
        }
        return put;
    }

    public static <T> List<Put> mapPuts(List<HEntity<T>> entities) {
        return entities.stream().map(HbaseUtils::mapPut).collect(Collectors.toList());
    }

    public static <T> Put mapPut(HEntity<T> entity) {
        return mapPut(entity.getRowKey(), entity.getEntity());
    }

    private static Field[] getAllClassFields(Class<?> clazz) {
        List<Field> fieldList = Lists.newArrayList();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList.toArray(new Field[0]);
    }

    private static <T> Column getHbaseColumn(Class<T> clazz, Field field) {
        // 优先获取字段名上的注解的列簇名，其次获取类名注解上的列簇名，如果都没有默认用“default“
        byte[] family = Bytes.toBytes("default");
        // 优先获取字段名上的注解的列名，如果没有默认用字段名
        byte[] qualifier = Bytes.toBytes(field.getName());

        if (clazz.isAnnotationPresent(HTable.class)) {
            HTable annotation = clazz.getAnnotation(HTable.class);
            if (StringUtils.isNotEmpty(annotation.family())) {
                family = Bytes.toBytes(annotation.family());
            }
        }
        if (field.isAnnotationPresent(HColumn.class)) {
            HColumn annotation = field.getAnnotation(HColumn.class);
            if (StringUtils.isNotEmpty(annotation.family())) {
                family = Bytes.toBytes(annotation.family());
            }
            if (StringUtils.isNotEmpty(annotation.qualifier())) {
                qualifier = Bytes.toBytes(annotation.qualifier());
            }
        }
        return new Column(family, qualifier);
    }

    @Data
    @AllArgsConstructor
    public static class Column {
        /**
         * 列族名
         */
        private byte[] family;
        /**
         * 列名
         */
        private byte[] qualifier;
    }
}
