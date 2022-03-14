package xyz.ziyublog.yxj.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import xyz.ziyublog.yxj.back.dao.NoteDao;
import xyz.ziyublog.yxj.back.dao.NoteTypeDao;
import xyz.ziyublog.yxj.back.dao.UserDao;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.forFrontClass.NoteInfor;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.pojo.User;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
        return noteDao.findAllByAuthorAndNoteTypeAndIsDelete(user,noteType,0);
    }

    // 更新笔记
    public void updateNote(Note note){
        ValueOperations<String, Note> operations = redisTemplate.opsForValue();
        noteDao.save(note);

        String key = "note_" + note.getId();
        boolean hashKey = redisTemplate.hasKey(key);
        if (hashKey) {
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
    
    public List<Note> getNotesByUser(String username){
        User user = userDao.findByUsername(username);
        return noteDao.findAllByAuthorAndIsDelete(user,0);
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
        int res = noteDao.deleteById(noteId);
        if(res == 1){
            result = true;
        }
        return result;
    }





    public static String timeStamp2Date(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

}
