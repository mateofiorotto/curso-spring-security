package com.mateo.springsecurity.service;

import com.mateo.springsecurity.dto.AuthLoginRequestDTO;
import com.mateo.springsecurity.dto.AuthResponseDTO;
import com.mateo.springsecurity.model.UserSec;
import com.mateo.springsecurity.repository.IUserRepository;
import com.mateo.springsecurity.utils.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final IUserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImp(IUserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
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

    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequest){
        //recuperar username y pw
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        //Crear auth llamando al metodo
        Authentication authentication = this.authenticate(username, password);

        //si todo esta bien guardar el securitycontextholder para que no mande todos los datos de nuevo
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //crear token
        String accessToken = jwtUtils.createToken(authentication);

        //creamos el msg de rta
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, "Logueado correctamente", accessToken, true);

        return authResponseDTO;
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        //si no hay user
        if (userDetails == null){
            throw new BadCredentialsException("Datos de logueo erroneos"); //Nunca poner exactamente lo que esta mal (ej, contraseña erronea)
        }

        //si la pw no coincide
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Datos de logueo erroneos");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}

