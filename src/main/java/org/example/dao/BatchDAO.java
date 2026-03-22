package org.example.dao;

import org.example.model.Batch;
import java.util.List;

public interface BatchDAO {

    // Create
    Batch create(Batch batch);

    // Read
    Batch findById(Long id);
    List<Batch> findAll();
    List<Batch> findByTidFk(Long tidFk);
    List<Batch> findActiveByTidFk(Long tidFk);
    List<Batch> findByTidFkAndStatus(Long tidFk, String status);
    Batch findLatestByTidFk(Long tidFk);

    // Update
    Batch update(Batch batch);
    boolean updateStatus(Long id, String status, String modifiedBy);
    boolean updateStatusByTidFkAndBatchNumber(Long tidFk, String batchNumber, String status, String modifiedBy);

    // Delete
    boolean delete(Long id);
    int deleteByTidFk(Long tidFk);

    // Count & Check
    int countByTidFk(Long tidFk);
    boolean existsByTidFkAndStatus(Long tidFk, String status);
}