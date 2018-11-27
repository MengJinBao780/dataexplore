package cn.csdb.controller;

import cn.csdb.model.ContentEditType;
import cn.csdb.model.Product;
import cn.csdb.model.Sdo;
import cn.csdb.model.TagDetail;
import cn.csdb.service.ProductService;
import cn.csdb.service.SdoService;
import cn.csdb.service.TagDetailService;
import cn.csdb.service.TagService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by huangwei on 2018/4/20.
 */
@Controller
public class TagController {


    private Logger logger = LoggerFactory.getLogger(TagController.class);

    @Resource
    private TagService tagService;
    @Resource
    private TagDetailService tagDetailService;
    @Resource
    private SdoService sdoService;
    @Resource
    private ProductService productService;


    @RequestMapping(value = "tag", method = RequestMethod.GET)
    public String category(Model model) {
        List<Product> list = productService.getAllProductList();
        model.addAttribute("list",list);
        return "admin/tag";
    }


    //取得标签列表
    @RequestMapping(value = "admin/getTagList", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getTagList(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                 int type,String sdoTitle,String prodId,int status) {
        long count = tagService.countByPage(type);
//        long count = tagService.getTagList(pageNo, pageSize,type,sdoTitle,prodId,status).size();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", tagService.getTagList(pageNo, pageSize,type,sdoTitle,prodId,status));
        jsonObject.put("totalCount", count);
        jsonObject.put("currentPage", pageNo);
        jsonObject.put("pageSize", pageSize);
        jsonObject.put("totalPages", count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
        return jsonObject;
    }


    @RequestMapping(value = "tag/approveTag", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject approveTag(String id, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        String auditor = String.valueOf(session.getAttribute("loginId"));
        if (auditor == "null")
            auditor = "admin";
        jsonObject.put("auditor", auditor);
        boolean i = tagService.approveTag(id, auditor);
        if (i == true) {
            jsonObject.put("result", "success");
        } else {
            jsonObject.put("result", "fail");
        }
        TagDetail tagDetail = tagDetailService.getTagDetail(id);
        jsonObject.put("tagDetail", tagDetail);
        return jsonObject;
    }


    @RequestMapping(value = "tag/rejectTag", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject rejectTag(String id, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        String auditor = String.valueOf(session.getAttribute("loginId"));
        if (auditor == "null")
            auditor = "admin";
        boolean i = tagService.rejectTag(id, auditor);
        if (i == true) {
            jsonObject.put("result", "success");
        } else {
            jsonObject.put("result", "fail");
        }
        TagDetail tagDetail = tagDetailService.getTagDetail(id);
        jsonObject.put("tagDetail", tagDetail);
        return jsonObject;
    }

//    @RequestMapping(value = "tag/getProName", method = RequestMethod.GET)
//    @ResponseBody
//    public JSONObject getProName(String id) {
//        String proName = tagService.getProName(id);
//        JSONObject jo = new JSONObject();
//        jo.put("proName",proName);
//        return jo;
//    }
    @RequestMapping(value = "tag/getSdoTitle", method = RequestMethod.GET)
    public String getSdoTitle(String id) {
        return tagService.getSdoTitle(id);
    }

    @ResponseBody
    @RequestMapping(value = "addTagOfSdo", method = RequestMethod.GET)
    public JSONObject addTagOfSdo(@RequestParam("tagName") String tagName, @RequestParam("sdoId") String sdoId) {
        JSONObject jo = new JSONObject();

        boolean hastag = tagService.hasTag(tagName,sdoId);
        if (hastag == true) {
            jo.put("result", "0");
            return jo;
        }
        Sdo sdo = sdoService.getSdoById(sdoId);
        TagDetail tagDetail = new TagDetail();
        tagDetail.setSdoId(sdoId);
        tagDetail.setSdoTitle(sdo.getTitle());
        tagDetail.setProdId(sdo.getProductId());
        tagDetail.setTagName(tagName);
        tagDetail.setStatus(0);
        tagDetail.setLoginId("loginUsername");
        tagDetail = tagService.addTagDetail(tagDetail);
        logger.debug("用户添加标签" + tagName);
        jo.put("result", "1");
        jo.put("tagId", tagDetail.getId());
        jo.put("tagName", tagDetail.getTagName());
        return jo;
    }


    @ResponseBody
    @RequestMapping(value = "deleteTagOfSdo", method = RequestMethod.GET)
    public JSONObject deleteTagOfSdo(@RequestParam("tagId") String tagId) {
        JSONObject jo = new JSONObject();
        tagService.delTag(tagId);
        jo.put("result", "1");
        return jo;
    }

}
