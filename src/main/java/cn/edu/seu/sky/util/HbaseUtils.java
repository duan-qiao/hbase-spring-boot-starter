package cn.edu.seu.sky.util;

import cn.edu.seu.sky.annotation.HColumn;
import cn.edu.seu.sky.annotation.HTable;
import cn.edu.seu.sky.common.HbaseConstants;
import cn.edu.seu.sky.common.ObjTypeEnum;
import cn.edu.seu.sky.exception.HbaseException;
import cn.edu.seu.sky.model.HColumnInfo;
import cn.edu.seu.sky.model.HPutInfo;
import cn.edu.seu.sky.request.FiledReq;
import cn.edu.seu.sky.request.ScanReq;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiaotian on 2022/4/16
 */
public final class HbaseUtils {

    public static <T> TableName getTableName(String prefix, Class<T> clazz) {
        if (clazz.isAnnotationPresent(HTable.class)) {
            HTable annotation = clazz.getAnnotation(HTable.class);
            String tableName = StringUtils.defaultString(annotation.value(), clazz.getSimpleName());
            return TableName.valueOf(StringUtils.defaultString(prefix) + tableName);
        }
        return null;
    }

    private static HColumnInfo convert(Field field) {
        if (field.isAnnotationPresent(HColumn.class)) {
            HColumn annotation = field.getAnnotation(HColumn.class);
            byte[] family = Bytes.toBytes(StringUtils.defaultString(annotation.family(), field.getName()));
            byte[] qualifier = Bytes.toBytes(StringUtils.defaultString(annotation.qualifier(), field.getName()));
            return new HColumnInfo(family, qualifier);
        }
        return null;
    }

    public static <T> T mapRow(Result result, Class<T> clazz) {
        try {
            if (result == null || result.isEmpty()) {
                return null;
            }
            T instance = clazz.newInstance();
            List<Field> fields = ReflectUtils.getAllClassFields(clazz);
            for (Field field : fields) {
                HColumnInfo column = convert(field);
                // 字段上没有注解
                if (column == null) {
                    continue;
                }
                byte[] byteValue = result.getValue(column.getFamily(), column.getQualifier());
                // 数据库查询无值
                if (byteValue == null) {
                    continue;
                }
                Object value = ObjTypeEnum.typeConvert(field.getGenericType(), Bytes.toString(byteValue));
                // 值类型转化失败，或不支持的值类型
                if (value == null) {
                    continue;
                }
                field.setAccessible(true);
                field.set(instance, value);
            }
            return instance;

        } catch (Exception e) {
            throw new HbaseException("mapRow", e);
        }
    }

    public static <T> List<T> mapRowList(Result[] results, Class<T> clazz) {
        return Arrays.stream(results)
                .map(result -> mapRow(result, clazz))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T> Put mapPut(HPutInfo<T> putInfo) {
        try {
            Put put = new Put(Bytes.toBytes(putInfo.getRowKey()));
            List<Field> fields = ReflectUtils.getAllClassFields(putInfo.getValue().getClass());
            for (Field field : fields) {
                HColumnInfo column = convert(field);
                // 字段上没有注解
                if (column == null) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(putInfo.getValue());
                // 入参值为空
                if (value == null) {
                    continue;
                }
                byte[] byteValue = Bytes.toBytes(value.toString());
                put.addColumn(column.getFamily(), column.getQualifier(), byteValue);
            }
            return put;

        } catch (Exception e) {
            throw new HbaseException("mapPut", e);
        }
    }

    public static <T> List<Put> mapPutList(List<HPutInfo<T>> putInfoList) {
        return putInfoList.stream().map(HbaseUtils::mapPut).collect(Collectors.toList());
    }

    public static Scan buildScanReq(ScanReq scanReq) {
        Scan scan = new Scan();
        if (StringUtils.isNotBlank(scanReq.getStartRow())) {
            scan.withStartRow(scan.getStartRow());
        }
        if (StringUtils.isNotBlank(scanReq.getEndRow())) {
            scan.withStopRow(scan.getStopRow());
        }
        scan.setLimit(HbaseConstants.PAGE_SIZE_50);
        if (scanReq.getPageSize() != null) {
            scan.setLimit(scanReq.getPageSize());
        }
        if (scanReq.getReverse() != null) {
            scan.setReversed(scanReq.getReverse());
        }
        if (!CollectionUtils.isEmpty(scanReq.getFiledReqList())) {
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            for (FiledReq filedReq : scanReq.getFiledReqList()) {
                SingleColumnValueFilter filter = new SingleColumnValueFilter(
                        Bytes.toBytes(filedReq.getFamily()),
                        Bytes.toBytes(filedReq.getQualifier()),
                        filedReq.getOperator(),
                        Bytes.toBytes(filedReq.getValue().toString()));
                filter.setFilterIfMissing(true);
                filterList.addFilter(filter);
            }
            scan.setFilter(filterList);
        }
        return scan;
    }
}
