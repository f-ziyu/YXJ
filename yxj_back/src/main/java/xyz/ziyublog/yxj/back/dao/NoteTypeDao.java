package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.NoteType;

public interface NoteTypeDao extends JpaRepository<NoteType, Integer> {
    NoteType findById(int typeID);
}
