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
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/4/17 0017.
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

}
