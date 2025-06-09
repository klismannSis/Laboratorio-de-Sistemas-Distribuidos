package main.java.com.miempresa.jdbcapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import main.java.com.miempresa.jdbcapp.model.Proyecto;
import main.java.com.miempresa.jdbcapp.util.DbConnection;

public class ProyectoDAO {
    public boolean insertar(Proyecto p) throws SQLException {
        String sql = "INSERT INTO Proyecto (Nombre, Fec_Inicio, Fec_Termino) VALUES (?,?,?)";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setDate(2, Date.valueOf(p.getFechaInicio()));
            ps.setDate(3, p.getFechaFin() != null ? Date.valueOf(p.getFechaFin()) : null);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) p.setIdProy(rs.getInt(1));
                }
            }
            return filas > 0;
        }
    }
    public boolean actualizar(Proyecto p) throws SQLException {
        String sql = "UPDATE Proyecto SET Nombre=?, Fec_Inicio=?, Fec_Termino=? WHERE IDProy=?";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDate(2, Date.valueOf(p.getFechaInicio()));
            ps.setDate(3, p.getFechaFin() != null ? Date.valueOf(p.getFechaFin()) : null);
            ps.setInt(4, p.getIdProy());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean eliminar(int idProy) throws SQLException {
        String sql = "DELETE FROM Proyecto WHERE IDProy=?";
        try (Connection c = DbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idProy);
            return ps.executeUpdate() > 0;
        }
    }
}
