package cn.csg.core.common.utils;

import cn.csg.core.common.entity.FtpBean;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FtpUtil {

    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * FTPClient对象
     **/
    private static FTPClient ftpClient = null;

    /**
     * 该目录不存在
     */
    public static final String DIR_NOT_EXIST = "该目录不存在";

    /**
     * "该目录下没有文件
     */
    public static final String DIR_CONTAINS_NO_FILE = "该目录下没有文件";

    /**
     * 本地字符编码
     **/
    private static String localCharset = "GBK";

    /**
     * FTP协议里面，规定文件名编码为iso-8859-1
     **/
    private static String serverCharset = "ISO-8859-1";

    /**
     * UTF-8字符编码
     **/
    private static final String CHARSET_UTF8 = "UTF-8";

    /**
     * OPTS UTF8字符串常量
     **/
    private static final String OPTS_UTF8 = "OPTS UTF8";

    /**
     * 设置缓冲区大小4M
     **/
    private static final int BUFFER_SIZE = 1024 * 1024 * 4;

    public static boolean uploadFile(FtpBean ftpBean) {

        login(ftpBean.getAddress(), Integer.parseInt(ftpBean.getPort()), ftpBean.getUsername(), ftpBean.getPassword());

        boolean result = false;
        try {

//            String tempPath = ftpBean.getBasepath();
            String fileName = ftpBean.getFileName();
            if (ftpClient.changeWorkingDirectory(ftpBean.getBasepath())) {
                //判断目录是否存在，如果目录不存在创建目录，目录存在则跳转到此目录下
                String []tempPathList = ftpBean.getSavePath().split("/");
                for (String dir : tempPathList) {
                    dir = changeEncoding(ftpBean,dir);
                    if(dir != null && dir != ""){
                        if (!ftpClient.changeWorkingDirectory(dir)) {
                            if (!ftpClient.makeDirectory(dir)) {
                                logger.info("目录创建失败，路径为："+dir);
                                return result;
                            } else {
                                ftpClient.changeWorkingDirectory(dir);
                            }
                        }
                    }
                }
            }else{
                logger.info("基本目录不存在，路径为："+ftpBean.getBasepath());
            }

            //保存文件方式  默认：1-覆盖；2-文件名称后面+(递增数据)
            if(ftpBean.getSaveFileType() == 2){
                FTPFile[]file = ftpClient.listFiles();
                Integer i = 1;
                //采用递归，文件名重复自动加(i)
                fileName = aaa(file, i, fileName, fileName);
            }
            //设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //设置模式很重要
            ftpClient.enterLocalActiveMode();
            //上传文件
            result = ftpClient.storeFile(changeEncoding(ftpBean,fileName), ftpBean.getInputStream());
            if(!result){
                logger.info("上传文件失败");
                return result;
            }

            ftpBean.getInputStream().close();
            ftpClient.logout();
            result = true;
            logger.info("上传文件到ftp服务器成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                closeConnect();
            }
        }
        return result;
    }


    //递归重命名去重复
    public static String aaa(FTPFile[] file, int i, String fileName, String nameFlag){
        boolean isOk = true;
        for (FTPFile file2 : file) {
            String flag = file2.getName();
            if(nameFlag.equals(flag)){
                String []fileNames = fileName.split("\\.");
                if(fileNames.length > 0){
                    nameFlag = "";
                }
                for (int j = 0; j < fileNames.length; j++) {
                    if(j == fileNames.length-1){
                        nameFlag = nameFlag+"("+i+")."+fileNames[j];
                    }else if(j == fileNames.length-2){
                        nameFlag = nameFlag + fileNames[j];
                    }else{
                        nameFlag = nameFlag + fileNames[j] + ".";
                    }
                }
                i += 1;
                isOk = false;
            }else{
                isOk = true;
            }
        }

        if(isOk == false){
            nameFlag = aaa(file, i, fileName, nameFlag);
        }
        return nameFlag;
    }

    /**
     * 下载该目录下所有文件到本地
     *
     * @param ftpPath
     * @return 成功返回true，否则返回false
     */
    public static InputStream downloadFiles(FtpBean ftpBean, String ftpPath) {
        // 登录
        login(ftpBean.getAddress(), Integer.parseInt(ftpBean.getPort()), ftpBean.getUsername(), ftpBean.getPassword());
        InputStream inputStream = null;
        if (ftpClient != null) {
            try {
                String path = ftpBean.getBasepath() + ftpPath.split("\\\\")[0];
                String name = ftpPath.split("\\\\")[1];
                // 判断是否存在该目录
                if (!ftpClient.changeWorkingDirectory(changeEncoding(ftpBean,path))) {
                    logger.error(ftpBean.getBasepath() + ftpPath + DIR_NOT_EXIST);
                    return null;
                }
                ftpClient.enterLocalPassiveMode();  // 设置被动模式，开通一个端口来传输数据
                String[] fs = ftpClient.listNames();
                // 判断该目录下是否有文件
                inputStream = ftpClient.retrieveFileStream(changeEncoding(ftpBean,path + "/" + name));
            } catch (IOException e) {
                logger.error("下载文件失败", e);
            } finally {
                closeConnect();
            }
        }
        return inputStream;
    }

    /**
     * @Author chengzhifeng
     * @Description 删除文件
     * @Date 16:55 2019/12/20
     * @Param [ftpBean, ftpPath]
     * @return void
     **/
    public static int deleteFiles(FtpBean ftpBean, String ftpPath) throws IOException {
        login(ftpBean.getAddress(), Integer.parseInt(ftpBean.getPort()), ftpBean.getUsername(), ftpBean.getPassword());
        String path = ftpBean.getBasepath() + "/" + ftpPath.split("\\\\")[0];
        String name = ftpPath.split("\\\\")[1];
        // 判断是否存在该目录
        if (!ftpClient.changeWorkingDirectory(changeEncoding(ftpBean,path))) {
            logger.error(ftpBean.getBasepath() + ftpPath + DIR_NOT_EXIST);
            return 0;
        }else{
            int delete = ftpClient.dele(changeEncoding(ftpBean,name));
            return delete;
        }

    }

    /**
     * 连接FTP服务器
     *
     * @param address  地址，如：127.0.0.1
     * @param port     端口，如：21
     * @param username 用户名，如：root
     * @param password 密码，如：root
     */
    private static void login(String address, int port, String username, String password) {

            ftpClient = new FTPClient();
            try {
                ftpClient.connect(address, port);
                boolean login =  ftpClient.login(username, password);
                if(!login){
                    logger.error("登录失败！");
                }else{
//                    logger.info("登录成功！，当前工作路径："+ftpClient.printWorkingDirectory());
//                    logger.info("工作目录有以下文件：");
//                    FTPFile[] files = ftpClient.listFiles();
//                    for (int i = 0; i < files.length; i++) {
//                        logger.info("名称："+files[i].getName());
//                    }
                }

                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //限制缓冲区大小
                ftpClient.setBufferSize(BUFFER_SIZE);
                int reply = ftpClient.getReplyCode();
                logger.info("ftp连接状态码："+reply);
                if (!FTPReply.isPositiveCompletion(reply)) {
                    closeConnect();
                    logger.error("FTP服务器连接失败");
                }
            } catch (Exception e) {
                logger.error("FTP登录失败", e);
            }
    }

    /**
     * 关闭FTP连接
     */
    private static void closeConnect() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.error("关闭FTP连接失败", e);
            }finally {
                ftpClient = null;
            }
        }
    }

    /**
     * FTP服务器路径编码转换
     *
     * @return String
     */
    private static String changeEncoding(FtpBean ftpBean,String str) throws UnsupportedEncodingException {
        return new String(new String(str.getBytes(ftpBean.getServerEncoded()),ftpBean.getLocalEncoded()));
    }

}
