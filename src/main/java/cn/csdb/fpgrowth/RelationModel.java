package cn.csdb.fpgrowth;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

/**
 * Created by ajian on 2018/5/25.
 */
@Document(collection = "sdo_relation_model")
public class RelationModel {

    @Id
    @Field("sdoId")
    private String sdoId;
    private int count;
    @Field("relations")
    private List<String> relations;
    private Set<String> relationSet;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSdoId() {
        return sdoId;
    }

    public void setSdoId(String sdoId) {
        this.sdoId = sdoId;
    }

    public List<String> getRelations() {
        return relations;
    }

    public void setRelations(List<String> relations) {
        this.relations = relations;
    }

    public Set<String> getRelationSet() {
        return relationSet;
    }

    public void setRelationSet(Set<String> relationSet) {
        this.relationSet = relationSet;
    }

}
