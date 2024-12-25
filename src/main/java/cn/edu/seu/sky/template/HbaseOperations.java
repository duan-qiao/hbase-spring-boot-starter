package cn.edu.seu.sky.template;

import cn.edu.seu.sky.model.HEntity;
import cn.edu.seu.sky.request.ScanRequest;

import java.util.List;

/**
 * @author xiaotian.zhou
 * @email xiaotian.zhou@amh-group.com
 * @date 2020/11/5 11:32
 */
public interface HbaseOperations {

    /**
     * get
     *
     * @param rowKey rowKey
     * @param clazz  clazz
     * @param <T>    T
     * @return T
     */
    <T> T get(String rowKey, Class<T> clazz);

    /**
     * batchGet
     *
     * @param rowKeys rowKeys
     * @param clazz   clazz
     * @param <T>     T
     * @return List<T>
     */
    <T> List<T> batchGet(List<String> rowKeys, Class<T> clazz);

    /**
     * scan
     *
     * @param startRow startRow
     * @param endRow   endRow
     * @param clazz    clazz
     * @param <T>      T
     * @return List<T>
     */
    <T> List<T> scan(String startRow, String endRow, Class<T> clazz);

    /**
     * scan
     *
     * @param request request
     * @param clazz   clazz
     * @param <T>     T
     * @return List<T>
     */
    <T> List<T> scan(ScanRequest request, Class<T> clazz);

    /**
     * put
     *
     * @param rowKey rowKey
     * @param value  value
     * @param <T>    T
     */
    <T> void put(String rowKey, T value);

    /**
     * put
     *
     * @param rowKey rowKey
     * @param value  value
     * @param ttl    ttl
     * @param <T>    T
     */
    <T> void put(String rowKey, T value, Long ttl);

    /**
     * putList
     *
     * @param entities values
     * @param <T>    t
     */
    <T> void putList(List<HEntity<T>> entities);

    /**
     * delete
     *
     * @param rowKey rowKey
     * @param clazz  clazz
     * @param <T>    T
     */
    <T> void delete(String rowKey, Class<T> clazz);

    /**
     * deleteList
     *
     * @param rowKeys rowKeys
     * @param clazz   clazz
     * @param <T>     T
     */
    <T> void batchDelete(List<String> rowKeys, Class<T> clazz);
}
