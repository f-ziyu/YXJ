package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.List;

public interface NoteDao extends JpaRepository<Note, Integer> {
    // 查看用户和类别下的笔记
    List<Note> findAllByAuthorAndNoteTypeAndIsDelete(User user, NoteType noteType,Integer isDelete);
    // 查看某个用户的笔记
    List<Note> findAllByAuthorAndIsDelete(User user,Integer isDelete);
    // 通过笔记id查看笔记
    Note findById(int noteId);
    // 通过笔记id删除笔记
    int deleteById(int noteId);

}
