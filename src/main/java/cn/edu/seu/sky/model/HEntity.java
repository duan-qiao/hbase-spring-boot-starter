package cn.edu.seu.sky.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaotian.zhou
 * @email xiaotian.zhou@amh-group.com
 * @date 2020/11/5 11:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HEntity<T> {
    /**
     * rowKey
     */
    private String rowKey;
    /**
     * 值
     */
    private T entity;
}
