package com.mateo.springsecurity.model;

//Utilizamos UserSec para evitar confusiones y mezclas con otras librerias

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users") //el nombre de la tabla sera en plural por convencion
public class UserSec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false) //El usuario será único
    private String username;
    private String password;
    //Exigencias de Spring security
    private boolean enabled; //Si esta disponible el usuario, si se da de baja se hace borrado LOGICO, no total
    private boolean accountNotExpired; //si la cuenta expiro o no
    private boolean accountNotLocked; //si la cuenta esta bloqueada o no
    private boolean credentialNotExpired; //si la credenciales expiraron o no

    //No permitimos repetidos porque con una vez que este el rol ya basta, entonces usamos SET (list si los permite).
    // Establecer relacion N:N
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //FECTH: Estrategia de carga de datos, Eager carga todos los roles
    //cascada, es que si hay relaciones con otras tablas borra los registros
    //join unidireccional -> user sabra que roles tiene, será tabla intermedia (usuarios - useuarios_roles - roles
    //joincolumn, especificamos la columna que representara a userSec en la tabla intermedia
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> rolesList = new HashSet<>();

    //Constructor

    public UserSec(){}

    public UserSec(Long id, String username, String password, boolean enabled, boolean accountNotExpired, boolean accountNotLocked, boolean credentialNotExpired, Set<Role> rolesList) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNotExpired = accountNotExpired;
        this.accountNotLocked = accountNotLocked;
        this.credentialNotExpired = credentialNotExpired;
        rolesList = rolesList;
    }

    //Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    public void setAccountNotExpired(boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
    }

    public boolean isAccountNotLocked() {
        return accountNotLocked;
    }

    public void setAccountNotLocked(boolean accountNotLocked) {
        this.accountNotLocked = accountNotLocked;
    }

    public boolean isCredentialNotExpired() {
        return credentialNotExpired;
    }

    public void setCredentialNotExpired(boolean credentialNotExpired) {
        this.credentialNotExpired = credentialNotExpired;
    }

    public Set<Role> getRolesList() {
        return rolesList;
    }

    public void setRolesList(Set<Role> rolesList) {
        this.rolesList = rolesList;
    }
}
