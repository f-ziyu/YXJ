package xyz.ziyublog.yxj.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "yxj_note_follow_comment")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class NoteFollowComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
    @Column(name = "follow_comment_id")
    private int followCommentId;

    @Column(name = "comment_id")
    private int commentId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "follow_comment_body")
    private String followCommentBody;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "is_delete")
    private Integer isDelete;

}

