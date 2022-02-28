package xyz.ziyublog.yxj.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import xyz.ziyublog.yxj.back.dao.NoteDao;
import xyz.ziyublog.yxj.back.dao.NoteTypeDao;
import xyz.ziyublog.yxj.back.dao.UserDao;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.pojo.User;

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

    public List<Note> getNotesByUserAndNoteType(String username, int noteTypeID){

        NoteType noteType = noteTypeDao.getById(noteTypeID);
        User user = userDao.findByUsername(username);
        return noteDao.findAllByAuthorAndNoteType(user,noteType);
    }

    // 更新笔记
    public void updateNote(Note note){
        ValueOperations<String, Note> operations = redisTemplate.opsForValue();
        noteDao.save(note);

        String key = "note_" + note.getId();
        boolean haskey = redisTemplate.hasKey(key);
        if (haskey) {
            redisTemplate.delete(key);
            System.out.println("删除缓存中的key-----------> " + key);
        }
        // 再将更新后的数据加入缓存
        Note newNote = noteDao.findById(note.getId());
        if (newNote != null) {
            operations.set(key, newNote, 3, TimeUnit.HOURS);
            System.out.println("更新缓存中的key-----------> " + key);
        }
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
    
    public List<Note> getNotesByUser(String username){
        User user = userDao.findByUsername(username);
        return noteDao.findAllByAuthor(user);
    }

    public Note getNoteById(int noteId){
        ValueOperations<String, Note> operations = redisTemplate.opsForValue();
        String key = "note_" + noteId;
        //判断redis中是否有键为key的缓存
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            Note note= operations.get(key);
            System.out.println("从缓存中获得数据："+note.toString());
            System.out.println("------------------------------------");
            return note;
        } else {
            Note note = noteDao.findById(noteId);
            System.out.println("查询数据库获得数据："+note.toString());
            System.out.println("------------------------------------");
            // 写入缓存
            operations.set(key, note, 5, TimeUnit.HOURS);
            return note;
        }
    }
}
