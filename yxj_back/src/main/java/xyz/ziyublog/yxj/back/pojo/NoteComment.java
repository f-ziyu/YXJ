package xyz.ziyublog.yxj.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "yxj_note_comment")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class NoteComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
    @Column(name = "comment_id")
    int commentId;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "comment_body")
    private String commentBody;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "is_delete")
    private Integer isDelete;

}

