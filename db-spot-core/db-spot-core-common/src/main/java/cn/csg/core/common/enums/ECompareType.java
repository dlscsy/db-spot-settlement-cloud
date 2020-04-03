package cn.csg.core.common.enums;

public enum ECompareType {
    EQUAL(0),
    GREATER(1),
    LESS(-1);

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    ECompareType(Integer code) {
        this.code = code;
    }
}
