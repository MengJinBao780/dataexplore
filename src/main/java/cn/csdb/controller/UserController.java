package cn.csdb.controller;

import cn.csdb.model.SdoDownload;
import cn.csdb.model.SdoFavorites;
import cn.csdb.repository.SdoDownloadDao;
import cn.csdb.service.SdoDownloadService;
import cn.csdb.service.SdoFavoritesService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by xiajl on 2018/05/03.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private SdoFavoritesService favoriteService;
    @Resource
    private SdoDownloadService sdoDownloadService;


    @RequestMapping("")
    public String userHome(String userID){
        return "user";
    }

    @ResponseBody
    @RequestMapping("myFavorite")
    public JSONObject myFavorite(HttpSession session,int pageNo){
        JSONObject jsonObject = new JSONObject();
        String loginId = String.valueOf(session.getAttribute("loginId"));
        if (StringUtils.isEmpty(loginId) ||loginId.equals("null"))
        {
            loginId = "admin";
        }
        long totalNum = 0;
        totalNum = favoriteService.getCount(loginId);
        List<SdoFavorites> favoriteList = favoriteService.getList(loginId,(pageNo-1)*10,10);
        jsonObject.put("favoriteList",favoriteList);
        jsonObject.put("currentPage",pageNo);
        jsonObject.put("totalNum",totalNum);
        if(totalNum%10==0){
            jsonObject.put("totalPage",totalNum/10);
        }else{
            jsonObject.put("totalPage",totalNum/10+1);
        }
        jsonObject.put("result","success");
        return jsonObject;
    }


    @ResponseBody
    @RequestMapping("myDownload")
    public JSONObject myDownload(HttpSession session,int pageNo){
        JSONObject jsonObject = new JSONObject();
        String loginId = String.valueOf(session.getAttribute("loginId"));
        if (StringUtils.isEmpty(loginId) ||loginId.equals("null"))
        {
            loginId = "admin";
        }
        long totalNum = sdoDownloadService.getCount(loginId);
        List<SdoDownload> downloadList = sdoDownloadService.getList(loginId,(pageNo-1)*10,10);
        jsonObject.put("downloadList",downloadList);
        jsonObject.put("currentPage",pageNo);
        jsonObject.put("totalNum",totalNum);
        if(totalNum%10==0){
            jsonObject.put("totalPage",totalNum/10);
        }else{
            jsonObject.put("totalPage",totalNum/10+1);
        }
        jsonObject.put("result","success");
        return jsonObject;
    }
}
