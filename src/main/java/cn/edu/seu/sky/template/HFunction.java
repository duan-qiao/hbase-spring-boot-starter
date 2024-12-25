package cn.edu.seu.sky.template;

/**
 * @author xiaotian.zhou
 * @date 2020/11/5 11:32
 */
@FunctionalInterface
public interface HFunction<T, R> {
    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     * @throws Throwable hbase exception
     */
    R apply(T t) throws Throwable;
}
