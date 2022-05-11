package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.NoteComment;
import xyz.ziyublog.yxj.back.pojo.NoteFollowComment;

import java.util.List;

public interface NoteFollowCommentDao extends JpaRepository<NoteFollowComment, Integer> {

    // 查看某个评论的全部跟评
    List<NoteFollowComment> findAllByCommentIdAndAndIsDelete(int commentId, int isDelete);

}
