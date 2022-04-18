package cn.edu.seu.sky.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xiaotian.zhou
 * @date 2020/11/5 11:32
 */
@Data
@AllArgsConstructor
public class HColumnInfo {
    /**
     * 列簇
     */
    private byte[] family;
    /**
     * 列名
     */
    private byte[] qualifier;
}
