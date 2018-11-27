package cn.csdb.controller;

import cn.csdb.model.Sdo;
import cn.csdb.service.RelationModelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ajian on 2018/5/29.
 */
@Controller
public class RelationModelController {
    @Resource
    private RelationModelService relationModelService;

    @RequestMapping(value = "/sdo/visit/relations", method = RequestMethod.GET)
    @ResponseBody
    public List<Sdo> getRelations(String sdoId, int count) throws InterruptedException, ClassNotFoundException {
        if (count < 1) {
            count = 10;
        }
        return relationModelService.getRelations(sdoId, count);
    }
}