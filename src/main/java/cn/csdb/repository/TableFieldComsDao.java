package cn.csdb.repository;

import cn.csdb.model.TableFieldComs;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by shibaoping on 2018/10/29 18:07.
 */
@Repository
public class TableFieldComsDao {

    @Resource
    private MongoTemplate mongoTemplate;


    public List<TableFieldComs> getTableFieldComsByUriEx(int uriHash) {
        return mongoTemplate.find(new Query(Criteria.where("uriHash").is(uriHash)),TableFieldComs.class);
    }

    public void updateFieldComs(TableFieldComs tableFieldComs,String subjectCode,String dbName,String tableName,String state) {

        mongoTemplate.findAndModify(new Query(Criteria.where("id").is(tableFieldComs.getId())),
                new Update().set("fieldComs", tableFieldComs.getFieldComs()).set("updateTime", tableFieldComs.getUpdateTime())
        ,TableFieldComs.class);
    }
}
