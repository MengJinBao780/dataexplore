package cn.csdb.service.fileview;

import cn.csdb.model.FileInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatatableStrategy extends Strategy{
    @Override
    public String handleView(FileInfo fileInfo, Model model){
        String filePath = fileInfo.getFilePath();
        List<List<List<String>>> sheets = new ArrayList<>();
        List<String> sheetNames = new ArrayList<>();
        File file = new File(("//"+filePath).replaceAll("\\\\", "//").replaceAll("//", "/") );

        if (file.exists() && file.isFile()) {
            try {
                //InputStream is = new FileInputStream("D:\\" + filePath);
                InputStream is = new FileInputStream(("//" + filePath).replaceAll("\\\\", "//").replaceAll("//", "/"));
                String excelType = filePath.substring(filePath.lastIndexOf("."));
                if (excelType.trim().equals(".xls")) {
                    HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
                    int size = hssfWorkbook.getNumberOfSheets();
                    // 循环每一页，并处理当前循环页
                    for (int numSheet = 0; numSheet < size; numSheet++) {
                        // HSSFSheet 标识某一页
                        List<List<String>> lists = new ArrayList<>();
                        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                        if (hssfSheet == null) {
                            continue;
                        }
                        HSSFRow firstRow = hssfSheet.getRow(0);
                        if (firstRow == null) {
                            continue;
                        }
                        sheetNames.add(hssfSheet.getSheetName());
                        int maxColIx = firstRow.getLastCellNum();
                        // 处理当前页，循环读取每一行
                        for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                            // HSSFRow表示行
                            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                            List<String> rowList = new ArrayList<String>();
                            // 遍历改行，获取处理每个cell元素
                            for (int colIx = 0; colIx < maxColIx; colIx++) {
                                // HSSFCell 表示单元格
                                HSSFCell cell = hssfRow.getCell(colIx);
                                if (cell == null) {
                                    rowList.add("");
                                    continue;
                                }
                                rowList.add(cell.toString());
                            }
                            lists.add(rowList);
                        }
                        sheets.add(lists);
                    }
                } else if (excelType.trim().equals(".xlsx")) {
                    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
                    int size = xssfWorkbook.getNumberOfSheets();
                    // 循环每一页，并处理当前循环页
                    for (int numSheet = 0; numSheet < size; numSheet++) {
                        // HSSFSheet 标识某一页
                        List<List<String>> lists = new ArrayList<>();
                        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                        if (xssfSheet == null) {
                            continue;
                        }
                        XSSFRow firstRow = xssfSheet.getRow(0);
                        if (firstRow == null) {
                            continue;
                        }
                        sheetNames.add(xssfSheet.getSheetName());
                        int maxColIx = firstRow.getLastCellNum();
                        // 处理当前页，循环读取每一行
                        for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                            // XSSFRow表示行
                            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                            List<String> rowList = new ArrayList<String>();
                            // 遍历改行，获取处理每个cell元素
                            for (int colIx = 0; colIx < maxColIx; colIx++) {
                                // HSSFCell 表示单元格
                                XSSFCell cell = xssfRow.getCell(colIx);
                                if (cell == null) {
                                    rowList.add("");
                                    continue;
                                }
                                rowList.add(cell.toString());
                            }
                            lists.add(rowList);
                        }
                        sheets.add(lists);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        model.addAttribute("sheetNames",sheetNames);
        model.addAttribute("sheets",sheets);
        return "excel";
    }
}
