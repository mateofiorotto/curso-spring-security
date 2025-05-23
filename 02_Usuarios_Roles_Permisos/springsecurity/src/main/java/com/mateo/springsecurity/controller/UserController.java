package com.mateo.springsecurity.controller;

import com.mateo.springsecurity.model.Role;
import com.mateo.springsecurity.model.UserSec;
import com.mateo.springsecurity.service.IRoleService;
import com.mateo.springsecurity.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("denyAll()")
public class UserController {

    private final IUserService userService;
    private final IRoleService roleService;

    public UserController(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<UserSec>> getAllUsers(){
        List<UserSec> users = userService.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserSec> getPremissionById(@PathVariable Long id){
        Optional<UserSec> user = userService.findById(id);

        //map se usa para transformar del valor obtenido en el parentesis a un optional si lo hubiese
        //orElseGet permite obtener un valor predeterminado si el objeto esta vacio
        //si tiene valor, devolver con response entity ok, sino un 404 (notFound)
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE')")
    public ResponseEntity<UserSec> createUser(@RequestBody UserSec user){
        //Lista de permisos
        Set<Role> roleList = new HashSet<>();
        Role readRole;

        //Encriptar PW
        user.setPassword(userService.encriptPassword(user.getPassword()));

        // Recuperar la Role/s por su ID
        for (Role per : user.getRolesList()) {
            //si encuentra los permisos guarda, sino devolve null
            readRole = roleService.findById(per.getId()).orElse(null);
            if (readRole != null) {
                //si encuentro, guardo en la lista
                roleList.add(readRole);
            }
        }

        if (!roleList.isEmpty()){
            user.setRolesList(roleList);
            UserSec newUser = userService.save(user);

            return ResponseEntity.ok(newUser);
        }
        return null;
    }
}
