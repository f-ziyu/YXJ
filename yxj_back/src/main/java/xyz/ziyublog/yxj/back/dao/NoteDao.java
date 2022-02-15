package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.List;

public interface NoteDao extends JpaRepository<Note, Integer> {
    List<Note> findAllByAuthorAndNoteType(User user, NoteType noteType);
}
