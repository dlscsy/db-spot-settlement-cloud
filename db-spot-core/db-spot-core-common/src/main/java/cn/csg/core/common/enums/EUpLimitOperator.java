package cn.csg.core.common.enums;

public enum EUpLimitOperator {
    LESS_THAN_OR_EQUAL("<="),
    LESS_THAN("<");

    private String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    EUpLimitOperator(String operator) {
        this.operator = operator;
    }
}
