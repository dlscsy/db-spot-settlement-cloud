package cn.csg.core.common.utils;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class CommonUtils {

    /**
     * 创建UUID
     */
    public static String createUUID() {
        Random random = new Random();
        String uuid = UUID.randomUUID().toString();
        for (int i = 0; i < 4; i++) {
            int ran = random.nextInt(10);
            uuid = uuid.replaceFirst("-", String.valueOf(ran));
        }
        return uuid;
    }

    /**
     * 生成当前时间
     */
    public static Timestamp  createCurrentTime() {
        Timestamp currentTime = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = sdf.format(new Date());
            long getTime = sdf.parse(timeStr).getTime();
            currentTime = new Timestamp(getTime);

        }catch (ParseException e){
            e.printStackTrace();
        }
        return  currentTime;
    }

    /**
     * 生成当前时间（字符串）
     */
    public static String  createCurrentTimeStr() {
        String timeStr = "";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timeStr = sdf.format(new Date());

        }catch (Exception e){
            e.printStackTrace();
        }
        return  timeStr;
    }

    /**
     * 描述:转换String成为Date对象
     * @param aMask 转换格式
     * @param strDate 转换日期
     * @return 日期
     */
    public static Date convertStringToDate(String aMask, String strDate) {
        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            date = null;
        }

        return date;
    }

    /**
     * 描述:日期转String
     * @param date 日期
     * @param dateFormat 转换格式
     * @return 日期string
     */
    public static String convertDate2String(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 20; i++) {
//            System.out.println(CommonUtils.createUUID());
//        }
////        File file = new File("D:\\test\\test1\\test2");
////        file.mkdirs();
////        File file1 = new File("D:\\test");
////        file1.mkdirs();
////        File file2 = new File("D:\\test\\test1\\test2\\test3");
////        file2.mkdirs();
////        System.out.println(file.exists());
//    }
}
