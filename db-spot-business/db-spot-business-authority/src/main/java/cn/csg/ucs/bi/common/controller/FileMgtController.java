package cn.csg.ucs.bi.common.controller;

import cn.csg.core.common.entity.FtpBean;
import cn.csg.core.common.utils.CommonUtils;
import cn.csg.core.common.utils.FtpUtil;
import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.common.entity.S_FILE_INFO;
import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import cn.csg.ucs.bi.common.entity.S_ORG_CODE_INFO;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.FileMgtService;
import cn.csg.ucs.bi.common.service.LogService;
import cn.csg.ucs.bi.common.structure.TreeNode;
import cn.csg.ucs.bi.utils.CommonUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@CrossOrigin
@Transactional
@RestController
@RequestMapping("/fileMgtController")
public class FileMgtController {

    private Logger logger = LoggerFactory.getLogger(FileMgtController.class);

    @Autowired
    @Qualifier("fileMgtService")
    private FileMgtService fileMgtService;

    @Value("${ftp.address}")
    private String address;

    @Value("${ftp.port}")
    private String port;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.basepath}")
    private String basepath;

    @Value("${ftp.localEncoded}")
    private String localEncoded;

    @Value("${ftp.serverEncoded}")
    private String serverEncoded;

    @Autowired
    private LogService logService;

    @ResponseBody
    @GetMapping("/getFileInfo")
    public JSONResponseBody getFileInfo(String params) {

        logger.info("进入获取附件接口，参数：{}",params);

        JSONObject jo = JSONObject.parseObject(params);
        String orgCode = jo.getString("orgCode");
        String projectCategory = jo.getString("projectCategory");
        String projectSide = jo.getString("projectSide");
        String projectId = jo.getString("projectId");
        String fileName = jo.getString("fileName");
        Integer page = jo.getInteger("page");
        Integer limit = jo.getInteger("limit");
        PageHelper.startPage(page, limit);
        List<S_FILE_INFO> fileInfos = fileMgtService.getFileInfo(orgCode,projectSide, projectCategory, projectId, fileName);
        JSONObject result = new JSONObject();
        result.put("fileInfos", fileInfos);
        result.put("total", new PageInfo<S_FILE_INFO>(fileInfos).getTotal());
        return JSONResponseBody.createSuccessResponseBody("", result);
    }

    /**
     * 上传文件
     *
     * @param orgCode
     * @param projectCategory
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public Object upload(@RequestParam("orgCode") String orgCode,
                         @RequestParam("projectCategory") String projectCategory,
                         @RequestParam("projectCategoryName") String projectCategoryName,
                         @RequestParam("projectId") String projectId,
                         @RequestParam("projectSide") String projectSide,
                         @RequestParam("projectSideName") String projectSideName,
                         @RequestParam("file") MultipartFile[] file, HttpServletRequest req) {
        S_ORG_CODE_INFO fwOrg = fileMgtService.getOrgByOrgCode(orgCode);
        String fwOrgOrgName = fwOrg.getOrgName();
        String orgPath = getOrgPath(fwOrg.getParentOrgCode(), fwOrgOrgName);
        logger.info("进入上传附件接口");
//        String projectPath = null;
//        try {
//            projectPath = ResourceUtils.getURL("classpath:").getPath();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return JSONResponseBody.createErrorResponseBody();
//        }
//        String path = projectPath.substring(1, projectPath.indexOf("csg-bi") + 7) + "报表附件" + File.separator + projectSide + File.separator +
//                projectCategory + File.separator +  projectId + File.separator + orgPath;
//        File pathFile = new File(path);
//        pathFile.mkdirs();

       // String savePath = projectSide + File.separator + projectCategory + File.separator +  projectId + File.separator + orgPath;
        String savePath = orgPath + "/" + projectSideName + "/" + projectCategoryName + "/" +  projectId;

        // 获取操作用户
        S_USER_INFO userInfo = (S_USER_INFO)req.getAttribute("LOGIN_USER");
        for (MultipartFile currFile : file) {
            String fileName = currFile.getOriginalFilename();
            int dot = fileName.lastIndexOf(".");
            String file_name = fileName;
            String file_suffix = "";
            if(dot > 0){
                file_name = fileName.substring(0, dot);
                file_suffix = fileName.substring(dot);
                if(Arrays.asList("bat","asp","jsp","exe").contains(file_suffix)){
                    return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR,"不能上传带有.bat\\asp\\jsp\\exe后缀的文件");
                }
            }
            Calendar nowTime = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String real_name = file_name + "-" + sdf.format(nowTime.getTime()) + file_suffix;
//            File f = new File(path + File.separator + real_name);
            try {
                //currFile.transferTo(f);
                FtpBean ftpBean = getFtpInfo(real_name,savePath);
                ftpBean.setInputStream(currFile.getInputStream());
                boolean upload = FtpUtil.uploadFile(ftpBean);
                if(!upload){
                    return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR,"上传文件失败");
                }

                S_FILE_INFO fileInfo = new S_FILE_INFO();
                fileInfo.setFileId(CommonUtils.createUUID());
                fileInfo.setFileName(real_name);
                fileInfo.setFilePath(savePath + File.separator + real_name);
                fileInfo.setCompanyCode(orgCode);
                fileInfo.setCompanyName(fwOrgOrgName);
                fileInfo.setProjectName("测试");
                fileInfo.setProjectCategory(projectCategory);
                fileInfo.setProjectSide(projectSide);
                fileInfo.setProjectId(projectId);
                fileInfo.setOperateDate(CommonUtils.createCurrentTimeStr());
                fileInfo.setOperator(userInfo.getUserName());
                fileInfo.setOperatorCompany(userInfo.getOrgCode());
                fileMgtService.addFileInfo(fileInfo);
            } catch (IOException e) {
                e.printStackTrace();
                return JSONResponseBody.createErrorResponseBody();
            }
        }

        // 异步插入日志数据库
        logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"上传附件成功",projectCategoryName, userInfo, CommonUtil.getLoginIp(req));
        return JSONResponseBody.createSuccessResponseBody("上传成功", null);
    }

    @PostMapping("/download")
    public void download(HttpServletResponse response, String params, HttpServletRequest req) {

        logger.info("进入下载附件接口，参数：{}",params);
        JSONObject jo = JSONObject.parseObject(params);
        String orgCode = jo.getString("orgCode");
        String projectId = jo.getString("projectId");
        String projectCategoryName = jo.getString("projectCategoryName");
        S_ORG_CODE_INFO orgCodeInfo = fileMgtService.getOrgByOrgCode(orgCode);
        String orgName = orgCodeInfo.getOrgName();
        List<S_FILE_INFO> files = fileMgtService.getAllFileInfoByOrgCode(projectId);
        String projectPath = null;
        try {
            projectPath = ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Calendar nowTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String zipName = orgName + "-" + projectCategoryName + "-" + sdf.format(nowTime.getTime()) + ".zip";
//        String zipPath = projectPath.substring(1, projectPath.indexOf("csg-bi") + 7) + "报表附件" + File.separator + zipName;
//        File zipFile = new File(zipPath);

        ZipOutputStream zipStream = null;
        InputStream zipSource = null;
        BufferedInputStream bufferStream = null;

        try {
            // 构造最终压缩包的输出流
            response.setContentType("application/force-download");// 设置强制下载不打开
            zipName = URLEncoder.encode(zipName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + zipName);// 设置文件名
            zipStream = new ZipOutputStream(response.getOutputStream());
            for (S_FILE_INFO file : files) {
//                File f = new File(file.getFilePath());
//                // 将需要压缩的文件格式化为输入流
//                zipSource = new FileInputStream(f);
                zipSource = FtpUtil.downloadFiles(getFtpInfo("",""),file.getFilePath());
                // 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样
                // 这里的name就是文件名，文件名和之前的重复就会导致文件被覆盖，在这用i加文件名进行单一文件识别
                ZipEntry zipEntry = new ZipEntry(file.getFilePath());
                // 定位该压缩条目位置，开始写入文件到压缩包中
                zipStream.putNextEntry(zipEntry);
                // 输入缓冲流
                //FtpBean ftpBean = getFtpInfo(file.getFileName());
                //zipSource = FtpUtil.downloadFiles(ftpBean,file.getFilePath());
//                bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                int read = 0;
                // 创建读写缓冲区
                byte[] buf = new byte[1024 * 10];
                while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
                    zipStream.write(buf, 0, read);
                }
            }

            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"下载附件成功",projectCategoryName, (S_USER_INFO) req.getAttribute("LOGIN_USER"), CommonUtil.getLoginIp(req));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != bufferStream)
                    bufferStream.close();
                if (null != zipStream)
                    zipStream.close();
                if (null != zipSource)
                    zipSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        File f = new File(zipPath);
//        FileInputStream fis = null;
//        BufferedInputStream bis = null;
//        OutputStream os = null;
//        try {
//            zipName = URLEncoder.encode(zipName, "UTF-8");
//            response.setContentType("application/force-download");// 设置强制下载不打开
//            response.addHeader("Content-Disposition", "attachment;fileName=" + zipName);// 设置文件名
//            byte[] buffer = new byte[1024];
//            fis = new FileInputStream(f);
//            bis = new BufferedInputStream(fis);
//            os = response.getOutputStream();
//            int i = bis.read(buffer);
//
//            while (i != -1) {
//                os.write(buffer, 0, i);
//                i = bis.read(buffer);
//                // os.flush();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭流
//            try {
//                if (null != os)
//                    os.close();
//                if (null != bis)
//                    bis.close();
//                if (null != fis)
//                    fis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            f.delete();
//        }
    }

    /**
     * 删除文件
     *
     * @param params
     * @return
     */
    @ResponseBody
    @PostMapping("/removeFileInfo")
    public Object removeFileInfo(String params,HttpServletRequest req) throws IOException {

        logger.info("进入删除附件接口，参数：{}",params);
        JSONObject jo = JSONObject.parseObject(params);
        List<S_FILE_INFO> datas = JSONArray.parseArray(jo.getString("selectedDatas"), S_FILE_INFO.class);
        String projectCategoryName = jo.getString("projectCategoryName");

        StringBuffer keys = new StringBuffer();
        for (int i = 0; i < datas.size(); i++) {
            String fileId = datas.get(i).getFileId();
            String filePath = datas.get(i).getFilePath();
            keys.append("'" + fileId + "'");
            if (i < datas.size() - 1) {
                keys.append(", ");
            }
            // 文件删除
//            File f = new File(datas.get(i).getFilePath());
//            if (f.exists() && f.isFile()) {
//                f.delete();
//            }
            FtpUtil.deleteFiles(getFtpInfo("",""),filePath);
        }
        fileMgtService.removeFileInfo(keys.toString());

        // 异步插入日志数据库
        logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"删除附件成功",projectCategoryName,(S_USER_INFO) req.getAttribute("LOGIN_USER"), CommonUtil.getLoginIp(req));
        return JSONResponseBody.createSuccessResponseBody("删除成功", null);
    }

    /**
     * 构建组织机构路径
     *
     * @param orgCode
     * @param path
     * @return
     */
    private String getOrgPath(String orgCode, String path) {
        S_ORG_CODE_INFO orgCodeInfo = fileMgtService.getOrgByOrgCode(orgCode);
        StringBuffer result = new StringBuffer("");
        if (orgCodeInfo == null) {
            result.append(path);
        } else {
            StringBuffer curr = new StringBuffer(orgCodeInfo.getOrgName());
            curr.append("/").append(path);
            result.append(getOrgPath(orgCodeInfo.getParentOrgCode(), curr.toString()));
        }

        return result.toString();
    }

    private FtpBean getFtpInfo(String fileName,String savePath){
        FtpBean ftpBean = new FtpBean();
        ftpBean.setAddress(address); // FTP服务器地址
        ftpBean.setBasepath(basepath); // 基本路径
        ftpBean.setPort(port); // 端口
        ftpBean.setPassword(password); // 密码
        ftpBean.setUsername(username); //账号
        ftpBean.setFileName(fileName); // 上传文件名称
        ftpBean.setSavePath(savePath);
        ftpBean.setSaveFileType(2); // 保存文件方式  默认：1-覆盖；2-文件名称后面+(递增数据)
        ftpBean.setLocalEncoded(localEncoded);
        ftpBean.setServerEncoded(serverEncoded);
        return ftpBean;
    }
}
