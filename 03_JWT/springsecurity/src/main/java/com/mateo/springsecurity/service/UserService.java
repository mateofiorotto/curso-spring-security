package com.mateo.springsecurity.service;

import com.mateo.springsecurity.model.UserSec;
import com.mateo.springsecurity.repository.IUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
* Logica para User Service
* */
@Service
public class UserService implements IUserService {

    //DI
    private final IUserRepository userRepository;

    /*
    * Constructor para inyectar el repositorio
    * */
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserSec> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserSec> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserSec save(UserSec user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserSec update(UserSec user, Long id) {
        Optional<UserSec> userFinded = this.findById(id);

        if (userFinded.isPresent()) {
            UserSec existingUser = userFinded.get();

            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setEnabled(user.isEnabled());
            existingUser.setAccountNotExpired(user.isAccountNotExpired());
            existingUser.setAccountNotLocked(user.isAccountNotLocked());
            existingUser.setCredentialNotExpired(user.isCredentialNotExpired());

            if (existingUser.getRolesList() != null) {
                existingUser.setRolesList(user.getRolesList());
            }

            return this.save(existingUser);
        } else {
            throw new RuntimeException("Permiso no encontrado con ID: " + id);
        }
    }

    @Override
    public String encriptPassword(String password) {
        //Creamos un nuevo bcrypt y le pasamos la contraseña para encriptar
        return new BCryptPasswordEncoder().encode(password);
    }
}
