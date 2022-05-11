package xyz.ziyublog.yxj.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import xyz.ziyublog.yxj.back.dao.NoteCollectionDao;
import xyz.ziyublog.yxj.back.dao.NoteDao;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteCollection;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class CollectionService {
    @Autowired
    NoteCollectionDao noteCollectionDao;

    @Autowired
    NoteService noteService;

    @Autowired
    RedisTemplate redisTemplate;

    // 获得用户是否收藏某笔记
    public Boolean getIsCollection(int noteId, int userId){
        boolean isCollection = noteCollectionDao.existsByNoteIdAndUserId(noteId, userId);
        if(isCollection){
            NoteCollection noteCollection = getNoteCollection(noteId, userId);
            if(noteCollection.getIsCollection() == 1){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public NoteCollection getNoteCollection(int noteId, int userId){
        ValueOperations<String, NoteCollection> operations = redisTemplate.opsForValue();
        String key = "Collection_" + userId + '_' + noteId;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            NoteCollection noteCollection = operations.get(key);
            return noteCollection;
        } else {
            NoteCollection noteCollection = noteCollectionDao.findByNoteIdAndUserId(noteId, userId);
            operations.set(key, noteCollection, 5, TimeUnit.HOURS);
            return noteCollection;
        }
    }

    // 保存收藏记录
    public void saveNoteCollection(NoteCollection noteCollection){
        ValueOperations<String, NoteCollection> operations = redisTemplate.opsForValue();
        String key = "Collection_" + noteCollection.getUserId() + '_' + noteCollection.getNoteId();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
        }
        operations.set(key, noteCollection, 5, TimeUnit.HOURS);
        noteCollectionDao.save(noteCollection);
    }

    // 收藏或取消收藏
    public void setCollection(int noteId, int userId){
        boolean isExist = noteCollectionDao.existsByNoteIdAndUserId(noteId,userId);
        if(!isExist){
            NoteCollection noteCollection = new NoteCollection();
            noteCollection.setNoteId(noteId);
            noteCollection.setUserId(userId);
            noteCollection.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            noteCollection.setIsCollection(1);
            saveNoteCollection(noteCollection);
        }else {
            NoteCollection noteCollection = noteCollectionDao.findByNoteIdAndUserId(noteId, userId);
            if(noteCollection.getIsCollection() == 1){
                noteCollection.setIsCollection(0);
            }else {
                noteCollection.setIsCollection(1);
            }
            saveNoteCollection(noteCollection);
        }
    }

    // 查看用户的所有收藏笔记
    public List<Note> getUserCollectionNote(int userId){
        List<Note> notes = new ArrayList<>();
        List<NoteCollection> noteCollections = noteCollectionDao.findAllByUserId(userId, Sort.by(Sort.Direction.DESC,"collectionId"));
        int[] noteIds = new int[noteCollections.size()];

        for (int i=0;i<noteCollections.size();i++){
            NoteCollection noteCollection=noteCollections.get(i);
            if(noteCollection.getIsCollection() == 1){
                noteIds[i]=noteCollection.getNoteId();
            }
        }

        for (int i=0;i<noteIds.length;i++){
            Note note = noteService.getNoteById(noteIds[i]);
            if(note != null){
                if (note.getIsPublic()==1 && note.getIsDelete()==0){
                    notes.add(note);
                }else if (note.getAuthor().getId()==userId && note.getIsDelete()==0){
                    notes.add(note);
                }
            }
        }
        return notes;
    }
}
