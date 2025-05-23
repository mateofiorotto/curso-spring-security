package com.mateo.springsecurity.controller;

import com.mateo.springsecurity.model.Permission;
import com.mateo.springsecurity.service.IPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
@PreAuthorize("denyAll()")
public class PermissionController {

    private final IPermissionService permissionService;

    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Permission>> getAllPermissions(){
        List<Permission> permissions = permissionService.findAll();

        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Permission> getPremissionById(@PathVariable Long id){
        Optional<Permission> permission = permissionService.findById(id);

        //map se usa para transformar del valor obtenido en el parentesis a un optional si lo hubiese
        //orElseGet permite obtener un valor predeterminado si el objeto esta vacio
        //si tiene valor, devolver con response entity ok, sino un 404 (notFound)
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE')")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission){
        Permission newPermission = permissionService.save(permission);

        return ResponseEntity.ok(newPermission);
    }
}
