package bdd.interface_mini_projet;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {



    @FXML
    private Text errorText;

    @FXML
    private Button closeButton;

    @FXML
    private Button ajoutStage;

    @FXML
    private TextField IdStage;

    @FXML
    private TextField structure;

    @FXML
    private TextField debutStage;

    @FXML
    private TextField dureeStage;

    @FXML
    private TextField promotion;

    @FXML
    private TextField sujetStage;
    private String selectedId;


    @FXML
    private TableView<Map<String, Object>> tableView;

    // Méthode pour charger les données depuis la base de données
    @FXML
    private void initialize() {
        initializeTableView();

    }



    @FXML
    private void close() {
        // Créer une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de fermeture");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir fermer l'application ?");

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Si l'utilisateur clique sur OK, fermer la fenêtre
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            }
        });
    }
    private void initializeTableView() {
        try (Connection conn = SQLiteConnection.connect()) {
            String query = "SELECT * FROM OffresStage";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            var metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(param -> {
                    Map<String, Object> rowData = param.getValue();
                    return new SimpleObjectProperty<>(rowData.get(columnName));
                });
                tableView.getColumns().add(column);
            }

            while (resultSet.next()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    rowData.put(columnName, value);
                }
                tableView.getItems().add(rowData);
            }

            // Ajouter un gestionnaire d'événements pour le clic sur une ligne du tableau
            tableView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) { // Vérifiez si un seul clic a été effectué
                    // Récupérer les données de la ligne sélectionnée dans le tableau
                    Map<String, Object> selectedRowData = tableView.getSelectionModel().getSelectedItem();
                    if (selectedRowData != null) {
                        selectedId = selectedRowData.get("id").toString(); // Mise à jour de selectedId
                        IdStage.setText(selectedRowData.get("id").toString());
                        structure.setText(selectedRowData.get("nomStructure").toString());
                        debutStage.setText(selectedRowData.get("moisDebut").toString());
                        dureeStage.setText(selectedRowData.get("duree").toString());
                        promotion.setText(selectedRowData.get("promotionEtudiant").toString());
                        sujetStage.setText(selectedRowData.get("sujet").toString());
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }











    @FXML
    private void ModifStage() {
        if (selectedId != null && !selectedId.isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("modif.fxml"));
                Parent modifRoot = loader.load();

                // Création de l'instance du contrôleur
                modifController modifController = loader.getController();

                // Appel de la méthode loadStageDetails avec l'instance du contrôleur
                modifController.loadStageDetails(selectedId);

                // Création de la nouvelle fenêtre (Stage) pour la fiche RDV
                Stage modifStage = new Stage();
                modifStage.setTitle("Modification_Stage");
                modifStage.setScene(new Scene(modifRoot));

                // Affichage de la nouvelle fenêtre
                modifStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Afficher un message d'erreur si aucun ID n'est sélectionné
            errorText.setText("Veuillez sélectionner un stage dans le tableau.");
        }
    }


    @FXML
    private void openAjoutStagePage() {
        try {
            // Charger la vue de la page d'ajout de stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajout.fxml"));
            Parent ajoutStageRoot = loader.load();

            // Créer une nouvelle scène
            Scene ajoutStageScene = new Scene(ajoutStageRoot);
            // Créer un nouveau stage
            Stage ajoutStage = new Stage();
            ajoutStage.setTitle("Ajout de Stage");
            ajoutStage.setScene(ajoutStageScene);
            // Afficher le nouveau stage
            ajoutStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void updateStage() {
        // Effacer tous les éléments actuels de la ComboBox


        // Effacer tous les éléments actuels du TableView
        tableView.getItems().clear();
        tableView.getColumns().clear(); // Effacer également les colonnes

        // Recharger les données depuis la base de données

        initializeTableView();
    }


}

