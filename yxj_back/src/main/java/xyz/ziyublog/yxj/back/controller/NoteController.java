package xyz.ziyublog.yxj.back.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.ziyublog.yxj.back.forFrontClass.NoteInfor;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.service.UserService;
import xyz.ziyublog.yxj.back.util.Response;
import xyz.ziyublog.yxj.back.service.NoteService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class NoteController {
    @Autowired
    NoteService noteService;

    @Autowired
    UserService userService;

    @ApiOperation("获得某个用户下的笔记")
    @CrossOrigin
    @GetMapping("/api/note/getNotes")
    @ResponseBody
    public Response getNotes(String username,Integer noteTypeId){
        Response response = new Response(0, "failed", null);
        try{
            if (username!=null&&noteTypeId!=null){
                List<Note> notes = noteService.getNotesByUserAndNoteType(username, noteTypeId);
                List<NoteInfor> noteInfors = new ArrayList<>();
                for (Note note:notes) {
                    noteInfors.add(new NoteInfor(note));
                }
                response.setResponse(200,"successful",notes);
            }else if (username!=null&&noteTypeId==null){
                List<Note> notes = noteService.getNotesByUser(username);
                List<NoteInfor> noteInfors = new ArrayList<>();
                for (Note note:notes) {
                    noteInfors.add(new NoteInfor(note));
                }
                response.setResponse(200,"successful",notes);
            }
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }

        return response;

    }
    @ApiOperation("通过笔记id获得笔记")
    @CrossOrigin
    @GetMapping("/api/note/getNote")
    @ResponseBody
    public Response getNoteById(int noteId){
        Response response = new Response(0, "failed", null);
        try{
            Note note = noteService.getNoteById(noteId);
            response.setResponse(200,"successful",note);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("通过笔记id删除笔记")
    @CrossOrigin
    @GetMapping("/api/note/deleteNote")
    @ResponseBody
    public Response DeleteNoteById(int noteId){
        Response response = new Response(0, "failed", null);
        try{
            boolean delResult = noteService.deleteNoteById(noteId);
            if(delResult){
                response.setResponse(200,"删除成功",null);
            }else {
                response.setResponse(405,"删除失败",null);
            }
        }catch (Exception e){
            log.info("<< 删除笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("更新笔记")
    @CrossOrigin
    @PostMapping("/api/note/updateNote")
    @ResponseBody
    public Response updateNote(@RequestBody Note note){
        Response response = new Response(0, "failed", null);
        try{
            note.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            noteService.updateNote(note);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }


    @ApiOperation("新增笔记")
    @CrossOrigin
    @PostMapping("/api/note/addNote")
    @ResponseBody
    public Response addNote(@RequestBody Note newNote){
        Response response = new Response(0, "failed", null);
        try{
            System.out.println(newNote.getAuthor());
            Note note=newNote;
            // 后修改为从登录状态中获得当前用户信息
            note.setAuthor(userService.getUserByID(10000011));
//            Note note = new Note();
//            note.setTitle(newNote.getTitle());
//            note.setIsPublic(newNote.getIsPublic());
//            note.setContentMd(newNote.getContentMd());
//            note.setAuthor(newNote.getAuthor());
//            note.setContentHtml(newNote.getContentHtml());
//            note.setDescribe(newNote.getDescribe());
//            note.setNoteType(newNote.getNoteType());
            note.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            note.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            note.setIsDelete(0);
            noteService.updateNote(note);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 新增笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("修改笔记公开状态")
    @CrossOrigin
    @GetMapping("/api/note/updatePublicValue")
    @ResponseBody
    public Response updatePublicValueByNoteId(int noteId){
        Response response = new Response(0, "failed", null);
        try{
            noteService.updateNotePublicStatus(noteId);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 修改笔记公开状态:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("将笔记移入回收站")
    @CrossOrigin
    @GetMapping("/api/note/moveNoteInRecycler")
    @ResponseBody
    public Response moveNoteInRecyclerByNoteId(int noteId){
        Response response = new Response(0, "failed", null);
        try{
            noteService.moveNoteInRecyclerByNoteId(noteId);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 修改笔记公开状态:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("将笔记移出回收站")
    @CrossOrigin
    @GetMapping("/api/note/moveNoteOutRecycler")
    @ResponseBody
    public Response moveNoteOutRecyclerByNoteId(int noteId){
        Response response = new Response(0, "failed", null);
        try{
            noteService.moveNoteOutRecyclerByNoteId(noteId);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 修改笔记公开状态:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

}
