package xyz.ziyublog.yxj.back.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.service.CollectionService;
import xyz.ziyublog.yxj.back.service.UserService;
import xyz.ziyublog.yxj.back.util.Response;

import java.util.List;

@Slf4j
@Controller
public class CollectionController {
    @Autowired
    CollectionService collectionService;

    @Autowired
    UserService userService;


    @ApiOperation("查看某用户是否收藏了某笔记")
    @CrossOrigin
    @GetMapping("/api/collection/getIsCollection")
    @ResponseBody
    public Response getIsCollection(int noteId, String username){
        Response response = new Response(0, "failed", null);
        try{
            int userId = userService.getUserByUsername(username).getId();
            Boolean isCollection = collectionService.getIsCollection(noteId, userId);
            response.setResponse(200,"successful", isCollection);
        }catch (Exception e){
            log.info("<< 获得收藏关系失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }


    @ApiOperation("用户取消或收藏笔记")
    @CrossOrigin
    @GetMapping("/api/collection/setCollection")
    @ResponseBody
    public Response setCollection(int noteId, String username){
        Response response = new Response(0, "failed", null);
        try{
            int userId = userService.getUserByUsername(username).getId();
            collectionService.setCollection(noteId,userId);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 用户取消或收藏笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }


    @ApiOperation("用户的收藏笔记")
    @CrossOrigin
    @GetMapping("/api/collection/getUserCollectionNotes")
    @ResponseBody
    public Response getCollectionNotes(String username){
        Response response = new Response(0, "failed", null);
        try{
            int userId = userService.getUserByUsername(username).getId();
            List<Note> notes = collectionService.getUserCollectionNote(userId);
            response.setResponse(200,"successful",notes);
        }catch (Exception e){
            log.info("<< 获得用户的收藏笔记:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }
}
