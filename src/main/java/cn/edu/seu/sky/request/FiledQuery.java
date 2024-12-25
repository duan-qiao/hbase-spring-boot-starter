package cn.edu.seu.sky.request;

import lombok.Data;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author xiaotian on 2024/11/24
 */
@Data
public class FiledQuery {
    /**
     * 列簇
     */
    private String family;
    /**
     * 列名
     */
    private String qualifier;
    /**
     * 值
     */
    private Object value;
    /**
     * 操作符
     */
    private CompareOperator operator;

    public SingleColumnValueFilter convert() {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(
                Bytes.toBytes(family),
                Bytes.toBytes(qualifier),
                operator,
                Bytes.toBytes(value.toString())
        );
        filter.setFilterIfMissing(true);
        return filter;
    }
}
