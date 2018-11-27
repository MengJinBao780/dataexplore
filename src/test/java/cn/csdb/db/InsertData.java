package cn.csdb.db;

import cn.csdb.model.*;
import cn.csdb.service.FileTypeService;
import cn.csdb.service.SdoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class InsertData {
    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private SdoService sdoService;

    @Resource
    private FileTypeService fileTypeService;

    String fileRootPath = "C:\\Users\\pirate\\Desktop\\gscloud导出全部数据\\";

    //读取excel2003-2006
    public List<List<String>> readExcel(String filePath) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            InputStream is = new FileInputStream(filePath);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
            int size = hssfWorkbook.getNumberOfSheets();
            // 循环每一页，并处理当前循环页
            for (int numSheet = 0; numSheet < size; numSheet++) {
                // HSSFSheet 标识某一页
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                HSSFRow firstRow = hssfSheet.getRow(0);
                if (firstRow == null) {
                    continue;
                }
                int maxColIx = firstRow.getLastCellNum();
                // 处理当前页，循环读取每一行
                for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    // HSSFRow表示行
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
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
            }
        }
        return rows;
    }

    //读取excel2007+
    public List<List<String>> readExcel2007(String filePath) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            InputStream is = new FileInputStream(filePath);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            int size = xssfWorkbook.getNumberOfSheets();
            // 循环每一页，并处理当前循环页
            for (int numSheet = 0; numSheet < size; numSheet++) {
                // XSSFSheet 标识某一页
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                XSSFRow firstRow = xssfSheet.getRow(0);
                if (firstRow == null) {
                    continue;
                }
                int maxColIx = firstRow.getLastCellNum();
                // 处理当前页，循环读取每一行
                for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    // XSSFRow表示行
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
                    List<String> row = new ArrayList<>();
                    // 遍历改行，获取处理每个cell元素
                    for (int colIx = 0; colIx < maxColIx; colIx++) {
                        // HSSFCell 表示单元格
                        XSSFCell cell = xssfRow.getCell(colIx);
                        if (cell == null) {
                            row.add("");
                            continue;
                        }
                        row.add(cell.toString());
                    }
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    //根据学科名字获得id
    public String getCatalogIdByName(String name) {
        DBObject query = QueryBuilder.start().and("c_name").is(name).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.findOne(basicQuery, Catalog.class).getId();
    }

    //第一步----读取excel插入学科目录catalog数据
    @Test
    public void insertCatalog() throws IOException {
        List<List<String>> data = readExcel("D:/catalog.xls");
        for (int i = 0; i < data.size(); i++) {
            Catalog catalog = new Catalog();
            catalog.setcName(data.get(i).get(0));
            catalog.setcCode(data.get(i).get(1));
            if (data.get(i).get(3).equals("0")) {
                catalog.setParentId("0");
            } else {
                catalog.setParentId(getCatalogIdByName(data.get(i).get(2)));
            }
            catalog.setIsLeaf(Integer.parseInt(data.get(i).get(3)));
            catalog.setOrder(Integer.parseInt(data.get(i).get(4)));
            catalog.setStatus(Integer.parseInt(data.get(i).get(5)));
            mongoTemplate.save(catalog);
        }
    }

    //根据产品名称得到产品id
    public String getProductIdByName(String name) {
        DBObject query = QueryBuilder.start().and("prod_name").is(name).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.findOne(basicQuery, Product.class).getId();
    }

    //第二步----读取excel向mongodb插入sdo数据
    @Test
    public void insertSdo() throws IOException, ParseException {
        List<List<String>> data = readExcel("D:/sdo.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < data.size(); i++) {
            Sdo sdo = new Sdo();
            sdo.setPid(data.get(i).get(0));
            sdo.setLoginId(data.get(i).get(1));
            sdo.setCatalogId(getCatalogIdByName(data.get(i).get(2)));
            sdo.setProductId(getProductIdByName(data.get(i).get(3)));
            sdo.setTitle(data.get(i).get(4));
            sdo.setDesc(data.get(i).get(5));
            sdo.setKeyword(data.get(i).get(6));
            sdo.setIconPath(data.get(i).get(7));
            sdo.setCreatorOrganization(JSON.parseArray(data.get(i).get(8), Organization.class));
            sdo.setCreatorCreateTime(DateUtils.addHours(sdf.parse(data.get(i).get(9)), 8));
            sdo.setPublisher(JSON.parseObject(data.get(i).get(10), Organization.class));
            sdo.setPublisherPublishTime(DateUtils.addHours(sdf.parse(data.get(i).get(11)), 8));
            sdo.setScores(Double.parseDouble(data.get(i).get(12)));
            sdo.setVisitCount(Integer.parseInt(data.get(i).get(13)));
            sdo.setDownloadCount(Integer.parseInt(data.get(i).get(14)));
            sdo.setStatus(Integer.parseInt(data.get(i).get(15)));
            sdo.setDataFormat(data.get(i).get(16));
            sdo.setTaxonomy(data.get(i).get(17));
            sdo.setPrdTaxonomy(data.get(i).get(18));
            sdo.setVisitLimit(data.get(i).get(19));
            sdo.setRangeDescription(data.get(i).get(20));
            sdo.setCenter(data.get(i).get(21));
            sdo.setUpLeft(data.get(i).get(22));
            sdo.setLowLeft(data.get(i).get(23));
            sdo.setUpRight(data.get(i).get(24));
            sdo.setLowRight(data.get(i).get(25));
            sdo.setStartTime(DateUtils.addHours(sdf.parse(data.get(i).get(26)), 8));
            sdo.setEndTime(DateUtils.addHours(sdf.parse(data.get(i).get(27)), 8));
            sdo.setSpatialResolution(data.get(i).get(28));
            sdo.setTimeResolution(data.get(i).get(29));
            sdo.setRightstatement(data.get(i).get(30));
            sdo.setToMemorySize(Double.parseDouble(data.get(i).get(31)));
            sdo.setToFilesNumber(Integer.parseInt(data.get(i).get(32)));
            sdo.setToRecordNumber(Integer.parseInt(data.get(i).get(33)));
            sdo.setTags(Arrays.asList(data.get(i).get(34).split(";")));
            sdo.setCreateTime(new Date());
            mongoTemplate.save(sdo);
        }
    }

    //判断tag标签是否存在，存在num+1,不存在插入tag,并获取id
    public String checkNameInTag(String productId, String tagName) {
        DBObject query = QueryBuilder.start().and("prod_id").is(productId).and("tag_name").is(tagName).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Tag tag = mongoTemplate.findOne(basicQuery, Tag.class);
        if (tag == null) {
            Tag data = new Tag();
            data.setTagName(tagName);
            data.setNumber(1);
            data.setProdId(productId);
            mongoTemplate.save(data);
        } else {
            Update count = Update.update("number", tag.getNumber() + 1);
            mongoTemplate.updateFirst(basicQuery, count, Tag.class);
        }
        return mongoTemplate.findOne(basicQuery, Tag.class).getId();
    }

    //第三步----将sdo中的tag标签添加到tag表和tag_mapping表,FileType表
    @Test
    public void insertTagAndTagMappingAndFileType() {
        List<Sdo> sdos = mongoTemplate.findAll(Sdo.class);
        for (Sdo sdo : sdos) {
            for (String tag : sdo.getTags()) {
                String tagId = checkNameInTag(sdo.getProductId(), tag);
                TagMapping tagMapping = new TagMapping();
                tagMapping.setTagId(tagId);
                tagMapping.setSdoId(sdo.getId());
                mongoTemplate.save(tagMapping);
            }
            FileType fileType = new FileType();
            fileType.setSdoId(sdo.getId());
            fileType.setFtName(sdo.getDataFormat());
            fileType.setFtDesc(sdo.getDataFormat());
            fileType.setFtIcon("#");
            fileType.setFtPhyType(sdo.getDataFormat());
            fileType.setFtDescAttch(sdo.getDataFormat());
            fileType.setPreviewType(sdo.getDataFormat());
            fileType.setCreateTime(DateUtils.addHours(new Date(), 8));
            fileType.setUpdateTime(DateUtils.addHours(new Date(), 8));
            fileType.setStatus(1);
            mongoTemplate.save(fileType);
        }
    }

    //插入cern数据
    @Test
    public void insertFileInfo_CERN() throws IOException {
        List<List<String>> data = readExcel2007(fileRootPath + "cern文件元数据20180621.xlsx");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 2; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(5));
            fileInfo.setFilePath(row.get(6));

            //fileInfo.setFtId(row.get(2));
            fileInfo.setFtName("xlsx");
            fileInfo.setPreviewType(row.get(3));
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid("");
            fileInfo.setNumber("");
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(8));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(13));
            fileInfo.setUpLeft(row.get(14));
            fileInfo.setUpRight(row.get(15));
            fileInfo.setLowRight(row.get(16));
            fileInfo.setLowLeft(row.get(17));


            //根据名称来获取sdoId
            String title = row.get(0);
            Sdo sdo = sdoService.getByTitle(title);
            //fileInfo.setSdoPid(row.get(1));
            fileInfo.setSdoPid(sdo.getId());

            FileType fileType = fileTypeService.getBySdoId(sdo.getId());
            if (fileType != null) {
                fileInfo.setFtId(fileType.getId());
            }

            mongoTemplate.save(fileInfo);
        }
    }

    //插入fileinfo数据metadata_modis_l1b_mod021km.xls
    @Test
    public void insertFileInfo_1() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_l1b_mod021km.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("293");
            fileInfo.setFtId("5b2c6e5d9ef7101a407846eb");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));

            try {
                fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
                fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            } catch (ParseException e) {
                continue;
            }
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082d9");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_l1b_mod02hkm.xls
    @Test
    public void insertFileInfo_2() throws IOException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_l1b_mod02hkm.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("294");
            fileInfo.setFtId("5b2c6e5d9ef7101a407846f1");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            try {
                fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
                fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            } catch (ParseException e) {
                continue;
            }

            fileInfo.setSdoPid("5b2c6df29ef7101a58a082da");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_l1b_mod02qkm.xls
    @Test
    public void insertFileInfo_3() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_l1b_mod02qkm.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("295");
            fileInfo.setFtId("5b2c6e5d9ef7101a407846f7");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082db");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_l1b_mod03
    @Test
    public void insertFileInfo_4() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_l1b_mod03.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("296");
            fileInfo.setFtId("5b2c6e5d9ef7101a407846fc");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082dc");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_eo1_ali.xls
    @Test
    public void insertFileInfo_5() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_eo1_ali.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("314");
            fileInfo.setFtId("5b2c6e5d9ef7101a40784701");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082dd");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_eo1.xls
    @Test
    public void insertFileInfo_6() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_eo1.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("316");
            fileInfo.setFtId("5b2c6e5d9ef7101a40784706");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082de");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_mod09gq.xls
    @Test
    public void insertFileInfo_7() throws IOException, ParseException {
        /*List<List<String>> data = readExcel(fileRootPath+"metadata_modis_mod09gq.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i=1;i<data.size();i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("256");
            fileInfo.setFtId("5b2c6e5d9ef7101a4078470a");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now,8));
            fileInfo.setUpdateTime(DateUtils.addHours(now,8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)),8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)),8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082df");
            mongoTemplate.save(fileInfo);
        }*/


        File file = new File(fileRootPath + "metadata_modis_mod09gq.xls");
        if (file.exists() && file.isFile()) {
            InputStream is = new FileInputStream(fileRootPath + "metadata_modis_mod09gq.xls");
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
            int size = hssfWorkbook.getNumberOfSheets();
            // 循环每一页，并处理当前循环页
            List<List<String>> rows = new ArrayList<>();
            for (int numSheet = 0; numSheet < size; numSheet++) {
                // HSSFSheet 标识某一页
                rows.clear();
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                HSSFRow firstRow = hssfSheet.getRow(0);
                if (firstRow == null) {
                    continue;
                }
                int maxColIx = firstRow.getLastCellNum();
                // 处理当前页，循环读取每一行
                for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    // HSSFRow表示行
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                for (int i = 1; i < rows.size(); i++) {
                    List<String> row = rows.get(i);
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileName(row.get(1));
                    fileInfo.setFilePath("256");
                    fileInfo.setFtId("5b2c6e5d9ef7101a4078470a");
                    fileInfo.setFtName("HDF");
                    fileInfo.setPreviewType("HDF");
                    fileInfo.setPreviewFile("");
                    fileInfo.setCreateTime(DateUtils.addHours(now, 8));
                    fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
                    fileInfo.setStatus(1);
                    fileInfo.setPid(row.get(0));
                    fileInfo.setNumber(row.get(4));
                    fileInfo.setCloudiness(0.0);
                    fileInfo.setNote("");
                    fileInfo.setSize(row.get(3));
                    fileInfo.setRecordNum(0);
                    fileInfo.setDataNote("");
                    fileInfo.setProperty("");
                    fileInfo.setCenter(row.get(5));
                    fileInfo.setUpLeft(row.get(6));
                    fileInfo.setUpRight(row.get(7));
                    fileInfo.setLowRight(row.get(8));
                    fileInfo.setLowLeft(row.get(9));
                    fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
                    fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
                    fileInfo.setSdoPid("5b2c6df29ef7101a58a082df");
                    mongoTemplate.save(fileInfo);
                }
            }
        }
    }

    //metadata_modis_mod09ga.xls
    @Test
    public void insertFileInfo_8() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_mod09ga.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("255");
            fileInfo.setFtId("5b2c6e5d9ef7101a4078470e");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e0");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_mod09q1.xls
    @Test
    public void insertFileInfo_9() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_mod09q1.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("257");
            fileInfo.setFtId("5b2c6e5e9ef7101a4078471b");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e1");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_mod09a1.xls
    @Test
    public void insertFileInfo_10() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_mod09a1.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("254");
            fileInfo.setFtId("5b2c6e5e9ef7101a40784723");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e2");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_mod11a1.xls
    @Test
    public void insertFileInfo_11() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_mod11a1.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("258");
            fileInfo.setFtId("5b2c6e5e9ef7101a4078472c");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e3");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_mod11a2.xls
    @Test
    public void insertFileInfo_12() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_mod11a2.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("259");
            fileInfo.setFtId("5b2c6e5e9ef7101a40784736");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e4");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_modis_mod11b1.xls
    @Test
    public void insertFileInfo_13() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_modis_mod11b1.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("260");
            fileInfo.setFtId("5b2c6e5e9ef7101a4078473e");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e5");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_mosaic_8497.xls
    @Test
    public void insertFileInfo_14() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_mosaic_8497.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("248");
            fileInfo.setFtId("5b2c6e5e9ef7101a40784747");
            fileInfo.setFtName("GEOTIFF");
            fileInfo.setPreviewType("GEOTIFF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e6");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_mosaic.xls
    @Test
    public void insertFileInfo_15() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_mosaic.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("247");
            fileInfo.setFtId("5b2c6e5e9ef7101a40784754");
            fileInfo.setFtName("SID");
            fileInfo.setPreviewType("SID");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));

            try {
                fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
                fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            } catch (ParseException e) {
                continue;
            }

            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e7");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_dem_srtm.xls
    @Test
    public void insertFileInfo_16() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_dem_srtm.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("305");
            fileInfo.setFtId("5b2c6e5e9ef7101a40784761");
            fileInfo.setFtName("IMG");
            fileInfo.setPreviewType("IMG");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e8");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_dem_gdem.xls
    @Test
    public void insertFileInfo_17() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "metadata_dem_gdem.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("310");
            fileInfo.setFtId("5b2c6e5f9ef7101a4078476a");
            fileInfo.setFtName("IMG");
            fileInfo.setPreviewType("IMG");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082e9");
            mongoTemplate.save(fileInfo);
        }
    }

    //metadata_dem_gdem_1.xls
    /*@Test
    public void insertFileInfo_18() throws IOException,ParseException{
        List<List<String>> data = readExcel(fileRootPath+"metadata_dem_gdem_1.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i=1;i<data.size();i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("421");
            fileInfo.setFtId("5b2c6e5f9ef7101a40784772");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now,8));
            fileInfo.setUpdateTime(DateUtils.addHours(now,8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082ea");
            mongoTemplate.save(fileInfo);
        }
    }*/

    //09C1.xls
    @Test
    public void insertFileInfo_19() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "09C1.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("363");
            fileInfo.setFtId("5b2c6e5f9ef7101a40784776");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082eb");
            mongoTemplate.save(fileInfo);
        }
    }

    //13C1.xls
    @Test
    public void insertFileInfo_20() throws IOException, ParseException {
        List<List<String>> data = readExcel(fileRootPath + "13C1.xls");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(row.get(1));
            fileInfo.setFilePath("364");
            fileInfo.setFtId("5b2c6e5f9ef7101a4078477a");
            fileInfo.setFtName("HDF");
            fileInfo.setPreviewType("HDF");
            fileInfo.setPreviewFile("");
            fileInfo.setCreateTime(DateUtils.addHours(now, 8));
            fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
            fileInfo.setStatus(1);
            fileInfo.setPid(row.get(0));
            fileInfo.setNumber(row.get(4));
            fileInfo.setCloudiness(0.0);
            fileInfo.setNote("");
            fileInfo.setSize(row.get(3));
            fileInfo.setRecordNum(0);
            fileInfo.setDataNote("");
            fileInfo.setProperty("");
            fileInfo.setCenter(row.get(5));
            fileInfo.setUpLeft(row.get(6));
            fileInfo.setUpRight(row.get(7));
            fileInfo.setLowRight(row.get(8));
            fileInfo.setLowLeft(row.get(9));
            fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
            fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
            fileInfo.setSdoPid("5b2c6df29ef7101a58a082ec");
            mongoTemplate.save(fileInfo);
        }
    }


    //    插入所有fileinfo数据
    @Test
    public void insertAllFileInfoData() throws IOException, ParseException {
//        insertFileInfo_CERN();
//        insertFileInfo_1();
//        insertFileInfo_2();
//        insertFileInfo_3();
//        insertFileInfo_4();
//        insertFileInfo_5();
//        insertFileInfo_6();

//        insertFileInfo_7();
//        insertFileInfo_8();

//        insertFileInfo_9();
//        insertFileInfo_10();

//        insertFileInfo_11();

        insertFileInfo_12();

//        insertFileInfo_13();

        insertFileInfo_14();
        insertFileInfo_15();
        insertFileInfo_16();
        insertFileInfo_17();
        insertFileInfo_19();
        insertFileInfo_20();
    }

    @Test
    public void updateCSTR() {
        List<Sdo> list = sdoService.getAll();
        for (Sdo sdo : list) {
            updateSdo(sdo);
        }
    }

    public void updateSdo(Sdo sdo) {
        JSONObject jsonObject = new JSONObject();
        JSONObject metadata = new JSONObject();
        metadata.put("resId", sdo.getId());
        metadata.put("idType", "handle,CSTR");
        metadata.put("otherId", "doi,pid,handle");
        metadata.put("resTitle", sdo.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        metadata.put("upDate", sdf.format(sdo.getCreateTime()));
        metadata.put("descrp", sdo.getDesc());
        metadata.put("keyword", sdo.getKeyword());
        metadata.put("accessConst", "1");
        metadata.put("onlAdd", "http://localhost:8080/sdo/detail/" + sdo.getId());
        metadata.put("idPoC", "cnic");
        //metadata.put("resCat","{\"catName\":\"科技资源\",\"catCode\":\"11\",\"catStdName\":\"分类标准名称\",\"catStdVer\":\"v1.0\"}");
        JSONObject json = new JSONObject();
        json.put("catName", "科技资源");
        json.put("catCode", "11");
        json.put("catStdName", "分类标准名称");
        json.put("catStdVer", "v1.0");
        metadata.put("resCat", json);

        metadata.put("creator", sdo.getCreatorOrganization());
        metadata.put("pubOrgName", sdo.getPublisher());

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("metadata", metadata);
        map.put("action", "post");
        list.add(map);
        jsonObject.put("dataModels", list);

        System.out.println("\n\n" + jsonObject);

        String cstr = getCSTR(jsonObject.toString());


        //更新cstr标识符
        //sdo.set
        //mongoTemplate.save(sdo);
    }


    public String getCSTR(String message) {
        String response = "";
        try {
            //创建URL对象
            long start = System.currentTimeMillis();
            URL url = new URL("http://14.23.68.86:9001/register");
            //返回一个URLConnection对象，它表示到URL所引用的远程对象的连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //在这里设置一些属性，详细见UrlConnection文档，HttpURLConnection是UrlConnection的子类
            //设置连接超时为5秒
            //httpURLConnection.setConnectTimeout(100000);
            //设定请求方式(默认为get)
            httpURLConnection.setRequestMethod("POST");
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            httpURLConnection.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            // 连接，从上述url.openConnection()至此的配置必须要在connect之前完成，
            httpURLConnection.connect();

            //这边设置请内容
            //getOutputStream()里默认就有connect（）了，可以不用写上面的连接
            //接下来我们设置post的请求参数，可以是JSON数据，也可以是普通的数据类型
            OutputStream outputStream = httpURLConnection.getOutputStream();
//	             DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            String content = "username=bigdata&password=bigdata&content=" + message;
//	             dataOutputStream.writeBytes(content);
//	             dataOutputStream.flush();
//	             dataOutputStream.close();
            outputStream.write(content.getBytes());
            outputStream.flush();
            //读取返回的数据
            //返回打开连接读取的输入流，输入流转化为StringBuffer类型，这一套流程要记住，常用
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                //转化为UTF-8的编码格式
                line = new String(line.getBytes("UTF-8"));
                stringBuffer.append(line);
            }
            bufferedReader.close();
            httpURLConnection.disconnect();
            long end = System.currentTimeMillis();
            System.err.println((end - start) + " ms");
            System.out.println(stringBuffer);

            return stringBuffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }




    @Test
    public void metadata_modis_mod09ga() throws IOException, ParseException {
        File file = new File(fileRootPath+"metadata_modis_mod09ga");
        String [] fileName = file.list();

        for (int j = 0; j < fileName.length; j++) {
            List<List<String>> data = readExcel(fileRootPath+"metadata_modis_mod09ga\\"+fileName[j]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            for (int i = 0; i < data.size(); i++) {
                List<String> row = data.get(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(row.get(1));
                fileInfo.setFilePath("255");
                fileInfo.setFtId("5b2c6e5d9ef7101a4078470e");
                fileInfo.setFtName("HDF");
                fileInfo.setPreviewType("HDF");
                fileInfo.setPreviewFile("");
                fileInfo.setCreateTime(DateUtils.addHours(now, 8));
                fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
                fileInfo.setStatus(1);
                fileInfo.setPid(row.get(0));
                fileInfo.setNumber(row.get(4));
                fileInfo.setCloudiness(0.0);
                fileInfo.setNote("");
                fileInfo.setSize(row.get(3));
                fileInfo.setRecordNum(0);
                fileInfo.setDataNote("");
                fileInfo.setProperty("");
                fileInfo.setCenter(row.get(5));
                fileInfo.setUpLeft(row.get(6));
                fileInfo.setUpRight(row.get(7));
                fileInfo.setLowRight(row.get(8));
                fileInfo.setLowLeft(row.get(9));
                fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
                fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
                fileInfo.setSdoPid("5b2c6df29ef7101a58a082e0");
                mongoTemplate.save(fileInfo);
            }
        }

    }

    @Test
    public void metadata_modis_mod09gq() throws IOException, ParseException {
        File file = new File(fileRootPath+"metadata_modis_mod09gq");
        String [] fileName = file.list();

        for (int j = 0; j < fileName.length; j++) {
            List<List<String>> data = readExcel(fileRootPath+"metadata_modis_mod09gq\\"+fileName[j]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            for (int i = 0; i < data.size(); i++) {
                List<String> row = data.get(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(row.get(1));
                fileInfo.setFilePath("256");
                fileInfo.setFtId("5b2c6e5d9ef7101a4078470a");
                fileInfo.setFtName("HDF");
                fileInfo.setPreviewType("HDF");
                fileInfo.setPreviewFile("");
                fileInfo.setCreateTime(DateUtils.addHours(now, 8));
                fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
                fileInfo.setStatus(1);
                fileInfo.setPid(row.get(0));
                fileInfo.setNumber(row.get(4));
                fileInfo.setCloudiness(0.0);
                fileInfo.setNote("");
                fileInfo.setSize(row.get(3));
                fileInfo.setRecordNum(0);
                fileInfo.setDataNote("");
                fileInfo.setProperty("");
                fileInfo.setCenter(row.get(5));
                fileInfo.setUpLeft(row.get(6));
                fileInfo.setUpRight(row.get(7));
                fileInfo.setLowRight(row.get(8));
                fileInfo.setLowLeft(row.get(9));
                fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
                fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
                fileInfo.setSdoPid("5b2c6df29ef7101a58a082df");
                mongoTemplate.save(fileInfo);
            }
        }
//        3248336   4187485  939149
    }

    @Test
    public void metadata_modis_mod11a1() throws IOException, ParseException {
        File file = new File(fileRootPath+"metadata_modis_mod11a1");
        String [] fileName = file.list();

        for (int j = 0; j < fileName.length; j++) {
            List<List<String>> data = readExcel(fileRootPath+"metadata_modis_mod11a1\\"+fileName[j]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            for (int i = 0; i < data.size(); i++) {
                List<String> row = data.get(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(row.get(1));
                fileInfo.setFilePath("258");
                fileInfo.setFtId("5b2c6e5e9ef7101a4078472c");
                fileInfo.setFtName("HDF");
                fileInfo.setPreviewType("HDF");
                fileInfo.setPreviewFile("");
                fileInfo.setCreateTime(DateUtils.addHours(now, 8));
                fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
                fileInfo.setStatus(1);
                fileInfo.setPid(row.get(0));
                fileInfo.setNumber(row.get(4));
                fileInfo.setCloudiness(0.0);
                fileInfo.setNote("");
                fileInfo.setSize(row.get(3));
                fileInfo.setRecordNum(0);
                fileInfo.setDataNote("");
                fileInfo.setProperty("");
                fileInfo.setCenter(row.get(5));
                fileInfo.setUpLeft(row.get(6));
                fileInfo.setUpRight(row.get(7));
                fileInfo.setLowRight(row.get(8));
                fileInfo.setLowLeft(row.get(9));
                fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
                fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
                fileInfo.setSdoPid("5b2c6df29ef7101a58a082e3");
                mongoTemplate.save(fileInfo);
            }
        }

    }
//4297156

    @Test
    public void metadata_modis_mod11b1() throws IOException, ParseException {
        File file = new File(fileRootPath+"metadata_modis_mod11b1");
        String [] fileName = file.list();

        for (int j = 0; j < fileName.length; j++) {
            List<List<String>> data = readExcel(fileRootPath+"metadata_modis_mod11b1\\"+fileName[j]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            for (int i = 0; i < data.size(); i++) {
                List<String> row = data.get(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(row.get(1));
                fileInfo.setFilePath("260");
                fileInfo.setFtId("5b2c6e5e9ef7101a4078473e");
                fileInfo.setFtName("HDF");
                fileInfo.setPreviewType("HDF");
                fileInfo.setPreviewFile("");
                fileInfo.setCreateTime(DateUtils.addHours(now, 8));
                fileInfo.setUpdateTime(DateUtils.addHours(now, 8));
                fileInfo.setStatus(1);
                fileInfo.setPid(row.get(0));
                fileInfo.setNumber(row.get(4));
                fileInfo.setCloudiness(0.0);
                fileInfo.setNote("");
                fileInfo.setSize(row.get(3));
                fileInfo.setRecordNum(0);
                fileInfo.setDataNote("");
                fileInfo.setProperty("");
                fileInfo.setCenter(row.get(5));
                fileInfo.setUpLeft(row.get(6));
                fileInfo.setUpRight(row.get(7));
                fileInfo.setLowRight(row.get(8));
                fileInfo.setLowLeft(row.get(9));
                fileInfo.setBeginTime(DateUtils.addHours(sdf.parse(row.get(10)), 8));
                fileInfo.setEndTime(DateUtils.addHours(sdf.parse(row.get(11)), 8));
                fileInfo.setSdoPid("5b2c6df29ef7101a58a082e5");
                mongoTemplate.save(fileInfo);
            }
        }

    }
}


