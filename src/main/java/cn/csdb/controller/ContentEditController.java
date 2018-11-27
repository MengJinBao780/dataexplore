package cn.csdb.controller;

import cn.csdb.model.*;
import cn.csdb.service.ContentEditService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sophie on 2017/11/7.
 */
@Controller
@RequestMapping("/collection")
public class ContentEditController {
    @Resource
    private ContentEditService contentEditService;

    @RequestMapping("/")
    public String showCollection(Model model) {
        List<ContentEditType> list = contentEditService.getAllCategoryList();
        model.addAttribute("list",list);
        return "collection";
    }

    @RequestMapping("/list")
    @ResponseBody
    public JSONObject getList(@RequestParam(name = "colType", defaultValue = "新闻") String colType,
                              @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        List<ContentEdit> list = contentEditService.getListByPage(colType,pageNo, pageSize);
        long count = contentEditService.countByPage(colType);
        JSONObject json = new JSONObject();
        json.put("list", list);
        json.put("totalCount", count);
        json.put("currentPage", pageNo);
        json.put("pageSize", pageSize);
        json.put("totalPages", count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
        return json;
    }

    @RequestMapping(value = "admin/add/preview", method = RequestMethod.POST)
    public String getCollectionPreviewByColId(ContentEdit contentEdit,
                                              @RequestParam(name = "content") String content,
                                              Model model) {
        contentEdit.setCreateTime(DateUtils.addHours(new Date(),8));
        contentEdit.setContent(content);
        model.addAttribute("collection", contentEdit);
        return "collectionConPreview";
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public JSONObject delete(@PathVariable("id") String id) {
        boolean i = contentEditService.delete(id);
        JSONObject jsonObject = new JSONObject();
        if (i == true) {
            jsonObject.put("result", "success");
        } else {
            jsonObject.put("result", "fail");
        }
        return jsonObject;
    }

    @RequestMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView("collectionView");
        ContentEdit contentEdit = contentEditService.getCollectionByColId(id);
//        collection.setColContent("asdfasdfa");
        modelAndView.addObject("collection", contentEdit);
        return modelAndView;
    }

    @RequestMapping(value = "admin/colId", method = RequestMethod.GET)
    public String getCollectionById(@RequestParam(name = "operate", defaultValue = "add") String operate,
                                    @RequestParam(name = "colId", required = false) String colId,
                                    Model model) {
        model.addAttribute("operate", operate);
        List<ContentEditType> list = contentEditService.getAllCategoryList();
        model.addAttribute("list",list);
        if (StringUtils.equals(operate, "add")) {
            ContentEdit contentEdit = new  ContentEdit();
            model.addAttribute("collection", contentEdit);
            return "collectionEdit";
        }
        ContentEdit contentEdit = contentEditService.getCollectionByColId(colId);
        model.addAttribute("collection", contentEdit);
        model.addAttribute("colId",colId);
        return "collectionEdit";
    }

    @RequestMapping(value = "admin/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute ContentEdit collection,String colId) {
        boolean result = contentEditService.edit(collection,colId);
        return "redirect:/collection/";
    }

    @RequestMapping(value = "admin/add", method = RequestMethod.POST)
    public String adminAdd(@ModelAttribute ContentEdit contentEdit, @RequestParam(name = "content", required = false) String content) {
       // contentEdit.setId(UUID.randomUUID().toString());
        boolean result = contentEditService.add(contentEdit, content);
        return "redirect:/collection/";
    }

    //栏目管理
    @RequestMapping(value = "admin/category", method = RequestMethod.GET)
    public String category() {
        return "admin/category";
    }


    //取得栏目
    @RequestMapping(value = "admin/getCategoryList", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getCategoryList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", contentEditService.getCategoryList());
        return jsonObject;
    }


    @RequestMapping(value = "admin/addCategory" , method = RequestMethod.POST)
    public String addCategory(@ModelAttribute("category")ContentEditType contentEditType ){
        contentEditService.addCategory(contentEditType);
        return "redirect:/collection/admin/category";
    }


    @RequestMapping(value = "admin/updateCategory" , method = RequestMethod.POST)
    public String updateCategory(@ModelAttribute("category")ContentEditType contentEditType ){
        contentEditService.updateCategory(contentEditType);
        return "redirect:/collection/admin/category";
    }


    @RequestMapping(value = "admin/getCategoryById", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getCategoryById(String id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("category", contentEditService.getCategoryById(id));
        return jsonObject;
    }


    @RequestMapping(value = "admin/deleteCategory", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject deleteCategory(String id) {
        JSONObject jsonObject = new JSONObject();
        boolean i = contentEditService.deleteCategory(id);
        if (i == true) {
            jsonObject.put("result", "success");
        } else {
            jsonObject.put("result", "fail");
        }
        return jsonObject;
    }


}
