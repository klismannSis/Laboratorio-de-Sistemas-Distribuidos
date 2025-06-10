package com.miempresa.jdbcapp.ui;

import com.miempresa.jdbcapp.dao.*;
import com.miempresa.jdbcapp.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class MainApp extends JFrame {
    private final DepartamentoDAO dDao = new DepartamentoDAO();
    private final IngenieroDAO iDao = new IngenieroDAO();
    private final ProyectoDAO pDao = new ProyectoDAO();
    private final AsignacionDAO aDao = new AsignacionDAO();

    public MainApp() {
        setTitle("Gestión de Empresa");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Departamentos", departamentoPanel());
        tabs.addTab("Ingenieros", ingenieroPanel());
        tabs.addTab("Proyectos", proyectoPanel());
        tabs.addTab("Asignaciones", asignacionPanel());
        tabs.addTab("Proyectos por Dpto", proyectosPorDptoPanel());
        tabs.addTab("Ingenieros por Proyecto", ingenierosPorProyectoPanel());

        add(tabs);
    }

    // --- Departamento Panel ---
    private JPanel departamentoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "Teléfono", "Fax"}, 0);
        JTable table = new JTable(model);
        refreshDepartamentos(model);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField id = new JTextField(), nombre = new JTextField(), tel = new JTextField(), fax = new JTextField();
        form.add(new JLabel("ID (para actualizar/eliminar):")); form.add(id);
        form.add(new JLabel("Nombre:")); form.add(nombre);
        form.add(new JLabel("Teléfono:")); form.add(tel);
        form.add(new JLabel("Fax:")); form.add(fax);

        JPanel btns = new JPanel();
        JButton insertar = new JButton("Insertar"), actualizar = new JButton("Actualizar"), eliminar = new JButton("Eliminar"), refrescar = new JButton("Refrescar");
        btns.add(insertar); btns.add(actualizar); btns.add(eliminar); btns.add(refrescar);

        insertar.addActionListener(e -> {
            try {
                Departamento d = new Departamento(0, nombre.getText(), tel.getText(), fax.getText());
                if (dDao.insertar(d)) {
                    JOptionPane.showMessageDialog(this, "Insertado con ID " + d.getId());
                    refreshDepartamentos(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        actualizar.addActionListener(e -> {
            try {
                Departamento d = new Departamento(Integer.parseInt(id.getText()), nombre.getText(), tel.getText(), fax.getText());
                if (dDao.actualizar(d)) {
                    JOptionPane.showMessageDialog(this, "Actualizado correctamente");
                    refreshDepartamentos(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        eliminar.addActionListener(e -> {
            try {
                if (DepartamentoDAO.eliminar(Integer.parseInt(id.getText()))) {
                    JOptionPane.showMessageDialog(this, "Eliminado correctamente");
                    refreshDepartamentos(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        refrescar.addActionListener(e -> refreshDepartamentos(model));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(form, BorderLayout.NORTH);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshDepartamentos(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            for (Departamento d : dDao.listar()) {
                model.addRow(new Object[]{d.getId(), d.getNombre(), d.getTelefono(), d.getFax()});
            }
        } catch (Exception ex) { showError(ex); }
    }

    // --- Ingeniero Panel ---
    private JPanel ingenieroPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "ID Dpto", "Nombre", "Apellido", "Especialidad", "Cargo"}, 0);
        JTable table = new JTable(model);
        refreshIngenieros(model);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField id = new JTextField(), idDpto = new JTextField(), nombre = new JTextField(), apellido = new JTextField(), especialidad = new JTextField(), cargo = new JTextField();
        form.add(new JLabel("ID (para actualizar/eliminar):")); form.add(id);
        form.add(new JLabel("ID Departamento:")); form.add(idDpto);
        form.add(new JLabel("Nombre:")); form.add(nombre);
        form.add(new JLabel("Apellido:")); form.add(apellido);
        form.add(new JLabel("Especialidad:")); form.add(especialidad);
        form.add(new JLabel("Cargo:")); form.add(cargo);

        JPanel btns = new JPanel();
        JButton insertar = new JButton("Insertar"), actualizar = new JButton("Actualizar"), eliminar = new JButton("Eliminar"), refrescar = new JButton("Refrescar");
        btns.add(insertar); btns.add(actualizar); btns.add(eliminar); btns.add(refrescar);

        insertar.addActionListener(e -> {
            try {
                Ingeniero i = new Ingeniero(0, Integer.parseInt(idDpto.getText()), nombre.getText(), apellido.getText(), especialidad.getText(), cargo.getText());
                if (iDao.insertar(i)) {
                    JOptionPane.showMessageDialog(this, "Insertado con ID " + i.getIdIng());
                    refreshIngenieros(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        actualizar.addActionListener(e -> {
            try {
                Ingeniero i = new Ingeniero(Integer.parseInt(id.getText()), Integer.parseInt(idDpto.getText()), nombre.getText(), apellido.getText(), especialidad.getText(), cargo.getText());
                if (iDao.actualizar(i)) {
                    JOptionPane.showMessageDialog(this, "Actualizado correctamente");
                    refreshIngenieros(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        eliminar.addActionListener(e -> {
            try {
                if (iDao.eliminar(Integer.parseInt(id.getText()))) {
                    JOptionPane.showMessageDialog(this, "Eliminado correctamente");
                    refreshIngenieros(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        refrescar.addActionListener(e -> refreshIngenieros(model));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(form, BorderLayout.NORTH);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshIngenieros(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            for (Ingeniero i : iDao.listar()) {
                model.addRow(new Object[]{i.getIdIng(), i.getIdDpto(), i.getNombre(), i.getApellido(), i.getEspecialidad(), i.getCargo()});
            }
        } catch (Exception ex) { showError(ex); }
    }

    // --- Proyecto Panel ---
    private JPanel proyectoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "Fecha Inicio", "Fecha Fin"}, 0);
        JTable table = new JTable(model);
        refreshProyectos(model);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField id = new JTextField(), nombre = new JTextField(), inicio = new JTextField(), fin = new JTextField();
        form.add(new JLabel("ID (para actualizar/eliminar):")); form.add(id);
        form.add(new JLabel("Nombre:")); form.add(nombre);
        form.add(new JLabel("Fecha inicio (YYYY-MM-DD):")); form.add(inicio);
        form.add(new JLabel("Fecha fin (YYYY-MM-DD, opcional):")); form.add(fin);

        JPanel btns = new JPanel();
        JButton insertar = new JButton("Insertar"), actualizar = new JButton("Actualizar"), eliminar = new JButton("Eliminar"), refrescar = new JButton("Refrescar");
        btns.add(insertar); btns.add(actualizar); btns.add(eliminar); btns.add(refrescar);

        insertar.addActionListener(e -> {
            try {
                LocalDate ini = LocalDate.parse(inicio.getText());
                LocalDate f = fin.getText().isEmpty() ? null : LocalDate.parse(fin.getText());
                Proyecto p = new Proyecto(0, nombre.getText(), ini, f);
                if (pDao.insertar(p)) {
                    JOptionPane.showMessageDialog(this, "Insertado con ID " + p.getIdProy());
                    refreshProyectos(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        actualizar.addActionListener(e -> {
            try {
                LocalDate ini = LocalDate.parse(inicio.getText());
                LocalDate f = fin.getText().isEmpty() ? null : LocalDate.parse(fin.getText());
                Proyecto p = new Proyecto(Integer.parseInt(id.getText()), nombre.getText(), ini, f);
                if (pDao.actualizar(p)) {
                    JOptionPane.showMessageDialog(this, "Actualizado correctamente");
                    refreshProyectos(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        eliminar.addActionListener(e -> {
            try {
                if (pDao.eliminar(Integer.parseInt(id.getText()))) {
                    JOptionPane.showMessageDialog(this, "Eliminado correctamente");
                    refreshProyectos(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        refrescar.addActionListener(e -> refreshProyectos(model));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(form, BorderLayout.NORTH);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshProyectos(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            for (Proyecto p : pDao.listar()) {
                model.addRow(new Object[]{p.getIdProy(), p.getNombre(), p.getFechaInicio(), p.getFechaFin()});
            }
        } catch (Exception ex) { showError(ex); }
    }

    // --- Asignacion Panel ---
    private JPanel asignacionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "ID Ingeniero", "ID Proyecto", "Rol"}, 0);
        JTable table = new JTable(model);
        refreshAsignaciones(model);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField id = new JTextField(), idIng = new JTextField(), idProy = new JTextField(), rol = new JTextField();
        form.add(new JLabel("ID (para actualizar/eliminar):")); form.add(id);
        form.add(new JLabel("ID Ingeniero:")); form.add(idIng);
        form.add(new JLabel("ID Proyecto:")); form.add(idProy);
        form.add(new JLabel("Rol en el Proyecto:")); form.add(rol);

        JPanel btns = new JPanel();
        JButton insertar = new JButton("Insertar"), actualizar = new JButton("Actualizar"), eliminar = new JButton("Eliminar"), refrescar = new JButton("Refrescar");
        btns.add(insertar); btns.add(actualizar); btns.add(eliminar); btns.add(refrescar);

        insertar.addActionListener(e -> {
            try {
                Asignacion a = new Asignacion(0, Integer.parseInt(idIng.getText()), Integer.parseInt(idProy.getText()), rol.getText());
                if (aDao.insertar(a)) {
                    JOptionPane.showMessageDialog(this, "Asignación insertada con ID " + a.getIdAsignacion());
                    refreshAsignaciones(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        actualizar.addActionListener(e -> {
            try {
                Asignacion a = new Asignacion(Integer.parseInt(id.getText()), Integer.parseInt(idIng.getText()), Integer.parseInt(idProy.getText()), rol.getText());
                if (aDao.actualizar(a)) {
                    JOptionPane.showMessageDialog(this, "Asignación actualizada correctamente");
                    refreshAsignaciones(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        eliminar.addActionListener(e -> {
            try {
                if (aDao.eliminar(Integer.parseInt(id.getText()))) {
                    JOptionPane.showMessageDialog(this, "Asignación eliminada correctamente");
                    refreshAsignaciones(model);
                }
            } catch (Exception ex) { showError(ex); }
        });
        refrescar.addActionListener(e -> refreshAsignaciones(model));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(form, BorderLayout.NORTH);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAsignaciones(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            for (Asignacion a : aDao.listar()) {
                model.addRow(new Object[]{a.getIdAsignacion(), a.getIdIng(), a.getIdProy(), a.getRolProyecto()});
            }
        } catch (Exception ex) { showError(ex); }
    }

    // --- Proyectos por Departamento Panel ---
    private JPanel proyectosPorDptoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "Fecha Inicio", "Fecha Fin", "Created At", "Updated At"}, 0);
        JTable table = new JTable(model);

        JPanel form = new JPanel();
        JTextField idDpto = new JTextField(5);
        JButton buscar = new JButton("Buscar");
        form.add(new JLabel("ID Departamento:"));
        form.add(idDpto);
        form.add(buscar);

        buscar.addActionListener(e -> {
            try {
                model.setRowCount(0);
                int id = Integer.parseInt(idDpto.getText());
                List<Proyecto> lista = dDao.obtenerProyectosPorDepartamento(id);
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay proyectos para este departamento.");
                    return;
                }
                for (Proyecto p : lista) {
                    model.addRow(new Object[]{
                        p.getIdProy(),
                        p.getNombre(),
                        p.getFechaInicio(),
                        (p.getFechaFin() != null ? p.getFechaFin() : "-"),
                        (p.getCreatedAt() != null ? p.getCreatedAt() : "-"),
                        (p.getUpdatedAt() != null ? p.getUpdatedAt() : "-")
                    });
                }
            } catch (Exception ex) { showError(ex); }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // --- Ingenieros por Proyecto Panel ---
    private JPanel ingenierosPorProyectoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "ID Dpto", "Nombre", "Apellido", "Especialidad", "Cargo"}, 0);
        JTable table = new JTable(model);

        JPanel form = new JPanel();
        JTextField idProy = new JTextField(5);
        JButton buscar = new JButton("Buscar");
        form.add(new JLabel("ID Proyecto:"));
        form.add(idProy);
        form.add(buscar);

        buscar.addActionListener(e -> {
            try {
                model.setRowCount(0);
                int id = Integer.parseInt(idProy.getText());
                List<Ingeniero> lista = iDao.obtenerIngenierosPorProyecto(id);
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay ingenieros asignados a este proyecto.");
                    return;
                }
                for (Ingeniero i : lista) {
                    model.addRow(new Object[]{
                        i.getIdIng(),
                        i.getIdDpto(),
                        i.getNombre(),
                        i.getApellido(),
                        i.getEspecialidad(),
                        i.getCargo()
                    });
                }
            } catch (Exception ex) { showError(ex); }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}