package xyz.ziyublog.yxj.back.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.ziyublog.yxj.back.pojo.Note;
import xyz.ziyublog.yxj.back.pojo.NoteType;
import xyz.ziyublog.yxj.back.pojo.User;

import java.util.List;

public interface NoteDao extends JpaRepository<Note, Integer> {
    // 查看用户的某个类别下的笔记
    List<Note> findAllByAuthorAndNoteTypeAndIsDelete(User user, NoteType noteType,Integer isDelete, Sort sort);
    int countByAuthorAndNoteTypeAndIsDelete(User user, NoteType noteType,Integer isDelete);

    // 查看某个用户的笔记
    List<Note> findAllByAuthorAndIsDelete(User user,Integer isDelete, Sort sort);
    int countByAuthorAndIsDelete(User user,Integer isDelete);

    // 查看某个类别下公开的笔记
    List<Note> findAllByNoteTypeAndIsDeleteAndIsPublic(NoteType noteType,Integer isDelete,Integer isPublic, Sort sort);
    // 查看所有公开的笔记
    List<Note> findAllByIsDeleteAndIsPublic(Integer isDelete,Integer isPublic, Sort sort);
    // 通过笔记id查看笔记
    Note findById(int noteId);
    // 通过笔记id删除笔记
    void deleteById(int noteId);
    // 通过用户获得回收站笔记
    List<Note> findAllByAuthorAndIsDelete(int userId, int isDelete, Sort sort);
    Boolean existsById(int noteID);
    // 搜索笔记
    List<Note> findAllByTitleContaining(String searchParams, Sort sort);


    // 分页查询
    // 查看所有公开的笔记
    List<Note> getAllByIsDeleteAndIsPublic(Integer isDelete,Integer isPublic, Pageable pageable);
    // 查看用户的某个类别下的笔记
    List<Note> getAllByAuthorAndNoteTypeAndIsDelete(User user, NoteType noteType,Integer isDelete, Pageable pageable);
    // 查看某个用户的笔记
    List<Note> getAllByAuthorAndIsDelete(User user,Integer isDelete, Pageable pageable);
    // 查看所有公开的笔记个数
    int countByIsDeleteAndIsPublic(Integer isDelete,Integer isPublic);
}
