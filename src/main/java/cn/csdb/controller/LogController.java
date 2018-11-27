package cn.csdb.controller;

import cn.csdb.service.SdoDownloadService;
import cn.csdb.service.SdoFavoritesService;
import cn.csdb.service.SdoSearchService;
import cn.csdb.service.SdoVisitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Controller
@RequestMapping("/admin/log")
public class LogController {
    @Resource
    private SdoVisitService sdoVisitService;
    @Resource
    private SdoFavoritesService sdoFavoritesService;
    @Resource
    private SdoDownloadService sdoDownloadService;
    @Resource
    private SdoSearchService sdoSearchService;

    @RequestMapping("/")
    public String logPage(){
        return "admin/log";
    }

    //浏览记录
    @ResponseBody
    @RequestMapping("getSdoVisit")
    public Map<String,Object> getSdoVisit(@RequestParam(value = "title",defaultValue = "")String title ,
                                          @RequestParam(value = "loginId",defaultValue = "")String loginId ,
                                          @RequestParam(value = "beginTime",defaultValue = "1905-1-1") String beginTime,
                                          @RequestParam(value = "endTime",defaultValue = "2020-12-31") String endTime,
                                          @RequestParam(value = "pageNum",defaultValue = "1") int pageNum)throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sdoVisitService.getBy(title,loginId,simpleDateFormat.parse(beginTime),simpleDateFormat.parse(endTime),pageNum);
    }

    //下载记录
    @ResponseBody
    @RequestMapping("getSdoDownload")
    public Map<String,Object> getSdoDownload(@RequestParam(value = "title",defaultValue = "")String title ,
                                             @RequestParam(value = "loginId",defaultValue = "")String loginId ,
                                             @RequestParam(value = "beginTime",defaultValue = "1905-1-1") String beginTime,
                                             @RequestParam(value = "endTime",defaultValue = "2020-12-31") String endTime,
                                             @RequestParam(value = "pageNum",defaultValue = "1") int pageNum)throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sdoDownloadService.getBy(title,loginId,simpleDateFormat.parse(beginTime),simpleDateFormat.parse(endTime),pageNum);
    }

    //收藏记录
    @ResponseBody
    @RequestMapping("getSdoFavorites")
    public Map<String,Object> getSdoFavorites(@RequestParam(value = "title",defaultValue = "")String title ,
                                              @RequestParam(value = "loginId",defaultValue = "")String loginId ,
                                              @RequestParam(value = "beginTime",defaultValue = "1905-1-1") String beginTime,
                                              @RequestParam(value = "endTime",defaultValue = "2020-12-31") String endTime,
                                              @RequestParam(value = "pageNum",defaultValue = "1") int pageNum)throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sdoFavoritesService.getBy(title,loginId,simpleDateFormat.parse(beginTime),simpleDateFormat.parse(endTime),pageNum);
    }

    //收索记录
    @ResponseBody
    @RequestMapping("getSdoSearch")
    public Map<String,Object> getSdoSearch(@RequestParam(value = "keyword",defaultValue = "")String keyword ,
                                           @RequestParam(value = "loginId",defaultValue = "")String loginId ,
                                           @RequestParam(value = "beginTime",defaultValue = "1905-1-1") String beginTime,
                                           @RequestParam(value = "endTime",defaultValue = "2020-12-31") String endTime,
                                           @RequestParam(value = "pageNum",defaultValue = "1") int pageNum)throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sdoSearchService.getBy(keyword,loginId,simpleDateFormat.parse(beginTime),simpleDateFormat.parse(endTime),pageNum);
    }
}
