package main.java.com.miempresa.jdbcapp.ui;

import main.java.com.miempresa.jdbcapp.dao.*;
import main.java.com.miempresa.jdbcapp.model.*;
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
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Insertar Departamento");
            System.out.println("2. Actualizar Departamento");
            System.out.println("3. Eliminar Departamento");
            System.out.println("4. Insertar Ingeniero");
            System.out.println("5. Actualizar Ingeniero");
            System.out.println("6. Eliminar Ingeniero");
            System.out.println("7. Insertar Proyecto");
            System.out.println("8. Actualizar Proyecto");
            System.out.println("9. Eliminar Proyecto");
            System.out.println("10. Insertar Asignación");
            System.out.println("11. Actualizar Asignación");
            System.out.println("12. Eliminar Asignación");
            System.out.println("13. Listar Proyectos por Departamento");
            System.out.println("14. Listar Ingenieros por Proyecto");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(sc.nextLine());
            switch (opcion) {
                case 1: insertarDepartamento(); break;
                case 2: actualizarDepartamento(); break;
                case 3: eliminarDepartamento(); break;
                case 4: insertarIngeniero(); break;
                case 5: actualizarIngeniero(); break;
                case 6: eliminarIngeniero(); break;
                case 7: insertarProyecto(); break;
                case 8: actualizarProyecto(); break;
                case 9: eliminarProyecto(); break;
                case 10: insertarAsignacion(); break;
                case 11: actualizarAsignacion(); break;
                case 12: eliminarAsignacion(); break;
                case 13: listarProyectosPorDpto(); break;
                case 14: listarIngenierosPorProyecto(); break;
            }
        } while (opcion != 0);
    }

    // --- Departamento ---
    private static void insertarDepartamento() throws Exception {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Teléfono: "); String tel = sc.nextLine();
        System.out.print("Fax: ");      String fax = sc.nextLine();
        Departamento d = new Departamento(0, nombre, tel, fax);
        if (dDao.insertar(d)) {
            System.out.println("→ Insertado con ID " + d.getId());
        }
    }
    private static void actualizarDepartamento() throws Exception {
        System.out.print("ID Departamento: "); int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo nombre: "); String nombre = sc.nextLine();
        System.out.print("Nuevo teléfono: "); String tel = sc.nextLine();
        System.out.print("Nuevo fax: "); String fax = sc.nextLine();
        Departamento d = new Departamento(id, nombre, tel, fax);
        if (dDao.actualizar(d)) {
            System.out.println("→ Actualizado correctamente");
        }
    }
    private static void eliminarDepartamento() throws Exception {
        System.out.print("ID Departamento: "); int id = Integer.parseInt(sc.nextLine());
        if (dDao.eliminar(id)) {
            System.out.println("→ Eliminado correctamente");
        }
    }

    // --- Ingeniero ---
    private static void insertarIngeniero() throws Exception {
        System.out.print("ID Departamento: "); int idDpto = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Apellido: "); String apellido = sc.nextLine();
        System.out.print("Especialidad: "); String especialidad = sc.nextLine();
        System.out.print("Cargo: "); String cargo = sc.nextLine();
        Ingeniero i = new Ingeniero(0, idDpto, nombre, apellido, especialidad, cargo);
        if (iDao.insertar(i)) {
            System.out.println("→ Insertado con ID " + i.getIdIng());
        }
    }
    private static void actualizarIngeniero() throws Exception {
        System.out.print("ID Ingeniero: "); int id = Integer.parseInt(sc.nextLine());
        System.out.print("ID Departamento: "); int idDpto = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Apellido: "); String apellido = sc.nextLine();
        System.out.print("Especialidad: "); String especialidad = sc.nextLine();
        System.out.print("Cargo: "); String cargo = sc.nextLine();
        Ingeniero i = new Ingeniero(id, idDpto, nombre, apellido, especialidad, cargo);
        if (iDao.actualizar(i)) {
            System.out.println("→ Actualizado correctamente");
        }
    }
    private static void eliminarIngeniero() throws Exception {
        System.out.print("ID Ingeniero: "); int id = Integer.parseInt(sc.nextLine());
        if (iDao.eliminar(id)) {
            System.out.println("→ Eliminado correctamente");
        }
    }

    // --- Proyecto ---
    private static void insertarProyecto() throws Exception {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Fecha inicio (YYYY-MM-DD): "); String fechaInicio = sc.nextLine();
        System.out.print("Fecha fin (YYYY-MM-DD, opcional): "); String fechaFin = sc.nextLine();
        java.time.LocalDate inicio = java.time.LocalDate.parse(fechaInicio);
        java.time.LocalDate fin = fechaFin.isEmpty() ? null : java.time.LocalDate.parse(fechaFin);
        Proyecto p = new Proyecto(0, nombre, inicio, fin);
        if (pDao.insertar(p)) {
            System.out.println("→ Insertado con ID " + p.getIdProy());
        }
    }
    private static void actualizarProyecto() throws Exception {
        System.out.print("ID Proyecto: "); int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Fecha inicio (YYYY-MM-DD): "); String fechaInicio = sc.nextLine();
        System.out.print("Fecha fin (YYYY-MM-DD, opcional): "); String fechaFin = sc.nextLine();
        java.time.LocalDate inicio = java.time.LocalDate.parse(fechaInicio);
        java.time.LocalDate fin = fechaFin.isEmpty() ? null : java.time.LocalDate.parse(fechaFin);
        Proyecto p = new Proyecto(id, nombre, inicio, fin);
        if (pDao.actualizar(p)) {
            System.out.println("→ Actualizado correctamente");
        }
    }
    private static void eliminarProyecto() throws Exception {
        System.out.print("ID Proyecto: "); int id = Integer.parseInt(sc.nextLine());
        if (pDao.eliminar(id)) {
            System.out.println("→ Eliminado correctamente");
        }
    }

    // --- Asignación ---
    private static void insertarAsignacion() throws Exception {
        System.out.print("ID Ingeniero: "); int idIng = Integer.parseInt(sc.nextLine());
        System.out.print("ID Proyecto: "); int idProy = Integer.parseInt(sc.nextLine());
        System.out.print("Rol en el Proyecto: "); String rol = sc.nextLine();
        Asignacion a = new Asignacion(0, idIng, idProy, rol);
        if (aDao.insertar(a)) {
            System.out.println("→ Asignación insertada con ID " + a.getIdAsignacion());
        }
    }
    private static void actualizarAsignacion() throws Exception {
        System.out.print("ID Asignación: "); int id = Integer.parseInt(sc.nextLine());
        System.out.print("ID Ingeniero: "); int idIng = Integer.parseInt(sc.nextLine());
        System.out.print("ID Proyecto: "); int idProy = Integer.parseInt(sc.nextLine());
        System.out.print("Rol en el Proyecto: "); String rol = sc.nextLine();
        Asignacion a = new Asignacion(id, idIng, idProy, rol);
        if (aDao.actualizar(a)) {
            System.out.println("→ Asignación actualizada correctamente");
        }
    }
    private static void eliminarAsignacion() throws Exception {
        System.out.print("ID Asignación: "); int id = Integer.parseInt(sc.nextLine());
        if (aDao.eliminar(id)) {
            System.out.println("→ Asignación eliminada correctamente");
        }
    }

    // --- Consultas requeridas ---
    private static void listarProyectosPorDpto() throws Exception {
        System.out.print("ID Departamento: ");
        int id = Integer.parseInt(sc.nextLine());
        List<Proyecto> lista = dDao.obtenerProyectosPorDepartamento(id);
        lista.forEach(System.out::println);
    }

    private static void listarIngenierosPorProyecto() throws Exception {
        System.out.print("ID Proyecto: ");
        int id = Integer.parseInt(sc.nextLine());
        List<Ingeniero> lista = iDao.obtenerIngenierosPorProyecto(id);
        lista.forEach(System.out::println);
    }
}