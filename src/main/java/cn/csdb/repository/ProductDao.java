package cn.csdb.repository;

import cn.csdb.model.Product;
import cn.csdb.model.User;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by huangwei on 2018/4/16.
 */
@Repository
public class ProductDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public List<Product> getIndexProductStat(){
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(0);
        basicQuery.limit(5);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.ASC, "order");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));

        List<Product> products = mongoTemplate.find(basicQuery, Product.class);
        return products;
    }

    public List<Product> getProductStatDetail(){
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.ASC, "order");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));

        List<Product> products = mongoTemplate.find(basicQuery, Product.class);
        return products;
    }



    //根据id获取Product对象
    public Product findById(String id){
        return mongoTemplate.findById(id,Product.class);
    }


    //根据id获取product
    public Product getProductById(String id){
        return mongoTemplate.findById(id,Product.class);
    }

    public List<Product> findAll(){
        return mongoTemplate.findAll(Product.class);
    }
}
