package top.zeds1aw.store.common.exception;

/**
 * 描述：     统一异常
 */
public class StoreException extends RuntimeException {

    private final Integer code;
    private final String message;

    public StoreException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public StoreException(StoreExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
