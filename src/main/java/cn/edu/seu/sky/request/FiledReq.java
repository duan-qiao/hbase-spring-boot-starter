package cn.edu.seu.sky.request;

import lombok.Data;
import org.apache.hadoop.hbase.CompareOperator;

/**
 * @author xiaotian on 2022/4/18
 */
@Data
public class FiledReq {

    private String family;

    private String qualifier;

    private Object value;

    private CompareOperator operator;
}
