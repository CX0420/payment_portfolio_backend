//package org.example.controller;
//
//import jakarta.annotation.Resource;
//import jakarta.validation.Valid;
//import org.example.dto.Result;
//import org.example.dto.BatchRequest;
//import org.example.model.Batch;
//import org.example.model.TID;
//import org.example.service.BatchService;
//import org.example.service.TIDService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api/v1/batches")
//public class BatchController {
//
//    @Resource
//    private BatchService batchService;
//
//    @Resource
//    private TIDService tidService;
//
//    @PostMapping
//    public Result<Batch> create(@Valid @RequestBody BatchRequest request) {
//        try {
//            // Verify TID exists
//            TID tid = tidService.findById(request.getTidId());
//            if (tid == null) {
//                return new Result<>(404, "TID not found", null);
//            }
//
//            Batch batch = new Batch();
//            batch.setStatus(request.getStatus());
//            batch.setTid(tid);
//
//            Batch created = batchService.create(batch);
//            return Result.success(created);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping
//    public Result<List<Batch>> getAll() {
//        try {
//            List<Batch> batches = batchService.findAll();
//            return Result.success(batches);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/{id}")
//    public Result<Batch> getById(@PathVariable Long id) {
//        try {
//            Batch batch = batchService.findById(id);
//            if (batch == null) {
//                return new Result<>(404, "Batch not found", null);
//            }
//            return Result.success(batch);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/tid/{tidId}")
//    public Result<List<Batch>> getByTidId(@PathVariable Long tidId) {
//        try {
//            // Verify TID exists
//            TID tid = tidService.findById(tidId);
//            if (tid == null) {
//                return new Result<>(404, "TID not found", null);
//            }
//
//            List<Batch> batches = batchService.findByTidId(tidId);
//            return Result.success(batches);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public Result<Batch> update(
//            @PathVariable Long id,
//            @Valid @RequestBody BatchRequest request) {
//        try {
//            Batch existing = batchService.findById(id);
//            if (existing == null) {
//                return new Result<>(404, "Batch not found", null);
//            }
//
//            // Verify TID exists
//            TID tid = tidService.findById(request.getTidId());
//            if (tid == null) {
//                return new Result<>(404, "TID not found", null);
//            }
//
//            existing.setStatus(request.getStatus());
//            existing.setTid(tid);
//
//            Batch updated = batchService.update(existing);
//            return Result.success(updated);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public Result<Void> delete(@PathVariable Long id) {
//        try {
//            boolean deleted = batchService.delete(id);
//            if (!deleted) {
//                return new Result<>(404, "Batch not found", null);
//            }
//            return Result.success(null);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//}
