package com.miempresa.jdbcapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.miempresa.jdbcapp.model.Asignacion;
import com.miempresa.jdbcapp.util.DbConnection;

public class AsignacionDAO {
    public boolean insertar(Asignacion a) throws SQLException {
        String sql = "INSERT INTO Asignacion (IDIng, IDProy, Rol_Proyecto) VALUES (?,?,?)";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getIdIng());
            ps.setInt(2, a.getIdProy());
            ps.setString(3, a.getRolProyecto());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean actualizar(Asignacion a) throws SQLException {
        String sql = "UPDATE Asignacion SET Rol_Proyecto=? WHERE IDIng=? AND IDProy=?";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getRolProyecto());
            ps.setInt(2, a.getIdIng());
            ps.setInt(3, a.getIdProy());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean eliminar(int idIng, int idProy) throws SQLException {
        String sql = "DELETE FROM Asignacion WHERE IDIng=? AND IDProy=?";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idIng);
            ps.setInt(2, idProy);
            return ps.executeUpdate() > 0;
        }
    }
    public List<Asignacion> listarTodas() throws SQLException {
        String sql = "SELECT * FROM Asignacion";
        List<Asignacion> lista = new ArrayList<>();
        try (Connection c = DbConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Asignacion a = new Asignacion(
                    rs.getInt("IDIng"),
                    rs.getInt("IDProy"),
                    rs.getString("Rol_Proyecto")
                );
                lista.add(a);
            }
        }
        return lista;
    }
}
