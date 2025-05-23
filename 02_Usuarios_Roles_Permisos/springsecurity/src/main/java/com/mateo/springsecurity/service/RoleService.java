package com.mateo.springsecurity.service;

import com.mateo.springsecurity.model.Role;
import com.mateo.springsecurity.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
* Logica para Role Service
* */
@Service
public class RoleService implements IRoleService {

    //DI
    private final IRoleRepository roleRepository;

    /*
    * Constructor para inyectar el repositorio
    * */
    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role update(Role role, Long id) {
        Optional<Role> roleFinded = this.findById(id);

        if (roleFinded.isPresent()) {
            Role existingRole = roleFinded.get();

            existingRole.setRole(role.getRole());
            if (role.getPermissionsList() != null) {
                existingRole.setPermissionsList(role.getPermissionsList());
            }

            return this.save(existingRole);
        } else {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
    }
}
