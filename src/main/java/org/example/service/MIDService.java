package org.example.service;

import jakarta.annotation.Resource;
import org.example.dao.MIDDAO;
import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.example.model.MID;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MIDService {
//
//    @Resource
//    private MIDDAO midDAO;
//
//    /**
//     * Process MID request using UniversalDTO
//     */
//    public ApiResponse<UniversalDTO> processMID(UniversalDTO request) {
//        try {
//            if (!"MID".equalsIgnoreCase(request.getServiceName())) {
//                return ApiResponse.badRequest("Invalid service type for MID");
//            }
//
//            // Validate required fields
//            if (request.getMid() == null) {
//                return ApiResponse.badRequest("MID value is required");
//            }
//
//            // Check if MID already exists
//            MID existing = midDAO.findByMid(request.getMid());
//            if (existing != null) {
//                return ApiResponse.badRequest("MID already exists");
//            }
//
//            // Create MID from DTO
//            MID mid = UniversalMapper.fromUniversalDTO(request, new MID());
//
//            MID created = midDAO.create(mid);
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(created, "MID");
//            return ApiResponse.success(response, "MID created successfully", 201);
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("MID processing failed: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Get MID by ID
//     */
//    public ApiResponse<UniversalDTO> getMID(Long id) {
//        try {
//            MID mid = midDAO.findById(id);
//            if (mid == null) {
//                return ApiResponse.notFound("MID not found");
//            }
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(mid, "MID");
//            return ApiResponse.success(response, "MID retrieved successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to get MID: " + e.getMessage());
//        }
//    }
//
//    /**
//     * List all MIDs
//     */
//    public ApiResponse<List<UniversalDTO>> listMIDs() {
//        try {
//            List<MID> mids = midDAO.findAll();
//            List<UniversalDTO> dtos = mids.stream()
//                .map(mid -> UniversalMapper.toUniversalDTO(mid, "MID"))
//                .collect(Collectors.toList());
//
//            return ApiResponse.success(dtos, "MIDs retrieved successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to list MIDs: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Update MID
//     */
//    public ApiResponse<UniversalDTO> updateMID(Long id, UniversalDTO request) {
//        try {
//            MID existingMID = midDAO.findById(id);
//            if (existingMID == null) {
//                return ApiResponse.notFound("MID not found");
//            }
//
//            // Update MID from DTO
//            MID updatedMID = UniversalMapper.fromUniversalDTO(request, existingMID);
//            updatedMID.setId(id); // Ensure ID is preserved
//            updatedMID.setModifiedDateTime(LocalDateTime.now());
//
//            MID saved = midDAO.update(updatedMID);
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(saved, "MID");
//            return ApiResponse.success(response, "MID updated successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to update MID: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Delete MID
//     */
//    public void deleteMID(Long id) {
//        midDAO.delete(id);
//    }
//
//    // Legacy methods for backward compatibility
//    public MID create(MID mid) {
//        return midDAO.create(mid);
//    }
//
//    public MID findById(Long id) {
//        return midDAO.findById(id);
//    }
//
//    public MID findByMid(String mid) {
//        return midDAO.findByMid(mid);
//    }
//
//    public List<MID> findAll() {
//        return midDAO.findAll();
//    }
//
//    public MID update(MID mid) {
//        return midDAO.update(mid);
//    }
//
//    public boolean delete(Long id) {
//        return midDAO.delete(id);
//    }

    public String validateAndGetMid(String mid) {
        if (mid == null) {
            throw new IllegalArgumentException("MID is required");
        }

        // Trim whitespace
        String trimmedMid = mid.trim();

        // Check if empty
        if (trimmedMid.isEmpty()) {
            throw new IllegalArgumentException("MID cannot be empty");
        }

        // Check if exactly 15 characters
        if (trimmedMid.length() != 15) {
            throw new IllegalArgumentException(
                    String.format("MID must be exactly 15 digits, but got %d characters", trimmedMid.length())
            );
        }

        // Check if all characters are digits
        if (!trimmedMid.matches("\\d+")) {
            throw new IllegalArgumentException("MID must contain only digits (0-9)");
        }

        return trimmedMid;
    }
}
