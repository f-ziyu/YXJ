package xyz.ziyublog.yxj.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import xyz.ziyublog.yxj.back.dao.NoteTypeDao;
import xyz.ziyublog.yxj.back.pojo.NoteType;


import java.util.List;

@Service
public class NoteTypeService {
    @Autowired
    NoteTypeDao noteTypeDao;

    public List<NoteType> getAllNoteType(){
        noteTypeDao.findAll(Sort.by(Sort.Direction.DESC,"id"));
        return noteTypeDao.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    public NoteType getTypeById(int typeId){
        return noteTypeDao.getById(typeId);
    }



}
