package cn.edu.seu.sky.exception;

/**
 * @author xiaotian.zhou
 * @date 2020/11/5 11:32
 */
public class HbaseException extends RuntimeException {

    private static final long serialVersionUID = -8724534970318379496L;

    public HbaseException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }

    public HbaseException(String message) {
        super(message);
    }

    public HbaseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
