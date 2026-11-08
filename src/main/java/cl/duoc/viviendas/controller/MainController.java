package cl.duoc.viviendas.controller;

import cl.duoc.viviendas.dao.ViviendaDAO;
import cl.duoc.viviendas.model.*;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML private ComboBox<String> cboTipo;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtSuperficie;
    @FXML private TextField txtHabitaciones;
    @FXML private TextField txtPiso;

    @FXML private CheckBox chkGastoComun;
    @FXML private CheckBox chkEstacionamiento;
    @FXML private CheckBox chkPatio;

    @FXML private Label lblMensaje;

    @FXML private TableView<Vivienda> tblViviendas;

    @FXML private TableColumn<Vivienda,String> colTipo;
    @FXML private TableColumn<Vivienda,String> colCodigo;
    @FXML private TableColumn<Vivienda,Double> colSuperficie;
    @FXML private TableColumn<Vivienda,Integer> colHabitaciones;
    @FXML private TableColumn<Vivienda,Double> colCosto;

    private final ObservableList<Vivienda> viviendas =
            FXCollections.observableArrayList();

    private final ViviendaDAO dao =
            new ViviendaDAO();

    @FXML
    public void initialize() {

        cboTipo.getItems().addAll(
                "Departamento",
                "Casa"
        );

        colTipo.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue().getTipo()
                ));

        colCodigo.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue().getCodigo()
                ));

        colSuperficie.setCellValueFactory(
                dato -> new SimpleObjectProperty<>(
                        dato.getValue().getSuperficie()
                ));

        colHabitaciones.setCellValueFactory(
                dato -> new SimpleObjectProperty<>(
                        dato.getValue().getHabitaciones()
                ));

        colCosto.setCellValueFactory(
                dato -> new SimpleObjectProperty<>(
                        dato.getValue()
                                .calcularCostoArriendo()
                ));

        viviendas.addAll(
                dao.cargarTodo()
        );

        tblViviendas.setItems(viviendas);

        cboTipo.setOnAction(
                e -> cambiarTipo()
        );
    }

    private void cambiarTipo() {

        boolean depto =
                "Departamento".equals(
                        cboTipo.getValue()
                );

        txtPiso.setDisable(!depto);
        chkGastoComun.setDisable(!depto);
        chkEstacionamiento.setDisable(!depto);

        chkPatio.setDisable(depto);
    }

    @FXML
    private void registrar() {

        try {

            String codigo =
                    txtCodigo.getText();

            double superficie =
                    Double.parseDouble(
                            txtSuperficie.getText()
                    );

            int habitaciones =
                    Integer.parseInt(
                            txtHabitaciones.getText()
                    );

            Vivienda vivienda;

            if("Departamento".equals(cboTipo.getValue())) {

                int piso =
                        Integer.parseInt(
                                txtPiso.getText()
                        );

                Departamento d =
                        new Departamento(
                                codigo,
                                superficie,
                                habitaciones,
                                piso,
                                chkGastoComun.isSelected()
                        );

                if(chkEstacionamiento.isSelected()) {
                    d.asignarEstacionamiento();
                }

                vivienda = d;

            } else {

                vivienda =
                        new Casa(
                                codigo,
                                superficie,
                                habitaciones,
                                chkPatio.isSelected()
                        );
            }

            viviendas.add(vivienda);

            dao.guardar(viviendas);

            lblMensaje.setText(
                    codigo +
                            " registrada correctamente."
            );

            limpiar();

        } catch(Exception ex) {

            lblMensaje.setText(
                    ex.getMessage()
            );
        }
    }

    @FXML
    private void eliminar() {

        Vivienda seleccionada =
                tblViviendas
                        .getSelectionModel()
                        .getSelectedItem();

        if(seleccionada == null) {

            lblMensaje.setText(
                    "Debe seleccionar una vivienda."
            );
            return;
        }

        viviendas.remove(seleccionada);

        dao.guardar(viviendas);

        lblMensaje.setText(
                "Vivienda eliminada."
        );
    }

    private void limpiar() {

        txtCodigo.clear();
        txtSuperficie.clear();
        txtHabitaciones.clear();
        txtPiso.clear();

        chkPatio.setSelected(false);
        chkGastoComun.setSelected(false);
        chkEstacionamiento.setSelected(false);
    }
}