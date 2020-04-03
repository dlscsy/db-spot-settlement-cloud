package cn.csg.ucs.bi.base.exception;

public class BasicException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String code = "";

    public BasicException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
