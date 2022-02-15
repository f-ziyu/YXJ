package xyz.ziyublog.yxj.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.service.NoteService;
import xyz.ziyublog.yxj.back.service.NoteTypeService;

import java.util.List;

@Controller
public class NoteController {
    @Autowired
    NoteTypeService noteTypeService;
    @Autowired
    NoteService noteService;

    @CrossOrigin
    @GetMapping("/api/noteTypes")
    @ResponseBody
    public List<NoteType> getNoteTypes(){
        return noteTypeService.getAllNoteType();
    }

    @CrossOrigin
    @GetMapping("/api/noteTypes/{username}/{noteTypeId}/notes")
    @ResponseBody
    public List<Note> getNotesByUserAndNoteType(@PathVariable("username") String username,@PathVariable("noteTypeId") int noteTypeId){
        return noteService.getNotesByUserAndNoteType(username,noteTypeId);
    }


}
