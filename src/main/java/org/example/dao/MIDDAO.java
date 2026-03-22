package org.example.dao;

import org.example.model.MID;
import java.util.List;

public interface MIDDAO {

    MID create(MID mid);

    MID findById(Long id);

    MID findByMid(String mid);

    List<MID> findAll();

    MID update(MID mid);

    boolean delete(Long id);

    boolean existsByMid(String mid);
}
