package cn.csdb.repository;

import cn.csdb.fpgrowth.RelationModel;
import cn.csdb.model.SdoRecomendations;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SdoRecomendationsDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public List<SdoRecomendations> getTopNRecListBySdoId(String sdoId,int n){

        DBObject query = QueryBuilder.start()
                .and("sdo_id")
                .is(sdoId)
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "similarity");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<SdoRecomendations> recSdoList = mongoTemplate.find(basicQuery, SdoRecomendations.class);
        if(recSdoList.size()>0){
            if(recSdoList.size()>n){
                return recSdoList.subList(0,n);
            }else{
                return recSdoList;
            }
        }else{
            return null;
        }
    }

}
