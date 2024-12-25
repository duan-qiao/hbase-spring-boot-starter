package cn.edu.seu.sky.template;

/**
 * @author xiaotian.zhou
 * @date 2020/11/5 11:32
 */
@FunctionalInterface
public interface HConsumer<T> {
    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws Throwable hbase exception
     */
    void accept(T t) throws Throwable;
}
