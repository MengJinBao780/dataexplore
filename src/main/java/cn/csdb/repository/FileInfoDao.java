package cn.csdb.repository;

import cn.csdb.model.FileInfo;
import cn.csdb.model.Sdo;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class FileInfoDao {
    @Resource
    private MongoTemplate mongoTemplate;
    private Integer totalCount;

    //根据文件类型和页码查出10条数据（每页10条）
    public List<FileInfo> findByPageNumAndFileType(String fileType, Integer pageNum) {
        DBObject query = QueryBuilder.start().and("ft_name").is(fileType).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "update_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip((pageNum - 1) * 10);
        basicQuery.limit(10);
        return mongoTemplate.find(basicQuery, FileInfo.class);
    }

    //根据文件类型和页码查出10条数据（每页10条）
    public List<FileInfo> getFileListBySdoId(String sdoId, Integer pageNum,Integer curPage) {
        DBObject query = QueryBuilder.start().and("sdo_pid").is(sdoId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "update_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip((curPage - 1) * pageNum);
        basicQuery.limit(pageNum);
        return mongoTemplate.find(basicQuery, FileInfo.class);
    }

    //根据文件类型，查询出数据一个多少页（每页10条）
    public Integer getTotalPageNum(String fileType) {
        DBObject query = QueryBuilder.start().and("ft_name").is(fileType).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "update_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        totalCount = mongoTemplate.find(basicQuery, FileInfo.class).size();
        return totalCount % 10 == 0 ? totalCount / 10 : totalCount / 10 + 1;
    }

    //根据sdopid，查询出数据一个多少页（每页10条）
    public JSONObject getTotalPageNumBySdoId(String sdoId, int pageNum) {
        DBObject query = QueryBuilder.start().and("sdo_pid").is(sdoId).get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "update_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        totalCount = mongoTemplate.find(basicQuery, FileInfo.class).size();
        Integer totalPage =  totalCount % pageNum == 0 ? totalCount / pageNum : totalCount / pageNum + 1;
        JSONObject jo = new JSONObject();
        jo.put("totalPage",totalPage);
        jo.put("totalCount",totalCount);
        return jo;
    }


    public Integer totalCount() {
        return totalCount;
    }

    //根据id获取fileinfo对象
    public FileInfo findById(String id) {
        return mongoTemplate.findById(id, FileInfo.class);
    }


    public List<FileInfo> getAll(){
        return mongoTemplate.findAll(FileInfo.class);

    }


    public List<FileInfo> getEffectiveData(){
        DBObject query = QueryBuilder.start().and("ft_name").in(new String[]{"xlsx"}).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.find(basicQuery, FileInfo.class);
    }


    public List<FileInfo> getList(int start, int pageSize, String fileType){
        DBObject query = QueryBuilder.start()
                .and("ft_name").is(fileType)
                //测试用20180723
                //.and("sdo_pid").is("5b2c6df29ef7101a58a082f8")
//                .and("sdo_pid").is("5b2c6df29ef7101a58a082de")
//                .and("sdo_pid").is("5b2c6df29ef7101a58a082df")
//                .and("sdo_pid").is("5b2c6df29ef7101a58a082e6")
//                .and("sdo_pid").is("5b2c6df29ef7101a58a082da")
//                .and("sdo_pid").is("5b2c6df29ef7101a58a082dd")
//                .and("sdo_pid").is("5b2c6df29ef7101a58a082df")
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(start);
        basicQuery.limit(pageSize);
//        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, "update_time");
//        List<Sort.Order> orders = Lists.newArrayList(sortOrder);
//        basicQuery.with(new Sort(orders));
        List<FileInfo> list = mongoTemplate.find(basicQuery, FileInfo.class);
        //System.out.println(list.size());
        return list;
    }

    public long getTotalCount(){
        DBObject query = QueryBuilder.start()
                .get();
        BasicQuery basicQuery = new BasicQuery(query);
        return  mongoTemplate.count(basicQuery, FileInfo.class);
    }


    public List<FileInfo> getExcelData(){
        DBObject query = QueryBuilder.start().and("ft_name").in(new String[]{"xlsx"}).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.find(basicQuery, FileInfo.class);
    }

    //获取时间范围
    public List<Date> getDateRange(){
        List<Date> dateRange = new ArrayList<>();
        DBObject dbObject = QueryBuilder.start().and("ft_name").in(new String[]{"HDF","xlsx"}).get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.ASC, "update_time");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(1);
        dateRange.add(mongoTemplate.find(basicQuery,FileInfo.class).get(0).getUpdateTime());
        so = new Sort.Order(Sort.Direction.DESC, "update_time");
        sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip(0);
        basicQuery.limit(1);
        dateRange.add(mongoTemplate.find(basicQuery,FileInfo.class).get(0).getUpdateTime());
        return dateRange;
    }

    /**
     * ceph test
     *
     * @param fileInfo
     */
    public void save(FileInfo fileInfo) {
        mongoTemplate.insert(fileInfo);
    }

    /**
     * ceph test
     *
     * @param cephFlag
     * @return
     */
    public List<FileInfo> getFiles(int cephFlag) {
        DBObject query = QueryBuilder.start().and("cephFlag").is(cephFlag).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.find(basicQuery, FileInfo.class);
    }

    /**
     * ceph test
     *
     * @param fileIds
     * @return
     */
    public List<FileInfo> getFiles(String[] fileIds) {
        BasicDBList docIds = new BasicDBList();
        docIds.addAll(Lists.newArrayList(fileIds));
        DBObject query = QueryBuilder.start().and("_id").in(docIds).get();
        BasicQuery basicQuery = new BasicQuery(query);
        return mongoTemplate.find(basicQuery, FileInfo.class);
    }

    public List<FileInfo> countFilesSize(Date starttime){
        Query query = new Query();
        Criteria criteria = Criteria.where("create_time").lte(starttime);
        query.addCriteria(criteria);
        return mongoTemplate.find(query,FileInfo.class);
    }

    public long countFiles(){
        long l = mongoTemplate.count(new Query(),FileInfo.class);
        return l;
    }

    public List<FileInfo> getFileInfosToES(int num){
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        basicQuery.skip(num * 1000);
        basicQuery.limit(1000);
        return mongoTemplate.find(basicQuery, FileInfo.class);
    }
}
