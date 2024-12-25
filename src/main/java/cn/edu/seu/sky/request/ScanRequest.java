package cn.edu.seu.sky.request;

import lombok.Data;

import java.util.List;

/**
 * @author xiaotian on 2024/11/24
 */
@Data
public class ScanRequest {
    /**
     * 开始rowKey
     */
    private String startRowKey;

    /**
     * 结束rowKey
     */
    private String endRowKey;

    /**
     * 查询条数
     */
    private Integer limit;

    /**
     * 是否倒序
     */
    private Boolean reverse;

    /**
     * 查询字段
     */
    private List<FiledQuery> filedQueries;
}
