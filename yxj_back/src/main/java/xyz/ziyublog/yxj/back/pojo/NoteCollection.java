package xyz.ziyublog.yxj.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "yxj_note_collection")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class NoteCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
    @Column(name = "collection_id")
    int collectionId;

    @Column(name = "note_id")
    private int noteId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "is_collection")
    private Integer isCollection;

}

