package cn.edu.seu.sky.core;

import cn.edu.seu.sky.callback.HConsumer;
import cn.edu.seu.sky.callback.HFunction;
import cn.edu.seu.sky.config.HbaseConnectionFactory;
import cn.edu.seu.sky.exception.HbaseException;
import cn.edu.seu.sky.model.HPutInfo;
import cn.edu.seu.sky.request.ScanReq;
import cn.edu.seu.sky.util.HbaseUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaotian on 2022/4/16
 */
@Component
public class HbaseTemplate implements HbaseOperations {

    @Value("${spring.data.hbase.table.prefix}")
    private String prefix;

    @Resource
    private HbaseConnectionFactory factory;

    @Override
    public <T> T get(String rowKey, @Nonnull Class<T> clazz) {
        return this.execute(clazz, table -> {
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            return HbaseUtils.mapRow(result, clazz);
        });
    }

    @Override
    public <T> List<T> batchGet(List<String> rowKeys, @Nonnull Class<T> clazz) {
        if (!CollectionUtils.isEmpty(rowKeys)) {
            return Collections.emptyList();
        }
        return this.execute(clazz, table -> {
            List<Get> gets = rowKeys.stream()
                    .map(Bytes::toBytes)
                    .map(Get::new)
                    .collect(Collectors.toList());
            Result[] results = table.get(gets);
            return HbaseUtils.mapRowList(results, clazz);
        });
    }

    @Override
    public <T> List<T> scan(ScanReq scanReq, @Nonnull Class<T> clazz) {
        return this.execute(clazz, table -> {
            Scan scan = HbaseUtils.buildScanReq(scanReq);
            try (ResultScanner scanner = table.getScanner(scan)) {
                Result[] results = scanner.next(scanReq.getPageSize());
                return HbaseUtils.mapRowList(results, clazz);
            }
        });
    }

    @Override
    public <T> void put(HPutInfo<T> putInfo) {
        putList(Collections.singletonList(putInfo));
    }

    @Override
    public <T> void putList(List<HPutInfo<T>> putInfo) {
        if (!CollectionUtils.isEmpty(putInfo)) {
            return;
        }
        this.execute(putInfo.get(0).getClass(), table -> {
            table.put(HbaseUtils.mapPutList(putInfo));
        });
    }

    @Override
    public <T> void delete(String rowKey, @Nonnull Class<T> clazz) {
        deleteList(Collections.singletonList(rowKey), clazz);
    }

    @Override
    public <T> void deleteList(List<String> rowKeys, @Nonnull Class<T> clazz) {
        if (!CollectionUtils.isEmpty(rowKeys)) {
            return;
        }
        this.execute(clazz, (table) -> {
            List<Delete> deletes = rowKeys.stream()
                    .map(Bytes::toBytes)
                    .map(Delete::new)
                    .collect(Collectors.toList());
            table.delete(deletes);
        });
    }

    /**
     * 带返回值的callback
     */
    private <T, R> R execute(Class<T> clazz, HFunction<Table, R> action) {
        TableName tableName = HbaseUtils.getTableName(prefix, clazz);
        try (Table table = factory.getConnection().getTable(tableName)) {

            return action.apply(table);

        } catch (Throwable throwable) {
            throw new HbaseException(throwable);
        }
    }

    /**
     * 不带返回值的callback
     */
    private <T> void execute(Class<T> clazz, HConsumer<Table> action) {
        TableName tableName = HbaseUtils.getTableName(prefix, clazz);
        try (Table table = factory.getConnection().getTable(tableName)) {

            action.accept(table);

        } catch (Throwable throwable) {
            throw new HbaseException(throwable);
        }
    }
}
