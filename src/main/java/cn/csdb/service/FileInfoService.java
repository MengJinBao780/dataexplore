package cn.csdb.service;

import cn.csdb.model.FileInfo;
import cn.csdb.repository.FileInfoDao;
import cn.csdb.utils.ESUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.CoordinatesBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileInfoService {
    @Resource
    private FileInfoDao fileInfoDao;
    @Resource
    private FileTypeService fileTypeService;
    @Resource
    private SdoService sdoService;
    private long totalCount=0;
    private TransportClient client = ESUtil.getClient();

    //获取对应文件类型，对应字段的数据
    public List<Map<String, String>> getFile(List<Map<String, String>> list, String fileType, Integer pageNum) {
        int totalPageNum = fileInfoDao.getTotalPageNum(fileType);
        if (totalPageNum < pageNum) {
            pageNum = 1;
        }
        List<FileInfo> fs = fileInfoDao.findByPageNumAndFileType(fileType, pageNum);
        List<Map<String, String>> fileInfos = getListMap(fs);
        List<Map<String, String>> maps = new ArrayList<>();
        for (int i = 0; i < fileInfos.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", fileInfos.get(i).get("id"));
            for (int j = 0; j < list.size(); j++) {
                map.put(list.get(j).get("fieldName"), fileInfos.get(i).get(list.get(j).get("fieldName")));
            }
            maps.add(map);
        }
        return maps;
    }

    //获取对应文件类型，对应字段的数据
    public JSONArray getFile(String sdoId, Integer pageNum, Integer curpage) {
        JSONObject joNum = fileInfoDao.getTotalPageNumBySdoId(sdoId,pageNum);
        int totalPageNum = (Integer)joNum.get("totalPage");
        if (totalPageNum < pageNum) {
            pageNum = 1;
        }
        List<FileInfo> fileInfos = fileInfoDao.getFileListBySdoId(sdoId, pageNum,curpage);
        JSONArray ja = (JSONArray)JSONObject.toJSON(fileInfos);
//        List<Map<String, String>> fileInfos = getListMap(fs);
//        List<Map<String, String>> maps = new ArrayList<>();
//        for (int i = 0; i < fileInfos.size(); i++) {
//            Map<String, String> map = new HashMap<>();
//            map.put("id", fileInfos.get(i).get("id"));
//            for (int j = 0; j < list.size(); j++) {
//                map.put(list.get(j).get("fieldName"), fileInfos.get(i).get(list.get(j).get("fieldName")));
//            }
//            maps.add(map);
//        }
        return ja;
    }

    //获取HDF查询条件数据
    public List<Map<String,String>> getFileByCondition(List<Map<String,String>> list,String pid,String number,Double x1 ,Double x2 ,Double y1,Double y2,Double cloudiness1,Double cloudiness2,String fileType,Integer pageNum,String sdoId)throws IOException{
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("fileinfoindex").setTypes("fileinfo")
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("fileindex3").setTypes("fileinfo2")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        if (pid!=null && !pid.equals("")){
            WildcardQueryBuilder publisherQueryBuilder =  QueryBuilders.wildcardQuery("pid", "*"+pid+"*");
            boolQueryBuilder.must(publisherQueryBuilder);
        }

        if (number!=null && !number.equals("")){
            WildcardQueryBuilder publisherQueryBuilder =  QueryBuilders.wildcardQuery("number", "*"+number+"*");
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        if (!(x1==0 && x2==0 && y1==0 && y2==0)){
            GeoShapeQueryBuilder filter = QueryBuilders.geoShapeQuery(
                    "shape",
                    ShapeBuilders.newPolygon(new CoordinatesBuilder()
                            .coordinate(x1,y2)
                            .coordinate(x2,y2)
                            .coordinate(x2,y1)
                            .coordinate(x1,y1)
                            .coordinate(x1,y2)))
                    .relation(ShapeRelation.INTERSECTS);
            boolQueryBuilder.must(filter);
        }
        RangeQueryBuilder rangeQueryBuilder =  QueryBuilders.rangeQuery("cloudiness").gte(cloudiness1).lte(cloudiness2);
        boolQueryBuilder.must(rangeQueryBuilder);
        if (fileType!=null && !fileType.equals("")){
            MatchPhraseQueryBuilder publisherQueryBuilder =  QueryBuilders.matchPhraseQuery("filetype", fileType);
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        if (sdoId!=null && !sdoId.equals("")){
            MatchPhraseQueryBuilder publisherQueryBuilder =  QueryBuilders.matchPhraseQuery("sdoid", sdoId);
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.addSort("updatetime", SortOrder.DESC);
        SearchResponse searchResponse= searchRequestBuilder.setQuery(boolQueryBuilder)
                .setFrom((pageNum-1) * 10).setSize(10).setExplain(true)
                .get();
        totalCount = searchResponse.getHits().getTotalHits();
        List<FileInfo>  fs = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> mo = hit.getSourceAsMap();
            fs.add(fileInfoDao.findById(mo.get("fileinfoid").toString()));
        }
        List<Map<String,String>> fileInfos =getListMap(fs);
        List<Map<String,String>> maps = new ArrayList<>();
        for (int i=0;i<fileInfos.size();i++){
            Map<String,String> map = new HashMap<>();
            map.put("id",fileInfos.get(i).get("id"));
            for (int j=0;j<list.size();j++){
                map.put(list.get(j).get("fieldName"),fileInfos.get(i).get(list.get(j).get("fieldName")));

            }
            maps.add(map);
        }
        return maps;
    }

    //获取CERN查询条件数据
    public List<Map<String,String>> getFileByCondition(List<Map<String,String>> list,String pid,String fileName,String fileType,Integer pageNum,String sdoId){
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("fileinfoindex").setTypes("fileinfo")
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("fileindex3").setTypes("fileinfo2")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        if (pid!=null && !pid.equals("")){
            WildcardQueryBuilder publisherQueryBuilder =  QueryBuilders.wildcardQuery("pid", "*"+pid+"*");
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        if (fileName!=null && !fileName.equals("")){
            WildcardQueryBuilder publisherQueryBuilder =  QueryBuilders.wildcardQuery("filename", "*"+fileName+"*");
            boolQueryBuilder.must(publisherQueryBuilder);
        }

        if (fileType!=null && !fileType.equals("")){
            if (fileType.equals("XLSX")){
                fileType = fileType.toLowerCase();
            }
            MatchPhraseQueryBuilder publisherQueryBuilder =  QueryBuilders.matchPhraseQuery("filetype", fileType);
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        if (sdoId!=null && !sdoId.equals("")){
            MatchPhraseQueryBuilder publisherQueryBuilder =  QueryBuilders.matchPhraseQuery("sdoid", sdoId);
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.addSort("updatetime", SortOrder.DESC);
        SearchResponse searchResponse= searchRequestBuilder.setQuery(boolQueryBuilder)
                .setFrom((pageNum-1) * 10).setSize(10).setExplain(true)
                .get();
        totalCount = searchResponse.getHits().getTotalHits();
        List<FileInfo>  fs = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> mo = hit.getSourceAsMap();
            fs.add(fileInfoDao.findById(mo.get("fileinfoid").toString()));
        }
        List<Map<String,String>> fileInfos =getListMap(fs);
        List<Map<String,String>> maps = new ArrayList<>();
        for (int i=0;i<fileInfos.size();i++){
            Map<String,String> map = new HashMap<>();
            map.put("id",fileInfos.get(i).get("id"));
            for (int j=0;j<list.size();j++){
                map.put(list.get(j).get("fieldName"),fileInfos.get(i).get(list.get(j).get("fieldName")));
            }
            maps.add(map);
        }
        return maps;
    }

    //格式转换
    private List<Map<String, String>> getListMap(List<FileInfo> fileInfos) {
        List<Map<String, String>> maps = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < fileInfos.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", fileInfos.get(i).getId());
            map.put("fileName", fileInfos.get(i).getFileName());
            map.put("filePath", fileInfos.get(i).getFilePath());
            map.put("ftId", fileInfos.get(i).getFtId());
            map.put("ftName", fileInfos.get(i).getFtName());
            map.put("previewType", fileInfos.get(i).getPreviewType());
            map.put("previewFile", fileInfos.get(i).getPreviewFile());
            map.put("createTime", simpleDateFormat.format(fileInfos.get(i).getCreateTime()));
            map.put("updateTime", simpleDateFormat.format(fileInfos.get(i).getUpdateTime()));
            map.put("status", fileInfos.get(i).getStatus().toString());
            map.put("pid", fileInfos.get(i).getPid());
            map.put("number", fileInfos.get(i).getNumber());
            map.put("cloudiness", fileInfos.get(i).getCloudiness().toString());
            map.put("note",fileInfos.get(i).getNote());
            map.put("size",fileInfos.get(i).getSize());
            map.put("recordNum",fileInfos.get(i).getRecordNum().toString());
            map.put("dataNote",fileInfos.get(i).getDataNote());
            map.put("property",fileInfos.get(i).getProperty());
            map.put("center",fileInfos.get(i).getCenter());
            map.put("upLeft",fileInfos.get(i).getUpLeft());
            map.put("upRight",fileInfos.get(i).getUpRight());
            map.put("lowRight",fileInfos.get(i).getLowRight());
            map.put("lowLeft",fileInfos.get(i).getLowLeft());
            map.put("beginTime",fileInfos.get(i).getBeginTime()==null?"无":simpleDateFormat.format(fileInfos.get(i).getBeginTime()));
            map.put("endTime",fileInfos.get(i).getEndTime()==null?"无":simpleDateFormat.format(fileInfos.get(i).getEndTime()));
            map.put("sdoPid",fileInfos.get(i).getSdoPid());
            maps.add(map);
        }
        return maps;
    }

    public Integer totalCount() {
        return fileInfoDao.totalCount();
    }

    //根据id获取文件格式
    public String getFileType(String id) {
        FileInfo fileInfo = fileInfoDao.findById(id);
        if (fileInfo == null) {
            return "";
        }
        return fileInfo.getFtName();
    }

    //HDF格式返回图片路径
    public String getPreviewPath(String id) {
        FileInfo fileInfo = fileInfoDao.findById(id);
        if (fileInfo == null) {
            return "";
        }
        return fileInfo.getPreviewFile();
    }

    //获取fileinfo对象
    public FileInfo getById(String id) {
        return fileInfoDao.findById(id);
    }

    //某格式文件总页数
    public Integer getTotalPageNum(String fileType) {
        return fileInfoDao.getTotalPageNum(fileType);
    }

    //根据sdoPid某格式文件总页数
    public JSONObject getTotalPageNumBySdoId(String sdoId,int pageNum) {
        return fileInfoDao.getTotalPageNumBySdoId( sdoId, pageNum);
    }


    public long getTotalNumByCondition(){
        return totalCount;
    }

    //es查询
    public Map<String, Object> getByES(String keyword, String fileType, String publisher, double x1, double x2, double y1, double y2, String beginTime, String endTime, int pageNum) throws IOException {
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("fileinfoindex").setTypes("fileinfo")
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("fileindex3").setTypes("fileinfo2")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (keyword != null && !keyword.equals("")) {
            MultiMatchQueryBuilder searchKeyQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "filename", "filetype", "publisher");
            boolQueryBuilder.must(searchKeyQueryBuilder);
        }
        if (fileType != null && !fileType.equals("")) {
            MatchPhraseQueryBuilder publisherQueryBuilder = QueryBuilders.matchPhraseQuery("filetype", fileType);
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        if (publisher != null && !publisher.equals("")) {
            MatchPhraseQueryBuilder publisherQueryBuilder = QueryBuilders.matchPhraseQuery("publisher", publisher);
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        if (!(x1 == 0 && x2 == 0 && y1 == 0 && y2 == 0)) {
            GeoShapeQueryBuilder filter = QueryBuilders.geoShapeQuery(
                    "shape",
                    ShapeBuilders.newPolygon(new CoordinatesBuilder()
                            .coordinate(x1,y2)
                            .coordinate(x2,y2)
                            .coordinate(x2,y1)
                            .coordinate(x1,y1)
                            .coordinate(x1,y2)))
                    .relation(ShapeRelation.INTERSECTS);
            boolQueryBuilder.must(filter);
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("updatetime").format("yyyy").gte(beginTime).lte(endTime);
        boolQueryBuilder.must(rangeQueryBuilder);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.addSort("updatetime", SortOrder.DESC);
        SearchResponse searchResponse = searchRequestBuilder.setQuery(boolQueryBuilder)
                .setFrom((pageNum - 1) * 10).setSize(10).setExplain(true)
                .get();
        long totalNum = searchResponse.getHits().getTotalHits();
        long totalPage = totalNum % 10 == 0 ? totalNum / 10 : totalNum / 10 + 1;
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> mo = hit.getSourceAsMap();
            Map<String, Object> ms = new HashMap<>();
            ms.put("fileinfoid", mo.get("fileinfoid").toString());
            ms.put("filename", mo.get("filename").toString());
            ms.put("updatetime", mo.get("updatetime").toString().substring(0,10));
            ms.put("filetype", mo.get("filetype").toString());
            ms.put("publisher", mo.get("publisher").toString());
            ms.put("sdoid",fileInfoDao.findById(ms.get("fileinfoid").toString()).getSdoPid());
            ms.put("sdotitle",sdoService.getSdoById(ms.get("sdoid").toString()).getTitle());
            ms.put("center",mo.get("center"));
            ms.put("upLeft",mo.get("upLeft"));
            ms.put("lowLeft",mo.get("lowLeft"));
            ms.put("upRight",mo.get("upRight"));
            ms.put("lowRight",mo.get("lowRight"));
            list.add(ms);
        }
        map.put("fileinfo", list);
        map.put("totalNum", totalNum);
        map.put("totalPage", totalPage);
        map.put("pageNum", pageNum);
        return map;
    }

    //时间范围
    public int[] getDateRange(){
        List<Date> list= fileInfoDao.getDateRange();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return new int[]{Integer.parseInt(simpleDateFormat.format(list.get(0))),Integer.parseInt(simpleDateFormat.format(list.get(1)))};
    }

    /**
     * ceph 测试
     *
     * @param originalFilename
     * @return
     */
    public String save(String originalFilename) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(originalFilename);
        fileInfo.setCephFlag(1);
        fileInfoDao.save(fileInfo);
        return fileInfo.getId();
    }


    public List<FileInfo> getFiles(int cephFlag) {
        return fileInfoDao.getFiles(cephFlag);
    }


    public List<FileInfo> getByIds(String[] fileIds) {
        return fileInfoDao.getFiles(fileIds);
    }

}
