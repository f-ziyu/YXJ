package xyz.ziyublog.yxj.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "yxj_note_type")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class NoteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
    @Column(name = "type_id")
    int id;

    @Column(name = "type_name")
    String name;

}

