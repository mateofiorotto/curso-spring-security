package com.mateo.ej01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class NotasController {
//    Ejercicio Nº 1: Notas escolares
//    En una escuela primaria se necesita el desarrollo de una aplicación en la que los padres, luego de loguearse mediante una pantalla de login, puedan acceder a dos endpoints distintos para obtener las calificaciones de sus hijos en el último trimestre:
//
//    Método: GET, Path: /califications: Un endpoint que devuelve 8 calificaciones de 8 materias de un alumno. Por ejemplo: 8, 7, 6, 10, 9, 5, 7, 9
//    Método: GET, Path: /califications/average: Une endpoint que devuelve el promedio de esas 8 calificaciones.
//    Lo ideal sería que cada padre tenga su propio usuario de ingreso y que pueda visualizar las notas, pero como aún no se establecieron los roles y permisos para cada uno de ellos, la institución necesita una DEMO mediante un usuario y contraseña por defecto para ver como funcionaría a futuro cuando se implementen los detalles mencionados anteriormente.
//
//    Desarrollar una API que se encuentre securizada con Spring Security que permita acceder a estos endpoints SI Y SOLO SI hay un usuario autenticado (que haya iniciado sesión).

    //Bienvenida = publica
    @GetMapping("/")
    public String bienvenida(){
        return "Hola usuario, accede a <a href='/notas'>Notas</a> o <a href='/notas/promedio'>Promedio</a> ";
    }

    private final int[] notas = {10,9,8,7,7,7,9,8};

    @GetMapping("/notas")
    public int[] notasAlumno(){
        return notas;
    }

    @GetMapping("/notas/promedio")
    public double promedio(){
        double sum = 0;

        for (double grado : notas){
            sum += grado;
        }

        double promedio = sum / notas.length;

        return promedio;
    }
}
