package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.List;

public interface NoteDao extends JpaRepository<Note, Integer> {
    // 查看用户和类别下的笔记
    List<Note> findAllByAuthorAndNoteType(User user, NoteType noteType);
    // 查看某个用户的笔记
    List<Note> findAllByAuthor(User user);
    // 通过笔记id查看笔记
    Note findById(int noteId);

}
