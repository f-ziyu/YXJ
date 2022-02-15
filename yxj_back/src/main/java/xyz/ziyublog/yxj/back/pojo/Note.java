package xyz.ziyublog.yxj.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "yxj_notes")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Note {
    @Id
    @Column(name = "note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "note_title")
    private String title;

    @Column(name = "note_describe")
    private String describe;

    @ManyToOne
    @JoinColumn(name = "note_author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "note_type")
    private NoteType noteType;

    @Column(name = "content_html")
    private String ContentHtml;

    @Column(name = "content_md")
    private String ContentMd;

    @Column(name = "created_time")
    private Timestamp CreatedTime;

    @Column(name = "last_modified_time")
    private Timestamp LastModifiedTime;

    @Column(name = "note_is_public")
    private boolean IsPublic;

    @Column(name = "note_is_delete")
    private boolean IsDelete;

}
