package cn.csg.ucs.bi.core.config.exception;

import cn.csg.core.common.exception.BaseBusinessMapperException;
import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.exception.BasicException;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常统一处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object errorHandler(NoHandlerFoundException exception) {
        exception.printStackTrace();
        return new JSONResponseBody(HttpStatus.NOT_FOUND.value(), BaseCode.SYS_ERROR, exception.getMessage(), null);
    }

    /**
     * 405异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Object errorHandler(HttpRequestMethodNotSupportedException exception) {
        exception.printStackTrace();
        return new JSONResponseBody(HttpStatus.METHOD_NOT_ALLOWED.value(), BaseCode.SYS_ERROR, exception.getMessage(), null);
    }

    /**
     * 415异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public Object errorHandler(HttpMediaTypeNotSupportedException exception) {
        exception.printStackTrace();
        return new JSONResponseBody(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), BaseCode.SYS_ERROR, exception.getMessage(), null);
    }

    /**
     * 500异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object errorHandler(Exception exception) {
        exception.printStackTrace();
        return new JSONResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), BaseCode.SYS_ERROR, exception.getMessage(), null);
    }

    /**
     * 表单验证异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Object validExceptionHandler(BindException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        exception.printStackTrace();
        return new JSONResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), BaseCode.SYS_ERROR, JSON.toJSONString(errors), null);
    }

    /**
     * 业务异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = BasicException.class)
    @ResponseBody
    public Object errorHandler(BasicException exception) {
        exception.printStackTrace();
        return new JSONResponseBody(HttpStatus.OK.value(), BaseCode.BUSINESS_ERROR, exception.getMessage(), null);
    }

    /**
     * 自定义扩展Mapper异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = BaseBusinessMapperException.class)
    @ResponseBody
    public Object errorHandler(BaseBusinessMapperException exception) {
        exception.printStackTrace();
        return new JSONResponseBody(HttpStatus.OK.value(), BaseCode.BUSINESS_ERROR, exception.getMessage(), null);
    }
}
