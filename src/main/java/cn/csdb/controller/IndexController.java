package cn.csdb.controller;

import cn.csdb.model.ContentEdit;
import cn.csdb.service.*;
import cn.csdb.model.Stat;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiajl on 2018/3/20.
 */
@Controller
public class IndexController {

    private Logger logger= LoggerFactory.getLogger(IndexController.class);

    @Resource
    private SdoService sdoService;
    @Resource
    private StatService statService;
    @Resource
    private ProductService productService;
    @Resource
    private TagService tagService;
    @Resource
    private ContentEditService contentEditService;
    @Resource
    private SdoSearchService sdoSearchService;

    @RequestMapping("/")
    public ModelAndView index() {
        logger.debug("进入首页");
        ModelAndView modelAndView = new ModelAndView("index");

//        统计结果
        Stat stat = statService.getLastStat();
        modelAndView.addObject("stat",stat);

        //byCql  新闻资讯
        List<ContentEdit> contentEdits=contentEditService.getTop5NewsListByField();
        modelAndView.addObject("newsList",contentEdits);

        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("/getInitDates")
    public  List<Map<String,String>> getInitDates(@RequestParam("type") String type ) {
        List<Map<String, String>> maps = new ArrayList<>();
        String order="";
        if ("recent".equals(type)) {
            order = "publisher_publishTime";
        } else if ("popular".equals(type)) {
            order = "visit_count";
        } else {
            return maps;
        }
        maps=sdoService.getInitDates(order);
        return maps;
    }


    @ResponseBody
    @RequestMapping("/getTagsByProd")
    public List<Map<String,Object>> getTagsByProd(){
        Map<String,List<String>> map = productService.findAll();
        return tagService.getTagsByProd(map.get("prodsName"),map.get("prodsId"),map.get("imgs"));
    }

    @ResponseBody
    @RequestMapping("/getTop40Tags")
    public List<Map<String,Object>> getTop40Tags(){
       return tagService.getTop40Tags();
    }

    @ResponseBody
    @RequestMapping("/elasticsearch")
    public String elasticsearch(@RequestParam("keyword") String keyword){
        return "";
    }

    @ResponseBody
    @RequestMapping("/getSearchWord")
    public List<BasicDBObject> getSearchWord(){
        return sdoSearchService.getSearchWord();
    }

}
