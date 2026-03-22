package org.example.dao;

import org.example.model.TID;
import java.util.List;

public interface TIDDAO {

    TID create(TID tid);

    TID findById(Long id);

    TID findByTid(String tid);

    List<TID> findByMobileUserId(String mobileUserId);

    List<TID> findByMidId(Long midId);

    List<TID> findAll();

    TID update(TID tid);

    boolean delete(Long id);

    boolean existsByTid(String tid);
}
