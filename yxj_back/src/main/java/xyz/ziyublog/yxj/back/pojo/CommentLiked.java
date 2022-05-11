package xyz.ziyublog.yxj.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "yxj_comment_liked")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class CommentLiked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
    @Column(name = "liked_id")
    int likedId;

    @Column(name = "comment_id")
    private int comment;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "create_time")
    private Timestamp createdTime;

    @Column(name = "is_liked")
    private Integer isDelete;

}

