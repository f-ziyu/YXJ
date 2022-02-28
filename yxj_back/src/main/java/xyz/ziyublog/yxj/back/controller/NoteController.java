package xyz.ziyublog.yxj.back.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.util.Response;
import xyz.ziyublog.yxj.back.service.NoteService;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Controller
public class NoteController {
    @Autowired
    NoteService noteService;

    @ApiOperation("获得某个用户下的笔记")
    @CrossOrigin
    @GetMapping("/api/note/getNotes")
    @ResponseBody
    public Response getNotes(String username,Integer noteTypeId){
        Response response = new Response(0, "出错了", null);
        try{
            if (username!=null&&noteTypeId!=null){
                List<Note> notes = noteService.getNotesByUserAndNoteType(username, noteTypeId);
                response.setResponse(200,"successful",notes);
            }else if (username!=null&&noteTypeId==null){
                List<Note> notes = noteService.getNotesByUser(username);
                response.setObject(notes);
                response.setStatus(200);
                response.setMsg("请求成功，返回数据");
            }
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed",e);
        }

        return response;

    }
    @ApiOperation("通过笔记id获得笔记")
    @CrossOrigin
    @GetMapping("/api/note/getNote")
    @ResponseBody
    public Response getNoteById(int noteId){
        Response response = new Response(0, "出错了", null);
        try{
            Note note = noteService.getNoteById(noteId);
            response.setResponse(200,"successful",note);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed",e);
        }
        return response;
    }
    @ApiOperation("更新笔记")
    @CrossOrigin
    @PostMapping("/api/note/updateNote")
    @ResponseBody
    public Response updateNote(@RequestBody Note newNote){
        Response response = new Response(0, "出错了", null);
        try{
            noteService.updateNote(newNote);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed",e);
        }
        return response;
    }

    @ApiOperation("新增笔记")
    @CrossOrigin
    @PostMapping("/api/note/addNote")
    @ResponseBody
    public Response addNote(@RequestBody Note newNote){
        Response response = new Response(0, "出错了", null);
        try{
            System.out.println(newNote.getAuthor());
            Note note = new Note();
            note.setTitle(newNote.getTitle());
            note.setIsPublic(newNote.getIsPublic());
            note.setContentMd(newNote.getContentMd());
            note.setAuthor(newNote.getAuthor());
            note.setContentHtml(newNote.getContentHtml());
            note.setDescribe(newNote.getDescribe());
            note.setNoteType(newNote.getNoteType());

            note.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            note.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            noteService.updateNote(newNote);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed",e);
        }
        return response;
    }

    @ApiOperation("修改笔记公开状态")
    @CrossOrigin
    @GetMapping("/api/note/updatePublicValue")
    @ResponseBody
    public Response updatePublicValueByNoteId(int noteId){
        Response response = new Response(0, "出错了", null);
        try{
            noteService.updateNotePublicStatus(noteId);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed",e);
        }
        return response;
    }

}
