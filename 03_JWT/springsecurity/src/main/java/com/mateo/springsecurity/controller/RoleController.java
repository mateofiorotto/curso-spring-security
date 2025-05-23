package com.mateo.springsecurity.controller;

import com.mateo.springsecurity.model.Permission;
import com.mateo.springsecurity.model.Role;
import com.mateo.springsecurity.service.IPermissionService;
import com.mateo.springsecurity.service.IRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("denyAll()")
public class RoleController {

    private final IRoleService roleService;
    private final IPermissionService permissionService;

    public RoleController(IRoleService roleService, IPermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roles = roleService.findAll();

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Role> getPremissionById(@PathVariable Long id){
        Optional<Role> role = roleService.findById(id);

        //map se usa para transformar del valor obtenido en el parentesis a un optional si lo hubiese
        //orElseGet permite obtener un valor predeterminado si el objeto esta vacio
        //si tiene valor, devolver con response entity ok, sino un 404 (notFound)
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Crear
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE')")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        //Lista de permisos
        Set<Permission> permissionList = new HashSet<>();
        Permission readPermission;

        // Recuperar la Permission/s por su ID
        for (Permission per : role.getPermissionsList()) {
            //si encuentra los permisos guarda, sino devolve null
            readPermission = permissionService.findById(per.getId()).orElse(null);
            if (readPermission != null) {
                //si encuentro, guardo en la lista
                permissionList.add(readPermission);
            }
        }

        role.setPermissionsList(permissionList);
        Role newRole = roleService.save(role);

        return ResponseEntity.ok(newRole);
    }

    //Actualizar
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> updateRole(@RequestBody Role role, @PathVariable Long id){
        Role updatedRole = roleService.update(role, id);

        return ResponseEntity.ok(updatedRole);
    }
}
