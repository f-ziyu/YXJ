package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteComment;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.List;

public interface NoteCommentDao extends JpaRepository<NoteComment, Integer> {
    // 查看某个笔记的全部评论
    List<NoteComment> findAllByNoteAndIsDelete(Note note, Integer isDelete, Sort sort);

    // 查看某个笔记的全部评论
    List<NoteComment> findAllByUserAndIsDelete(User user, Integer isDelete, Sort sort);

}
