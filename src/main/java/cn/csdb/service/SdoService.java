package cn.csdb.service;

import cn.csdb.model.Sdo;
import cn.csdb.model.Tag;
import cn.csdb.repository.SdoDao;
import cn.csdb.repository.TagDao;
import cn.csdb.utils.ESUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.CoordinatesBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SdoService {
    @Resource
    private SdoDao sdoDao;

    @Resource
    private TagDao tagDao;


    private  TransportClient client = ESUtil.getClient();
    //获取sdo对象
    public cn.csdb.model.Resource getSdoById(String id){
        return sdoDao.getSdoById(id);
    }
    //把MB单位的数据转换成GB,TB,PB
    public String transMData(Double d){
        String mSize;
        if (d<1000){
            mSize = String.format("%.2f", d)+"MB";
        }else if(d<1024*1000){
            mSize = String.format("%.2f", d/1024)+"GB";
        }else if(d<1000*1024*1024){
            mSize = String.format("%.2f", d/(1024*1024))+"TB";
        }else{
            mSize = String.format("%.2f", d/(1024*1024*1024))+"PB";
        }
        return mSize;
    }
    //首页提取sdo中需要字段并加工成需要的格式
    public List<Map<String,String>> getInitDates(String order) {
        List<cn.csdb.model.Resource> sdos=sdoDao.getInitDates(order);
        List<Map<String, String>> maps = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cn.csdb.model.Resource temp = null;
        Map<String, String> map = null;
        for (int i = 0; i < sdos.size(); i++) {
            temp = sdos.get(i);
            map = new HashMap<>();
            map.put("id", temp.getId());
            map.put("title", temp.getTitle());
            map.put("iconPaht", temp.getImagePath());
            map.put("publisherPublishTime", simpleDateFormat.format(temp.getCreateTime()));
            map.put("publishOrganization", temp.getPublishOrgnization());
/*
            map.put("visitCount",temp.getVisitCount().toString());
*/
            maps.add(map);
        }
        return maps;
    }

    //浏览sdo详情页,就是页面关于sdo基本信息
    public Map<String,Object> visitSdo(String id){
        Map<String,Object> map  = new HashMap<>();
        cn.csdb.model.Resource sdo =  sdoDao.getSdoById(id);
        if (sdo==null){
            return map;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        map.put("id",sdo.getId());
        map.put("title",sdo.getTitle());
        map.put("creator",sdo.getCreatePerson());
        map.put("time",sdo.getCreateTime());
        map.put("vCount",sdo.getvCount());
        map.put("dCount",sdo.getdCount());
        map.put("pOrganization",sdo.getPublishOrgnization());        /*List<String> list = new ArrayList<>();
        for (int i = 0;i<sdo.getCreatorOrganization().size();i++){
            list.add(sdo.getCreatorOrganization().get(i).getOrganization());
        }
        map.put("creator",list);
        map.put("time",simpleDateFormat.format(sdo.getPublisherPublishTime()));
        map.put("pid",sdo.getPid());
        map.put("quote",sdo.getPid());
        map.put("fNumber",sdo.getToFilesNumber());
        map.put("mSize",transMData(sdo.getToMemorySize()));
        map.put("vCount",sdo.getVisitCount());
        map.put("dCount",sdo.getDownloadCount());
        map.put("pOrganization",sdo.getPublisher().getOrganization());
        map.put("pName",sdo.getPublisher().getName());
        map.put("pTel",sdo.getPublisher().getTelPhone());
        map.put("pEmail",sdo.getPublisher().getEmail());
        map.put("fileType",sdo.getDataFormat());
        list = new ArrayList<>();
        for (int i = 0 ; i<sdo.getTags().size();i++){
            list.add(sdo.getTags().get(i));
        }
        map.put("tags",list);
        map.put("desc",sdo.getDesc());*/
        return map;
    }

    //获取sdo元数据
    public Map<String,String> getSdoDetails(String id ){
        cn.csdb.model.Resource sdo = sdoDao.getSdoById(id);
        Map<String,String> map = new HashMap<>();
        if (sdo == null){
            return  map;
        }
        map.put("title",sdo.getTitle());
/*
        map.put("desc",sdo.getDesc());
*/
        map.put("keyword",sdo.getKeyword());
       /* map.put("visitLimit",sdo.getVisitLimit().equals("1")? "公开":"限制");
        map.put("rangeDescription",sdo.getRangeDescription());
        map.put("center",sdo.getCenter());
        map.put("upLeft",sdo.getUpLeft());
        map.put("lowLeft",sdo.getLowLeft());
        map.put("upRight",sdo.getUpRight());
        map.put("lowRight",sdo.getLowRight());*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        map.put("startTime",simpleDateFormat.format(sdo.getStartTime()));
        map.put("endTime",simpleDateFormat.format(sdo.getEndTime()));
/*
        map.put("rSpatial",sdo.getSpatialResolution());
*/
        /*map.put("rTime",sdo.getTimeResolution());
        map.put("rightstatement",sdo.getRightstatement());
        String cOrganization="";
        String cName="";
        for (int i = 0 ; i<sdo.getCreatorOrganization().size();i++){
            if (i !=0){
                cOrganization =cOrganization+",";
                cName = cName +",";
            }
            cOrganization=cOrganization+sdo.getCreatorOrganization().get(i).getOrganization();
            cName = cName + sdo.getCreatorOrganization().get(i).getName();
        }
        map.put("cOrganization",cOrganization);
        map.put("cName",cName);
        map.put("cTime",simpleDateFormat.format(sdo.getCreatorCreateTime()));
        map.put("pOrganization",sdo.getPublisher().getOrganization());
        map.put("email",sdo.getPublisher().getEmail());
        map.put("tel",sdo.getPublisher().getTelPhone());
        map.put("pTime",simpleDateFormat.format(sdo.getPublisherPublishTime()));
        map.put("mSize",transMData(sdo.getToMemorySize()));
        map.put("toFilesNumber",sdo.getToFilesNumber().toString());
        map.put("toRecordNumber",sdo.getToRecordNumber().toString());*/
        return map;
    }


    //sdo浏览次数+1
    public void addVisitCount(String id){
        sdoDao.addVisitCount(id);
    }

    //sdo下载次数+1
    public void addDownloadCount(String id){
        sdoDao.addDownloadCount(id);
    }

    //获取所有的sdo记录
    public List<Sdo> getAll(){
        return sdoDao.getAll();
    }

    //获取sdo对像中不同的文件类型
    public List<String> findDistinctDataType(){
        return sdoDao.findDistinctDataType();
    }

    //获取sdo对像中不同的数据发布者
    public List<String> findDistinctPublisher(){
        return sdoDao.findDistinctPublisher();
    }

    public JSONObject getData(String prodId, String searchKey, String tags, String dataFormat, String publisher, String timerange,
                     String sortName, int pageNo, int pageSize) {
        System.out.println(prodId+"-"+searchKey+"-"+tags+"-"+dataFormat+"-"+publisher+"-"+timerange+"-"+sortName+"-"+pageNo+"-"+pageSize);
        //HighlightBuilder.Field desc = getHighlightField();

        /*SearchResponse searchResponse = client.prepareSearch("sdoindex").setTypes("sdo")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("user", "aa"))
                .setFrom((pageNo-1) * pageSize).setSize(pageSize).setExplain(true)
                .get();
        */
        HighlightBuilder hiBuilder=new HighlightBuilder();
        hiBuilder.preTags("<mark2>");
        hiBuilder.postTags("</mark2>");
        hiBuilder.field("desc",500).field("title",100);
        //hiBuilder.field("title",100);
        //hiBuilder.field("desc");

        SearchRequestBuilder  searchRequestBuilder = client.prepareSearch("sdoindex").setTypes("sdo")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        // QueryBuilder qb2 = QueryBuilders.multiMatchQuery("git", "title", "content");
        if (StringUtils.isNoneBlank(prodId)){
            TermQueryBuilder termQueryBuilder =  QueryBuilders.termQuery("productId", prodId);
            boolQueryBuilder.must(termQueryBuilder);

        }
        if (StringUtils.isNoneBlank(searchKey)){
            MultiMatchQueryBuilder searchKeyQueryBuilder =  QueryBuilders.multiMatchQuery(searchKey,"title","desc","tags","keyword","publisher","dataFormat");
            boolQueryBuilder.must(searchKeyQueryBuilder);
        }
        if (StringUtils.isNoneBlank(tags)){
            System.out.println(tags);
            String[] list = tags.split(",");
            for (String s : list) {
                TermQueryBuilder tagsQueryBuilder = QueryBuilders.termQuery("tags", s);
                boolQueryBuilder.must(tagsQueryBuilder);
            }
        }
        if (StringUtils.isNoneBlank(dataFormat)){
            MatchQueryBuilder dataTypeQueryBuilder =  QueryBuilders.matchQuery("dataFormat", dataFormat);
            boolQueryBuilder.must(dataTypeQueryBuilder);
        }
        if (StringUtils.isNoneBlank(publisher)){
            MatchPhraseQueryBuilder publisherQueryBuilder =  QueryBuilders.matchPhraseQuery("publisher", publisher);
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        if (StringUtils.isNoneBlank(timerange)){
            String[] datas = timerange.split(",");
            if (datas.length == 2){
                RangeQueryBuilder startQueryBuilder =  QueryBuilders.rangeQuery("publisherPublishTime").gte(datas[0]);
                RangeQueryBuilder endQueryBuilder =  QueryBuilders.rangeQuery("publisherPublishTime").lte(datas[1]);
                boolQueryBuilder.must(startQueryBuilder);
                boolQueryBuilder.must(endQueryBuilder);
            }
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        if (StringUtils.isNoneBlank(sortName)){
            searchRequestBuilder.addSort(sortName, SortOrder.DESC);
        }

        SearchResponse searchResponse= searchRequestBuilder.setQuery(boolQueryBuilder)
                .highlighter(hiBuilder).setFrom(pageNo * pageSize).setSize(pageSize).setExplain(true)
                .get();
        SearchResponse top_200= searchRequestBuilder.setQuery(boolQueryBuilder)
                .setFrom(pageNo * pageSize).setSize(200).setExplain(true)
                .get();
        long totalNum = searchResponse.getHits().getTotalHits();
        System.out.println("总条数:" + totalNum);
        System.out.println(searchResponse.getHits().getHits().length);
        List<Map<String,Object>> list = Lists.newArrayList();
        List<cn.csdb.model.Resource> sdoList = Lists.newArrayList();
        for(SearchHit hit : searchResponse.getHits().getHits())
        {
            Map<String,Object> map = hit.getSourceAsMap();
            HighlightField descHightField = hit.getHighlightFields().get("desc");
            if (descHightField != null){
                Text[] fragments = descHightField.fragments();
                String descTmp ="";
                for(Text text:fragments){
                    descTmp+=text;
                }
                if (StringUtils.isNotEmpty(descTmp)){
                    map.put("desc",descTmp);
                }
            }
            System.out.println("11111" + map.get("desc"));

            /*HighlightField titleHightField = hit.getHighlightFields().get("title");
            if (titleHightField != null){
                Text[] titlefragments = titleHightField.fragments();
                String titleTmp ="";
                for(Text text:titlefragments){
                    titleTmp+=text;
                }
                if (StringUtils.isNotEmpty(titleTmp)){
                    map.put("title",titleTmp);
                    map.put("titlexia",titleTmp);
                }
            }
            System.out.println("33333" + map.get("title"));
            System.out.println("444444" + map.get("titlexia"));*/

            list.add(map);

            System.out.println(hit.getSourceAsString());
            //System.out.println(hit.getSourceAsMap().get("title"));
            //System.out.println(hit.getSourceAsMap().get("desc"));
            System.out.println(hit.getHighlightFields());
            //System.out.println(hit.getHighlightFields().get("desc").getFragments()[0].toString());
            System.out.println("===========================");
        }
        for(SearchHit hit : top_200.getHits().getHits()){
            Map<String,Object> map = hit.getSourceAsMap();
            cn.csdb.model.Resource sdo = sdoDao.getSdoById(map.get("sdoid").toString());
            sdoList.add(sdo);
        }

/*
        List<Map<String,String>> tagList = getMapsBySdoList(sdoList);
*/
        long totalPage = totalNum % pageSize == 0 ? (totalNum / pageSize) : (totalNum / pageSize + 1);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("searchKey", searchKey);
        jsonObject.put("pageNo", pageNo);
        jsonObject.put("list",list);
        jsonObject.put("currentPage",pageNo);
        jsonObject.put("totalCount",totalNum);
        jsonObject.put("totalPages",totalPage);
/*
        jsonObject.put("tagList",tagList);
*/

        return jsonObject;
    }



    private HighlightBuilder.Field getHighlightField() {
        HighlightBuilder.Field introduction = new HighlightBuilder.Field("desc");
        introduction.preTags("<mark2>");
        introduction.postTags("</mark2>");
        introduction.numOfFragments(0);
        return introduction;
    }


   /* public List<Map<String,String>> getMapsBySdoList(List<cn.csdb.model.Resource> sdoList){
        List<Map<String,String>> list = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        for (cn.csdb.model.Resource sdo : sdoList){
            for (String str : sdo.getTags()){
                if(!tags.contains(str)) {
                    tags.add(str);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("prodId", sdo.getProductId());
                    map.put("sdoId", sdo.getId());
                    Tag tag = tagDao.findByProductIdAndTagName(sdo.getProductId(), str);
                    if (tag != null)
                        map.put("num", tag.getNumber().toString());
                    else
                        map.put("num", "1");
                    map.put("tagName", str);
                    list.add(map);
                }
            }
        }
        return list.subList(0,list.size()>40?40:list.size());
    }*/


    //mapSearch页面查询sdo
    public Map<String,Object> getByES(String keyword, String fileType, String publisher, double x1, double x2, double y1, double y2, String beginTime,String endTime,int pageNum) throws IOException {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("sdoindex").setTypes("sdo")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        if (keyword!=null && !keyword.equals("")){
            MultiMatchQueryBuilder searchKeyQueryBuilder =  QueryBuilders.multiMatchQuery(keyword,"title","desc","tags","keyword","publisher","dataFormat");
            boolQueryBuilder.must(searchKeyQueryBuilder);
        }
        if (fileType!=null && !fileType.equals("")){
            MatchPhraseQueryBuilder publisherQueryBuilder =  QueryBuilders.matchPhraseQuery("dataFormat", fileType);
            boolQueryBuilder.must(publisherQueryBuilder);
        }
        if (publisher!=null && !publisher.equals("")){
            MatchPhraseQueryBuilder publisherQueryBuilder =  QueryBuilders.matchPhraseQuery("publisher", publisher);
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
        RangeQueryBuilder rangeQueryBuilder =  QueryBuilders.rangeQuery("publisherPublishTime").format("yyyy").gte(beginTime).lte(endTime);
        boolQueryBuilder.must(rangeQueryBuilder);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.addSort("publisherPublishTime", SortOrder.DESC);
        SearchResponse searchResponse= searchRequestBuilder.setQuery(boolQueryBuilder)
                .setFrom((pageNum-1) * 10).setSize(10).setExplain(true)
                .get();
        long totalNum = searchResponse.getHits().getTotalHits();
        long totalPage = totalNum%10==0?totalNum/10:totalNum/10+1;
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()){
            Map<String,Object> mo = hit.getSourceAsMap();
            Map<String,Object> ms = new HashMap<>();
            ms.put("sdoid",mo.get("sdoid"));
            ms.put("title",mo.get("title"));
            ms.put("desc",mo.get("desc"));
            ms.put("img",mo.get("iconPath"));
            ms.put("publisherPublishTime",mo.get("publisherPublishTime").toString().substring(0,10));
            ms.put("publisher",mo.get("publisher"));
            ms.put("dataFormat",mo.get("dataFormat"));
            ms.put("center",mo.get("center"));
            ms.put("upLeft",mo.get("upLeft"));
            ms.put("lowLeft",mo.get("lowLeft"));
            ms.put("upRight",mo.get("upRight"));
            ms.put("lowRight",mo.get("lowRight"));
            list.add(ms);
        }
        map.put("sdo",list);
        map.put("totalNum",totalNum);
        map.put("totalPage",totalPage);
        map.put("pageNum",pageNum);
        return map;
    }

    //时间范围
    public int[] getDateRange(){
        List<Date> list = sdoDao.getDateRange();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return new int[]{Integer.parseInt(simpleDateFormat.format(list.get(0))),Integer.parseInt(simpleDateFormat.format(list.get(1)))};
    }


    public Sdo getSdoByPid(String pid){
        return sdoDao.getSdoByPid(pid);
    }

    public long countFileSize() {
        List<Sdo> sdos = sdoDao.getAllSdos();
        long fileSize = 0L;
        for (Sdo sdo : sdos) {
            fileSize += sdo.getToMemorySize();
        }
        return fileSize;
    }

    //20180717根据名称获取实体信息
    public Sdo getByTitle(String title){
        return  sdoDao.getByTitle(title);
    }
}
