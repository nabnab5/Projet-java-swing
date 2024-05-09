package com.nabnab.reservation;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class Utilisateurs implements Initializable {

    @FXML
    TextField txt_name;
    @FXML
    TextField txt_username;
    @FXML
    TextField txt_CIN;
    @FXML
    Button btn_delete;

    @FXML Label status;
    @FXML TableView<Ulisateur> gridUtilisateurs;
    @FXML private TableColumn<Ulisateur, String> cin ;
    @FXML private TableColumn<Ulisateur, String> name ;
    @FXML private TableColumn<Ulisateur, String> username ;

    @FXML private Button btn_update;
    ArrayList<Ulisateur> utilisateursList = new ArrayList<>();
    ConnectionDB connectionDB = new ConnectionDB();

    public static Ulisateur selected ;

    private List<Ulisateur> parseUtilisateursList(){
        if(connectionDB.getUtilisateurs() != null){
            utilisateursList = connectionDB.getUtilisateurs();
            return utilisateursList;
        }else return new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionDB = new ConnectionDB();
        Boolean test = connectionDB.CreateConnection();
        try {
            // Set cell value factories using Callback
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            username.setCellValueFactory(new PropertyValueFactory<>("username"));
            cin.setCellValueFactory(new PropertyValueFactory<>("cin"));

            gridUtilisateurs.getItems().setAll(parseUtilisateursList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        btn_delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (gridUtilisateurs.getSelectionModel().isEmpty()) {
                    status.setVisible(true);
                    status.setText("Select first");
                    status.setStyle("-fx-text-fill : #C81D3D");
                } else {
                    boolean transition = connectionDB.removeUtilisateur(txt_CIN.getText());
                    if(transition) {
                        status.setVisible(true);
                        status.setText("(1) Row removed");
                        status.setStyle("-fx-text-fill : #C81D3D");
                        setStatus();
                        gridUtilisateurs.getItems().setAll(parseUtilisateursList());
                        reset();
                        refresh_click();
                    } else {
                        status.setVisible(false);
                        status.setText("Delete problem");
                        status.setStyle("-fx-text-fill : #C81D3D");
                    }
                }
            }
        });

        gridUtilisateurs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Ulisateur selectedUtilisateur = newSelection;
                selected = selectedUtilisateur;
                txt_name.setText(selectedUtilisateur.getName());
                txt_username.setText(selectedUtilisateur.getUsername());
                txt_CIN.setText(selectedUtilisateur.getCin());

                btn_update.setText("Modifier");
                btn_update.setStyle("-fx-background-color :  #2F9AD1 ;-fx-text-fill : #fff");
            }
        });

        btn_update.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(btn_update.getText().equalsIgnoreCase("Update")) {
                    if(txt_name.getText().isEmpty() || txt_username.getText().isEmpty() || txt_CIN.getText().isEmpty()) {
                        status.setVisible(true);
                        status.setText("Fields Required");
                        status.setStyle("-fx-text-fill : #C81D3D");
                        setStatus();
                        return;
                    }
                    if(connectionDB.cinIsExist(txt_CIN.getText().trim())){
                        Ulisateur utilisateur = new Ulisateur();
                        utilisateur.setName(txt_name.getText());
                        utilisateur.setUsername(txt_username.getText());
                        utilisateur.setCin(txt_CIN.getText());
                        update(utilisateur);
                        reset();
                        refresh_click();
                    }else{
                        status.setVisible(true);
                        status.setText("CIN not exist");
                        status.setStyle("-fx-text-fill : #C81D3D");
                        setStatus();
                    }

                } else {
                    if(txt_name.getText().isEmpty() || txt_username.getText().isEmpty() || txt_CIN.getText().isEmpty()) {
                        status.setVisible(true);
                        status.setText("Fields Required");
                        status.setStyle("-fx-text-fill : #C81D3D");
                        setStatus();
                        return;
                    }
                    if(!connectionDB.cinIsExist(txt_CIN.getText().trim())){
                        Ulisateur utilisateur = new Ulisateur();
                        utilisateur.setName(txt_name.getText());
                        utilisateur.setUsername(txt_username.getText());
                        utilisateur.setCin(txt_CIN.getText());
                        add(utilisateur);
                    }else{
                        status.setVisible(true);
                        status.setText("CIN all ready exist");
                        status.setStyle("-fx-text-fill : #C81D3D");
                        setStatus();
                    }

                }
            }
        });
    }

    public void reset(){
        txt_name.setText("");
        txt_username.setText("");
        txt_CIN.setText("");
        gridUtilisateurs.getSelectionModel().clearSelection();
        gridUtilisateurs.getItems().setAll(parseUtilisateursList());
    }


    public void refresh_click(){
        try {
            btn_update.setText("Add");
            btn_update.setStyle("-fx-background-color : #000 ;-fx-text-fill : #fff");
            reset();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(Ulisateur utilisateur){
        boolean transition_update = connectionDB.updateUtilisateur(utilisateur);
        status.setVisible(true);
        if(transition_update){
            System.out.println("Done");
            status.setText("(1) Row Updated");
            status.setStyle("-fx-text-fill : #188870");
            gridUtilisateurs.getItems().setAll(parseUtilisateursList());
            reset();
        } else {
            status.setText("Update problem");
            status.setStyle("-fx-text-fill : #C81D3D");
        }
        setStatus();
    }

    public void add(Ulisateur utilisateur){
        boolean transition_update = connectionDB.addUtilisateur(utilisateur);
        status.setVisible(true);
        if(transition_update){
            System.out.println("Done");
            status.setText("(1) Row Added");
            status.setStyle("-fx-text-fill : #188870");
            gridUtilisateurs.getItems().setAll(parseUtilisateursList());
            reset();
        } else {
            status.setText("Add problem");
            status.setStyle("-fx-text-fill : #C81D3D");
        }
        setStatus();
    }

    public void setStatus(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    status.setVisible(false);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }}).start();
    }
}
