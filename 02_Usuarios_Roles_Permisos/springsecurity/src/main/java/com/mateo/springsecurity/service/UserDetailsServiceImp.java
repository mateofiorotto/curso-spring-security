package com.mateo.springsecurity.service;

import com.mateo.springsecurity.model.UserSec;
import com.mateo.springsecurity.repository.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final IUserRepository userRepository;

    public UserDetailsServiceImp(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
            PRIMER PASO
            tenemos User sec y necesitamos devolver UserDetails
            traemos el usuario de la bd con findUserEntityByUsername (el metodo personalizado de JPA)
        */

        UserSec userSec = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + "no fue encontrado"));

        /*
         * SEGUNDO PASO
         * Creamos una lista auxiliar donde guardamos los permisos
         * SimpleGrantedAuthority --> clase usada por SS para manejar permisos
         * */
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        /*
        * PASO 3
        * tomamos roles y los convertimos en SimpleGrantedAuthority para poder agregarlos a la authorityList
        * */
        userSec.getRolesList()
                //establecemos que estamos guardando un rol y no un permiso
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        /*
         * PASO 4
         * AGREGAR PERMISOS
         * usaremos STREAM que son secuencias de elementos precargados para array, colecciones, etc.
         * Transforma LISTAS a streams para usar algunos metodos de streams
         */

        userSec.getRolesList().stream() //lista de roles a stream para usar flatmap
                .flatMap(role -> role.getPermissionsList().stream()) //funcion lambda que trae desde los roles la lista de permisos y la convertimos a stream
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName()))); //por cada permiso encontrado agregalo al authority

        /*
        * PASO 5
        * Retornar el usuario que necesita SS
        * Usaremos User de Spring security
        * */

        return new User(
                userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNotExpired(),
                userSec.isAccountNotLocked(),
                userSec.isCredentialNotExpired(),
                authorityList
        );
    }

}

