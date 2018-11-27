package cn.csdb.service;

import cn.csdb.model.Product;
import cn.csdb.repository.ProductDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pirate on 2018/4/16.
 */
@Service
public class ProductService {

    @Resource
    private ProductDao productDao;

    public List<Product> getIndexProductStat() {
        return productDao.getIndexProductStat();
    }

    public List<Product> getProductStatDetail() {
        return productDao.getProductStatDetail();
    }

    public Product findById(String id) {
        return productDao.findById(id);
    }

    //获取product名称
    public Map<String, String> getProductName(String id) {
        Map<String, String> map = new HashMap<>();
        Product product = productDao.getProductById(id);
        if (product == null) {
            map.put("productName", "");
            return map;
        }
        map.put("productName", product.getProdName());
        return map;
    }

    public List<Product> getAllProductList() {
        List<Product> list = productDao.findAll();
        return list;
    }

    public Map<String, List<String>> findAll() {
        Map<String, List<String>> map = new HashMap<>();
        List<Product> list = productDao.findAll();
        List<String> prodsName = new ArrayList<>();
        List<String> prodsId = new ArrayList<>();
        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            prodsId.add(list.get(i).getId());
            prodsName.add(list.get(i).getProdName());
            imgs.add(list.get(i).getProdIconPath());
        }
        map.put("prodsName", prodsName);
        map.put("prodsId", prodsId);
        map.put("imgs", imgs);
        return map;
    }
}
