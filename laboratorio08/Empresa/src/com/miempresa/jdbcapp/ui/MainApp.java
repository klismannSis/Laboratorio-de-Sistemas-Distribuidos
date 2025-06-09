package com.miempresa.jdbcapp.ui;

import com.miempresa.jdbcapp.dao.*;
import com.miempresa.jdbcapp.model.*;
import java.util.*;

public class MainApp {
    private static final Scanner sc = new Scanner(System.in);
    private static final DepartamentoDAO dDao = new DepartamentoDAO();
    private static final IngenieroDAO iDao = new IngenieroDAO();
    private static final ProyectoDAO pDao = new ProyectoDAO();
    private static final AsignacionDAO aDao = new AsignacionDAO();

    public static void main(String[] args) throws Exception {
        int opcion;
        do {
            System.out.println("1. Insertar Departamento");
            System.out.println("2. Listar Proyectos por Departamento");
            System.out.println("3. Listar Ingenieros por Proyecto");
            System.out.println("0. Salir");
            opcion = Integer.parseInt(sc.nextLine());
            switch (opcion) {
                case 1: insertarDepartamento(); break;
                case 2: listarProyectosPorDpto(); break;
                case 3: listarIngenierosPorProyecto(); break;
            }
        } while (opcion != 0);
    }

    private static void insertarDepartamento() throws Exception {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Teléfono: "); String tel = sc.nextLine();
        System.out.print("Fax: ");      String fax = sc.nextLine();
        Departamento d = new Departamento(0, nombre, tel, fax);
        if (dDao.insertar(d)) {
            System.out.println("→ Insertado con ID " + d.getId());
        }
    }

    private static void listarProyectosPorDpto() throws Exception {
        System.out.print("ID Departamento: ");
        int id = Integer.parseInt(sc.nextLine());
        List<Proyecto> lista = dDao.obtenerProyectosPorDepartamento(id);
        lista.forEach(p -> System.out.println(p));
    }

    private static void listarIngenierosPorProyecto() throws Exception {
        System.out.print("ID Proyecto: ");
        int id = Integer.parseInt(sc.nextLine());
        List<Ingeniero> lista = iDao.obtenerIngenierosPorProyecto(id);
        lista.forEach(i -> System.out.println(i));
    }
}
