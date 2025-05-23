package com.mateo.springsecurity.service;

import com.mateo.springsecurity.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    //metodos crud..
    public List<Permission> findAll();
    public Optional<Permission> findById(Long id);
    public Permission save(Permission permission);
    public void deleteById(Long id);
    public Permission update(Permission permission, Long id);
}
