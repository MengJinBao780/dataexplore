package cn.csdb.controller;

import cn.csdb.model.Subject;
import cn.csdb.service.SubjectService;
import cn.csdb.utils.dataSrc.DataSourceFactory;
import cn.csdb.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.Connection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: DataSync
 * @description: Resource Controller class
 * @author: xiajl
 * @create: 2018-10-23 16:32
 **/
@Controller
@RequestMapping("/resource")
public class ResourceController {

    @Resource
    private SubjectService subjectService;


    private Logger logger = LoggerFactory.getLogger(ResourceController.class);

    /**
     * Function Description: 获得mysql数据库表单
     *
     * @param: []
     * @return: com.alibaba.fastjson.JSONObject
     * @auther: hw
     * @date: 2018/11/1 10:36
     */
    @ResponseBody
    @RequestMapping(value = "relationalDatabaseTableList")
    public JSONObject relationalDatabaseTableList(HttpSession session) {
/*
        String subjectCode = session.getAttribute("SubjectCode").toString();
*/
        String subjectCode = "student";
        Subject subject = subjectService.findBySubjectCode(subjectCode);
        JSONObject jsonObject = new JSONObject();
        IDataSource dataSource = DataSourceFactory.getDataSource("mysql");
        Connection connection = dataSource.getConnection(subject.getDbHost(), subject.getDbPort(),
                subject.getDbUserName(), subject.getDbPassword(), subject.getDbName());
        if (connection == null)
            return null;
        List<String> list = dataSource.getTableList(connection);
        jsonObject.put("list", list);
//        jsonObject.put("dataSourceName", dataSrc.getDataSourceName());
        return jsonObject;
    }



}
