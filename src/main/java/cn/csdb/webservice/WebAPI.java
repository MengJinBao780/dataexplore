package cn.csdb.webservice;

import cn.csdb.model.FileInfo;
import cn.csdb.model.Sdo;
import cn.csdb.service.FileInfoService;
import cn.csdb.service.SdoService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class WebAPI {

    @Resource
    private SdoService sdoService;

    @Resource
    private FileInfoService fileInfoService;

    //获取数据集的元数据信息
    @RequestMapping(value = "/sdoMetadata/{id}", method = RequestMethod.GET)
    public JSONObject getSdoMetaData(@PathVariable("id") String id) {
        JSONObject jsonObject = new JSONObject();
        Sdo sdo = sdoService.getSdoById(id);
        jsonObject.put("data",sdo);
        return jsonObject;
    }

    //获取文件的元数据信息
    @RequestMapping(value = "/fileMetadata/{fileId}", method = RequestMethod.GET)
    public JSONObject getFileMetaData(@PathVariable("fileId") String fileId) {
        JSONObject jsonObject = new JSONObject();
        FileInfo fileInfo = fileInfoService.getById(fileId);
        jsonObject.put("data", fileInfo);
        return jsonObject;
    }

    //    文件列表API
    @ResponseBody
    @RequestMapping("getFileListBySdoPid")
    public JSONObject getFileListBySdoPid(@RequestParam("sdoPid") String sdoPid,Integer pageNum,Integer curPage){
        JSONObject jo = new JSONObject();
        Sdo sdo = sdoService.getSdoByPid(sdoPid);
        JSONObject joNum =fileInfoService.getTotalPageNumBySdoId(sdo.getId(),pageNum);
        int pageSum =(Integer)joNum.get("totalPage");
        if (pageNum ==null || pageNum<=0 || pageSum<pageNum){
            pageNum=1;
        }
        JSONArray fileListJson = fileInfoService.getFile(sdo.getId(),pageNum,curPage);
        jo.put("总文件数",(Integer)joNum.get("totalPage"));
        jo.put("总页数",(Integer)joNum.get("totalCount"));
        jo.put("每页条数",pageNum);
        jo.put("当前页数",curPage);
        jo.put("文件列表",fileListJson);
        return jo;
    }

    //    文件列表API
    @ResponseBody
    @RequestMapping("getFileListBySdoId")
    public JSONObject getFileListBySdoId(@RequestParam("sdoId") String sdoId,Integer pageNum,Integer curPage){
        JSONObject jo = new JSONObject();
        JSONObject joNum =fileInfoService.getTotalPageNumBySdoId(sdoId,pageNum);
        int pageSum =(Integer)joNum.get("totalPage");
        if (pageNum ==null || pageNum<=0 || pageSum<pageNum){
            pageNum=1;
        }
        JSONArray fileListJson = fileInfoService.getFile(sdoId,pageNum,curPage);
        jo.put("总文件数",(Integer)joNum.get("totalPage"));
        jo.put("总页数",(Integer)joNum.get("totalCount"));
        jo.put("每页条数",pageNum);
        jo.put("当前页数",curPage);
        jo.put("文件列表",fileListJson);
        return jo;
    }
}
