package cn.csg.ucs.bi.base.exception;

public class BusinessException extends BasicException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String code, String message) {
        super(code, message);
    }
}
