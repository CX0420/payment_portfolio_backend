//package org.example.controller;
//
//import jakarta.annotation.Resource;
//import jakarta.validation.Valid;
//import org.example.dto.Result;
//import org.example.dto.TIDRequest;
//import org.example.model.TID;
//import org.example.model.MID;
//import org.example.model.MobileUser;
//import org.example.service.TIDService;
//import org.example.service.MIDService;
//import org.example.dao.LoginDAO;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api/v1/tids")
//public class TIDController {
//
//    @Resource
//    private TIDService tidService;
//
//    @Resource
//    private MIDService midService;
//
//    @Resource
//    private LoginDAO loginDAO;
//
//    @PostMapping
//    public Result<TID> create(@Valid @RequestBody TIDRequest request) {
//        try {
//            // Verify MobileUser exists
//            MobileUser mobileUser = loginDAO.findByIdentifier(request.getMobileUserId());
//            if (mobileUser == null) {
//                return new Result<>(404, "Mobile User not found", null);
//            }
//
//            // Verify MID exists
//            MID mid = midService.findById(request.getMidId());
//            if (mid == null) {
//                return new Result<>(404, "MID not found", null);
//            }
//
//            TID tid = new TID();
//            tid.setTid(request.getTid());
//            tid.setMobileUser(mobileUser);
//            tid.setMid(mid);
//
//            TID created = tidService.create(tid);
//            return Result.success(created);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping
//    public Result<List<TID>> getAll() {
//        try {
//            List<TID> tids = tidService.findAll();
//            return Result.success(tids);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/{id}")
//    public Result<TID> getById(@PathVariable Long id) {
//        try {
//            TID tid = tidService.findById(id);
//            if (tid == null) {
//                return new Result<>(404, "TID not found", null);
//            }
//            return Result.success(tid);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/tid/{tid}")
//    public Result<TID> getByTid(@PathVariable String tid) {
//        try {
//            TID foundTid = tidService.findByTid(tid);
//            if (foundTid == null) {
//                return new Result<>(404, "TID not found", null);
//            }
//            return Result.success(foundTid);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/user/{mobileUserId}")
//    public Result<List<TID>> getByMobileUserId(@PathVariable String mobileUserId) {
//        try {
//            List<TID> tids = tidService.findByMobileUserId(mobileUserId);
//            return Result.success(tids);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/mid/{midId}")
//    public Result<List<TID>> getByMidId(@PathVariable Long midId) {
//        try {
//            List<TID> tids = tidService.findByMidId(midId);
//            return Result.success(tids);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public Result<TID> update(
//            @PathVariable Long id,
//            @Valid @RequestBody TIDRequest request) {
//        try {
//            TID existing = tidService.findById(id);
//            if (existing == null) {
//                return new Result<>(404, "TID not found", null);
//            }
//
//            // Verify MobileUser exists
//            MobileUser mobileUser = loginDAO.findByIdentifier(request.getMobileUserId());
//            if (mobileUser == null) {
//                return new Result<>(404, "Mobile User not found", null);
//            }
//
//            // Verify MID exists
//            MID mid = midService.findById(request.getMidId());
//            if (mid == null) {
//                return new Result<>(404, "MID not found", null);
//            }
//
//            existing.setTid(request.getTid());
//            existing.setMobileUser(mobileUser);
//            existing.setMid(mid);
//
//            TID updated = tidService.update(existing);
//            return Result.success(updated);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public Result<Void> delete(@PathVariable Long id) {
//        try {
//            boolean deleted = tidService.delete(id);
//            if (!deleted) {
//                return new Result<>(404, "TID not found", null);
//            }
//            return Result.success(null);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//}
