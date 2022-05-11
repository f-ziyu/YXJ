package xyz.ziyublog.yxj.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.ziyublog.yxj.back.dao.NoteCommentDao;
import xyz.ziyublog.yxj.back.dao.NoteDao;
import xyz.ziyublog.yxj.back.dao.NoteFollowCommentDao;
import xyz.ziyublog.yxj.back.dao.UserDao;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteComment;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    NoteCommentDao noteCommentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    NoteDao noteDao;

    @Autowired
    NoteFollowCommentDao noteFollowCommentDao;

    @Autowired
    private RedisTemplate redisTemplate;
    // 添加笔记评论
    public void addNoteComment(NoteComment noteComment){
        noteCommentDao.save(noteComment);
    }
    // 删除评论
    public void deleteNoteComment(int commentId){
        noteCommentDao.deleteById(commentId);
    }

    // 获取某个笔记的所有评论
    public List<NoteComment> getNoteCommentByNoteId(int noteId){
        Note note = noteDao.findById(noteId);
        List<NoteComment> noteComments = noteCommentDao.findAllByNoteAndIsDelete(note,0, Sort.by(Sort.Direction.DESC,"commentId"));
        for (int i=0;i<noteComments.size();i++){
            NoteComment comment = noteComments.get(i);
            comment.setNote(new Note());
        }
        return noteComments;
    }

    // 获取某个用户的所有评论
    public List<NoteComment> getNoteCommentByUsername(String username){
        User user = userDao.findByUsername(username);
        List<NoteComment> noteComments = noteCommentDao.findAllByUserAndIsDelete(user,0, Sort.by(Sort.Direction.DESC,"commentId"));
        for (int i=0;i<noteComments.size();i++){
            Note note = noteComments.get(i).getNote();
            Note newNote = new Note();
            newNote.setTitle(note.getTitle());
            newNote.setDescribe(note.getDescribe());
            newNote.setId(note.getId());
            noteComments.get(i).setNote(newNote);
        }
        return noteComments;
    }


}
