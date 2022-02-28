package xyz.ziyublog.yxj.back.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.util.Response;
import xyz.ziyublog.yxj.back.service.NoteTypeService;

import java.util.List;

@Slf4j
@Controller
public class NoteTypeController {
    @Autowired
    NoteTypeService noteTypeService;

    @ApiOperation("获得笔记类别")
    @CrossOrigin
    @GetMapping("/api/noteType/getNoteTypes")
    @ResponseBody
    public Response getNoteTypes(){
        Response response = new Response(0, "出错了", null);
        try{
            List<NoteType> allNoteType = noteTypeService.getAllNoteType();
            response.setObject(allNoteType);
            response.setStatus(200);
            response.setMsg("请求成功，返回数据");
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed",e);
        }
        return response;
    }
}
