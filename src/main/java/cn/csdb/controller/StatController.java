package cn.csdb.controller;

import cn.csdb.model.Product;
import cn.csdb.model.Stat;
import cn.csdb.service.ProductService;
import cn.csdb.service.StatService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangwei on 2018/4/20.
 * 统计相关功能
 */
@Controller
public class StatController {

    private Logger logger= LoggerFactory.getLogger(StatController.class);

    @Resource
    private ProductService productService;

    @Resource
    private StatService statService;

    @ResponseBody
    @RequestMapping("/getIndexProductStat")
    public JSONObject getIndexProductStat(){
        JSONObject jo = new JSONObject();
        List<Product> products = productService.getIndexProductStat();
        List<String> productName = new ArrayList<String>();
        List<String> datasetNumList = new ArrayList<String>();
        List<String> fileNumList = new ArrayList<String>();
        List<String> storageCapacityList = new ArrayList<String>();
        for(Product product:products){
            productName.add(product.getProdName());
            datasetNumList.add(product.getDatasetNum());
            fileNumList.add(product.getFileNum());
            storageCapacityList.add(product.getStorageCapacity());
        }
        jo.put("productName",productName);
        jo.put("datasetNumList",datasetNumList);
        jo.put("fileNumList",fileNumList);
        jo.put("storageCapacityList",storageCapacityList);
        return jo;
    }

    @RequestMapping("/getMoreStat")
    public ModelAndView getMoreStat() {
        logger.debug("浸入统计详细页面");
        ModelAndView modelAndView = new ModelAndView("getMoreStat");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getProductStatDetail")
    public JSONObject getProductStatDetail(){
        JSONObject jo = new JSONObject();
        List<Product> products = productService.getProductStatDetail();
        List<String> productName = new ArrayList<String>();
        List<String> datasetNumList = new ArrayList<String>();
        List<String> fileNumList = new ArrayList<String>();
        List<String> storageCapacityList = new ArrayList<String>();
        for(Product product:products){
            productName.add(product.getProdName());
            datasetNumList.add(product.getDatasetNum());
            fileNumList.add(product.getFileNum());
            storageCapacityList.add(product.getStorageCapacity());
        }
        jo.put("productName",productName);
        jo.put("datasetNumList",datasetNumList);
        jo.put("fileNumList",fileNumList);
        jo.put("storageCapacityList",storageCapacityList);
        return jo;
    }
}
