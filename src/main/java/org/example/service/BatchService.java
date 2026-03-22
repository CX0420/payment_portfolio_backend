package org.example.service;

import jakarta.annotation.Resource;
import org.example.dao.BatchDAO;
import org.example.dao.TIDDAO;
import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.example.model.Batch;
import org.example.model.TID;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchService {

    @Resource
    private BatchDAO batchDAO;

    @Resource
    private TIDDAO tidDAO;

    /**
     * Create a new batch
     */
    public ApiResponse<UniversalDTO> createBatch(UniversalDTO request) {
        try {
            // Validate required fields
            if (request.getTid() == null) {
                return ApiResponse.badRequest("TID is required");
            }

            // Find TID
            TID tid = tidDAO.findByTid(request.getTid());

            if (tid == null) {
                return ApiResponse.badRequest("TID not found: " + request.getTid());
            }

            // Create batch
            Batch batch = new Batch();
            batch.setTid(tid);
            batch.setStatus("PENDING");
            batch.setCreatedBy(request.getMobileUserId() != null ? request.getMobileUserId() : "SYSTEM");

            Batch created = batchDAO.create(batch);

            // Return response with created batch info
            request.setTid(created.getTid().getTid());

            return ApiResponse.success(request, "Batch created successfully", 201);

        } catch (Exception e) {
            return ApiResponse.internalServerError("Batch creation failed: " + e.getMessage());
        }
    }

    /**
     * Get batch by ID
     */
    public ApiResponse<UniversalDTO> getBatch(Long id) {
        try {
            Batch batch = batchDAO.findById(id);
            if (batch == null) {
                return ApiResponse.notFound("Batch not found");
            }

            UniversalDTO response = new UniversalDTO();
            if (batch.getTid() != null) {
                response.setTid(batch.getTid().getTid());
            }

            return ApiResponse.success(response, "Batch retrieved successfully");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to get batch: " + e.getMessage());
        }
    }

    /**
     * Get all batches by TID FK
     */
    public ApiResponse<List<UniversalDTO>> getBatchesByTidFk(Long tidFk) {
        try {
            if (tidFk == null) {
                return ApiResponse.badRequest("TID FK is required");
            }

            List<Batch> batches = batchDAO.findByTidFk(tidFk);

            if (batches.isEmpty()) {
                return ApiResponse.success(List.of(), "No batches found for this TID");
            }

            List<UniversalDTO> dtos = batches.stream()
                    .map(batch -> {
                        UniversalDTO dto = new UniversalDTO();
                        if (batch.getTid() != null) {
                            dto.setTid(batch.getTid().getTid());
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success(dtos, "Batches retrieved successfully");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to get batches: " + e.getMessage());
        }
    }

    /**
     * Get active batches by TID FK
     */
    public ApiResponse<List<UniversalDTO>> getActiveBatchesByTidFk(Long tidFk) {
        try {
            if (tidFk == null) {
                return ApiResponse.badRequest("TID FK is required");
            }

            List<Batch> batches = batchDAO.findActiveByTidFk(tidFk);

            List<UniversalDTO> dtos = batches.stream()
                    .map(batch -> {
                        UniversalDTO dto = new UniversalDTO();
                        if (batch.getTid() != null) {
                            dto.setTid(batch.getTid().getTid());
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success(dtos, "Active batches retrieved successfully");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to get active batches: " + e.getMessage());
        }
    }

    /**
     * Get batches by TID FK and status
     */
    public ApiResponse<List<UniversalDTO>> getBatchesByTidFkAndStatus(Long tidFk, String status) {
        try {
            if (tidFk == null) {
                return ApiResponse.badRequest("TID FK is required");
            }

            if (status == null || status.isEmpty()) {
                return ApiResponse.badRequest("Status is required");
            }

            List<Batch> batches = batchDAO.findByTidFkAndStatus(tidFk, status);

            List<UniversalDTO> dtos = batches.stream()
                    .map(batch -> {
                        UniversalDTO dto = new UniversalDTO();
                        if (batch.getTid() != null) {
                            dto.setTid(batch.getTid().getTid());
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success(dtos, "Batches retrieved successfully");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to get batches: " + e.getMessage());
        }
    }

    /**
     * Get latest batch by TID FK
     */
    public ApiResponse<UniversalDTO> getLatestBatchByTidFk(Long tidFk) {
        try {
            if (tidFk == null) {
                return ApiResponse.badRequest("TID FK is required");
            }

            Batch batch = batchDAO.findLatestByTidFk(tidFk);

            if (batch == null) {
                return ApiResponse.notFound("No batches found for this TID");
            }

            UniversalDTO response = new UniversalDTO();
            if (batch.getTid() != null) {
                response.setTid(batch.getTid().getTid());
            }

            return ApiResponse.success(response, "Latest batch retrieved successfully");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to get latest batch: " + e.getMessage());
        }
    }

    /**
     * Update batch status
     */
    public ApiResponse<UniversalDTO> updateBatchStatus(Long id, String status, String modifiedBy) {
        try {
            if (id == null) {
                return ApiResponse.badRequest("Batch ID is required");
            }

            if (status == null || status.isEmpty()) {
                return ApiResponse.badRequest("Status is required");
            }

            Batch existingBatch = batchDAO.findById(id);
            if (existingBatch == null) {
                return ApiResponse.notFound("Batch not found");
            }

            boolean updated = batchDAO.updateStatus(id, status, modifiedBy);

            if (updated) {
                Batch updatedBatch = batchDAO.findById(id);
                UniversalDTO response = new UniversalDTO();
                if (updatedBatch.getTid() != null) {
                    response.setTid(updatedBatch.getTid().getTid());
                }
                return ApiResponse.success(response, "Batch status updated successfully");
            } else {
                return ApiResponse.internalServerError("Failed to update batch status");
            }

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to update batch status: " + e.getMessage());
        }
    }

    /**
     * Update batch
     */
    public ApiResponse<UniversalDTO> updateBatch(Long id, UniversalDTO request) {
        try {
            Batch existingBatch = batchDAO.findById(id);
            if (existingBatch == null) {
                return ApiResponse.notFound("Batch not found");
            }
            existingBatch.setModifiedBy(request.getMobileUserId() != null ? request.getMobileUserId() : "SYSTEM");

            Batch updated = batchDAO.update(existingBatch);

            UniversalDTO response = new UniversalDTO();
            if (updated.getTid() != null) {
                response.setTid(updated.getTid().getTid());
            }

            return ApiResponse.success(response, "Batch updated successfully");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to update batch: " + e.getMessage());
        }
    }

    /**
     * Delete batch
     */
    public ApiResponse<Void> deleteBatch(Long id) {
        try {
            Batch batch = batchDAO.findById(id);
            if (batch == null) {
                return ApiResponse.notFound("Batch not found");
            }

            boolean deleted = batchDAO.delete(id);

            if (deleted) {
                return ApiResponse.success(null, "Batch deleted successfully");
            } else {
                return ApiResponse.internalServerError("Failed to delete batch");
            }

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to delete batch: " + e.getMessage());
        }
    }

    /**
     * Delete all batches by TID FK
     */
    public ApiResponse<Void> deleteBatchesByTidFk(Long tidFk) {
        try {
            if (tidFk == null) {
                return ApiResponse.badRequest("TID FK is required");
            }

            int deletedCount = batchDAO.deleteByTidFk(tidFk);
            return ApiResponse.success(null, deletedCount + " batch(es) deleted successfully");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to delete batches: " + e.getMessage());
        }
    }

    /**
     * Get batch statistics by TID FK
     */
    public ApiResponse<UniversalDTO> getBatchStatistics(Long tidFk) {
        try {
            if (tidFk == null) {
                return ApiResponse.badRequest("TID FK is required");
            }

            List<Batch> batches = batchDAO.findByTidFk(tidFk);
            TID tid = tidDAO.findById(tidFk);

            UniversalDTO stats = new UniversalDTO();
            if (tid != null) {
                stats.setTid(tid.getTid());
            }
            stats.addAttribute("totalBatches", batches.size());
            stats.addAttribute("activeBatches", batches.stream().filter(b -> "ACTIVE".equals(b.getStatus())).count());
            stats.addAttribute("completedBatches", batches.stream().filter(b -> "COMPLETED".equals(b.getStatus())).count());
            stats.addAttribute("failedBatches", batches.stream().filter(b -> "FAILED".equals(b.getStatus())).count());
            stats.addAttribute("pendingBatches", batches.stream().filter(b -> "PENDING".equals(b.getStatus())).count());

            return ApiResponse.success(stats, "Batch statistics retrieved successfully");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Failed to get batch statistics: " + e.getMessage());
        }
    }
}