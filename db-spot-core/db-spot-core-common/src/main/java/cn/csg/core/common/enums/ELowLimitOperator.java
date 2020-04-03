package cn.csg.core.common.enums;

public enum ELowLimitOperator {
    GREATER_THAN_OR_EQUAL(">="),
    GREATER_THAN(">");

    private String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    ELowLimitOperator(String operator) {
        this.operator = operator;
    }
}
