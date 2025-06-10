package com.miempresa.jdbcapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import com.miempresa.jdbcapp.model.Proyecto;
import com.miempresa.jdbcapp.util.DbConnection;

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

    public List<Proyecto> listar() throws Exception {
        List<Proyecto> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getConnection();
            ps = conn.prepareStatement("SELECT IDProy, Nombre, Fec_Inicio, Fec_Termino FROM Proyecto");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("IDProy");
                String nombre = rs.getString("Nombre");
                LocalDate fechaInicio = rs.getDate("Fec_Inicio").toLocalDate();
                Date fechaFinDate = rs.getDate("Fec_Termino");
                LocalDate fechaFin = (fechaFinDate != null) ? fechaFinDate.toLocalDate() : null;
                Proyecto p = new Proyecto(id, nombre, fechaInicio, fechaFin);
                lista.add(p);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return lista;
    }
}
