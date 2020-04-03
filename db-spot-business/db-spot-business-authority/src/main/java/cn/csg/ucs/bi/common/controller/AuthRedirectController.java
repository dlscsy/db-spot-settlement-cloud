package cn.csg.ucs.bi.common.controller;

import cn.csg.ucs.bi.base.structure.JSONResponseBody;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/authRedirectController")
public class AuthRedirectController {
	private static String TOKEN_INVALID_MSG = "这是一个不良请求，已被拒绝访问！";
	private static String TOKEN_ONLINE_INVALID_MSG = "您所登录的账户已在别处登录，相同登录的账户不允许同时在线操作系统！";
	private static String TOKEN_EXPIRE_MSG = "您所登录的账户已长时间未进行任何操作，请重新登录之后在进行系统操作！";
	private static String TOKEN_PARSE_EXCEPTION_MSG = "请求认证时出错，请重新登录！";

	@RequestMapping(value = "/tokenInvalid")
	public @ResponseBody Object tokenInvalid(HttpServletResponse response) {
		return JSONResponseBody.createHttpErrorResponseBody(HttpStatus.FORBIDDEN, TOKEN_INVALID_MSG);
	}

	@RequestMapping(value = "/tokenOnLineInvalid")
	public @ResponseBody Object tokenOnLineInvalid(HttpServletResponse response) {
		return JSONResponseBody.createHttpErrorResponseBody(HttpStatus.FORBIDDEN, TOKEN_ONLINE_INVALID_MSG);
	}

	@RequestMapping(value = "/tokenExpire")
	public @ResponseBody Object tokenExpire(HttpServletResponse response) {
		return JSONResponseBody.createHttpErrorResponseBody(HttpStatus.FORBIDDEN, TOKEN_EXPIRE_MSG);
	}

	@RequestMapping(value = "/tokenParseException")
	public @ResponseBody Object tokenParseException(HttpServletResponse response) {
		return JSONResponseBody.createHttpErrorResponseBody(HttpStatus.FORBIDDEN, TOKEN_PARSE_EXCEPTION_MSG);
	}
}
