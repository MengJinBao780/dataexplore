package cn.csdb.job;


import cn.csdb.model.Sdo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xiajl on 2018/4/16.
 */


@Service
public class SdoIndexJob {
    private Logger logger = LoggerFactory.getLogger(SdoIndexJob.class);
    private final static String dicPath = "/WEB-INF/terms.dic";

    /*@Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    */

    @PostConstruct
    public void initIndex() {
       // elasticsearchTemplate.createIndex(Sdo.class);
       // elasticsearchTemplate.putMapping(Sdo.class);
        //int count = sdoDao.getCount();
        int pageSize = 10;
        int pageIndex = 0;
    }


   /* public void deleteAndInsertIndex(Sdo sdo) {
        elasticsearchTemplate.delete(Sdo.class, String.valueOf(sdo.getId()));
        insert(sdo);
    }

    private String insert(Sdo sdo) {
        IndexQueryBuilder indexQueryBuilder = new IndexQueryBuilder();
        IndexQuery indexQuery = indexQueryBuilder
                .withObject(sdo)
                .withId(String.valueOf(sdo.getId()))
                .build();
        return elasticsearchTemplate.index(indexQuery);
    }*/

    public void updateIndex(){
        logger.info("开始新一轮遍历更新索引");
        //List<Sdo> list = sdoDa
    }
}
