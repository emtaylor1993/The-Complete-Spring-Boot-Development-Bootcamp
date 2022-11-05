package com.ltp.authentication.service;

import com.ltp.authentication.entity.User;

public interface UserService {
    User getUser(Long id);
    User getUser(String username);
    User saveUser(User user);
    User updatePassword(String password, Long id);
    User updatePassword(String password, String username);
}
