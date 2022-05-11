package xyz.ziyublog.yxj.back.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.ziyublog.yxj.back.pojo.NoteComment;
import xyz.ziyublog.yxj.back.pojo.NoteFollowComment;
import xyz.ziyublog.yxj.back.service.CommentService;
import xyz.ziyublog.yxj.back.service.NoteService;
import xyz.ziyublog.yxj.back.service.UserService;
import xyz.ziyublog.yxj.back.util.Response;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;


    @ApiOperation("提交新的评论")
    @CrossOrigin
    @PostMapping("/api/comment/addNoteComment")
    @ResponseBody
    public Response addNoteComment(@RequestBody NoteComment noteComment){
        Response response = new Response(0, "failed", null);
        try {
            noteComment.setNote(noteService.getNoteById(noteComment.getNote().getId()));
            noteComment.setUser(userService.getUserByUsername(noteComment.getUser().getUsername()));
            noteComment.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            noteComment.setIsDelete(0);
            commentService.addNoteComment(noteComment);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 添加评论失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes-->",e);
        }
        return response;
    }


    @ApiOperation("通过笔记id获取它的所有评论")
    @CrossOrigin
    @GetMapping("/api/comment/getAllCommentByNoteId")
    @ResponseBody
    public Response getCommentByNoteId(int noteId){
        Response response = new Response(0, "failed", null);
        try{
            List<NoteComment> noteComments = commentService.getNoteCommentByNoteId(noteId);
            response.setResponse(200,"successful", noteComments);
        }catch (Exception e){
            log.info("<< 获得笔记评论失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }


    @ApiOperation("删除某评论")
    @CrossOrigin
    @GetMapping("/api/comment/deleteComment")
    @ResponseBody
    public Response deleteCommentByCommentId(int commentId) {
        Response response = new Response(0, "failed", null);
        try {
            commentService.deleteNoteComment(commentId);
            response.setResponse(200, "successful", null);
        } catch (Exception e) {
            log.info("<< 获得笔记评论失败:\n {}", e);
            response.setResponse(500, "failed by some mistakes", e);
        }
        return response;
    }


    @ApiOperation("获取用户的所有笔记")
    @CrossOrigin
    @GetMapping("/api/comment/getAllCommentByUsername")
    @ResponseBody
    public Response getCommentByUsername(String username){
        Response response = new Response(0, "failed", null);
        try{
            List<NoteComment> noteComments = commentService.getNoteCommentByUsername(username);
            response.setResponse(200,"successful", noteComments);
        }catch (Exception e){
            log.info("<< 获得笔记评论失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

//    @ApiOperation("提交新的跟评")
//    @CrossOrigin
//    @PostMapping("/api/comment/addNoteComment")
//    @ResponseBody
//    public Response addNoteFollowComment(@RequestBody NoteFollowComment noteFollowComment){
//        Response response = new Response(0, "failed", null);
//        try {
//            noteFollowComment.setCreatedTime(new Timestamp(System.currentTimeMillis()));
//            noteFollowComment.setIsDelete(0);
//            commentService.addNoteFollowComment(noteFollowComment);
//            response.setResponse(200,"successful",null);
//        }catch (Exception e){
//            log.info("<< 添加评论失败:\n {}", e);
//            response.setResponse(500,"failed by some mistakes-->",e);
//        }
//        return response;
//    }
//
//
//    @ApiOperation("通过评论id获取它的跟评")
//    @CrossOrigin
//    @GetMapping("/api/comment/getFollowComment")
//    @ResponseBody
//    public Response getFollowCommentByCommentId(int commentId){
//        Response response = new Response(0, "failed", null);
//        try{
//            List<NoteFollowComment> noteFollowComments = commentService.getNoteFollowCommentByCommentId(commentId);
//            response.setResponse(200,"successful", noteFollowComments);
//        }catch (Exception e){
//            log.info("<< 获得笔记评论失败:\n {}", e);
//            response.setResponse(500,"failed by some mistakes",e);
//        }
//        return response;
//    }

}
