package bdd.interface_mini_projet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AjoutController {

    @FXML
    private TextField structure;
    @FXML
    private ComboBox<String> debut; // Modification : spécifier le type générique de la ComboBox
    @FXML
    private ComboBox<Integer> duree; // Modification : spécifier le type générique de la ComboBox
    @FXML
    private ComboBox<String> promotion; // Modification : spécifier le type générique de la ComboBox
    @FXML
    private TextField sujet;
    @FXML
    private Button closeButton;
    @FXML
    private Text erreurText;

    // Initialise les ComboBox au chargement du contrôleur
    @FXML
    private void initialize() {
        // ComboBox "duree" : valeurs de 1 à 26
        List<Integer> dureeList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);
        duree.getItems().addAll(dureeList);

        // ComboBox "debut" : mois en majuscules
        List<String> moisList = Arrays.asList("JANVIER", "FEVRIER", "MARS", "AVRIL", "MAI", "JUIN", "JUILLET", "AOUT", "SEPTEMBRE", "OCTOBRE", "NOVEMBRE", "DECEMBRE");
        debut.getItems().addAll(moisList);

        // ComboBox "promotion" : L3, M1, M2
        List<String> promotionList = Arrays.asList("L3", "M1", "M2");
        promotion.getItems().addAll(promotionList);
    }

    @FXML
    private void close() {
        // Créer une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de fermeture");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir fermer la fenêtre d'ajout ?");

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Si l'utilisateur clique sur OK, fermer la fenêtre
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    @FXML
    private void insertInto() {
        String newStructure = structure.getText();
        String newDebut = debut.getValue();
        Integer newDuree = duree.getValue();
        String newPromotion = promotion.getValue();
        String newSujet = sujet.getText();

        if (newStructure == null || newStructure.isEmpty() ||
                newDebut == null || newDuree == null || newPromotion == null || newSujet == null || newSujet.isEmpty()) {
            erreurText.setText("Veuillez remplir tous les champs.");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation d'ajout");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir ajouter ce stage ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    insertStageIntoDatabase(newStructure, newDebut, newDuree, newPromotion, newSujet);
                }
            });
        }
    }


    private void insertStageIntoDatabase(String newStructure, String newDebut, Integer newDuree, String newPromotion, String newSujet) {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            // Établir une connexion à la base de données
            conn = SQLiteConnection.connect();

            // Requête SQL pour insérer un nouveau stage
            String query = "INSERT INTO OffresStage (nomStructure, moisDebut, duree, promotionEtudiant, sujet) VALUES (?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(query);
            statement.setString(1, newStructure);
            statement.setString(2, newDebut);
            statement.setInt(3, newDuree);
            statement.setString(4, newPromotion);
            statement.setString(5, newSujet);

            // Exécuter la requête
            statement.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion à la base de données ou d'exécution de la requête
        } finally {
            // Fermer la connexion et la déclaration
            SQLiteConnection.closeConnection(conn);
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
