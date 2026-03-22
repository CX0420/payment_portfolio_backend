//package org.example.controller;
//
//import jakarta.annotation.Resource;
//import jakarta.validation.Valid;
//import org.example.dto.Result;
//import org.example.dto.MIDRequest;
//import org.example.model.MID;
//import org.example.service.MIDService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api/v1/mids")
//public class MIDController {
//
//    @Resource
//    private MIDService midService;
//
//    @PostMapping
//    public Result<MID> create(@Valid @RequestBody MIDRequest request) {
//        try {
//            MID mid = new MID();
//            mid.setMid(request.getMid());
//            mid.setCardType(request.getCardType());
//
//            MID created = midService.create(mid);
//            return Result.success(created);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping
//    public Result<List<MID>> getAll() {
//        try {
//            List<MID> mids = midService.findAll();
//            return Result.success(mids);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/{id}")
//    public Result<MID> getById(@PathVariable Long id) {
//        try {
//            MID mid = midService.findById(id);
//            if (mid == null) {
//                return new Result<>(404, "MID not found", null);
//            }
//            return Result.success(mid);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/mid/{mid}")
//    public Result<MID> getByMid(@PathVariable String mid) {
//        try {
//            MID foundMid = midService.findByMid(mid);
//            if (foundMid == null) {
//                return new Result<>(404, "MID not found", null);
//            }
//            return Result.success(foundMid);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public Result<MID> update(
//            @PathVariable Long id,
//            @Valid @RequestBody MIDRequest request) {
//        try {
//            MID existing = midService.findById(id);
//            if (existing == null) {
//                return new Result<>(404, "MID not found", null);
//            }
//
//            existing.setMid(request.getMid());
//            existing.setCardType(request.getCardType());
//
//            MID updated = midService.update(existing);
//            return Result.success(updated);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public Result<Void> delete(@PathVariable Long id) {
//        try {
//            boolean deleted = midService.delete(id);
//            if (!deleted) {
//                return new Result<>(404, "MID not found", null);
//            }
//            return Result.success(null);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//}
