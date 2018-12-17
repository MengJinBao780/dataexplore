package cn.csdb.controller;
import cn.csdb.model.*;
import cn.csdb.service.SdoService;
import cn.csdb.utils.UrlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.csdb.service.*;
import com.google.common.collect.Maps;
import com.sun.org.apache.bcel.internal.generic.INEG;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/sdo")
public class SdoController {
    private Logger logger= LoggerFactory.getLogger(SdoController.class);

    @Resource
    private SdoService sdoService;
    @Resource
    private CatalogService catalogService;
    @Resource
    private FileTypeService fileTypeService;
    @Resource
    private ProductService productService;
    @Resource
    private SdoDownloadService sdoDownloadService;
    @Resource
    private SdoFavoritesService sdoFavoritesService;
    @Resource
    private TagDetailService tagDetailService;
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private SdoVisitService sdoVisitService;
    @Resource
    private SdoCommentService sdoCommentService;
    @Resource
    private FileTemplateService fileTemplateService;
    @Resource
    private SdoRecomendationsService sdoRecomendationsService;

    @Resource
    private SdoSearchService sdoSearchService;

    @Resource
    private SdoRelationService sdoRelationService;

    @Resource
    private TableFieldComsService tableFieldComsService;

    /*
    //原来的
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, Model model) {
        Map<String,Object> sdoMap =  sdoService.visitSdo(id);
        model.addAttribute("tags",sdoMap.get("tags"));
        model.addAttribute("id",id);
        model.addAttribute("test","test");
        model.addAttribute("id",id);
        return "sdoDetail";
        */

