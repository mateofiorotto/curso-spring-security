package com.mateo.springsecurity.service;

import com.mateo.springsecurity.model.Permission;
import com.mateo.springsecurity.repository.IPermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
* Logica para Permission Service
* */
@Service
public class PermissionService implements IPermissionService {

    //DI
    private final IPermissionRepository permissionRepository;

    /*
    * Constructor para inyectar el repositorio
    * */
    public PermissionService(IPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission update(Permission permission, Long id) {
        Optional<Permission> permissionFinded = this.findById(id);

        if (permissionFinded.isPresent()) {
            Permission existingPermission = permissionFinded.get();

            existingPermission.setPermissionName(permission.getPermissionName());

            return this.save(existingPermission);
        } else {
            throw new RuntimeException("Permiso no encontrado con ID: " + id);
        }
    }
}
