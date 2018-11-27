package cn.csdb.fpgrowth;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * Created by ajian on 2018/5/29.
 */
public class MapLinked<T> extends HashMap<T, RelationModel> {

    private int maxCount = 20; //最大条数

    public MapLinked(int maxCount) {
        if(maxCount < 1){
            return;
        }
        this.maxCount = maxCount;
    }

    @Override
    public RelationModel put(T key, RelationModel value) {
        RelationModel relationModel = get(key);
        if (relationModel == null) {
            LinkedHashSet<String> sets = Sets.newLinkedHashSet(value.getRelations());
            value.setRelationSet(sets);
            value.setCount(0);
            value.setRelations(null);
            return super.put(key, value);
        }
        List<String> relations = value.getRelations();
        Set<String> relationSet = relationModel.getRelationSet();
        for (String relation : relations) {
            if (relationSet.size() < maxCount) {
                relationSet.add(relation);
            } else {
                break;
            }
        }
        return relationModel;
    }
}
