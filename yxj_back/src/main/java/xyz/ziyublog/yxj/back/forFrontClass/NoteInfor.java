package xyz.ziyublog.yxj.back.forFrontClass;
import lombok.Data;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
public class NoteInfor {
    int id;
    private String title;
    private String describe;
    private int  authorId;
    private NoteType noteType;
    private String contentHtml;
    private String contentMd;
    private Date createdTime;
    private Date lastModifiedTime;
    private Boolean isPublic;

    public NoteInfor(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.describe = note.getDescribe();
        this.authorId = note.getAuthor().getId();
        this.noteType = note.getNoteType();
        this.contentHtml = note.getContentHtml();
        this.contentMd = note.getContentMd();
        //this.createdTime = note.getCreatedTime();
        //this.lastModifiedTime = note.getLastModifiedTime());
        this.isPublic = intToBool(note.getIsPublic());
    }




    public static Boolean intToBool(int isPublic){
        if(isPublic == 1){
            return true;
        }else {
            return false;
        }
    }
}
