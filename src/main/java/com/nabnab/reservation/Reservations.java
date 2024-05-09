package com.nabnab.reservation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Reservations {

    @FXML
    private Button buttonreserve;
    @FXML
    private DatePicker datereservations;
    @FXML
    private ComboBox<String> comboreservations;
    @FXML
    private ComboBox<String> comboutilisateur;
    // Ensure the type parameter matches expected input.

    private ArrayList<Ulisateur> liutilisateur = new ArrayList<>();
    private ArrayList<Room> li = new ArrayList<>();
    private ConnectionDB c;

    @FXML
    private void initialize() {
        c = new ConnectionDB();
        boolean test = c.CreateConnection();
        if (test) {
            li = c.getRooms();
            if (li != null) {
                for (Room room : li) {
                    comboreservations.getItems().add(room.getRoomname());
                }
                if (!comboreservations.getItems().isEmpty()) {
                    comboreservations.getSelectionModel().selectFirst();
                }
            } else {
                System.out.println("List is empty");
            }
            liutilisateur = c.getUtilisateurs();
            if (liutilisateur != null) {
                for (Ulisateur utili : liutilisateur) {
                    comboutilisateur.getItems().add(utili.getCin());
                }
                if (!comboutilisateur.getItems().isEmpty()) {
                    comboutilisateur.getSelectionModel().selectFirst();
                }
            } else {
                System.out.println("List is empty");
            }
        } else {
            System.out.println("Failed to establish connection");
        }

        setupListeners();
    }

  private void setupListeners() {
        buttonreserve.setOnAction(e -> handleReserveButton());
    }

    private void handleReserveButton() {
        String selectedRoom = comboreservations.getValue();
        String selectedutilisateur = comboutilisateur.getValue();
        LocalDate selectedDate = datereservations.getValue();

        if (selectedRoom == null || selectedDate == null) {

            System.out.println("Please select both a room and a date.");

        } else {
            if (c.addReservation(selectedRoom,  selectedDate,selectedutilisateur)) {

                System.out.println("Reservation successful for " + selectedRoom + " on " + selectedDate);

            } else {

                System.out.println("Failed to make reservation.");

            }
        }
    }

    private void handleCancelButton() {
        comboreservations.setValue(null);
        datereservations.setValue(null);
    }
}



