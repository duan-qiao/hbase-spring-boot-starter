package cn.edu.seu.sky.request;

import lombok.Data;

import java.util.List;

/**
 * @author xiaotian on 2022/4/18
 */
@Data
public class ScanReq {

    private String startRow;

    private String endRow;

    private Integer pageSize;

    private Boolean reverse;

    private List<FiledReq> filedReqList;
}
