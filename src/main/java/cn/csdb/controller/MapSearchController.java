package cn.csdb.controller;

import cn.csdb.service.FileInfoService;
import cn.csdb.service.SdoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mapSearch")
public class MapSearchController {
    @Resource
    private SdoService sdoService;
    @Resource
    private FileInfoService fileInfoService;

    @RequestMapping("/")
    public String mapSearchPage(){
        return "mapSearch";
    }

    @ResponseBody
    @RequestMapping("getInitData")
    public Map<String,Object> getInitData(){
        List<String> dataType = sdoService.findDistinctDataType();
        List<String> publisher = sdoService.findDistinctPublisher();
        Map<String,Object> map = new HashMap<>();
        map.put("dataType",dataType);
        map.put("publisher",publisher);
        map.put("minYear",sdoService.getDateRange()[0]>fileInfoService.getDateRange()[0]?fileInfoService.getDateRange()[0]:sdoService.getDateRange()[0]);
        map.put("maxYear",sdoService.getDateRange()[1]>fileInfoService.getDateRange()[1]?sdoService.getDateRange()[1]+1:fileInfoService.getDateRange()[1]+1);
        return map;
    }

    @ResponseBody
    @RequestMapping("getFileInfoBy")
    public Map<String,Object> getFileInfoBy(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(name = "fileType", defaultValue = "") String fileType,
                                        @RequestParam(name = "publisher", defaultValue = "") String publisher,
                                        @RequestParam(name = "x1", defaultValue = "0") double x1,
                                        @RequestParam(name = "x2", defaultValue = "0") double x2,
                                        @RequestParam(name = "y1", defaultValue = "0") double y1,
                                        @RequestParam(name = "y2", defaultValue = "0") double y2,
                                        @RequestParam(name = "beginTime", defaultValue = "1905") String beginTime,
                                        @RequestParam(name = "endTime", defaultValue = "2020") String endTime,
                                        @RequestParam(name = "pageNum", defaultValue = "1") int pageNum) throws IOException {
        if ((x1==x2 && x1!=0) || (y1==y2 && y1!=0) ||y1>y2 || x1>x2||(x1==0&&x2==0&&(y1!=0||y2!=0))||(y1==0&&y2==0&&(x1!=0||x2!=0))){
            return new HashMap<>();
        }
        return fileInfoService.getByES(keyword,fileType,publisher,x1,x2,y1,y2,beginTime,endTime,pageNum);
    }

    @ResponseBody
    @RequestMapping("getSdoBy")
    public Map<String,Object> getSdoBy(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                            @RequestParam(name = "fileType", defaultValue = "") String fileType,
                                            @RequestParam(name = "publisher", defaultValue = "") String publisher,
                                            @RequestParam(name = "x1", defaultValue = "0") double x1,
                                            @RequestParam(name = "x2", defaultValue = "0") double x2,
                                            @RequestParam(name = "y1", defaultValue = "0") double y1,
                                            @RequestParam(name = "y2", defaultValue = "0") double y2,
                                            @RequestParam(name = "beginTime", defaultValue = "1905") String beginTime,
                                            @RequestParam(name = "endTime", defaultValue = "2020") String endTime,
                                            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum) throws IOException {
        if ((x1==x2 && x1!=0) || (y1==y2 && y1!=0) ||y1>y2 || x1>x2||(x1==0&&x2==0&&(y1!=0||y2!=0))||(y1==0&&y2==0&&(x1!=0||x2!=0))){
            return new HashMap<>();
        }
        return sdoService.getByES(keyword,fileType,publisher,x1,x2,y1,y2,beginTime,endTime,pageNum);
    }
}
