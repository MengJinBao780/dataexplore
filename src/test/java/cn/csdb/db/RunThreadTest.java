package cn.csdb.db;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RunThreadTest {
    @Resource
    private MongoTemplate mongoTemplate;
    //读取excel2003-2006
    @Test
    public void readExcel()throws IOException {

        File file = new File("D:/file/20元数据处理hf/metadata_modis_mod09gq.xls");
        if (file.exists() && file.isFile()) {
            InputStream is = new FileInputStream("D:/file/20元数据处理hf/metadata_modis_mod09gq.xls");
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
            int size = hssfWorkbook.getNumberOfSheets();
            // 循环每一页，并处理当前循环页
            for (int numSheet = 0; numSheet < size; numSheet++) {
                // HSSFSheet 标识某一页
                List<List<String>> rows = new ArrayList<>();
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                HSSFRow firstRow = hssfSheet.getRow(0);
                if (firstRow==null){
                    continue;
                }
                int maxColIx = firstRow.getLastCellNum();
                // 处理当前页，循环读取每一行
                for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    // HSSFRow表示行
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow==null){
                        continue;
                    }
                    List<String> row = new ArrayList<>();
                    // 遍历改行，获取处理每个cell元素
                    for (int colIx = 0; colIx < maxColIx; colIx++) {
                        // HSSFCell 表示单元格
                        HSSFCell cell = hssfRow.getCell(colIx);
                        if (cell == null) {
                            row.add("");
                            continue;
                        }
                        row.add(cell.toString());
                    }
                    rows.add(row);
                }
                Thread thread = new ThreadTest(rows,mongoTemplate);
                thread.start();
                System.out.println(numSheet);
            }
        }
    }
}
