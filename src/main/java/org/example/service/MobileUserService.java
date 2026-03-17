package org.example.service;

import jakarta.annotation.Resource;
import org.example.dao.MobileUserDAO;
import org.example.dto.MobileUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileUserService{

    @Resource
    MobileUserDAO mobileUserDAO;

    public List<MobileUser> getMobileUserList(){
        return mobileUserDAO.getMobileUserList();
    }
}