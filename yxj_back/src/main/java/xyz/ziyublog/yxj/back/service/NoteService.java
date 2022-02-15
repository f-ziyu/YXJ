package xyz.ziyublog.yxj.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.ziyublog.yxj.back.dao.NoteDao;
import xyz.ziyublog.yxj.back.dao.NoteTypeDao;
import xyz.ziyublog.yxj.back.dao.UserDao;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    NoteDao noteDao;

    @Autowired
    NoteTypeDao noteTypeDao;

    @Autowired
    UserDao userDao;

    public List<Note> getNotesByUserAndNoteType(String username, int noteTypeID){
        NoteType noteType = noteTypeDao.getById(noteTypeID);
        User user = userDao.findByUsername(username);
        return noteDao.findAllByAuthorAndNoteType(user,noteType);
    }
}
