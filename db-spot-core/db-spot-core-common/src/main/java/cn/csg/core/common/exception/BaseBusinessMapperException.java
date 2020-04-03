package cn.csg.core.common.exception;

public class BaseBusinessMapperException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    public BaseBusinessMapperException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
