package com.bit.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.bit.base.exception.BusinessException;
import com.bit.module.cbo.vo.ResidentHomeCareRosterExcelVO;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @Description :
 * @Date ： 2019/1/3 15:12
 */
public class ExcelHandler {

    public static <T> void exportExcelFile(HttpServletResponse response, List<T> exportData,
                                       Class<T> clazz, String fileName) throws IOException {
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        ExportParams exportParams = new ExportParams();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, clazz, exportData);
        workbook.write(response.getOutputStream());
    }

    public static <T> void exportProtection(HttpServletResponse response, List<T> exportData,
                                            Class<T> clazz, String fileName, List<Integer> cells, String password) throws IOException {
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        ExportParams exportParams = new ExportParams();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, clazz, exportData);
        lockCells(cells, password, workbook);
        workbook.write(response.getOutputStream());
    }

    public static <T> List<T> importExcelFile(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> clazz) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
        } catch (NoSuchElementException e) {
            throw new BusinessException("excel文件不能为空");
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return list;
    }

    private static CellStyle setUnlockStyle(Workbook workbook) {
        CellStyle unlockStyle = workbook.createCellStyle();
        unlockStyle.setLocked(false);
        unlockStyle.setAlignment(HorizontalAlignment.CENTER);
        unlockStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return unlockStyle;
    }

    private static void lockCells(List<Integer> cells, String password, Workbook workbook) {
        CellStyle unlockStyle = setUnlockStyle(workbook);
        Sheet sheet = workbook.getSheetAt(0);
        sheet.protectSheet(password);
        // 标题进行单元格保护
        for (int row = 1; row < sheet.getLastRowNum() + 1; row++) {
            for (int cell = 0; cell < sheet.getRow(row).getLastCellNum(); cell++) {
                // 找到不需要加密的单元格
                if (!cells.contains(cell)) {
                    sheet.getRow(row).getCell(cell).setCellStyle(unlockStyle);
                }
            }
        }
    }

    /**
     * 导出excel
     * @param list 导出的集合
     * @param fileName 文件名称
     * @param clazz excel 文件列
     * @param response
     */
    public static void  exportExcel(List list, String fileName, String sheetName , Class clazz, HttpServletResponse response){
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "UTF-8")+".xls";
            response.setHeader("content-type","application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            //导出操作
            ExcelUtil.exportExcel(list,null,sheetName,clazz, fileName,response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
