package cn.edu.seu.sky.core;

import cn.edu.seu.sky.model.HPutInfo;
import cn.edu.seu.sky.request.ScanReq;

import java.util.List;

/**
 * @author xiaotian on 2022/4/16
 */
public interface HbaseOperations {
    /**
     * get
     * @param rowKey rowKey
     * @param clazz clazz
     * @param <T> T
     * @return T
     */
    <T> T get(String rowKey, Class<T> clazz);

    /**
     * batchGet
     * @param rowKeys rowKeys
     * @param clazz clazz
     * @param <T> T
     * @return List<T>
     */
    <T> List<T> batchGet(List<String> rowKeys, Class<T> clazz);

    /**
     * scan，默认最多查50条数据
     * @param scanReq 查询参数
     * @param clazz clazz
     * @param <T> T
     * @return List<T>
     */
    <T> List<T> scan(ScanReq scanReq, Class<T> clazz);

    /**
     * put
     * @param putInfo putInfo
     * @param <T> T
     */
    <T> void put(HPutInfo<T> putInfo);

    /**
     * putList
     * @param putInfo putInfo
     * @param <T> T
     */
    <T> void putList(List<HPutInfo<T>> putInfo);

    /**
     * delete
     * @param rowKey rowKey
     * @param clazz clazz
     * @param <T> T
     */
    <T> void delete(String rowKey, Class<T> clazz);

    /**
     * deleteList
     * @param rowKeys rowKeys
     * @param clazz clazz
     * @param <T> T
     */
    <T> void deleteList(List<String> rowKeys, Class<T> clazz);
}
