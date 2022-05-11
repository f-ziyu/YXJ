package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.NoteCollection;

import java.util.List;

public interface NoteCollectionDao extends JpaRepository<NoteCollection, Integer> {
    NoteCollection findByNoteIdAndUserId(int noteId, int userId);
    Boolean existsByNoteIdAndUserId(int noteId, int userId);
    List<NoteCollection> findAllByUserId(int userId, Sort sort);
}
