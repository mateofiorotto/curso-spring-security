package com.mateo.springsecurity.service;

import com.mateo.springsecurity.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    //metodos crud..
    public List<Role> findAll();
    public Optional<Role> findById(Long id);
    public Role save(Role role);
    public void deleteById(Long id);
    public Role update(Role role, Long id);
}
