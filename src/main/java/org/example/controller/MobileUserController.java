package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.dto.MobileUser;
import org.example.dto.Result;
import org.example.service.MobileUserService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/mobileUser")
public class MobileUserController {

    @Resource
    MobileUserService mobileUserService;

    @GetMapping(value = "/list")
    public List<MobileUser> getMobileUserList(HttpServletResponse response) throws SQLException {
        List<MobileUser> map = mobileUserService.getMobileUserList();
        return map;
    }
}