package com.mateo.springsecurity.service;

import com.mateo.springsecurity.model.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    //metodos crud..
    public List<UserSec> findAll();
    public Optional<UserSec> findById(Long id);
    public UserSec save(UserSec userSec);
    public void deleteById(Long id);
    public UserSec update(UserSec userSec, Long id);
    public String encriptPassword(String password);
}
