package cn.edu.seu.sky.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaotian.zhou
 * @date 2020/11/5 11:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HPutInfo<T> {

    private String rowKey;

    private T value;
}
