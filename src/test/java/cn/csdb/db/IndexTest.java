package cn.csdb.db;

import cn.csdb.model.*;
import cn.csdb.repository.*;
import cn.csdb.utils.ESUtil;
import org.apache.logging.log4j.LogManager;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.CoordinatesBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by Administrator on 2017/7/5 0005.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})

public class IndexTest {
    @Resource
    private SdoDao sdoDao;

    @Resource
    private FileInfoDao fileInfoDao;

    @Resource
    private FileTypeDao fileTypeDao;

    @Resource
    private ProductDao productDao;

    @Resource
    private SdoVisitDao sdoVisitDao;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(IndexTest.class);

    @Test
    public void test1() throws UnknownHostException {
        // 设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "es20180408").build();
        // 创建client
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("10.0.83.109"), 9300));
        // 搜索数据
        //3.获取IndicesAdminClient对象
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        //4.创建索引
        //CreateIndexResponse ciReponse=indicesAdminClient.prepareCreate("index1").get();
        //System.out.println(ciReponse.isAcknowledged());

        /*IndicesExistsResponse response=indicesAdminClient.prepareExists("index1").get();
        System.out.println(response.isExists());*/

        DeleteIndexResponse deleteResponse = indicesAdminClient
                .prepareDelete("sdoindex")
                .execute()
                .actionGet();
        System.out.println(deleteResponse.isAcknowledged());
    }

    @Test
    public void test2() throws IOException {
        // 设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "es20180408").build();
        // 创建client
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("10.0.83.109"), 9300));
        // 搜索数据
        //3.获取IndicesAdminClient对象
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        //4.创建索引
        //CreateIndexResponse ciReponse=indicesAdminClient.prepareCreate("index1").get();
        //System.out.println(ciReponse.isAcknowledged());

        /*IndicesExistsResponse response=indicesAdminClient.prepareExists("index1").get();
        System.out.println(response.isExists());*/

        IndexResponse response = client.prepareIndex("sdoindex", "sdo", "1001")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "xiajl")
                        .field("address", "北京Beijing")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")

                        /*.field("user", "aaa")
                        .field("address","北京Beijing")
                        .field("postDate", new Date())
                        .field("message", "玩玩Elasticsearch6")*/
                        .endObject()

                )
                .get();
        System.out.println(response.getIndex());
        System.out.println(response.getId());
        System.out.println(response.getResult());
        //System.out.println(response.get);

        GetResponse getResponse = client.prepareGet("sdoindex", "sdo", "1001").get();
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse.getSource());

        //DeleteIndexResponse deleteIndexResponse = indicesAdminClient.prepareDelete("index1").get();
        //System.out.println(deleteIndexResponse.isAcknowledged());

        /*client.prepareUpdate("sdoindex", "sdo", "1001")
                .setDoc(jsonBuilder()
                        .startObject()
                        .field("user", "xiajlpeter")
                        .endObject())
                .get();

        getResponse = client.prepareGet("sdoindex", "sdo","1001").get();
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse.getSource());*/

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        bulkRequestBuilder.add(client.prepareIndex("sdoindex", "sdo", "1002")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "aaaa")
                        .field("postDate", new Date())
                        .field("message", "aaaaa")
                        .endObject()
                )
        );

        bulkRequestBuilder.add(client.prepareIndex("sdoindex", "sdo", "1003")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "bbb")
                        .field("postDate", new Date())
                        .field("message", "bbbbb")
                        .endObject()
                )
        );

        //BulkResponse bulkResponse = bulkRequestBuilder.get();

        getResponse = client.prepareGet("sdoindex", "sdo", "1002").get();
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse.getSource());


         /*searchResponse = client.prepareSearch("sdoindex")
                .setTypes("sdo")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("user", "xiajl"))                 // Query
                //.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                .setFrom(0).setSize(10).setExplain(false)
                .get();*/
        //System.out.println(searchResponse.getHits().totalHits);
        //System.out.println(searchResponse.getHits().getHits().length);

        SearchResponse searchResponse = client.prepareSearch("sdoindex").setTypes("sdo")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("user", "aa"))
                .setFrom(0).setSize(10).setExplain(true)
                .get();
        System.out.println(searchResponse.getHits().getHits().length);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        DeleteResponse deleteResponse = client.prepareDelete("sdoindex", "sdo", "1001").get();
        System.out.println(deleteResponse.getResult());
        System.out.println(deleteResponse.getIndex());

        // 关闭client
        client.close();
    }

    /*public void main(String[] args) throws IOException {
        //createIndex("index1");
        insertData();
    }*/

    @Test
    public void createIndex() {
        //1.判定索引是否存在
        String indexname = "sdoindex";
        boolean flag = ESUtil.isExists(indexname);
        System.out.println("isExists:" + flag);
        if (flag)
            ESUtil.deleteIndex(indexname);
        //2.创建索引
        flag = ESUtil.createIndex(indexname);
        System.out.println("createIndex:" + flag);
        //3.设置Mapping
        try {
            XContentBuilder builder = jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("id")
                    .field("type", "long")
                    .endObject()
                    .startObject("title")
                    .field("type", "text")
                    .field("analyzer", "standard")
                    .field("search_analyzer", "standard")
                    .field("boost", 2)
                    .endObject()
                    .startObject("content")
                    .field("type", "text")
                    .field("analyzer", "standard")
                    .field("search_analyzer", "standard")
                    .endObject()
                    .startObject("postdate")
                    .field("type", "date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("url")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject();

            System.out.println(builder.string());
            ESUtil.setMapping(indexname, "sdo", builder.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //20180424创建SDO表的索引
    @Test
    public void createSdoIndex() {
        //1.判定索引是否存在
        String indexname = "sdoindex";
        boolean flag = ESUtil.isExists(indexname);
        System.out.println("isExists:" + flag);
        if (flag)
            ESUtil.deleteIndex(indexname);
        //2.创建索引
        flag = ESUtil.createIndex(indexname);
        System.out.println("createIndex:" + flag);
        //3.设置Mapping
        try {
            XContentBuilder builder = jsonBuilder()
                    .startObject()
                    .startObject("properties")

                    .startObject("sdoid")
                    .field("type", "text")
                    .endObject()

                    .startObject("title")
                    //.field("type", "text")
                    .field("type", "keyword")
                    //.field("fielddata", true)
                    //.field("analyzer", "ik_smart")
                    //.field("search_analyzer", "ik_smart")
                    .field("boost", 2)

                    .endObject()

                    .startObject("desc")
                    .field("type", "text")
                    .field("analyzer", "ik_smart")
                    .field("search_analyzer", "ik_smart")
                    .endObject()
                    .startObject("keyword")
                    .field("type", "text")
                    .field("analyzer", "ik_smart")
                    .field("search_analyzer", "ik_smart")
                    .endObject()

                    .startObject("taxonomy")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("spatialResolution")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("timeResolution")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("tags")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("publisher")
                    .field("type", "text")
                    .endObject()
                    .startObject("publisherPublishTime")
                    .field("type", "date")
                    .endObject()
                    .startObject("toFilesNumber")
                    .field("type", "integer")
                    .endObject()
                    .startObject("downloadCount")
                    .field("type", "integer")
                    .endObject()
                    .startObject("startTime")
                    .field("type", "text")
                    .endObject()
                    .startObject("endTime")
                    .field("type", "text")
                    .endObject()
                    .startObject("iconPath")
                    .field("type", "text")
                    .endObject()
                    .startObject("visitCount")
                    .field("type", "integer")
                    .endObject()

                    .startObject("productId")
                    .field("type", "text")
                    .endObject()
                    .startObject("productName")
                    .field("type", "text")
                    .endObject()

                    .startObject("dataFormat")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("center")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("upLeft")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("lowLeft")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("upRight")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("lowRight")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("shape")
                    .field("type", "geo_shape")
                    .field("tree", "quadtree")
                    .endObject()

                    .endObject()
                    .endObject();

            System.out.println(builder.string());
            ESUtil.setMapping(indexname, "sdo", builder.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //20180424插入SDO数据
    @Test
    public void insertSdoData() throws IOException {
        TransportClient transportClient = ESUtil.getClient();
        List<Sdo> lists = sdoDao.getAll();
        for (Sdo sdo : lists) {
            XContentBuilder doc1 = jsonBuilder()
                    .startObject();
            String productName = productDao.findById(sdo.getProductId()).getProdName();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            if (!sdo.getCenter().trim().equals("无")) {
                String[] center = sdo.getCenter().split(",");
                String[] upLeft = sdo.getUpLeft().split(",");
                String[] lowLeft = sdo.getLowLeft().split(",");
                String[] upRight = sdo.getUpRight().split(",");
                String[] lowRight = sdo.getLowRight().split(",");
                double[] centerPoint = new double[]{Double.parseDouble(center[0]), Double.parseDouble(center[1])};
                double[] upLeftPoint = new double[]{Double.parseDouble(upLeft[0]), Double.parseDouble(upLeft[1])};
                double[] lowLeftPoint = new double[]{Double.parseDouble(lowLeft[0]), Double.parseDouble(lowLeft[1])};
                double[] upRightPoint = new double[]{Double.parseDouble(upRight[0]), Double.parseDouble(upRight[1])};
                double[] lowRightPoint = new double[]{Double.parseDouble(lowRight[0]), Double.parseDouble(lowRight[1])};
                doc1.field("center", centerPoint)
                        .field("upLeft", upLeftPoint)
                        .field("lowLeft", lowLeftPoint)
                        .field("upRight", upRightPoint)
                        .field("lowRight", lowRightPoint)
                        .startObject("shape").field("type", "polygon")
                        .startArray("coordinates").startArray()
                        .startArray().value(upLeftPoint[0]).value(upLeftPoint[1]).endArray()
                        .startArray().value(upRightPoint[0]).value(upRightPoint[1]).endArray()
                        .startArray().value(lowRightPoint[0]).value(lowRightPoint[1]).endArray()
                        .startArray().value(lowLeftPoint[0]).value(lowLeftPoint[1]).endArray()
                        .startArray().value(upLeftPoint[0]).value(upLeftPoint[1]).endArray()
                        .endArray().endArray()
                        .endObject();
            }
            doc1.field("sdoid", sdo.getId())
                    .field("title", sdo.getTitle())
                    .field("desc", sdo.getDesc())
                    .field("keyword", sdo.getKeyword())
                    .field("taxonomy", sdo.getTaxonomy())

                    .field("spatialResolution", sdo.getSpatialResolution())
                    .field("timeResolution", sdo.getTimeResolution())
                    .field("tags", sdo.getTags())
                    .field("publisher", sdo.getPublisher().getName())
                    .field("publisherPublishTime", sdo.getPublisherPublishTime())

                    .field("toFilesNumber", sdo.getToFilesNumber())
                    .field("downloadCount", sdo.getDownloadCount())
                    .field("startTime", simpleDateFormat.format(sdo.getStartTime()))
                    .field("endTime", simpleDateFormat.format(sdo.getEndTime()))
                    .field("iconPath", sdo.getIconPath())
                    .field("visitCount", sdo.getVisitCount())

                    .field("productId", sdo.getProductId())
                    .field("productName", productName)
                    .field("dataFormat", sdo.getDataFormat())
                    .endObject();
            System.out.println(doc1.toString());
            IndexResponse response = transportClient.prepareIndex("sdoindex", "sdo", sdo.getId())
                    .setSource(doc1)
                    .get();
            System.out.println(response.status());
            GetResponse getResponse = transportClient.prepareGet("sdoindex", "sdo", sdo.getId()).get();
            System.out.println(getResponse.getSource());
        }
        transportClient.close();
    }

    //创建fileinfo表的索引
    @Test
    public void createFileInfoIndex() {
        String indexname = "fileinfoindex";
        boolean flag = ESUtil.isExists(indexname);
        System.out.println("isExists:" + flag);
        if (flag)
            ESUtil.deleteIndex(indexname);
        //2.创建索引
        flag = ESUtil.createIndex(indexname);
        System.out.println("createIndex:" + flag);
        //3.设置Mapping
        try {
            XContentBuilder builder = jsonBuilder()
                    .startObject()
                    .startObject("properties")

                    .startObject("fileinfoid")
                    .field("type", "text")
                    .endObject()

                    .startObject("filename")
                    .field("type", "text")
                    .endObject()

                    .startObject("pid")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("number")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("filetype")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("sdoid")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("publisher")
                    .field("type", "text")
                    .endObject()

                    .startObject("updatetime")
                    .field("type", "date")
                    .endObject()

                    .startObject("cloudiness")
                    .field("type", "double")
                    .endObject()

                    .startObject("size")
                    .field("type", "text")
                    .endObject()

                    .startObject("recordNum")
                    .field("type", "integer")
                    .endObject()

                    .startObject("center")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("upLeft")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("lowLeft")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("upRight")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("lowRight")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("shape")
                    .field("type", "geo_shape")
                    .field("tree", "quadtree")
                    .endObject()

                    .endObject()
                    .endObject();

            System.out.println(builder.string());
            ESUtil.setMapping(indexname, "fileinfo", builder.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //插入fileinfo数据
    @Test
    public void insertFileinfo() throws IOException {
        TransportClient transportClient = ESUtil.getClient();
        List<FileInfo> list = fileInfoDao.getEffectiveData();
        for (FileInfo fileInfo : list) {
            try {
                XContentBuilder doc = jsonBuilder().startObject();
                FileType fileType = fileTypeDao.getById(fileInfo.getFtId());
                cn.csdb.model.Resource sdo = sdoDao.getSdoById(fileType.getSdoId());
                if (!fileInfo.getCenter().trim().equals("无")) {
                    String[] center = fileInfo.getCenter().split(",");
                    String[] upLeft = fileInfo.getUpLeft().split(",");
                    String[] lowLeft = fileInfo.getLowLeft().split(",");
                    String[] upRight = fileInfo.getUpRight().split(",");
                    String[] lowRight = fileInfo.getLowRight().split(",");
                    double[] centerPoint = new double[]{Double.parseDouble(center[0]), Double.parseDouble(center[1])};
                    double[] upLeftPoint = new double[]{Double.parseDouble(upLeft[0]), Double.parseDouble(upLeft[1])};
                    double[] lowLeftPoint = new double[]{Double.parseDouble(lowLeft[0]), Double.parseDouble(lowLeft[1])};
                    double[] upRightPoint = new double[]{Double.parseDouble(upRight[0]), Double.parseDouble(upRight[1])};
                    double[] lowRightPoint = new double[]{Double.parseDouble(lowRight[0]), Double.parseDouble(lowRight[1])};
                    if (centerPoint[0] > 180 || centerPoint[0] < -180 || centerPoint[1] > 90 || centerPoint[1] < -90
                            || upLeftPoint[0] > 180 || upLeftPoint[0] < -180 || upLeftPoint[1] > 90 || upLeftPoint[1] < -90
                            || lowLeftPoint[0] > 180 || lowLeftPoint[0] < -180 || lowLeftPoint[1] > 90 || lowLeftPoint[1] < -90
                            || upRightPoint[0] > 180 || upRightPoint[0] < -180 || upRightPoint[1] > 90 || upRightPoint[1] < -90
                            || lowRightPoint[0] > 180 || lowRightPoint[0] < -180 || lowRightPoint[1] > 90 || lowRightPoint[1] < -90) {
                        continue;
                    }
                    doc.field("center", centerPoint)
                            .field("upLeft", upLeftPoint)
                            .field("lowLeft", lowLeftPoint)
                            .field("upRight", upRightPoint)
                            .field("lowRight", lowRightPoint)
                            .startObject("shape").field("type", "polygon")
                            .startArray("coordinates").startArray()
                            .startArray().value(upLeftPoint[0]).value(upLeftPoint[1]).endArray()
                            .startArray().value(upRightPoint[0]).value(upRightPoint[1]).endArray()
                            .startArray().value(lowRightPoint[0]).value(lowRightPoint[1]).endArray()
                            .startArray().value(lowLeftPoint[0]).value(lowLeftPoint[1]).endArray()
                            .startArray().value(upLeftPoint[0]).value(upLeftPoint[1]).endArray()
                            .endArray().endArray()
                            .endObject();
                }
                doc.field("fileinfoid", fileInfo.getId())
                        .field("filename", fileInfo.getFileName())
                        .field("pid", fileInfo.getPid())
                        .field("number", fileInfo.getNumber())
                        .field("filetype", fileInfo.getFtName())
                        .field("publisher", sdo.getOrganizationName())
                        .field("updatetime", fileInfo.getUpdateTime())
                        .field("cloudiness", fileInfo.getCloudiness())
                        .field("size", fileInfo.getSize())
                        .field("recordNum", fileInfo.getRecordNum())
                        .field("sdoid", fileInfo.getSdoPid())
                        .endObject();
                System.out.println(doc.toString());
                IndexResponse response = transportClient.prepareIndex("fileinfoindex", "fileinfo", fileInfo.getId())
                        .setSource(doc)
                        .get();
                System.out.println(response.status());
                GetResponse getResponse = transportClient.prepareGet("fileinfoindex", "fileinfo", fileInfo.getId()).get();
                System.out.println(getResponse.getSource());
            } catch (Exception e) {
                continue;
            }
        }
        transportClient.close();
    }


    //读取数据
    @Test
    public void readData() throws IOException {

        sdoDao.findDistinctPublisher();

        TransportClient client = ESUtil.getClient();
        GetResponse response = client.prepareGet("sdoindex", "sdo", "5aceb97e9ef710073861fba7")
                .execute()
                .actionGet();
        System.out.println("id:" + response.getId());
        System.out.println("index:" + response.getIndex());
        //System.out.println("title:" + response.getFields().get("title"));
        System.out.println("desc:" + response.getSource().get("desc"));
        System.out.println("tags:" + response.getSource().get("title"));
        client.close();
    }


    @Test
    public void insertData() throws IOException {
        TransportClient transportClient = ESUtil.getClient();
        XContentBuilder doc1 = jsonBuilder()
                .startObject()
                .field("id", "2")
                .field("title", "Java设计模式之单例模式")
                .field("content", "枚举单例模式可以防反射攻击。")
                .field("postdate", "2018-02-03 19:27:00")
                .field("url", "csdn.net/79247746")
                .endObject();
        System.out.println(doc1.toString());
        IndexResponse response = transportClient.prepareIndex("index1", "blog", "2")
                .setSource(doc1)
                .get();
        System.out.println(response.status());
        GetResponse getResponse = transportClient.prepareGet("index1", "blog", "2").get();
        System.out.println(getResponse.getSource());
    }

    @Test
    public void getData() {
        List<Sdo> list = new ArrayList<>();
        list = sdoDao.getAll();
        System.out.println(list.size());
    }

    @Test
    public void testSdoVisitDao() {
        List<SdoVisit> list = sdoVisitDao.getBy("植被", "用", new Date("2018/10/10"), null);
        for (SdoVisit sdoVisit : list) {
            System.out.println(sdoVisit.getTitle() + "," + sdoVisit.getLoginId() + "," + sdoVisit.getCreateTime());
        }
    }


    //===============================================================
    //创建文件索引ES 20180710
    @Test
    public void createFileIndex1() {
        String indexname = "fileindex3";
        boolean flag = ESUtil.isExists(indexname);
        System.out.println("isExists:" + flag);
        if (flag)
            ESUtil.deleteIndex(indexname);
        //2.创建索引
        flag = ESUtil.createIndex(indexname);
        System.out.println("createIndex:" + flag);
        //3.设置Mapping
        try {
            XContentBuilder builder = jsonBuilder()
                    .startObject()
                    .startObject("properties")

                    .startObject("fileinfoid")
                    .field("type", "text")
                    .endObject()

                    .startObject("filename")
                    .field("type", "text")
                    .endObject()

                    .startObject("pid")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("number")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("filetype")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("sdoid")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("publisher")
                    .field("type", "text")
                    .endObject()

                    .startObject("updatetime")
                    .field("type", "date")
                    .endObject()

                    .startObject("cloudiness")
                    .field("type", "double")
                    .endObject()

                    .startObject("size")
                    .field("type", "text")
                    .endObject()

                    .startObject("recordNum")
                    .field("type", "integer")
                    .endObject()

                    .startObject("center")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("upLeft")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("lowLeft")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("upRight")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("lowRight")
                    .field("type", "geo_point")
                    .endObject()

                    .startObject("shape")
                    .field("type", "geo_shape")
                    .field("tree", "quadtree")
                    .endObject()

                    .endObject()
                    .endObject();

            System.out.println(builder.string());
            ESUtil.setMapping(indexname, "fileinfo2", builder.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        DeleteIndexResponse deleteResponse = ESUtil.getClient().admin().indices()
                .prepareDelete("fileindex1")
                .execute()
                .actionGet();
        System.out.println(deleteResponse.isAcknowledged());
    }

    @Test
    public void deleteBySdopid() {
        BulkByScrollResponse response =
                DeleteByQueryAction.INSTANCE.newRequestBuilder(ESUtil.getClient())
                        .filter(QueryBuilders.matchQuery("sdoid", "5b2c6df29ef7101a58a082de")) //查询条件
                        .source("fileindex1") //index(索引名)
                        .get();  //执行

        long deleted = response.getDeleted(); //删除文档的数量
        System.out.println(deleted);
    }

    @Test
    public void count() {
        TransportClient transportClient = ESUtil.getClient();

        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch("fileindex1");

        searchRequestBuilder.setTypes("fileinfo2");


        SearchResponse response = searchRequestBuilder.execute().actionGet();
        SearchHits searchHits = response.getHits();
        System.out.println("总数：" + searchHits.getTotalHits());

    }

    @Test
    public void getfileinfo() {
        List<FileInfo> list = null;
        list = fileInfoDao.getList(38000, 1000, "HDF");

//        System.out.println(fileInfoDao.getTotalCount());
        System.out.println(list.size());
    }


    //批量插入数据到ES中
    @Test
    public void testBulkInsert() {
        TransportClient transportClient = ESUtil.getClient();
        int start = 0;
        int pageSize = 1000;
        int offset = 0;
//        long total = 26048L;
        long total = 10000;
        //String strfileType="xlsx";
        //String strfileType = "HDF";
        String strfileType = "IMG";
        long startTimeall = System.currentTimeMillis();
        List<FileInfo> list = null;


//        for (int j = offset; j < 1; j++) {
        while (offset + 1000 <= total) {
            list = null;
            list = fileInfoDao.getList(offset, pageSize, strfileType);

            /*if (list.size() < 1000) {
                System.out.println("在" + offset + "处list数量小于1000");
                break;
            }*/

            BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
            int count = 0;
//            for (FileInfo fileInfo : list) {
            for (int i = 0; i < list.size(); i++) {
                FileInfo fileInfo = list.get(i);
                count++;
                try {
                    XContentBuilder doc = jsonBuilder().startObject();
//                FileType fileType = fileTypeDao.getById(fileInfo.getFtId());
                    cn.csdb.model.Resource sdo = sdoDao.getSdoById(fileInfo.getSdoPid());
                    if (!fileInfo.getCenter().trim().equals("无")) {
                        String[] center = fileInfo.getCenter().split(",");
                        String[] upLeft = fileInfo.getUpLeft().split(",");
                        String[] lowLeft = fileInfo.getLowLeft().split(",");
                        String[] upRight = fileInfo.getUpRight().split(",");
                        String[] lowRight = fileInfo.getLowRight().split(",");
                        double[] centerPoint = new double[]{Double.parseDouble(center[0]), Double.parseDouble(center[1])};
                        double[] upLeftPoint = new double[]{Double.parseDouble(upLeft[0]), Double.parseDouble(upLeft[1])};
                        double[] lowLeftPoint = new double[]{Double.parseDouble(lowLeft[0]), Double.parseDouble(lowLeft[1])};
                        double[] upRightPoint = new double[]{Double.parseDouble(upRight[0]), Double.parseDouble(upRight[1])};
                        double[] lowRightPoint = new double[]{Double.parseDouble(lowRight[0]), Double.parseDouble(lowRight[1])};
                        if (centerPoint[0] > 180 || centerPoint[0] < -180 || centerPoint[1] > 90 || centerPoint[1] < -90
                                || upLeftPoint[0] > 180 || upLeftPoint[0] < -180 || upLeftPoint[1] > 90 || upLeftPoint[1] < -90
                                || lowLeftPoint[0] > 180 || lowLeftPoint[0] < -180 || lowLeftPoint[1] > 90 || lowLeftPoint[1] < -90
                                || upRightPoint[0] > 180 || upRightPoint[0] < -180 || upRightPoint[1] > 90 || upRightPoint[1] < -90
                                || lowRightPoint[0] > 180 || lowRightPoint[0] < -180 || lowRightPoint[1] > 90 || lowRightPoint[1] < -90) {

                            if (i == 0)
                                System.out.println("开始处理 " + (offset) + "  条之后的记录!");
                            if ((i == list.size()-1)) {
                                bulkRequest.execute().actionGet();
                                long endTime = System.currentTimeMillis(); // 获取结束时间
                                System.out.println((offset + i) + " 条记录插入时间： " + (endTime - startTimeall) / 1000 + "s");
                            }

                            continue;
                        }
                        doc.field("center", centerPoint)
                                .field("upLeft", upLeftPoint)
                                .field("lowLeft", lowLeftPoint)
                                .field("upRight", upRightPoint)
                                .field("lowRight", lowRightPoint)
                                .startObject("shape").field("type", "polygon")
                                .startArray("coordinates").startArray()
                                .startArray().value(upLeftPoint[0]).value(upLeftPoint[1]).endArray()
                                .startArray().value(upRightPoint[0]).value(upRightPoint[1]).endArray()
                                .startArray().value(lowRightPoint[0]).value(lowRightPoint[1]).endArray()
                                .startArray().value(lowLeftPoint[0]).value(lowLeftPoint[1]).endArray()
                                .startArray().value(upLeftPoint[0]).value(upLeftPoint[1]).endArray()
                                .endArray().endArray()
                                .endObject();
                    }
                    doc.field("fileinfoid", fileInfo.getId())
                            .field("filename", fileInfo.getFileName())
                            .field("pid", fileInfo.getPid())
                            .field("number", fileInfo.getNumber())
                            .field("filetype", fileInfo.getFtName())
                            .field("publisher", sdo.getOrganizationName())
                            .field("updatetime", fileInfo.getUpdateTime())
                            .field("cloudiness", fileInfo.getCloudiness())
                            .field("size", fileInfo.getSize())
                            .field("recordNum", fileInfo.getRecordNum())
                            .field("sdoid", fileInfo.getSdoPid())
                            .endObject();
                    //System.out.println(doc.toString());


                    bulkRequest.add(transportClient.prepareIndex("fileindex3", "fileinfo2").setSource(doc));
                    //System.out.println("******" + count);
                    //bulkRequest.add(transportClient.prepareIndex("fileinfoindex","fileinfo").setSource(doc));
                    if (i == 0)
                        System.out.println("开始处理 " + (offset) + "  条之后的记录!");
                    if ((i == list.size()-1)) {
                        bulkRequest.execute().actionGet();
                        long endTime = System.currentTimeMillis(); // 获取结束时间
                        System.out.println((offset + i) + " 条记录插入时间： " + (endTime - startTimeall) / 1000 + "s");
                    }

                /*IndexResponse response = transportClient.prepareIndex("fileindex2", "fileInfo2", fileInfo.getId())
                        .setSource(doc)
                        .get();
                System.out.println(response.status());*/
                    //GetResponse getResponse = transportClient.prepareGet("fileindex2", "fileinfo2", fileInfo.getId()).get();

                    //System.out.println(getResponse.getSource());
                } catch (Exception e) {
                    System.out.println("第" + (offset + count - 1) + "数据有误 " + e.getMessage());
                    continue;
                }
            }
//            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
//            if (bulkResponse.hasFailures()) {
//                System.out.println("在"+offset+"批量插入有误！！！");
//            }
            offset = offset + 1000;
        }


        long allendTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("总时间插入时间： " + (allendTime - startTimeall) / 1000 + "s");
        transportClient.close();
    }


    /*@Test
    public void testGeoShapeQuery() throws  IOException{
        GeoShapeQueryBuilder filter = QueryBuilders.geoShapeQuery(
                "shape",
                ShapeBuilders.newPolygon(new CoordinatesBuilder()
                        .coordinate(136,25)
                        .coordinate(105,25)
                        .coordinate(105,44)
                        .coordinate(136,44)
                        .coordinate(136,25)))
                .relation(ShapeRelation.INTERSECTS);
        System.out.println(filter);

        TransportClient transportClient = ESUtil.getClient();
        SearchResponse response = transportClient.prepareSearch("fileindex1")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(filter).get();
        long totalNum = response.getHits().getTotalHits();
        System.out.println(totalNum);
    }*/

    @Test
    public void testBulkProcessorInsert() {
        TransportClient transportClient = ESUtil.getClient();
        int start = 0;
        //int start = 0;

        int pageSize = 100;
        //String strfileType="xlsx"; //HDF
        String strfileType = "xlsx";
        List<FileInfo> list = fileInfoDao.getList(start, pageSize, strfileType);

        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        int count = 1;
        long startTimeall = System.currentTimeMillis();


        BulkProcessor bulkProcessor = BulkProcessor.builder(
                transportClient,
                new BulkProcessor.Listener() {
                    long begin;
                    long cost;
                    int count = 0;

                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                        begin = System.currentTimeMillis();
                        System.out.println("\n\n准备开始导入数据......");
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                        cost = (System.currentTimeMillis() - begin) / 1000;
                        count += request.numberOfActions();
                        logger.info("bulk success. size:[{}] cost:[{}s]", count, cost);
                        System.out.println("bulk success. size:" + count + " ,  共用时: " + cost);
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        logger.error("bulk update has failures, will retry:" + failure.getMessage());
                        System.out.println("error" + failure.getMessage());
                    }
                }

        ).setBulkActions(1) //<5>
                .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB)) //<6>
                .setFlushInterval(TimeValue.timeValueSeconds(5)) //<7>
                .setConcurrentRequests(1) //<8>
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)) //<9>
                .build();


        for (FileInfo fileInfo : list) {
            try {
                XContentBuilder doc = jsonBuilder().startObject();
                //FileType fileType = fileTypeDao.getById(fileInfo.getFtId());
                cn.csdb.model.Resource sdo = sdoDao.getSdoById(fileInfo.getSdoPid());
                if (!fileInfo.getCenter().trim().equals("无")) {
                    String[] center = fileInfo.getCenter().split(",");
                    String[] upLeft = fileInfo.getUpLeft().split(",");
                    String[] lowLeft = fileInfo.getLowLeft().split(",");
                    String[] upRight = fileInfo.getUpRight().split(",");
                    String[] lowRight = fileInfo.getLowRight().split(",");
                    double[] centerPoint = new double[]{Double.parseDouble(center[0]), Double.parseDouble(center[1])};
                    double[] upLeftPoint = new double[]{Double.parseDouble(upLeft[0]), Double.parseDouble(upLeft[1])};
                    double[] lowLeftPoint = new double[]{Double.parseDouble(lowLeft[0]), Double.parseDouble(lowLeft[1])};
                    double[] upRightPoint = new double[]{Double.parseDouble(upRight[0]), Double.parseDouble(upRight[1])};
                    double[] lowRightPoint = new double[]{Double.parseDouble(lowRight[0]), Double.parseDouble(lowRight[1])};
                    if (centerPoint[0] > 180 || centerPoint[0] < -180 || centerPoint[1] > 90 || centerPoint[1] < -90
                            || upLeftPoint[0] > 180 || upLeftPoint[0] < -180 || upLeftPoint[1] > 90 || upLeftPoint[1] < -90
                            || lowLeftPoint[0] > 180 || lowLeftPoint[0] < -180 || lowLeftPoint[1] > 90 || lowLeftPoint[1] < -90
                            || upRightPoint[0] > 180 || upRightPoint[0] < -180 || upRightPoint[1] > 90 || upRightPoint[1] < -90
                            || lowRightPoint[0] > 180 || lowRightPoint[0] < -180 || lowRightPoint[1] > 90 || lowRightPoint[1] < -90) {
                        continue;
                    }
                    doc.field("center", centerPoint)
                            .field("upLeft", upLeftPoint)
                            .field("lowLeft", lowLeftPoint)
                            .field("upRight", upRightPoint)
                            .field("lowRight", lowRightPoint)
                            .startObject("shape").field("type", "polygon")
                            .startArray("coordinates").startArray()
                            .startArray().value(upLeftPoint[0]).value(upLeftPoint[1]).endArray()
                            .startArray().value(upRightPoint[0]).value(upRightPoint[1]).endArray()
                            .startArray().value(lowRightPoint[0]).value(lowRightPoint[1]).endArray()
                            .startArray().value(lowLeftPoint[0]).value(lowLeftPoint[1]).endArray()
                            .startArray().value(upLeftPoint[0]).value(upLeftPoint[1]).endArray()
                            .endArray().endArray()
                            .endObject();
                }
                doc.field("fileinfoid", fileInfo.getId())
                        .field("filename", fileInfo.getFileName())
                        .field("pid", fileInfo.getPid())
                        .field("number", fileInfo.getNumber())
                        .field("filetype", fileInfo.getFtName())
                        .field("publisher", sdo.getOrganizationName())
                        .field("updatetime", fileInfo.getUpdateTime())
                        .field("cloudiness", fileInfo.getCloudiness())
                        .field("size", fileInfo.getSize())
                        .field("recordNum", fileInfo.getRecordNum())
                        .field("sdoid", fileInfo.getSdoPid())
                        .endObject();

                //bulkRequest.add(transportClient.prepareIndex("fileindex2","fileinfo2").setSource(doc));
                //bulkRequest.add(transportClient.prepareIndex("fileinfoindex","fileinfo").setSource(doc));
                //bulkProcessor.add(new IndexRequest("fileindex2","fileinfo2").source(doc));
                bulkProcessor.add(new IndexRequest("fileindex3", "fileinfo2").source(doc));


                if (count % 1000 == 0)
                    System.out.println("正在处理 " + count + "  条记录!");

                count++;

            } catch (Exception e) {
                System.out.println("第" + count + "数据有误 " + e.getMessage());
                continue;
            }
        }
        // bulkRequest.execute().actionGet();
        long allendTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("总时间插入时间： " + (allendTime - startTimeall) / 1000 + "s");
        transportClient.close();
    }
}
