import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class transac extends JFrame {
    private Connection conexion;
    private JTable table;
    private DefaultTableModel tableModel;

    public transac() {
        setTitle("Banco Transacciones");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnVerCuentas = new JButton("Mostrar todas las cuentas");
        JButton btnEstadoCuenta = new JButton("Ver estado de cuenta");
        JButton btnTransaccion = new JButton("Hacer transaccion");
        JButton btnReset = new JButton("Resetear saldos");

        tableModel = new DefaultTableModel(new Object[]{"ID", "Titular", "Saldo (S/.)"}, 0);
        table = new JTable(tableModel);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 4));
        panel.add(btnVerCuentas);
        panel.add(btnEstadoCuenta);
        panel.add(btnTransaccion);
        panel.add(btnReset);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        conectar();

        btnVerCuentas.addActionListener(e -> mostrarTodasLasCuentas());
        btnEstadoCuenta.addActionListener(e -> mostrarEstadoCuenta());
        btnTransaccion.addActionListener(e -> hacerTransaccion());
        btnReset.addActionListener(e -> resetearCuentas());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cerrarConexion();
            }
        });
    }

    private void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/banco_transacciones", "root", "root");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos:\n" + e.getMessage());
            System.exit(1);
        }
    }

    private void mostrarTodasLasCuentas() {
        tableModel.setRowCount(0);
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, titular, saldo FROM cuentas ORDER BY id")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("titular"),
                    String.format("%.2f", rs.getDouble("saldo"))
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar cuentas:\n" + e.getMessage());
        }
    }

    private void mostrarEstadoCuenta() {
        String id = JOptionPane.showInputDialog(this, "Ingrese el ID de la cuenta:");
        if (id == null) return;
        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT titular, saldo FROM cuentas WHERE id = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this,
                        "Titular: " + rs.getString("titular") +
                        "\nSaldo disponible: S/. " + String.format("%.2f", rs.getDouble("saldo")));
                } else {
                    JOptionPane.showMessageDialog(this, "Cuenta no encontrada.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage());
        }
    }

    private void hacerTransaccion() {
        String origen = JOptionPane.showInputDialog(this, "ID de cuenta que envia el dinero:");
        if (origen == null) return;
        String destino = JOptionPane.showInputDialog(this, "ID de cuenta que recibe el dinero:");
        if (destino == null) return;
        String montoStr = JOptionPane.showInputDialog(this, "Monto a transferir:");
        if (montoStr == null) return;

        if (origen.equals(destino)) {
            JOptionPane.showMessageDialog(this, "No puedes transferir a la misma cuenta.");
            return;
        }

        try {
            double monto = Double.parseDouble(montoStr);
            conexion.setAutoCommit(false);

            // Validar origen
            double saldoOrigen = 0;
            try (PreparedStatement ps = conexion.prepareStatement(
                    "SELECT saldo FROM cuentas WHERE id = ?")) {
                ps.setInt(1, Integer.parseInt(origen));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        saldoOrigen = rs.getDouble("saldo");
                    } else {
                        JOptionPane.showMessageDialog(this, "Cuenta de origen no encontrada.");
                        conexion.rollback();
                        return;
                    }
                }
            }

            if (saldoOrigen < monto) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente.");
                conexion.rollback();
                return;
            }

            // Validar destino
            try (PreparedStatement ps = conexion.prepareStatement(
                    "SELECT id FROM cuentas WHERE id = ?")) {
                ps.setInt(1, Integer.parseInt(destino));
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Cuenta de destino no encontrada.");
                        conexion.rollback();
                        return;
                    }
                }
            }

            // Realizar transaccion
            try (PreparedStatement ps1 = conexion.prepareStatement(
                    "UPDATE cuentas SET saldo = saldo - ? WHERE id = ?");
                 PreparedStatement ps2 = conexion.prepareStatement(
                    "UPDATE cuentas SET saldo = saldo + ? WHERE id = ?")) {
                ps1.setDouble(1, monto);
                ps1.setInt(2, Integer.parseInt(origen));
                ps1.executeUpdate();

                ps2.setDouble(1, monto);
                ps2.setInt(2, Integer.parseInt(destino));
                ps2.executeUpdate();
            }

            conexion.commit();
            JOptionPane.showMessageDialog(this, "Transaccion realizada con exito.");
            mostrarTodasLasCuentas();
        } catch (Exception e) {
            try { conexion.rollback(); } catch (SQLException ex) {}
            JOptionPane.showMessageDialog(this, "Error en la transaccion:\n" + e.getMessage());
        } finally {
            try { conexion.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    private void resetearCuentas() {
        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(
                "UPDATE cuentas SET saldo = CASE id " +
                "WHEN 1 THEN 400.00 " +
                "WHEN 2 THEN 1100.00 " +
                "WHEN 3 THEN 600.00 " +
                "WHEN 4 THEN 900.00 END");
            JOptionPane.showMessageDialog(this, "Saldos reiniciados correctamente.");
            mostrarTodasLasCuentas();
        } catch (SQLException e) {
            try { conexion.rollback(); } catch (SQLException ex) {}
            JOptionPane.showMessageDialog(this, "Error al resetear:\n" + e.getMessage());
        }
    }

    private void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            // Ignorar
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new transac().setVisible(true));
    }
}
