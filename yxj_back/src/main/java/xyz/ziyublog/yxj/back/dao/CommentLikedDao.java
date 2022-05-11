package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.CommentLiked;

public interface CommentLikedDao extends JpaRepository<CommentLiked, Integer> {
    

}