    //cql添加收藏后的
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, Model model, HttpSession session) {
        Map<String, Object> sdoMap = sdoService.visitSdo(id);
        //收藏数据
        String loginId = String.valueOf(session.getAttribute("loginId"));
        if (StringUtils.isEmpty(loginId) ||loginId.equals("null"))
        {
            loginId = "admin";
        }
        SdoFavorites favorites=sdoFavoritesService.getByLoginIdAndSdoId(loginId,id);
        int hasFavorite=0;
        if(favorites!=null&&favorites.getIsValid()==1){
            hasFavorite=1;
        }
        model.addAttribute("favorite",hasFavorite);
        //推荐数据
        //List<SdoRecomendations> recList=sdoRecomendationsService.getTopNRecListBySdoId(id,5);
        //model.addAttribute("recList",recList);
        List<SdoRelate> sdoRelates = sdoRelationService.relationList(id);
        if (sdoRelates!=null) {
            int size = sdoRelates.size();
            System.out.println(size);
            if (size > 10) {
                sdoRelates = sdoRelates.subList(size - 10, size);
            }
        }
        model.addAttribute("recList",sdoRelates);
        //return size <= 10 ? sdoRelates : sdoRelates.subList(size - 10, size);

        //sdo元素信息
        cn.csdb.model.Resource sdo = sdoService.getSdoById(id);
        model.addAttribute("tags", sdoMap.get("tags"));
        model.addAttribute("id", id);
        model.addAttribute("sdo", sdo);
        model.addAttribute("test", "test");
        return "sdoDetail";
    }

    //收藏--byCql
    @RequestMapping(value = "/favorite",method = RequestMethod.GET)
    @ResponseBody
    public boolean favorite(String sdoId, int isValid,HttpSession session) {
        String loginId = String.valueOf(session.getAttribute("loginId"));
        if (StringUtils.isEmpty(loginId) ||loginId.equals("null"))
        {
            loginId = "admin";
        }
        int hasFavorite=sdoFavoritesService.hasFavorite(loginId,sdoId);
        int i=0;
        if(hasFavorite!=0){
            i=sdoFavoritesService.updateFavorite(loginId,sdoId,isValid);
        }else {
            cn.csdb.model.Resource sdo = sdoService.getSdoById(sdoId);
            i=sdoFavoritesService.addData(loginId,sdoId,sdo.getTitle());
        }
        if (i < 0)
            return false;
        else
            return true;
    }


    //详情页基本内容
    @ResponseBody
    @RequestMapping("visitSdo")
    public Map<String,Object> visitSdo(@RequestParam("id")String id,HttpServletRequest request){
        Map<String,Object> map = sdoService.visitSdo(id);
        if(map.isEmpty()){
            return new HashMap<>();
        }
        map.put("condition",fileTemplateService.findSearchField("XLSX"));
        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if (loginId.equals("") || loginId.equals("null")){
            map.put("isLoad","");
            loginId ="匿名用户";
        }else {
            map.put("isLoad",loginId);
            map.put("isValid",sdoFavoritesService.checkIsValid(loginId,id));
            map.put("userTags",tagDetailService. selTag(loginId,id));
        }
        cn.csdb.model.Resource sdo = sdoService.getSdoById(id);
        sdoService.addVisitCount(id);
        sdoVisitService.addLog(loginId,id,sdo.getTitle());
        return map;
    }
    //查看元数据
    @ResponseBody
    @RequestMapping("getSdoDetails")
    public Map<String,String> getSdoDetails(@RequestParam("id") String id){
        Map<String,String> map = new HashMap<>();
        cn.csdb.model.Resource sdo = sdoService.getSdoById(id);
        if (sdo ==null){
            return map;
        }
        map = sdoService.getSdoDetails(id);
        map.putAll(catalogService.getCatalogName(sdo.getCatalogId()));
        map.putAll(fileTypeService.getFtName(id));
/*
        map.putAll(productService.getProductName(sdo.getProductId()));
*/
        return map;
    }
    //下载元数据excel
    @RequestMapping("downloadExcel")
    public void downloadExcel(@RequestParam("id") String id , HttpServletResponse response , HttpServletRequest request){
        Map<String,String> map =getSdoDetails(id);
        if ( map == null || map.size()==0){
            return;
        }
        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if (loginId.equals("") || loginId.equals("null")){
            loginId ="匿名用户";
            return;
        }
        cn.csdb.model.Resource sdo = sdoService.getSdoById(id);
        sdoService.addDownloadCount(id);
        sdoDownloadService.addLog(loginId,"","",id,sdo.getTitle());
        Workbook workbook = new HSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        Sheet sheet= workbook.createSheet("元数据");
        sheet.addMergedRegion(new CellRangeAddress(0,7,0,0));
        sheet.addMergedRegion(new CellRangeAddress(8,12,0,0));
        sheet.addMergedRegion(new CellRangeAddress(13,14,0,0));
        sheet.addMergedRegion(new CellRangeAddress(15,16,0,0));
        sheet.addMergedRegion(new CellRangeAddress(18,20,0,0));
        sheet.addMergedRegion(new CellRangeAddress(21,24,0,0));
        sheet.addMergedRegion(new CellRangeAddress(25,27,0,0));
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("基本信息");
        row.createCell(1).setCellValue("数据对象名称");
        row.createCell(2).setCellValue(map.get("title"));
        row = sheet.createRow(1);
        row.createCell(1).setCellValue("简介");
        row.createCell(2).setCellValue(map.get("desc"));
        row = sheet.createRow(2);
        row.createCell(1).setCellValue("资源关键词");
        row.createCell(2).setCellValue(map.get("keyword"));
        row = sheet.createRow(3);
        row.createCell(1).setCellValue("学科分类");
        row.createCell(2).setCellValue(map.get("catalog"));
        row = sheet.createRow(4);
        row.createCell(1).setCellValue("产品分类");
        row.createCell(2).setCellValue(map.get("productName"));
        row = sheet.createRow(5);
        row.createCell(1).setCellValue("访问限制");
        row.createCell(2).setCellValue(map.get("visitLimit"));
        row = sheet.createRow(6);
        row.createCell(1).setCellValue("格式");
        row.createCell(2).setCellValue(map.get("ftName"));
        row = sheet.createRow(7);
        row.createCell(1).setCellValue("范围说明");
        row.createCell(2).setCellValue(map.get("rangeDescription"));
        row = sheet.createRow(8);
        row.createCell(0).setCellValue("地理范围");
        row.createCell(1).setCellValue("地理中心点坐标");
        row.createCell(2).setCellValue(map.get("center"));
        row = sheet.createRow(9);
        row.createCell(1).setCellValue("地理左上角坐标");
        row.createCell(2).setCellValue(map.get("upLeft"));
        row = sheet.createRow(10);
        row.createCell(1).setCellValue("地理右上角坐标");
        row.createCell(2).setCellValue(map.get("upRight"));
        row = sheet.createRow(11);
        row.createCell(1).setCellValue("地理右下角坐标");
        row.createCell(2).setCellValue(map.get("lowRight"));
        row = sheet.createRow(12);
        row.createCell(1).setCellValue("地理左下角坐标");
        row.createCell(2).setCellValue(map.get("lowLeft"));
        row = sheet.createRow(13);
        row.createCell(0).setCellValue("时间范围");
        row.createCell(1).setCellValue("开始时间");
        row.createCell(2).setCellValue(map.get("startTime"));
        row = sheet.createRow(14);
        row.createCell(1).setCellValue("结束时间");
        row.createCell(2).setCellValue(map.get("endTime"));
        row = sheet.createRow(15);
        row.createCell(0).setCellValue("分辨率");
        row.createCell(1).setCellValue("空间分辨率");
        row.createCell(2).setCellValue(map.get("rSpatial"));
        row = sheet.createRow(16);
        row.createCell(1).setCellValue("时间分辨率");
        row.createCell(2).setCellValue(map.get("rTime"));
        row = sheet.createRow(17);
        row.createCell(1).setCellValue("版权声明");
        row.createCell(2).setCellValue(map.get("rightstatement"));
        row = sheet.createRow(18);
        row.createCell(0).setCellValue("创建信息");
        row.createCell(1).setCellValue("创建机构");
        row.createCell(2).setCellValue(map.get("cOrganization"));
        row = sheet.createRow(19);
        row.createCell(1).setCellValue("创建人员");
        row.createCell(2).setCellValue(map.get("cName"));
        row = sheet.createRow(20);
        row.createCell(1).setCellValue("创建日期");
        row.createCell(2).setCellValue(map.get("cTime"));
        row = sheet.createRow(21);
        row.createCell(0).setCellValue("发布信息");
        row.createCell(1).setCellValue("发布机构");
        row.createCell(2).setCellValue(map.get("pOrganization"));
        row = sheet.createRow(22);
        row.createCell(1).setCellValue("邮件");
        row.createCell(2).setCellValue(map.get("email"));
        row = sheet.createRow(23);
        row.createCell(1).setCellValue("电话");
        row.createCell(2).setCellValue(map.get("tel"));
        row = sheet.createRow(24);
        row.createCell(1).setCellValue("最新发布日期");
        row.createCell(2).setCellValue(map.get("pTime"));
        row = sheet.createRow(25);
        row.createCell(0).setCellValue("文件信息");
        row.createCell(1).setCellValue("总存储量");
        row.createCell(2).setCellValue(map.get("mSize"));
        row = sheet.createRow(26);
        row.createCell(1).setCellValue("总文件数");
        row.createCell(2).setCellValue(map.get("toFilesNumber"));
        row = sheet.createRow(27);
        row.createCell(1).setCellValue("总记录数");
        row.createCell(2).setCellValue(map.get("toRecordNumber"));
        try {
            response.setHeader("Content-Disposition", "attachment; filename="+new String((map.get("title")+".xls").getBytes(),"iso-8859-1"));
            response.setContentType("application/vnd.ms-excel; charset=utf-8") ;
            OutputStream out = response.getOutputStream() ;
            workbook.write(out) ;
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //用户收藏或者取消收藏标签
    @RequestMapping("isValid")
    public Map<String,String> isValid(@RequestParam("id") String id ,@RequestParam("isValid") Integer isValid,HttpServletRequest request){
        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if (loginId.equals("") || loginId.equals("null")){
            Map<String,String> map = new HashMap<>();
            map.put("result","登录后才能收藏");
            return map;
        }
        cn.csdb.model.Resource sdo = sdoService.getSdoById(id);
        return sdoFavoritesService.getResult(loginId,id,isValid,sdo.getTitle());
    }

    //用户打标签
    /*@ResponseBody
    @RequestMapping("addTag")
    public boolean addTag(@RequestParam("sdoId") String sdoId ,@RequestParam("tagName")String tagName,HttpServletRequest request){
        cn.csdb.model.Resource sdo = sdoService.getSdoById(sdoId);
        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if (sdo == null || loginId.equals("") ){
            return false;
        }
        return tagDetailService.addTag(loginId,sdoId,tagName,sdo.getProductId());
    }*/
    //用户删除标签
    @ResponseBody
    @RequestMapping("delTag")
    public boolean delTag(@RequestParam("sdoId") String sdoId ,@RequestParam("tagName")String tagName,HttpServletRequest request){
        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if (loginId.equals("") ){
            return false;
        }
        return tagDetailService.delTag(loginId,sdoId,tagName);
    }

    //查询字段展示
    @ResponseBody
    @RequestMapping("findSearchField")
    public List<Map<String,String>> findSearchField(String fileType){
        return fileTemplateService.findSearchField(fileType);
    }

    //文件列表
    @ResponseBody
    @RequestMapping("getMidData")
    public Map<String,Object> getMidData(@RequestParam("fileType") String fileType,Integer pageNum){
        int pageSum =fileInfoService.getTotalPageNum(fileType);
        if (pageNum ==null || pageNum<=0 || pageSum<pageNum){
            pageNum=1;
        }
        List<Map<String,String>> list1 = fileTemplateService.findShowField(fileType);
        Map<String,String> map = new HashMap<>();
        map.put("fieldName","updateTime");
        map.put("fieldTitle","日期");
        list1.add(map);
        List<Map<String,String>> list2 = fileInfoService.getFile(list1,fileType,pageNum);
        Map<String,Object> m = new HashMap<>();
        m.put("topTitle",list1);
        m.put("data",list2);
        m.put("pageNum",pageNum);
        m.put("pageSum",pageSum);
        m.put("totalCount",fileInfoService.totalCount());
        return m;
    }

    //条件查询HDF文件列表
    @ResponseBody
    @RequestMapping("getFileByHDF")
    public Map<String,Object> getFileByHDF(@RequestParam(name = "pid", defaultValue = "")String pid,
                                                 @RequestParam(name = "number", defaultValue = "")String number,
                                                 @RequestParam(name = "x1", defaultValue = "0")Double x1 ,
                                                 @RequestParam(name = "x2", defaultValue = "0")Double x2 ,
                                                 @RequestParam(name = "y1", defaultValue = "0")Double y1,
                                                 @RequestParam(name = "y2", defaultValue = "0")Double y2,
                                                 @RequestParam(name = "cloudiness1", defaultValue = "0")Double cloudiness1,
                                                 @RequestParam(name = "cloudiness2", defaultValue = "100")Double cloudiness2,
                                                 @RequestParam(name = "fileType", defaultValue = "HDF")String fileType,
                                                 @RequestParam(name = "pageNum", defaultValue = "1")Integer pageNum,
                                                 @RequestParam(name ="sdoId")String sdoId)throws IOException{
        List<Map<String,String>> list1 = fileTemplateService.findShowField(fileType);
        Map<String,String> map = new HashMap<>();
        map.put("fieldName","updateTime");
        map.put("fieldTitle","日期");
        list1.add(map);
        List<Map<String,String>> list2 ;
        if((x1==x2 && x1!=0) || (y1==y2 && y1!=0) ||y1>y2 || x1>x2||(x1==0&&x2==0&&(y1!=0||y2!=0))||(y1==0&&y2==0&&(x1!=0||x2!=0))){
            list2 = new ArrayList<>();
        }else {
            list2=fileInfoService.getFileByCondition(list1,pid,number,x1,x2,y1,y2,cloudiness1,cloudiness2,fileType,pageNum,sdoId);
        }
        long totalCount =fileInfoService.getTotalNumByCondition();
        long pageSum = totalCount%10==0?totalCount/10:totalCount/10+1;
        Map<String,Object> m = new HashMap<>();
        m.put("topTitle",list1);
        m.put("data",list2);
        m.put("pageNum",pageNum);
        m.put("pageSum",pageSum);
        m.put("totalCount",totalCount);
        return m;
    }

    //条件查询CERN文件列表
    @ResponseBody
    @RequestMapping("getFileByCERN")
    public Map<String,Object> getFileByCERN(@RequestParam(name = "pid", defaultValue = "")String pid,
                                             @RequestParam(name = "fileName", defaultValue = "")String fileName,
                                             @RequestParam(name = "fileType", defaultValue = "xlsx")String fileType,
                                             @RequestParam(name = "pageNum", defaultValue = "1")Integer pageNum,
                                             @RequestParam(name ="sdoId")String sdoId,
                                             @RequestParam(name="subjectCode",required = false,defaultValue = "sdc")String subjectCode){
        List<Map<String,String>> list1 = fileTemplateService.findShowField("XLSX");
        Map<String,String> map = new HashMap<>();
        map.put("fieldName","updateTime");
        map.put("fieldTitle","日期");
        list1.add(map);
        List<Map<String,String>> list2 = new ArrayList<>();
        cn.csdb.model.Resource res = sdoService.getBysubjectCode(subjectCode);
        String[] filePath = res.getFilePath().split(";");
        if(!"".equals(fileName)){
            for (String filePathString : filePath) {
                Map<String, String> map1 = new HashMap<>();
                String s = filePathString.substring(filePathString.lastIndexOf("%") + 1);
                if(s.indexOf(fileName)!=-1) {
                    map1.put("fileName", s);
                    map1.put("filePathString", filePathString);
                    map1.put("size", "0.1M");
                    map1.put("recordNum", "1");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map1.put("updateTime", sdf.format(res.getCreateTime()));
                    list2.add(map1);
                }
            }
        }else {
            for (String filePathString : filePath) {
                Map<String, String> map1 = new HashMap<>();
                String s = filePathString.substring(filePathString.lastIndexOf("%") + 1);
                map1.put("fileName", s);
                map1.put("filePathString", filePathString);
                map1.put("size", "0.1M");
                map1.put("recordNum", "1");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map1.put("updateTime", sdf.format(res.getCreateTime()));
                list2.add(map1);
            }
        }
/*
        List<Map<String,String>> list2 = fileInfoService.getFileByCondition(list1,pid,fileName,fileType,pageNum,sdoId);
*/
        long totalCount =list2.size();
        long pageSum = totalCount%10==0?totalCount/10:totalCount/10+1;
        Map<String,Object> m = new HashMap<>();
        m.put("topTitle",list1);
        m.put("data",list2);
        m.put("pageNum",pageNum);
        m.put("pageSum",pageSum);
        m.put("totalCount",totalCount);
        return m;
    }

    //用户下载一个文件
    @RequestMapping("downloadOneFile")
    public void downloadOneFile(@RequestParam("filePathString")String filePathString,String sdoId,HttpServletRequest request,HttpServletResponse response) throws IOException {

        cn.csdb.model.Resource res = sdoService.getBysubjectCode("sdc");

        if (filePathString == "") {
            return;
        }
        String fileRealPath = filePathString.replaceAll("%_%", Matcher.quoteReplacement(File.separator));

        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if (loginId.equals("") || loginId.equals("null")) {
            loginId = "匿名用户";
            return;
        }
//            String path = this.getClass().getResource("/").getPath().substring(1);
//            path =path.substring(0,path.lastIndexOf("/"));
//            path =path.substring(0,path.lastIndexOf("/"));
//            path =path.substring(0,path.lastIndexOf("/"));
           // File file = new File("D:\\"+fileInfo.getFilePath());
/*
            File file = new File(("//"+fileInfo.getFilePath()).replaceAll("\\\\", "//").replaceAll("//", "/") );
*/
            File file = new File(fileRealPath);
            if (file.exists() && file.isFile()) {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[bis.available()];
                bis.read(buffer);
                bis.close();
                // 清空response
                response.reset();
                OutputStream out = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
                out.write(buffer);
                out.flush();
                out.close();
            }
        cn.csdb.model.Resource sdo = sdoService.getSdoById(sdoId);
        if (sdo!=null){
            sdoService.addDownloadCount(sdoId);
        }

        /*String sdoId= fileTypeService.getById(fileInfo.getFtId()).getSdoId();
        cn.csdb.model.Resource sdo = sdoService.getSdoById(sdoId);
        if (sdo!=null){
            sdoService.addDownloadCount(sdoId);
            sdoDownloadService.addLog(loginId, fileInfo.getId(), fileInfo.getFileName(),sdo.getId(),sdo.getTitle());
        }*/
    }
    //用户批量下载文件，并且文件总大小小于500MB
    @RequestMapping("downloadFiles")
    public void downloadFiles(@RequestParam("listId")String sList ,String sdoId,HttpServletRequest request,HttpServletResponse response )throws IOException{
        String[] listId = sList.split(",");
        List<String> listPath = new ArrayList<>();
        List<String> listName = new ArrayList<>();
//        String path = this.getClass().getResource("/").getPath().substring(1);
//        path =path.substring(0,path.lastIndexOf("/"));
//        path =path.substring(0,path.lastIndexOf("/"));
//        path =path.substring(0,path.lastIndexOf("/"));
        /*for (int i =0;i<listId.length;i++){
            FileInfo fileInfo = fileInfoService.getById(listId[i]);
            if (fileInfo !=null){
                if(!listName.contains(fileInfo.getFileName())) {
                   // listPath.add("D:\\" + fileInfo.getFilePath());
                    listPath.add(("//" + fileInfo.getFilePath()).replaceAll("\\\\", "//").replaceAll("//", "/") );
                    listName.add(fileInfo.getFileName());
                }
            }
        }*/
        for(String filePath:listId){
            listPath.add(filePath);
            String fileName = filePath.substring(filePath.lastIndexOf(Matcher.quoteReplacement(File.separator)) + 1);
            listName.add(fileName);
        }
        String loginId = String.valueOf(request.getSession().getAttribute("loginId"));
        if (loginId.equals("") || loginId.equals("null")){
            loginId ="匿名用户";
            return;
        }
        String zipName = UUID.randomUUID().toString() +".zip";
        //File f = new File("D:/"+zipName);
        File f = new File(("//"+zipName).replaceAll("\\\\", "//").replaceAll("//", "/") );
        if (!f.exists()){
            f.createNewFile();
        }else{
            f.delete();
            f.createNewFile();
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(f));
        InputStream inputStream =null;
        for (int i=0;i<listPath.size();i++) {
            if (new File(listPath.get(i)).exists()) {
                inputStream = new FileInputStream(new File(listPath.get(i)));
                zipOutputStream.putNextEntry(new ZipEntry(listName.get(i)));
                int temp = 0;
                while ((temp = inputStream.read()) != -1) {
                    zipOutputStream.write(temp);
                }
                inputStream.close();
            }
        }
        zipOutputStream.close();
        //File file = new File("D:/"+zipName);
        File file = new File(("//"+zipName).replaceAll("\\\\", "//").replaceAll("//", "/") );
        if (file.exists()&&file.isFile()){
            if (file.length()<=500*1024*1024){
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[bis.available()];
                bis.read(buffer);
                bis.close();
                // 清空response
                response.reset();
                OutputStream out = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
                out.write(buffer);
                out.flush();
                out.close();
                //删除临时文件
                file.delete();
/*
                        String sdoId= fileTypeService.getById(fileInfo.getFtId()).getSdoId();
*/
                cn.csdb.model.Resource sdo = sdoService.getSdoById(sdoId);
                if (sdo!=null){
                    sdoService.addDownloadCount(sdoId);
                }
            }
        }
    }

    //点击预览按钮
    @ResponseBody
    @RequestMapping("getPreview")
    public Map<String,String> getPreview(@RequestParam("id")String id)throws IOException{
        FileInfo fileInfo = fileInfoService.getById(id);
        Map<String,String> map= new HashMap<>();
        if (fileInfo==null){
            return map;
        }
        ClassPathResource classPathResource = new ClassPathResource("/view.properties");
        Properties properties = PropertiesLoaderUtils.loadProperties(classPathResource);
        String previewType = properties.getProperty(fileInfo.getPreviewType());
        if (previewType.equals("img")){
            UrlUtil urlUtil = new UrlUtil();
            map.put("previewType","img");
            String url="http://www.gscloud.cn/sources/getpreviewimg/"+fileInfo.getFilePath()+"/"+fileInfo.getPid()+"?userid=data@cnic.cn";
            map.put("previewFile","http://www.gscloud.cn"+urlUtil.getUrlFromGscloud(url));

        }else if(previewType.equals("datatable")){
            map.put("previewType","datatable");
            map.put("fileType",fileInfo.getPreviewType());
            map.put("filePath",fileInfo.getFilePath());
        }else if(previewType.equals("openoffice")){
            map.put("previewType","openoffice");
            map.put("filePath",fileInfo.getFilePath());
            map.put("fileType",fileInfo.getPreviewType());
        }
        else if (StringUtils.equals("shape", fileInfo.getPreviewType())) {
            map.put("previewType", fileInfo.getPreviewType());
        }

        return map;
    }

    //openOffice功能页面跳转
//    @RequestMapping("office")
//    public String toOfficePage(@RequestParam("filePath")String filePath,Model model)throws IOException{
////        String path = this.getClass().getResource("/").getPath().substring(1);
////        path =path.substring(0,path.lastIndexOf("/"));
////        path =path.substring(0,path.lastIndexOf("/"));
////        path =path.substring(0,path.lastIndexOf("/"));
////        filePath = path+filePath;
//        File file = new File("D:/"+filePath);
//        if (file.exists() && file.isFile()){
//            String targetFilePath = filePath.substring(0,filePath.lastIndexOf("."))+".pdf";
//            File targetFile = new File(targetFilePath);
//            if (targetFile.exists()){
//                targetFile.delete();
//            }
//            ClassPathResource classPathResource = new ClassPathResource("/cas_urls.properties");
//            Properties properties = PropertiesLoaderUtils.loadProperties(classPathResource);
//            try {
//                DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
//                configuration.setOfficeHome(properties.getProperty("OfficeHome"));
//                configuration.setPortNumber(8100);
//                configuration.setTaskExecutionTimeout(1000 * 60 * 1L);
//                configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
//                OfficeManager officeManager = configuration.buildOfficeManager();
//                OfficeDocumentConverter officeDocumentConverter = new OfficeDocumentConverter(officeManager);
//                officeManager.start();
//                officeDocumentConverter.convert(file,targetFile);
//                if (officeManager !=null){
//                    officeManager.stop();
//                }
//            }catch (Exception e){
//                System.out.println(e);
//            }
//            model.addAttribute("targetFilePath",targetFilePath);
//        }
//        return "office";
//    }

    //获取pdf文件流
    @RequestMapping("getPDF")
    public void getPDF(@RequestParam("targetFilePath")String targetFilePath,HttpServletResponse response)throws IOException{
        File file = new File(targetFilePath);
        if(file.exists()){
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024 * 2];
            int len;
            OutputStream outputStream = response.getOutputStream();
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            file.delete();
        }
    }

    //读取所要预览的Excel
//    @RequestMapping("readExcel")
//    public String readExcel(@RequestParam("filePath")String filePath,Model model)throws IOException{
////        String path = this.getClass().getResource("/").getPath().substring(1);
////        path =path.substring(0,path.lastIndexOf("/"));
////        path =path.substring(0,path.lastIndexOf("/"));
////        path =path.substring(0,path.lastIndexOf("/"));
////        filePath = path+filePath;
//        List<List<List<String>>> sheets = new ArrayList<>();
//        List<String> sheetNames = new ArrayList<>();
//        File file = new File("D:\\"+filePath);
//        if (file.exists() && file.isFile()) {
//            InputStream is = new FileInputStream("D:\\"+filePath);
//            String excelType = filePath.substring(filePath.lastIndexOf("."));
//            if(excelType.trim().equals(".xls")) {
//                HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
//                int size = hssfWorkbook.getNumberOfSheets();
//                // 循环每一页，并处理当前循环页
//                for (int numSheet = 0; numSheet < size; numSheet++) {
//                    // HSSFSheet 标识某一页
//                    List<List<String>> lists = new ArrayList<>();
//                    HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
//                    if (hssfSheet == null) {
//                        continue;
//                    }
//                    HSSFRow firstRow = hssfSheet.getRow(0);
//                    if (firstRow == null) {
//                        continue;
//                    }
//                    sheetNames.add(hssfSheet.getSheetName());
//                    int maxColIx = firstRow.getLastCellNum();
//                    // 处理当前页，循环读取每一行
//                    for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
//                        // HSSFRow表示行
//                        HSSFRow hssfRow = hssfSheet.getRow(rowNum);
//                        List<String> rowList = new ArrayList<String>();
//                        // 遍历改行，获取处理每个cell元素
//                        for (int colIx = 0; colIx < maxColIx; colIx++) {
//                            // HSSFCell 表示单元格
//                            HSSFCell cell = hssfRow.getCell(colIx);
//                            if (cell == null) {
//                                rowList.add("");
//                                continue;
//                            }
//                            rowList.add(cell.toString());
//                        }
//                        lists.add(rowList);
//                    }
//                    sheets.add(lists);
//                }
//            }else if(excelType.trim().equals(".xlsx")){
//                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
//                int size = xssfWorkbook.getNumberOfSheets();
//                // 循环每一页，并处理当前循环页
//                for (int numSheet = 0; numSheet < size; numSheet++) {
//                    // HSSFSheet 标识某一页
//                    List<List<String>> lists = new ArrayList<>();
//                    XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
//                    if (xssfSheet == null) {
//                        continue;
//                    }
//                    XSSFRow firstRow = xssfSheet.getRow(0);
//                    if (firstRow == null) {
//                        continue;
//                    }
//                    sheetNames.add(xssfSheet.getSheetName());
//                    int maxColIx = firstRow.getLastCellNum();
//                    // 处理当前页，循环读取每一行
//                    for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
//                        // XSSFRow表示行
//                        XSSFRow xssfRow = xssfSheet.getRow(rowNum);
//                        List<String> rowList = new ArrayList<String>();
//                        // 遍历改行，获取处理每个cell元素
//                        for (int colIx = 0; colIx < maxColIx; colIx++) {
//                            // HSSFCell 表示单元格
//                            XSSFCell cell = xssfRow.getCell(colIx);
//                            if (cell == null) {
//                                rowList.add("");
//                                continue;
//                            }
//                            rowList.add(cell.toString());
//                        }
//                        lists.add(rowList);
//                    }
//                    sheets.add(lists);
//                }
//            }
//        }
//        model.addAttribute("sheetNames",sheetNames);
//        model.addAttribute("sheets",sheets);
//        return "excel";
//    }
    //查看文件元数据
    @ResponseBody
    @RequestMapping("getFileInfo")
    public FileInfo getFileInfo(@RequestParam("id") String id){
        return fileInfoService.getById(id);
    }


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model model) {
        List<String> dataTypeList = sdoService.findDistinctDataType();
        List<String> publisherList = sdoService.findDistinctPublisher();
        List<String> dateRange = new ArrayList<>();
        dateRange.add(sdoService.getDateRange()[0]+"");
        dateRange.add((sdoService.getDateRange()[1]+1)+"");
        model.addAttribute("dataTypeList",dataTypeList);
        model.addAttribute("publisherList",publisherList);
        model.addAttribute("dateRange",dateRange);
        return "sdoList";
    }

    @ResponseBody
    @RequestMapping("getAll")
    public JSONObject getAll() {
        JSONObject jsonObject = new JSONObject();
        List<Sdo> sdoList = sdoService.getAll();
        jsonObject.put("list",sdoList);
        jsonObject.put("totalPages",2);
        jsonObject.put("totalCount",20);
/*
        List<Map<String,String>> tagList = sdoService.getMapsBySdoList(sdoList);
*/
/*
        jsonObject.put("tagList",tagList);
*/
        return jsonObject;
    }

    @RequestMapping("/getData")
    @ResponseBody
    public JSONObject getData(
            HttpSession session,
            @RequestParam(name = "prodId", defaultValue = "") String prodId,
            @RequestParam(name = "searchKey", defaultValue = "") String searchKey,
            @RequestParam(name = "tags", defaultValue = "") String tags,
            @RequestParam(name = "dataFormat", defaultValue = "") String dataFormat,
            @RequestParam(name = "publisher", defaultValue = "") String publisher,
            @RequestParam(name = "timerange", defaultValue = "") String timerange,
            @RequestParam(name = "sortName", defaultValue = "") String sortName,
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
        if (pageNo >= 1) {
            pageNo -= 1;
        }
        searchKey = searchKey.trim();
        //如果搜索关键词不为空，则增加搜索日志记录
        if (StringUtils.isNoneBlank(searchKey)) {
            String username = "匿名用户";
            String loginId = String.valueOf(session.getAttribute("loginId"));
            if (StringUtils.isEmpty(loginId) ||loginId.equals("null"))
            {
                loginId = "admin";
                sdoSearchService.add(loginId,searchKey);
            }

        }
        return sdoService.getData(prodId,searchKey,tags,dataFormat,publisher,timerange,sortName, pageNo, pageSize);
    }


    @RequestMapping(value = "getScoreList", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getScoreList(int pageNo,String sdoId) {
        JSONObject jsonObject = new JSONObject();
//        评论打分
        int pageSize = 3;
//        sdoId = "5aceb6609ef71018d432f91b";
        List<SdoComment> scoreList = sdoCommentService.getSdoCommentList(sdoId, (pageNo - 1) * pageSize, pageSize);
        double avgScore = sdoCommentService.getAvgScore(sdoId);
        long totalNum = sdoCommentService.getSdoCommentNum(sdoId);
        jsonObject.put("scoreList", scoreList);
        jsonObject.put("totalCount", totalNum);
        long totalPages = 0;
        if (totalNum % pageSize== 0) {
            totalPages = totalNum / pageSize;
        } else {
            totalPages = totalNum / pageSize + 1;
        }
        jsonObject.put("totalPages", totalPages);
        jsonObject.put("currentPage", pageNo);
        jsonObject.put("avgScore",avgScore);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "scoresubmit", method = RequestMethod.POST)
    public JSONObject scoresubmit(String sdoId, double score, String comment, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        String loginId = String.valueOf(session.getAttribute("loginId"));
        if (loginId == "null") {
//            loginId = "admin";
            jsonObject.put("result", "fail");
            return jsonObject;
        }
        SdoComment sdoComment = new SdoComment();
        sdoComment.setSdoId(sdoId);
        sdoComment.setScore(score);
        Date now = new Date();
//        DateFormat df = DateFormat.getDateInstance();
//        String s = df.format(now);
        sdoComment.setCreateTime(DateUtils.addHours(now,8));
        sdoComment.setContent(comment);
        sdoComment.setLoginId(loginId);
        boolean hasScored = sdoCommentService.hasScored(sdoId,loginId);
        int i = sdoCommentService.insertSdoComment(sdoComment);
        if (i > 0) {
            jsonObject.put("result", "success");
        } else {
            jsonObject.put("result", "fail");
        }
        return jsonObject;
    }
    @RequestMapping("deleteScore/{id}")
    @ResponseBody
    public JSONObject deleteScore(@PathVariable("id")String id) {
        boolean i = sdoCommentService.deleteScore(id);
        JSONObject jsonObject = new JSONObject();
        if (i == true) {
            jsonObject.put("result", "success");
        } else {
            jsonObject.put("result", "fail");
        }
        return jsonObject;
    }

    //取消收藏
    @ResponseBody
    @RequestMapping("/deleteFavorite")
    public boolean deleteFavorite(String id, HttpSession session) {
        String loginId = String.valueOf(session.getAttribute("loginId"));
        boolean result =false;
        result = sdoFavoritesService.deleteFavorite(id) == 1 ? true : false;
        return result;
    }

    //下载excel筛选后的数据
    @RequestMapping(value = "downloadDataInExcel",method = RequestMethod.POST)
    public void downloadDataInExcel(String data,HttpServletResponse response){
        String[] rows =data.replace("\"","")
                .replace("[","")
                .replace("]]","")
                .split("],");
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet= workbook.createSheet("元数据");
        for (int i=0;i<rows.length;i++){
            Row row = sheet.createRow(i);
            String[] cells = rows[i].split(",");
            for (int j=0;j<cells.length;j++){
                row.createCell(j).setCellValue(cells[j]);
            }
        }
        try {
            response.setHeader("Content-Disposition", "attachment; filename="+UUID.randomUUID());
            response.setContentType("application/vnd.ms-excel; charset=utf-8") ;
            OutputStream out = response.getOutputStream() ;
            workbook.write(out);
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //获取表字段信息
    @ResponseBody
    @RequestMapping(value = "getTableFieldComs")
    public JSONObject getFieldComsByTableName(String subjectCode,
                                              String tableName,
                                              String columnName,
                                              String searchConditon,
                                              @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
                                              @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List search = new ArrayList();
        if(searchConditon!="") {
            search = JSON.parseObject(searchConditon, List.class);
        }else{
            search = null;
        }
        JSONObject jsonObject = new JSONObject();
        Map<String, List<TableInfo>> fieldComsByTableName = tableFieldComsService.getDefaultFieldComsByTableName(subjectCode, tableName);
        if (fieldComsByTableName != null) {
            List<TableInfo> tableInfos = fieldComsByTableName.get(tableName);
            if(columnName==""||columnName==null) {
                jsonObject.put("tableInfos", tableInfos);
            }else {
                String[] s = columnName.split(",");
                List<TableInfo> tableInfos1 = new ArrayList<>();
                for (TableInfo tableInfo : tableInfos) {
                    for(String column : s){
                        if(column.equals(tableInfo.getColumnName())){
                            tableInfos1.add(tableInfo);
                        }
                    }
                }
                jsonObject.put("tableInfos", tableInfos1);
            }
        }
        List<Map<String,Object>> datas = new ArrayList<>();
        if(columnName==""||columnName==null){
            datas = tableFieldComsService.getDataByTable(search,null,tableName ,subjectCode, (pageNo-1)*pageSize , pageSize);
        }else{
            String[] s = columnName.split(",");
            datas = tableFieldComsService.getDataByTable(search,s,tableName ,subjectCode, (pageNo-1)*pageSize , pageSize);
        }
        jsonObject.put("datas", datas);
        jsonObject.put("totalCount",datas.size());
        jsonObject.put("currentPage",pageNo);
        jsonObject.put("pageSize",pageSize);
        jsonObject.put("totalPages",datas.size() % pageSize == 0 ? datas.size() / pageSize : datas.size() / pageSize + 1);
        return jsonObject;
    }

    //获取表字段单行内容
    @ResponseBody
    @RequestMapping(value = "/getRelationalDatabaseByTableName")
    public JSONObject previewRelationalDatabaseByTableName(
            @RequestParam String tableName,
            @RequestParam String subjectCode,
            @RequestParam(value = "pageNo",defaultValue = "1") int pageNo) {
        JSONObject jsonObject = new JSONObject();
        List<TableInfo> tableInfos = new ArrayList<>();
        Map<String, List<TableInfo>> fieldComsByTableName = tableFieldComsService.getDefaultFieldComsByTableName(subjectCode, tableName);
        if (fieldComsByTableName != null) {
            tableInfos = fieldComsByTableName.get(tableName);
        }

        List<Map<String,Object>> datas = tableFieldComsService.getDataByTable(null,null,tableName ,subjectCode, 0 , pageNo);
        Map<String,Object>rowData = datas.get(datas.size()-1);
        Map<String,Object>newRowData = new HashMap<>();
        for(TableInfo tableInfo:tableInfos){
            for(String key:rowData.keySet()){
                if((tableInfo.getColumnName().equals(key))) {
                    newRowData.put(tableInfo.getColumnComment(), rowData.get(key));
                }
            }
        }
        jsonObject.put("rowData", newRowData);
        return jsonObject;
    }
}

