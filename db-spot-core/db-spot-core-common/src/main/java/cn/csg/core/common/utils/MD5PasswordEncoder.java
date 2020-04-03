/**
 * @创建人：刘明昆
 * @创建时间：2019/9/21
 * @描述：
 */
package cn.csg.core.common.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @program: db-spot-settlement-cloud
 * @description: Md5加密、解密工具
 * @author: 刘明昆
 * @create: 2019-09-21 11:14
 */
public class MD5PasswordEncoder implements PasswordEncoder {

    private static final String HEX_NUMS_STR = "0123456789ABCDEF";
    private static final Integer SALT_LENGTH = 12;

    /**
     * @描述：重写实现PasswordEncoder接口的matches方法，用来验证消息摘要是否相同。
     * @参数：[password,strInDb]
     * @返回值：boolean
     * @创建人：刘明昆
     * @创建时间：2019/9/21
     * @修改人和其它信息：
     */
    @Override
    public boolean matches(CharSequence password, String strInDb) {
        // 将16进制字符串格式口令转换成字节数组
        byte[] str_InDb = hexStringToByte(strInDb);
        // 声明盐变量
        byte[] salt = new byte[SALT_LENGTH];
        // 将盐从数据库中保存的口令字节数组中提取出来
        System.arraycopy(str_InDb, 0, salt, 0, SALT_LENGTH);
        // 声明消息摘要对象
        MessageDigest md = null;
        try {
            // 创建消息摘要
            md = MessageDigest.getInstance("MD5");
            // 将盐数据传入消息摘要对象
            md.update(salt);
            // 将口令的数据传给消息摘要对象
            md.update(password.toString().getBytes("UTF-8"));
            // 生成输入口令的消息摘要
            byte[] digest = md.digest();
            // 声明一个保存数据库中口令消息摘要的变量
            byte[] digestInDb = new byte[str_InDb.length - SALT_LENGTH];
            // 取得数据库中口令的消息摘要
            System.arraycopy(str_InDb, SALT_LENGTH, digestInDb, 0, digestInDb.length);
            // 比较根据输入口令生成的消息摘要和数据库中消息摘要是否相同
            if (Arrays.equals(digest, digestInDb)) {
                // 验证正确返回true
                return true;
            } else {
                // 验证不正确返回false
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @描述：重写PasswordEncoder接口的encode方法，用来给密码加密。
     * @参数：[password]
     * @返回值：java.lang.String
     * @创建人：刘明昆
     * @创建时间：2019/9/21
     * @修改人和其它信息：
     */
    @Override
    public String encode(CharSequence password) {
        // 声明加密后的口令数组变量
        byte[] encode_str = null;
        // 随机数生成器
        SecureRandom random = new SecureRandom();
        // 声明盐数组变量
        byte[] salt = new byte[SALT_LENGTH];
        // 将随机数放入盐变量中
        random.nextBytes(salt);

        // 声明消息摘要对象
        MessageDigest md = null;

        try {
            // 创建消息摘要
            md = MessageDigest.getInstance("MD5");
            // 将盐数据传入消息摘要对象
            md.update(salt);
            // 将口令的数据传给消息摘要对象
            md.update(password.toString().getBytes("UTF-8"));
            // 获得消息摘要的字节数组
            byte[] digest = md.digest();
            // 因为要在口令的字节数组中存放盐，所以加上盐的字节长度
            encode_str = new byte[digest.length + SALT_LENGTH];
            // 将盐的字节拷贝到生成的加密口令字节数组的前12个字节，以便在验证口令时取出盐
            System.arraycopy(salt, 0, encode_str, 0, SALT_LENGTH);
            // 将消息摘要拷贝到加密口令字节数组从第13个字节开始的字节
            System.arraycopy(digest, 0, encode_str, SALT_LENGTH, digest.length);
            // 将字节数组格式加密后的口令转化为16进制字符串格式的口令
            return byteToHexString(encode_str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @描述：将16进制字符串转换成字节数组
     * @参数：[hex]
     * @返回值：byte[]
     * @创建人：刘明昆
     * @创建时间：2019/9/21
     * @修改人和其它信息：
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR.indexOf(hexChars[pos + 1]));
        }
        return result;
    }

    /**
     * @描述：将指定byte数组转换成16进制字符串
     * @参数：[b]
     * @返回值：java.lang.String
     * @创建人：刘明昆
     * @创建时间：2019/9/21
     * @修改人和其它信息：
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        MD5PasswordEncoder dd = new MD5PasswordEncoder();
		try {
            System.out.println(dd.encode("1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}