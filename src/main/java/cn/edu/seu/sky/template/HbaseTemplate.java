package cn.edu.seu.sky.template;

import cn.edu.seu.sky.exception.HbaseException;
import cn.edu.seu.sky.model.HEntity;
import cn.edu.seu.sky.request.FiledQuery;
import cn.edu.seu.sky.request.ScanRequest;
import cn.edu.seu.sky.util.HbaseUtils;
import cn.edu.seu.sky.util.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


public class HbaseTemplate implements HbaseOperations {

    private final Connection connection;

    public HbaseTemplate(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> T get(String rowKey, Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotBlank(rowKey);
        return this.execute(clazz, table -> {
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            return HbaseUtils.mapRow(result, clazz);
        });
    }

    @Override
    public <T> List<T> batchGet(List<String> rowKeys, Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotEmpty(rowKeys);
        return this.execute(clazz, table -> {
            List<Get> gets = rowKeys.stream()
                    .map(Bytes::toBytes)
                    .map(Get::new)
                    .collect(Collectors.toList());
            return HbaseUtils.mapRows(table.get(gets), clazz);
        });
    }

    @Override
    public <T> List<T> scan(String startRow, String endRow, Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        return this.execute(clazz, table -> {
            Scan scan = new Scan();
            if (StringUtils.isNotBlank(startRow)) {
                scan.withStartRow(Bytes.toBytes(startRow));
            }
            if (StringUtils.isNotBlank(endRow)) {
                scan.withStopRow(Bytes.toBytes(endRow));
            }
            List<T> results = Lists.newArrayList();
            try (ResultScanner scanner = table.getScanner(scan)) {
                for (Result result : scanner) {
                    results.add(HbaseUtils.mapRow(result, clazz));
                }
            }
            return results;
        });
    }

    @Override
    public <T> List<T> scan(ScanRequest request, Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(request);
        return this.execute(clazz, table -> {
            Scan scan = new Scan();
            if (StringUtils.isNotBlank(request.getStartRowKey())) {
                scan.withStartRow(Bytes.toBytes(request.getStartRowKey()));
            }
            if (StringUtils.isNotBlank(request.getEndRowKey())) {
                scan.withStopRow(Bytes.toBytes(request.getEndRowKey()));
            }
            if (request.getLimit() == null) {
                request.setLimit(50);
            }
            scan.setLimit(request.getLimit());

            if (request.getReverse() != null) {
                scan.setReversed(request.getReverse());
            }
            if (!CollectionUtils.isEmpty(request.getFiledQueries())) {
                FilterList filters = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                request.getFiledQueries().stream().map(FiledQuery::convert).forEach(filters::addFilter);
                scan.setFilter(filters);
            }
            try (ResultScanner scanner = table.getScanner(scan)) {
                Result[] results = scanner.next(request.getLimit());
                return HbaseUtils.mapRows(results, clazz);
            }
        });
    }

    @Override
    public <T> void put(String rowKey, T value) {
        Preconditions.checkNotBlank(rowKey);
        Preconditions.checkNotNull(value);
        this.execute(value.getClass(), table -> {
            Put put = HbaseUtils.mapPut(rowKey, value);
            table.put(put);
        });
    }

    @Override
    public <T> void put(String rowKey, T value, Long ttl) {
        Preconditions.checkNotBlank(rowKey);
        Preconditions.checkNotNull(value);
        Preconditions.checkNotNull(ttl);
        this.execute(value.getClass(), table -> {
            Put put = HbaseUtils.mapPut(rowKey, value);
            put.setTTL(ttl);
            table.put(put);
        });
    }

    @Override
    public <T> void putList(List<HEntity<T>> entities) {
        Preconditions.checkNotEmpty(entities);
        this.execute(entities.get(0).getEntity().getClass(), table -> {
            table.put(HbaseUtils.mapPuts(entities));
        });
    }

    @Override
    public <T> void delete(String rowKey, Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotBlank(rowKey);
        this.execute(clazz, table -> {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        });
    }

    @Override
    public <T> void batchDelete(List<String> rowKeys, Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotEmpty(rowKeys);
        this.execute(clazz, (table) -> {
            List<Delete> deletes = rowKeys.stream()
                    .map(Bytes::toBytes)
                    .map(Delete::new)
                    .collect(Collectors.toList());
            table.delete(deletes);
        });
    }

    private <T, E> E execute(Class<T> clazz, HFunction<Table, E> action) {
        TableName name = HbaseUtils.getTableName(clazz);
        try (Table table = connection.getTable(name)) {
            return action.apply(table);
        } catch (Throwable throwable) {
            throw new HbaseException(throwable);
        }
    }

    private <T> void execute(Class<T> clazz, HConsumer<Table> action) {
        TableName name = HbaseUtils.getTableName(clazz);
        try (Table table = connection.getTable(name)) {
            action.accept(table);
        } catch (Throwable throwable) {
            throw new HbaseException(throwable);
        }
    }
}
