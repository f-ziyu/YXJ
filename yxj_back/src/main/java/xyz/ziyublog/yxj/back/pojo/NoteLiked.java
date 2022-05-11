package xyz.ziyublog.yxj.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "yxj_note_liked")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class NoteLiked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
    @Column(name = "liked_id")
    int likedId;

    @Column(name = "note_id")
    private int noteId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "is_liked")
    private Integer isDelete;

}

