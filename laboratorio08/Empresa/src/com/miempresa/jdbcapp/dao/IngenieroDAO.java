package com.miempresa.jdbcapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import com.miempresa.jdbcapp.model.Ingeniero;
import com.miempresa.jdbcapp.util.DbConnection;

public class IngenieroDAO {
    public boolean insertar(Ingeniero i) throws SQLException {
        String sql = "INSERT INTO Ingeniero (IDDpto, Nombre, Apellido, Especialidad, Cargo) VALUES (?,?,?,?,?)";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getIdDpto());
            ps.setString(2, i.getNombre());
            ps.setString(3, i.getApellido());
            ps.setString(4, i.getEspecialidad());
            ps.setString(5, i.getCargo());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) i.setIdIng(rs.getInt(1));
                }
            }
            return filas > 0;
        }
    }
    public boolean actualizar(Ingeniero i) throws SQLException {
        String sql = "UPDATE Ingeniero SET Nombre=?, Apellido=?, Especialidad=?, Cargo=?, IDDpto=? WHERE IDIng=?";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, i.getNombre());
            ps.setString(2, i.getApellido());
            ps.setString(3, i.getEspecialidad());
            ps.setString(4, i.getCargo());
            ps.setInt(5, i.getIdDpto());
            ps.setInt(6, i.getIdIng());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean eliminar(int idIng) throws SQLException {
        String sql = "DELETE FROM Ingeniero WHERE IDIng=?";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idIng);
            return ps.executeUpdate() > 0;
        }
    }
    public List<Ingeniero> obtenerIngenierosPorProyecto(int idProy) throws SQLException {
        String sql = "SELECT i.* FROM Ingeniero i " +
                     "JOIN Asignacion a ON i.IDIng = a.IDIng " +
                     "WHERE a.IDProy = ?";
        List<Ingeniero> lista = new ArrayList<>();
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idProy);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingeniero ing = new Ingeniero(
                        rs.getInt("IDIng"),
                        rs.getInt("IDDpto"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getString("Especialidad"),
                        rs.getString("Cargo")
                    );
                    lista.add(ing);
                }
            }
        }
        return lista;
    }
}
