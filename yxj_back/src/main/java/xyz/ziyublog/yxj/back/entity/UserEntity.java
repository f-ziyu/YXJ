package xyz.ziyublog.yxj.back.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
@Data
public class UserEntity implements Serializable {
    private Long id;
    private String guid;
    private String name;
    private String age;
    private Date createTime;
}