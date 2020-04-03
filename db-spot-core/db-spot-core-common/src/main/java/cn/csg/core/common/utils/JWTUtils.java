package cn.csg.core.common.utils;

//package com.cypc.utils;

import java.util.Date;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

import io.jsonwebtoken.*;

public class JWTUtils {

    private static String SECRET_KEY = "743cypc3a53jwt25ff1704bsecretkey303aeea01701170664fc";
    // private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String createJWT(String id, String subject, Map<String, Object> claims) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        long nowMillis = System.currentTimeMillis();// 生成JWT的时间
        Date now = new Date(nowMillis);
        // System.err.println("createJWT-now:" + df.format(now));
        // 生成签名的时候使用的秘钥secret，这个秘钥不能外露，它就是你服务端的私钥
        SecretKey key = generalKey();
        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder(); // 这里其实就是new一个JwtBuilder，设置jwt的body
        if (claims != null && claims.size() > 0) {
            builder.setClaims(claims); // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明
        }

        builder.setId(id); // JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token，从而回避重放攻击
        builder.setIssuedAt(now); // JWT的签发时间(iat)
        if (subject != null && !"".equals(subject)) {
            builder.setSubject(subject); // JWT的主体，是一个JSON格式的字符串，作为公有声明的设定
        }

        builder.signWith(signatureAlgorithm, key);// 设置签名使用的签名算法和签名使用的秘钥
        // long expMillis = nowMillis + 30 * 1000 * 60;
        // Date exp = new Date(expMillis);
        // System.err.println("createJWT-exp:" + df.format(exp));
        // builder.setExpiration(exp); // 设置过期时间
        return builder.compact(); // 生成签发证书
    }

    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey(); //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser() //得到DefaultJwtParser
                .setSigningKey(key) //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();//设置需要解析的jwt
        return claims;
    }

    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.decodeBase64(SECRET_KEY);//本地的密码解码
        // System.out.println(encodedKey);
        // System.out.println(Base64.encodeBase64URLSafeString(encodedKey));
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, SignatureAlgorithm.HS256.getJcaName());// 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
        return key;
    }

	/*public static void main(String[] args) {

		try {
			String id = Utils.createUUID();
			System.err.println(id);
			String jwt = JWTUtils.createJWT(id, "{account:wcs}", null);
			System.err.println(jwt);

			Claims c = JWTUtils.parseJWT(jwt);
			System.err.println(c.getId());
			System.err.println(c.getSubject());
			// System.err.println("main-now:" + df.format(c.getIssuedAt()));
			// System.err.println("main-exp:" + df.format(c.getExpiration()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
