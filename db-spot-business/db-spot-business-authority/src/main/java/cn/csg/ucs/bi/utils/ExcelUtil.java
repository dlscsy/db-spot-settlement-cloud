package cn.csg.ucs.bi.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @author: lsj
 * @create: 2019/12/4
 **/
public class ExcelUtil {

    /**
     * @param @param filePath  Excel文件路径
     * @param @param handers   Excel列标题(数组)
     * @param @param downData  下拉框数据(数组)
     * @param @param downRows  下拉列的序号(数组,序号从0开始)
     *               * @param @param types  列格式
     * @return void
     * @throws
     * @Title: createExcelTemplate
     * @Description: 生成Excel导入模板
     */
    public static File createExcelTemplate(String filePath, String[] handers,
                                           List<String[]> downData, String[] downRows, JSONArray types) {
        File f = new File(filePath); //写文件
        HSSFWorkbook wb = new HSSFWorkbook();//创建工作薄

        //表头样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        //字体样式
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short) 12);
        //fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(fontStyle);
        HSSFFont redfontStyle = wb.createFont();
        redfontStyle.setFontName("微软雅黑");
        redfontStyle.setFontHeightInPoints((short) 12);
        redfontStyle.setColor(Font.COLOR_RED);
        ;
        //新建sheet
        HSSFSheet sheet1 = wb.createSheet("Sheet1");
        HSSFSheet sheet2 = wb.createSheet("Sheet2");
        //HSSFSheet sheet3 = wb.createSheet("Sheet3");

        //生成sheet1内容
        HSSFRow rowFirst = sheet1.createRow(0);//第一个sheet的第一行为标题

        // 6 添加单元格注释
        // 7 创建HSSFPatriarch对象,HSSFPatriarch是所有注释的容器.
        HSSFPatriarch patr = sheet1.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("凡带下拉框单元格不可复制粘贴,其余单元格可复制粘贴。日期类型格式如：2019-01-01，月份格式如：2019-01"));
        //设置列格式
        for (int i = 0; i < types.size(); i++) {
            JSONObject json = types.getJSONObject(i);
            HSSFCellStyle cellstyle = wb.createCellStyle();
            HSSFDataFormat format = wb.createDataFormat();
            cellstyle.setDataFormat(format.getFormat(json.getString("format")));  // 格式
            sheet1.setDefaultColumnStyle((Integer) json.get("column"), cellstyle); // 设置格式
        }
        for (int i = 0; i < handers.length; i++) {
            String column = handers[i];
            HSSFCell cell = rowFirst.createCell(i); //获取第一行的每个单元格
            sheet1.setColumnWidth(i, 4000); //设置每列的列宽
            if (column.contains("*")) {
                HSSFRichTextString richString = new HSSFRichTextString(column);
                richString.applyFont(column.indexOf("*"), column.indexOf("*") + 1, redfontStyle);
                cell.setCellValue(richString);
            } else {
                cell.setCellStyle(style); //加样式
                cell.setCellValue(column); //往单元格里写数据
            }
            if (i == 0) {
                cell.setCellComment(comment);//添加注释
            }
        }
        //设置下拉框数据
        String[] arr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int index = 0;
        HSSFRow row = null;
        for (int r = 0; r < downRows.length; r++) {
            String[] dlData = downData.get(r);//获取下拉对象
            int rownum = Integer.parseInt(downRows[r]);

            if (dlData.length < 5) { //255以内的下拉
                //255以内的下拉,参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
                sheet1.addValidationData(setDataValidation(sheet1, dlData, 1, 50000, rownum, rownum)); //超过255个报错
            } else { //255以上的下拉，即下拉列表元素很多的情况

                //1、设置有效性
                //String strFormula = "Sheet2!$A$1:$A$5000" ; //Sheet2第A1到A5000作为下拉列表来源数据
                String strFormula = "Sheet2!$" + arr[index] + "$1:$" + arr[index] + "$" + dlData.length; //Sheet2第A1到A5000作为下拉列表来源数据
                sheet2.setColumnWidth(r, 4000); //设置每列的列宽
                //设置数据有效性加载在哪个单元格上,参数分别是：从sheet2获取A1到A5000作为一个下拉的数据、起始行、终止行、起始列、终止列
                sheet1.addValidationData(SetDataValidation(strFormula, 1, 50000, rownum, rownum)); //下拉列表元素很多的情况

                //2、生成sheet2内容
                for (int j = 0; j < dlData.length; j++) {
                    if (index == 0) { //第1个下拉选项，直接创建行、列
                        row = sheet2.createRow(j); //创建数据行
                        sheet2.setColumnWidth(j, 4000); //设置每列的列宽
                        row.createCell(0).setCellValue(dlData[j]); //设置对应单元格的值

                    } else { //非第1个下拉选项

                        int rowCount = sheet2.getLastRowNum();
                        //System.out.println("========== LastRowNum =========" + rowCount);
                        if (j <= rowCount) { //前面创建过的行，直接获取行，创建列
                            //获取行，创建列
                            sheet2.getRow(j).createCell(index).setCellValue(dlData[j]); //设置对应单元格的值

                        } else { //未创建过的行，直接创建行、创建列
                            sheet2.setColumnWidth(j, 4000); //设置每列的列宽
                            //创建行、创建列
                            sheet2.createRow(j).createCell(index).setCellValue(dlData[j]); //设置对应单元格的值
                        }
                    }
                }
                index++;
            }
        }

        try {


            //不存在则新增
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(f);
            out.flush();
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * @param @param  strFormula
     * @param @param  firstRow   起始行
     * @param @param  endRow     终止行
     * @param @param  firstCol   起始列
     * @param @param  endCol     终止列
     * @param @return
     * @return HSSFDataValidation
     * @throws
     * @Title: SetDataValidation
     * @Description: 下拉列表元素很多的情况 (255以上的下拉)
     */
    private static HSSFDataValidation SetDataValidation(String strFormula,
                                                        int firstRow, int endRow, int firstCol, int endCol) {

        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        dataValidation.createErrorBox("Error", "Error");
        dataValidation.createPromptBox("", null);

        return dataValidation;
    }


    /**
     * @param @param  sheet
     * @param @param  textList
     * @param @param  firstRow
     * @param @param  endRow
     * @param @param  firstCol
     * @param @param  endCol
     * @param @return
     * @return DataValidation
     * @throws
     * @Title: setDataValidation
     * @Description: 下拉列表元素不多的情况(255以内的下拉)
     */
    private static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol) {

        DataValidationHelper helper = sheet.getDataValidationHelper();
        //加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        //DVConstraint constraint = new DVConstraint();
        constraint.setExplicitListValues(textList);

        //设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList((short) firstRow, (short) endRow, (short) firstCol, (short) endCol);

        //数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
        //DataValidation data_validation = new DataValidation(regions, constraint);

        return data_validation;
    }

}
