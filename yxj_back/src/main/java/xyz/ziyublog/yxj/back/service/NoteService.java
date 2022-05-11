package xyz.ziyublog.yxj.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import xyz.ziyublog.yxj.back.dao.NoteDao;
import xyz.ziyublog.yxj.back.dao.NoteTypeDao;
import xyz.ziyublog.yxj.back.dao.UserDao;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NoteService {
    @Autowired
    NoteDao noteDao;

    @Autowired
    NoteTypeDao noteTypeDao;

    @Autowired
    UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    // 通过用户和笔记类型获得笔记
    public List<Note> getNotesByUserAndNoteType(String username, int noteTypeID){
        NoteType noteType = noteTypeDao.getById(noteTypeID);
        User user = userDao.findByUsername(username);
        return noteDao.findAllByAuthorAndNoteTypeAndIsDelete(user,noteType,0,Sort.by(Sort.Direction.ASC,"id"));
    }

    // 通过用户和笔记类型获得笔记总数
    public int getNotesByUserAndNoteTypeSize(String username, int noteTypeID){
        NoteType noteType = noteTypeDao.getById(noteTypeID);
        User user = userDao.findByUsername(username);
        return noteDao.countByAuthorAndNoteTypeAndIsDelete(user,noteType,0);
    }

    // 获得公开笔记
    public List<Note> getPublicNotesByNoteType(){
        return noteDao.findAllByIsDeleteAndIsPublic(0,1,Sort.by(Sort.Direction.DESC,"id"));
    }

    // 获得用户回收站笔记
    public List<Note> getRecycleNotes(User user){
        List<Note> notes = noteDao.findAllByAuthorAndIsDelete(user, 1,Sort.by(Sort.Direction.ASC,"id"));
        return notes;
    }
    // 获得用户回收站笔记总数
    public int getRecycleNotesSize(User user){
        return noteDao.countByAuthorAndIsDelete(user, 1);
    }

    // 更新笔记
    public void updateNote(Note note){
        ValueOperations<String, Note> operations = redisTemplate.opsForValue();
        noteDao.save(note);
        String key = "note_" + note.getId();
        boolean hashKey = redisTemplate.hasKey(key);
        if (hashKey) {
            redisTemplate.delete(key);
        }
        Note newNote = noteDao.findById(note.getId());
        if (newNote != null) {
            operations.set(key, newNote, 5, TimeUnit.HOURS);
        }
    }
    
    public List<Note> getNotesByUser(String username){
        User user = userDao.findByUsername(username);
        return noteDao.findAllByAuthorAndIsDelete(user,0,Sort.by(Sort.Direction.DESC,"id"));
    }
    public int getNotesByUserSize(String username){
        User user = userDao.findByUsername(username);
        return noteDao.countByAuthorAndIsDelete(user,0);
    }

    public Note getNoteById(int noteId){
        ValueOperations<String, Note> operations = redisTemplate.opsForValue();
        String key = "note_" + noteId;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            Note note= operations.get(key);
            return note;
        } else {
            Note note = noteDao.findById(noteId);
            operations.set(key, note, 5, TimeUnit.HOURS);
            return note;
        }
    }

    // 删除笔记
    public boolean deleteNoteById(int noteId){
        boolean result=false;
        ValueOperations<String, Note> operations = redisTemplate.opsForValue();
        String key = "note_" + noteId;
        // 删除缓存
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
        }
        // 删除数据库
        if(noteDao.existsById(noteId)){
            try {
                noteDao.deleteById(noteId);
                result = true;
            }catch (Exception e){

            }
        }
        return result;
    }

    // 更新笔记公开状态
    public void updateNotePublicStatus(int noteId){
        Note note = noteDao.findById(noteId);
        if(note.getIsPublic() == 1){
            note.setIsPublic(0);
        }else {
            note.setIsPublic(1);
        }
        updateNote(note);
    }

    // 修改笔记的笔记类型
    public void updateTypeOfNote(int noteId,int typeId){
        Note note = noteDao.findById(noteId);
        if(typeId<=0){
            note.setNoteType(null);
        }else {
            note.setNoteType(noteTypeDao.findById(typeId));
        }
        updateNote(note);
    }

    // 将笔记移入回收站
    public void moveNoteInRecyclerByNoteId(int noteId){
        Note note = noteDao.findById(noteId);
        note.setIsDelete(1);
        updateNote(note);
    }

    // 将笔记移出回收站
    public void moveNoteOutRecyclerByNoteId(int noteId){
        Note note = noteDao.findById(noteId);
        note.setIsDelete(0);
        updateNote(note);
    }

    // 搜索笔记
    public List<Note> searchNotesByTitle(String searchParams, String username){
        List<Note> result = new ArrayList<>();
        List<Note> notes = noteDao.findAllByTitleContaining(searchParams,Sort.by(Sort.Direction.ASC,"id"));
        User user = userDao.findByUsername(username);
        for (int i=0;i<notes.size();i++){
            Note note = notes.get(i);
            if (note.getAuthor().getId()==user.getId() && note.getIsDelete()==0 ){
                result.add(note);
            }else if(note.getIsDelete()==0 && note.getIsPublic()==1){
                result.add(note);
            }
        }
        return result;
    }

    // 获得公开笔记总数
    public int getPublicNotesTotalSize(){
        return noteDao.countByIsDeleteAndIsPublic(0,1);
    }

    // 分页查询
    // 获得公开笔记
    public List<Note> getPublicNotesPageable(int pageNum, int pageSize){
        pageNum=pageNum-1;
        Pageable pageable = PageRequest.of(pageNum,pageSize,Sort.by(Sort.Direction.DESC,"id"));
        return noteDao.getAllByIsDeleteAndIsPublic(0, 1, pageable);
    }

    // 通过用户和笔记类型获得笔记
    public List<Note> getNotesByUserAndNoteTypePageable(String username, int noteTypeID,int pageNum, int pageSize){
        NoteType noteType = noteTypeDao.getById(noteTypeID);
        User user = userDao.findByUsername(username);
        pageNum=pageNum-1;
        Pageable pageable = PageRequest.of(pageNum,pageSize,Sort.by(Sort.Direction.DESC,"id"));
        return noteDao.getAllByAuthorAndNoteTypeAndIsDelete(user,noteType,0,pageable);
    }

    public List<Note> getNotesByUserPageable(String username,int pageNum, int pageSize){
        User user = userDao.findByUsername(username);
        pageNum=pageNum-1;
        Pageable pageable = PageRequest.of(pageNum,pageSize,Sort.by(Sort.Direction.DESC,"id"));
        return noteDao.getAllByAuthorAndIsDelete(user,0,pageable);
    }
}
