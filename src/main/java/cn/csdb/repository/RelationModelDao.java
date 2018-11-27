package cn.csdb.repository;

import cn.csdb.fpgrowth.RelationModel;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ajian on 2018/5/29.
 */
@Repository
public class RelationModelDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public void save(RelationModel relationModel) {
        mongoTemplate.save(relationModel);
    }

    public List<String> getRelationsBySdoId(String sdoId) {
        RelationModel relationModel = mongoTemplate.findById(sdoId, RelationModel.class);
        return relationModel != null ? relationModel.getRelations() : null;
    }

}
