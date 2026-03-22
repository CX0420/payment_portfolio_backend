package org.example.service;
import jakarta.annotation.Resource;
import org.example.dao.TIDDAO;
import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.example.model.TID;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TIDService {

    @Resource
    private TIDDAO tidDAO;
//
//    /**
//     * Process TID request using UniversalDTO
//     */
//    public ApiResponse<UniversalDTO> processTID(UniversalDTO request) {
//        try {
//            if (!"TID".equalsIgnoreCase(request.getServiceName())) {
//                return ApiResponse.badRequest("Invalid service type for TID");
//            }
//
//            // Validate required fields
//            if (request.getTid() == null || request.getMid() == null) {
//                return ApiResponse.badRequest("TID and MID values are required");
//            }
//
//            // Check if TID already exists
//            TID existing = tidDAO.findByTid(request.getTid());
//            if (existing != null) {
//                return ApiResponse.badRequest("TID already exists");
//            }
//
//            // Create TID from DTO
//            TID tid = UniversalMapper.fromUniversalDTO(request, new TID());
//
//            TID created = tidDAO.create(tid);
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(created, "TID");
//            return ApiResponse.success(response, "TID created successfully", 201);
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("TID processing failed: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Get TID by ID
//     */
//    public ApiResponse<UniversalDTO> getTID(Long id) {
//        try {
//            TID tid = tidDAO.findById(id);
//            if (tid == null) {
//                return ApiResponse.notFound("TID not found");
//            }
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(tid, "TID");
//            return ApiResponse.success(response, "TID retrieved successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to get TID: " + e.getMessage());
//        }
//    }
//
//    /**
//     * List all TIDs
//     */
//    public ApiResponse<List<UniversalDTO>> listTIDs() {
//        try {
//            List<TID> tids = tidDAO.findAll();
//            List<UniversalDTO> dtos = tids.stream()
//                .map(tid -> UniversalMapper.toUniversalDTO(tid, "TID"))
//                .collect(Collectors.toList());
//
//            return ApiResponse.success(dtos, "TIDs retrieved successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to list TIDs: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Update TID
//     */
//    public ApiResponse<UniversalDTO> updateTID(Long id, UniversalDTO request) {
//        try {
//            TID existingTID = tidDAO.findById(id);
//            if (existingTID == null) {
//                return ApiResponse.notFound("TID not found");
//            }
//
//            // Update TID from DTO
//            TID updatedTID = UniversalMapper.fromUniversalDTO(request, existingTID);
//            updatedTID.setId(id); // Ensure ID is preserved
//            updatedTID.setModifiedDateTime(LocalDateTime.now());
//
//            TID saved = tidDAO.update(updatedTID);
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(saved, "TID");
//            return ApiResponse.success(response, "TID updated successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to update TID: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Delete TID
//     */
//    public void deleteTID(Long id) {
//        tidDAO.delete(id);
//    }
//
//    // Legacy methods for backward compatibility
//    public TID create(TID tid) {
//        return tidDAO.create(tid);
//    }
//
//    public TID findById(Long id) {
//        return tidDAO.findById(id);
//    }
//
//    public TID findByTid(String tid) {
//        return tidDAO.findByTid(tid);
//    }
//
//    public List<TID> findByMobileUserId(String mobileUserId) {
//        return tidDAO.findByMobileUserId(mobileUserId);
//    }
//
//    public List<TID> findByMidId(Long midId) {
//        return tidDAO.findByMidId(midId);
//    }
//
//    public List<TID> findAll() {
//        return tidDAO.findAll();
//    }
//
//    public TID update(TID tid) {
//        return tidDAO.update(tid);
//    }
//
//    public boolean delete(Long id) {
//        return tidDAO.delete(id);
//    }
    public String validateAndGetTid(String tid) {
        if (tid == null) {
            throw new IllegalArgumentException("TID is required");
        }

        // Trim whitespace
        String trimmedTid = tid.trim();

        // Check if empty
        if (trimmedTid.isEmpty()) {
            throw new IllegalArgumentException("TID cannot be empty");
        }

        // Check if exactly 8 digits
        if (trimmedTid.length() != 8) {
            throw new IllegalArgumentException(
                    String.format("TID must be exactly 8 digits (current: %d digits)", trimmedTid.length())
            );
        }

        // Check if all characters are digits
        if (!trimmedTid.matches("\\d+")) {
            throw new IllegalArgumentException("TID must contain only digits 0-9");
        }

        return trimmedTid;
    }
}
