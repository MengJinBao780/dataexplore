package cn.csdb.controller;

import cn.csdb.model.Sdo;
import cn.csdb.model.SdoRelate;
import cn.csdb.service.SdoRelationService;
import cn.csdb.service.SdoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ajian on 2018/5/15.
 */
@Controller
public class SdoRelationController {

    private Logger logger = LoggerFactory.getLogger(SdoRelationController.class);

    @Resource
    private SdoRelationService sdoRelationService;
    @Resource
    private SdoService sdoService;

    @RequestMapping(value = "/sdo/relation/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<SdoRelate> relationList(@PathVariable("id") String sdoId) {
        return sdoRelationService.relationList(sdoId);
    }

    @RequestMapping(value = "/sdo/relation/page/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<SdoRelate> relationListByPage(@PathVariable("id") String sdoId) {
        List<SdoRelate> sdoRelates = sdoRelationService.relationList(sdoId);
        int size = sdoRelates.size();
        return size <= 8 ? sdoRelates : sdoRelates.subList(size - 8, size);
    }

    //sdo map
    @RequestMapping(value = "/sdo/relation/map/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, Model model) {
        cn.csdb.model.Resource sdo = sdoService.getSdoById(id);
        if (sdo == null) {
            return "sdoRelation";
        }
        model.addAttribute("id", id);
        model.addAttribute("title", sdo.getTitle());
        return "sdoRelation";
    }

}
