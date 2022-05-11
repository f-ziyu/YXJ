package xyz.ziyublog.yxj.back.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.User;
import xyz.ziyublog.yxj.back.service.NoteTypeService;
import xyz.ziyublog.yxj.back.service.UserService;
import xyz.ziyublog.yxj.back.util.Response;
import xyz.ziyublog.yxj.back.service.NoteService;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Controller
public class NoteController {
    @Autowired
    NoteService noteService;
    @Autowired
    UserService userService;
    @Autowired
    NoteTypeService noteTypeService;

    @ApiOperation("获得所有的公开笔记")
    @CrossOrigin
    @GetMapping("/api/note/getPublicNotes")
    @ResponseBody
    public Response getPublicNotes(){
        Response response = new Response(0, "failed", null);
        try{
            List<Note> notes = noteService.getPublicNotesByNoteType();
            response.setResponse(200,"success",notes);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("获得某个用户下的笔记")
    @CrossOrigin
    @GetMapping("/api/note/getNotes")
    @ResponseBody
    public Response getNotes(String username,Integer noteTypeId){
        Response response = new Response(0, "failed", null);
        try{
            if (username!=null&&noteTypeId!=null){
                List<Note> notes = noteService.getNotesByUserAndNoteType(username, noteTypeId);
                for (Note note:notes) {
                    note.setContentHtml("");
                    note.setContentMd("");
                }
                response.setResponse(200,"successful",notes);
            }else if (username!=null&&noteTypeId==null){
                List<Note> notes = noteService.getNotesByUser(username);
                for (Note note:notes) {
                    note.setContentHtml("");
                    note.setContentMd("");
                }
                response.setResponse(200,"successful",notes);
            }
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("获得某个用户下的笔记总数")
    @CrossOrigin
    @GetMapping("/api/note/getNotesSize")
    @ResponseBody
    public Response getNotesSize(String username,Integer noteTypeId){
        Response response = new Response(0, "failed", null);
        try{
            if (username!=null&&noteTypeId!=null){
                int notesByUserAndNoteTypeSize = noteService.getNotesByUserAndNoteTypeSize(username, noteTypeId);
                response.setResponse(200,"successful",notesByUserAndNoteTypeSize);
            }else if (username!=null&&noteTypeId==null){
                int notesByUserSize = noteService.getNotesByUserSize(username);
                response.setResponse(200,"successful",notesByUserSize);
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
            note.setAuthor(userService.getUserByUsername(newNote.getAuthor().getUsername()));
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

    @ApiOperation("获得用户的回收站笔记")
    @CrossOrigin
    @GetMapping("/api/note/getRecycleNotes")
    @ResponseBody
    public Response getRecycleNotesByUser(String username){
        Response response = new Response(0, "failed", null);
        try{
            User user = userService.getUserByUsername(username);
            List<Note> recycleNotes = noteService.getRecycleNotes(user);
            response.setResponse(200,"successful",recycleNotes);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
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

    @ApiOperation("修改笔记的笔记类型")
    @CrossOrigin
    @GetMapping("/api/note/modifyNoteType")
    @ResponseBody
    public Response updateTypeOfNote(int noteId,int typeId){
        Response response = new Response(0, "failed", null);
        try{
            noteService.updateTypeOfNote(noteId, typeId);
            response.setResponse(200,"successful",null);
        }catch (Exception e){
            log.info("<< 修改笔记的笔记类型:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }


    @ApiOperation("搜索笔记")
    @CrossOrigin
    @GetMapping("/api/note/searchNotes")
    @ResponseBody
    public Response searchNotes(String searchParams, String username){
        Response response = new Response(0, "failed", null);
        try{
            List<Note> notes = noteService.searchNotesByTitle(searchParams, username);
            response.setResponse(200,"successful",notes);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    // 分页查询
    @ApiOperation("获得所有的公开笔记总数")
    @CrossOrigin
    @GetMapping("/api/note/pageable/getPublicNotesTotalSize")
    @ResponseBody
    public Response getPublicNotesTotalSize(){
        Response response = new Response(0, "failed", null);
        try{
            int publicNotesTotalSize = noteService.getPublicNotesTotalSize();
            response.setResponse(200,"success",publicNotesTotalSize);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("分页查询所有的公开笔记")
    @CrossOrigin
    @GetMapping("/api/note/pageable/getPublicNotes")
    @ResponseBody
    public Response getPublicNotesPageable(int currentPage, int pageSize){
        Response response = new Response(0, "failed", null);
        try{
            List<Note> notes = noteService.getPublicNotesPageable(currentPage, pageSize);
            response.setResponse(200,"success",notes);
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }

    @ApiOperation("获得某个用户下的笔记")
    @CrossOrigin
    @GetMapping("/api/note/pageable/getNotes")
    @ResponseBody
    public Response getNotesPageable(String username,Integer noteTypeId, int currentPage, int pageSize){
        Response response = new Response(0, "failed", null);
        try{
            if (username!=null&&noteTypeId!=null){
                List<Note> notes = noteService.getNotesByUserAndNoteTypePageable(username,noteTypeId,currentPage,pageSize);
                for (Note note:notes) {
                    note.setContentHtml("");
                    note.setContentMd("");
                }
                response.setResponse(200,"successful",notes);
            }else if (username!=null&&noteTypeId==null){
                List<Note> notes = noteService.getNotesByUserPageable(username,currentPage,pageSize);
                for (Note note:notes) {
                    note.setContentHtml("");
                    note.setContentMd("");
                }
                response.setResponse(200,"successful",notes);
            }
        }catch (Exception e){
            log.info("<< 获得笔记失败:\n {}", e);
            response.setResponse(500,"failed by some mistakes",e);
        }
        return response;
    }


}
