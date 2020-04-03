package cn.csg.ucs.bi.base.structure;

import cn.csg.ucs.bi.base.constant.BaseCode;
import org.springframework.http.HttpStatus;

public class JSONResponseBody {
    // HTTP状态码
    private Integer httpStatus;

    // 业务状态码
    private String businessCode;

    // 响应消息
    private String message;

    // 响应数据结构体
    private Object body;

    public JSONResponseBody(){};

    public JSONResponseBody(Integer httpStatus, String businessCode, String message, Object body) {
        this.httpStatus = httpStatus;
        this.businessCode = businessCode;
        this.message = message;
        this.body = body;
    }

    /**
     * 构造业务处理成功响应体
     * @param message
     * @param body
     * @return
     */
    public static JSONResponseBody createSuccessResponseBody(String message, Object body) {
        return new JSONResponseBody(HttpStatus.OK.value(), BaseCode.BUSINESS_SUCCESS, message, body);
    }

    /**
     * 构造业务处理失败响应体
     * @param failCode
     * @param failMessage
     * @return
     */
    public static JSONResponseBody createFailResponseBody(String failCode, String failMessage) {
        return new JSONResponseBody(HttpStatus.OK.value(), failCode, failMessage, null);
    }

    /**
     * 构造系统异常响应体
     * @return
     */
    public static JSONResponseBody createErrorResponseBody() {
        return new JSONResponseBody(HttpStatus.OK.value(), BaseCode.SYS_ERROR, "系统异常", null);
    }

    /**
     * 构造http层面的异常响应体
     * @param httpStatus
     * @param httpErrorMessage
     * @return
     */
    public static JSONResponseBody createHttpErrorResponseBody(HttpStatus httpStatus, String httpErrorMessage) {
        return new JSONResponseBody(httpStatus.value(), "", httpErrorMessage, null);
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
