package cn.csdb.service;


import cn.csdb.model.DataSrc;
import cn.csdb.model.Subject;
import cn.csdb.model.TableFieldComs;
import cn.csdb.model.TableInfo;
import cn.csdb.repository.CheckUserDao;
import cn.csdb.repository.TableFieldComsDao;
import cn.csdb.utils.dataSrc.DataSourceFactory;
import cn.csdb.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;


/**
 * Created by shibaoping on 2018/12/11.
 */
@Service
public class TableFieldComsService {
    private Logger logger = LoggerFactory.getLogger(TableFieldComsService.class);

    /*@Resource
    private DataSrcDao dataSrcDao;*/

    @Resource
    private TableFieldComsDao tableFieldComsDao;

    @Resource
    private CheckUserDao checkUserDao;

    private final static String URISPLIT = "#";

    @Transactional(readOnly = true)
    public Map<String, List<TableInfo>> getDefaultFieldComsByTableName(String subjectCode, String tableName) {


        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        tableName = tableName.trim();

/*
        DataSrc dataSrc = dataSrcDao.findById(dataSourceId);
*/
        Subject subject = checkUserDao.getSubjectByCode(subjectCode);
        DataSrc datasrc = new DataSrc();
        datasrc.setDatabaseName(subject.getDbName());
        datasrc.setDatabaseType("mysql");
        datasrc.setHost(subject.getDbHost());
        datasrc.setPort(subject.getDbPort());
        datasrc.setUserName(subject.getDbUserName());
        datasrc.setPassword(subject.getDbPassword());
        if (datasrc == null) {
            return null;
        }
        Map<String, List<TableInfo>> maps = Maps.newHashMap();
        TableFieldComs tableFieldComs = getTableFieldComsByUriEx(datasrc, tableName);
        List<TableInfo> tableInfosCur = null;
        if (tableFieldComs != null) {
            String fieldComs = tableFieldComs.getFieldComs();
            tableInfosCur = JSON.parseArray(fieldComs, TableInfo.class);
        }

        IDataSource dataSource = DataSourceFactory.getDataSource(datasrc.getDatabaseType());
        Connection connection = dataSource.getConnection(datasrc.getHost(), datasrc.getPort(), datasrc.getUserName(), datasrc.getPassword(), datasrc.getDatabaseName());
        List<TableInfo> tableInfos = null;
        try {
            tableInfos = dataSource.getTableFieldComs(connection, datasrc.getDatabaseName(), tableName);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
        if (tableInfos != null && tableInfosCur != null) {
            List intersectionList = ListUtils.intersection(tableInfos, tableInfosCur);//1 获取交集，取tableInfosCur中元素
            tableInfos.removeAll(tableInfosCur);//2 去除重复元素
            tableInfos.addAll(intersectionList);//3 然后取并集
            maps.put(tableName, tableInfos);
        } else if (tableInfos != null) {
            maps.put(tableName, tableInfos);
        }
        return maps;

    }

    private TableFieldComs getTableFieldComsByUriEx(DataSrc dataSrc, String tableName) {
        TableFieldComs tableFieldComs = null;
        int uriHash = getUriHash(dataSrc, tableName);
        List<TableFieldComs> tableFieldComsList = tableFieldComsDao.getTableFieldComsByUriEx(uriHash);
        if (tableFieldComsList.size() == 1) {
            tableFieldComs = tableFieldComsList.get(0);
        }
        if (tableFieldComsList.size() > 1) {
            for (TableFieldComs tableFieldComsCur : tableFieldComsList) {
                if (StringUtils.equals(tableFieldComsCur.getUriEx(), getUriEx(dataSrc, tableName))) {
                    tableFieldComs = tableFieldComsCur;
                    break;
                }
            }
        }
        return tableFieldComs;
    }

    private int getUriHash(DataSrc dataSrc, String tableName) {
        String uriEx = getUriEx(dataSrc, tableName);
        return Hashing.murmur3_32().hashBytes(uriEx.getBytes()).asInt();
    }

    private String getUriEx(DataSrc dataSrc, String tableName) {
        String host = dataSrc.getHost();
        String port = dataSrc.getPort();
        String databaseName = dataSrc.getDatabaseName();
        return Joiner.on(URISPLIT).skipNulls().join(host, port, databaseName, tableName);
    }

    public List<Map<String,Object>> getDataBySql(List searchCondition, String sql, String SubjectCode, int start, int limit) {
        Subject subject = checkUserDao.getSubjectByCode(SubjectCode);
        DataSrc datasrc = new DataSrc();
        datasrc.setDatabaseName(subject.getDbName());
        datasrc.setDatabaseType("mysql");
        datasrc.setHost(subject.getDbHost());
        datasrc.setPort(subject.getDbPort());
        datasrc.setUserName(subject.getDbUserName());
        datasrc.setPassword(subject.getDbPassword());
        IDataSource dataSource = DataSourceFactory.getDataSource(datasrc.getDatabaseType());
        Connection connection = dataSource.getConnection(datasrc.getHost(), datasrc.getPort(),
                datasrc.getUserName(), datasrc.getPassword(), datasrc.getDatabaseName());
        List<Object>condition = new ArrayList<>();
        PreparedStatement paginationSql;
        if(searchCondition!=null){
            for (int i = 0; i < searchCondition.size(); i++) {
                List<String> list1 = (List) searchCondition.get(i);
                condition.add(list1.get(3));
            }
            paginationSql = dataSource.getPaginationSql(connection, sql, condition, start, limit);
        }else{
            paginationSql = dataSource.getPaginationSql(connection, sql, null, start, limit);
        }
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            ResultSet resultSet = paginationSql.executeQuery();
            //遍历resultset
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int count = rsmd.getColumnCount();
            while (resultSet.next()) {
                Map rowData = new HashMap();//声明Map
                for (int i = count; i >= 1; i--) {
                    System.out.println(rsmd.getColumnName(i)+"="+resultSet.getObject(i));
                    rowData.put(rsmd.getColumnName(i), resultSet.getObject(i));//获取键名及值
                }
                list.add(rowData);
            }
            System.out.println("list = " + list.size());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
        return list;
    }

    public List<Map<String,Object>> getDataByTable(List list,String[]column,String tableName, String SubjectCode, int start, int limit) {
        String sql = " ";
        if(column==null){
            sql = "select * from ";
        }else{
            sql = "select ";
            int n = 0;
            for(String s:column){
                n++;
            }
            for(String s1:column){
                if(n>1){
                    sql = sql + s1 + ",";
                    n--;
                }else{
                    sql = sql + s1;
                }
            }
            sql = sql + " from ";
        }
        sql = sql + tableName + " where 1=1 ";
        if(list!=null){
            for (int i = 0; i < list.size(); i++) {
                List<String> list1 = (List) list.get(i);
                if("&".equals(list1.get(0))){
                    sql = sql + "and "+list1.get(1)+" "+list1.get(2)+" ? ";
                }
                if("||".equals(list1.get(0))){
                    sql = sql + "or "+list1.get(1)+" "+list1.get(2)+" ? ";
                }
            }
            return getDataBySql(list, sql , SubjectCode, start, limit);
        }else{
            return getDataBySql(null, sql , SubjectCode, start, limit);
        }
    }
}
