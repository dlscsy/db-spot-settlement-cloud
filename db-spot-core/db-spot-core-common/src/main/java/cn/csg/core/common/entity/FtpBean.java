package cn.csg.core.common.entity;

import lombok.Data;

import java.io.InputStream;

@Data
public class FtpBean {

    /**
     * 部分信息如果传空的话，默认赋值，根据业务需求修改
     */
    //FTP服务器地址
    private String address;
    //FTP服务器端口号
    private String port;
    //FTP服务器用户名
    private String username;
    //FTP服务器密码
    private String password;
    //上传文件名称
    private String fileName;
    //基本路径
    private String basepath;
    //保存路径
    private String savePath;
    //文件输入流
    private InputStream inputStream;
    //保存文件方式  默认：1-覆盖；2-文件名称后面+(递增数据)
    private Integer saveFileType;
    //服务器编码
    private String serverEncoded;
    //本地编码
    private String localEncoded;
}
