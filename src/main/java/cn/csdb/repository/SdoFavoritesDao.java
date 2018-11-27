package cn.csdb.repository;

import cn.csdb.model.SdoFavorites;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class SdoFavoritesDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //byCql
    //设置isValid字段。取消收藏，更新数据库，即改变isValid字段值
    //添加收藏信息
    public int addData(String loginId,String sdoId,String title){
        SdoFavorites sdoFavorites = new SdoFavorites();
        sdoFavorites.setLoginId(loginId);
        sdoFavorites.setSdoId(sdoId);
        sdoFavorites.setIsValid(1);
        sdoFavorites.setUpdateTime(DateUtils.addHours(new Date(),8));
        sdoFavorites.setTitle(title);
        mongoTemplate.save(sdoFavorites);
        if(getByLoginIdAndSdoId(loginId,sdoId)!=null)
            return 1;
        else
            return -1;
    }

    //收藏或者取消收藏结果
    public int updateFavorite(String loginId ,String sdoId ,int isValid){
        DBObject query = QueryBuilder.start().and("sdo_id").is(sdoId).and("login_id").is(loginId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Update stat = Update.update("isValid",isValid).set("update_time",DateUtils.addHours(new Date(),8));
        mongoTemplate.updateFirst(basicQuery, stat,SdoFavorites.class);
        DBObject findQuery = QueryBuilder.start().and("sdo_id").is(sdoId).and("login_id").is(loginId).and("isValid").is(isValid).get();
        BasicQuery findBasicQuery = new BasicQuery(findQuery);
        SdoFavorites sdoFavorites = mongoTemplate.findOne(findBasicQuery,SdoFavorites.class);
        if (sdoFavorites!=null){
            return 1;
        }
        return -1;
    }

    //判断favorite表中是否有该条记录，不关注isValid值
    public int hasFavorite(String loginId ,String sdoId){
        DBObject query = QueryBuilder.start().and("sdo_id").is(sdoId).and("login_id").is(loginId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        SdoFavorites sdoFavorites = mongoTemplate.findOne(basicQuery,SdoFavorites.class);
        if (sdoFavorites==null){
            return 0;
        }
        return 1;
    }

    //由loginId和sdoId获得sdoFavorite
    public SdoFavorites getByLoginIdAndSdoId(String loginId,String sdoId){
        DBObject query = QueryBuilder.start().and("sdoId").is(sdoId).and("loginId").is(loginId).and("isValid").is(1).get();
        BasicQuery basicQuery = new BasicQuery(query);
        SdoFavorites favorites= mongoTemplate.findOne(basicQuery,SdoFavorites.class);
        if(favorites!=null){
            return favorites;
        }else{
            return null;
        }
    }

        /*取消收藏就删除sdo，不设置isValid字段，运行，点击“已收藏”，却添加该条记录
    //添加收藏信息
    public int addData(String loginId,String sdoId){
        SdoFavorites sdoFavorites = new SdoFavorites();
        sdoFavorites.setLoginId(loginId);
        sdoFavorites.setSdoId(sdoId);
        //sdoFavorites.setIsValid(1);
        sdoFavorites.setUpdateTime(new Date());
        mongoTemplate.save(sdoFavorites);
        if(getByLoginIdAndSdoId(loginId,sdoId)!=null)
            return 1;
        else
            return -1;
    }

    //删除收藏信息--取消收藏即删除
    public int deleteData(String loginId,String sdoId){
        DBObject query = QueryBuilder.start().and("sdo_id").is(sdoId).and("login_id").is(loginId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        mongoTemplate.findAndRemove(basicQuery,SdoFavorites.class);
        if(getByLoginIdAndSdoId(loginId,sdoId)==null)
            return 1;
        else
            return -1;
    }

    //由loginId和sdoId获得sdoFavorite
    public SdoFavorites getByLoginIdAndSdoId(String loginId,String sdoId){
        DBObject query = QueryBuilder.start().and("sdo_id").is(sdoId).and("login_id").is(loginId).and("isValid").is('1').get();
        BasicQuery basicQuery = new BasicQuery(query);
        SdoFavorites favorites= mongoTemplate.findOne(basicQuery,SdoFavorites.class);
        if(favorites!=null){
            return favorites;
        }else{
            return null;
        }
    }

    //判断是否收藏sdo
    public int hasFavorite(String loginId,String sdoId){
        SdoFavorites favorites=getByLoginIdAndSdoId(loginId,sdoId);
        if(favorites!=null&&!favorites.equals("")&&!favorites.equals("null"))
            return 1;
        else
            return 0;
    }
    */

    //推荐数据

    //根据sodId获取收藏数据
    public List<SdoFavorites> getFavoritesBySdoId(String sdoId){
        DBObject query = QueryBuilder.start().and("sdoId").is(sdoId).and("isValid").is(1).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<SdoFavorites> sdoIdFavorites=mongoTemplate.find(basicQuery,SdoFavorites.class);
        if(sdoIdFavorites.size()>0){
            return sdoIdFavorites;
        }else{
            return null;
        }
    }
    //统计收藏A的用户数
    public int getsdoAUserCount(String sdoId){
        List<SdoFavorites> aFavorites=getFavoritesBySdoId(sdoId);
        if(aFavorites.size()>0){
            return aFavorites.size();
        }else{
            return 0;
        }
    }

    //统计收藏A也收藏B的用户数
    public int getAandBUserCount(String sdoIdA,String sdoIdB){
        List<SdoFavorites> aFavorites=getFavoritesBySdoId(sdoIdA);
        List<SdoFavorites> bFavorites=getFavoritesBySdoId(sdoIdB);
        int i=0;
        int contB=0;
        if(aFavorites.size()>0){
            if(bFavorites.size()>0){

            }
        }
        return i;
    }


    //  以下是后台写的
    //判断sdo是否收藏
    public Integer checkIsValid(String loginId ,String sdoId){
        DBObject query = QueryBuilder.start().and("sdo_id").is(sdoId).and("login_id").is(loginId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        SdoFavorites sdoFavorites = mongoTemplate.findOne(basicQuery,SdoFavorites.class);
        if (sdoFavorites==null){
            //return 2;
            return null;
        }
        return 1;
    }

    //收藏或者取消收藏结果
    public void updateIsValid(String loginId ,String sdoId ,Integer isValid){
        DBObject query = QueryBuilder.start().and("sdo_id").is(sdoId).and("login_id").is(loginId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Update stat = Update.update("isValid",isValid).set("update_time",DateUtils.addHours(new Date(),8));
        mongoTemplate.updateFirst(basicQuery, stat,SdoFavorites.class);
    }


    //统计用户收藏的记录条数
    public long getCount(String loginId){
        return mongoTemplate.count(new Query(Criteria.where("loginId").is(loginId)),SdoFavorites.class);
    }


    public List<SdoFavorites> getList(String loginid, int start, int pageSize){
        DBObject query = QueryBuilder.start()
                .and("loginId")
                .is(loginid)
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(start);
        basicQuery.limit(pageSize);
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "updateTime");
        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
        basicQuery.with(new Sort(orders));
        List<SdoFavorites> list = mongoTemplate.find(basicQuery, SdoFavorites.class);
        System.out.println(list.size());
        return list;
    }


    //删除
    public int deleteFavorite(String id) {
        return mongoTemplate.remove(mongoTemplate.findById(id, SdoFavorites.class)).getN();
    }

    public List<SdoFavorites> getBy(String title, String loginId, Date beginTime, Date endTime){
        QueryBuilder queryBuilder = QueryBuilder.start();
        if(title !=null && !title.equals("")){
            queryBuilder = queryBuilder.and("title").regex(Pattern.compile("^.*"+title+".*$"));
        }
        if (loginId !=null && !loginId.equals("")){
            queryBuilder = queryBuilder.and("login_id").regex(Pattern.compile("^.*"+loginId+".*$"));
        }
        if (beginTime !=null){
            queryBuilder =queryBuilder.and("update_time").greaterThanEquals(beginTime);
        }
        if (endTime != null){
            queryBuilder = queryBuilder.and("update_time").lessThanEquals(endTime);
        }
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "update_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        return mongoTemplate.find(basicQuery,SdoFavorites.class);
    }
}
