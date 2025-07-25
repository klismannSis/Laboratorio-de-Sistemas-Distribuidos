package com.miempresa.jdbcapp.dao;

import com.miempresa.jdbcapp.model.Departamento;
import com.miempresa.jdbcapp.model.Proyecto;
import com.miempresa.jdbcapp.util.DbConnection;
import java.sql.*;
import java.util.*;

public class DepartamentoDAO {
    public boolean insertar(Departamento d) throws SQLException {
        String sql = "INSERT INTO Departamento (Nombre, Telefono, Fax) VALUES (?,?,?)";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, d.getNombre());
            p.setString(2, d.getTelefono());
            p.setString(3, d.getFax());
            int filas = p.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = p.getGeneratedKeys()) {
                    if (rs.next()) d.setId(rs.getInt(1));
                }
            }
            return filas > 0;
        }
    }

    public boolean actualizar(Departamento d) throws SQLException {
        String sql = "UPDATE Departamento SET Nombre=?, Telefono=?, Fax=? WHERE IDDpto=?";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, d.getNombre());
            p.setString(2, d.getTelefono());
            p.setString(3, d.getFax());
            p.setInt(4, d.getId());
            return p.executeUpdate() > 0;
        }
    }

    public static boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Departamento WHERE IDDpto=?";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }

    public List<Proyecto> obtenerProyectosPorDepartamento(int idDpto) throws SQLException {
        String sql = "SELECT DISTINCT p.* FROM Proyecto p " +
                    "JOIN Asignacion a ON p.IDProy = a.IDProy " +
                    "JOIN Ingeniero i ON a.IDIng = i.IDIng " +
                    "WHERE i.IDDpto = ?";
        List<Proyecto> lista = new ArrayList<>();
        try (Connection c = DbConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idDpto);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Proyecto pto = new Proyecto(
                      rs.getInt("IDProy"),
                      rs.getString("Nombre"),
                      rs.getDate("Fec_Inicio").toLocalDate(),
                      rs.getDate("Fec_Termino") != null
                        ? rs.getDate("Fec_Termino").toLocalDate() : null
                    );
                    lista.add(pto);
                }
            }
        }
        return lista;
    }

    public static List<Departamento> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM Departamento";
        List<Departamento> lista = new ArrayList<>();
        try (Connection c = DbConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                Departamento d = new Departamento(
                    rs.getInt("IDDpto"),
                    rs.getString("Nombre"),
                    rs.getString("Telefono"),
                    rs.getString("Fax")
                );
                lista.add(d);
            }
        }
        return lista;
    }

    public List<Departamento> listar() throws Exception {
        List<Departamento> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getConnection();
            ps = conn.prepareStatement("SELECT IDDpto, Nombre, Telefono, Fax FROM Departamento");
            rs = ps.executeQuery();
            while (rs.next()) {
                Departamento d = new Departamento(
                    rs.getInt("IDDpto"),
                    rs.getString("Nombre"),
                    rs.getString("Telefono"),
                    rs.getString("Fax")
                );
                lista.add(d);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return lista;
    }
}
