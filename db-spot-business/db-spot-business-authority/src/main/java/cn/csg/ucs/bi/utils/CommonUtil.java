package cn.csg.ucs.bi.utils;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

    /**
     * @Description: 获取请求来源的ip
     * @Author: chengzhf
     * @date 2018年11月3日 下午8:13:17
     * @param request
     * @return
     */
    public static String getLoginIp(HttpServletRequest request) {
        // 获取ip
        String loginIp = request.getHeader("x-forwarded-for");
        if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
            loginIp = request.getHeader("Proxy-Client-IP");
        }
        if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
            loginIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
            loginIp = request.getRemoteAddr();
        }
        return loginIp;
    }

}
