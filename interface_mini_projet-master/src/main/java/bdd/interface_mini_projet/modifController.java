package bdd.interface_mini_projet;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class modifController {
        @FXML
        private TextField structure;
        @FXML
        private TextField idStage;
        @FXML
        private ComboBox<String> debut; // ComboBox pour le mois de début
        @FXML
        private ComboBox<Integer> duree; // ComboBox pour la durée en semaines
        @FXML
        private ComboBox<String> promotion; // ComboBox pour la promotion
        @FXML
        private Button closeButton;
        @FXML
        private TextField sujet;
        @FXML
        private Text erreurText;

        private String selectedId;

        @FXML
        private void close() {
                // Créer une boîte de dialogue de confirmation
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de fermeture");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de vouloir fermer l'application ? Toutes les modifications non sauvegardées seront perdues.");

                // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                                // Si l'utilisateur clique sur OK, fermer la fenêtre
                                Stage stage = (Stage) closeButton.getScene().getWindow();
                                stage.close();
                        }
                });
        }

        // Méthode d'initialisation pour les ComboBox
        @FXML
        private void initialize() {
                initializeDebutComboBox();
                initializeDureeComboBox();
                initializePromotionComboBox();
        }

        // Initialisation de la ComboBox pour les mois de début
        private void initializeDebutComboBox() {
                List<String> moisList = Arrays.asList("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre");
                debut.getItems().addAll(moisList);
        }

        // Initialisation de la ComboBox pour la durée en semaines
        private void initializeDureeComboBox() {
                List<Integer> dureeList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);
                duree.getItems().addAll(dureeList);
        }

        // Initialisation de la ComboBox pour la promotion
        private void initializePromotionComboBox() {
                List<String> promotionList = Arrays.asList("L3", "M1", "M2");
                promotion.getItems().addAll(promotionList);
        }

        // Méthode pour charger les détails du stage depuis la base de données
        void loadStageDetails(String selectedId) {
                Connection conn = null;
                PreparedStatement statement = null;
                ResultSet resultSet = null;

                try {
                        // Établir une connexion à la base de données
                        conn = SQLiteConnection.connect();

                        // Requête SQL pour récupérer les détails du stage
                        String query = "SELECT id, nomStructure, moisDebut, duree, promotionEtudiant, sujet FROM OffresStage WHERE id = ?";
                        statement = conn.prepareStatement(query);
                        statement.setString(1, selectedId);
                        resultSet = statement.executeQuery();

                        // Si une ligne est retournée, affichez les détails du stage
                        if (resultSet.next()) {
                                idStage.setText(resultSet.getString("id"));
                                structure.setText(resultSet.getString("nomStructure"));
                                debut.setValue(resultSet.getString("moisDebut"));
                                duree.setValue(resultSet.getInt("duree"));
                                promotion.setValue(resultSet.getString("promotionEtudiant"));
                                sujet.setText(resultSet.getString("sujet"));
                        } else {
                                // Si aucun résultat n'est retourné, affichez un message d'erreur ou une action appropriée
                                System.out.println("Aucun détail trouvé pour l'ID de stage sélectionné.");
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                        // Gérer les erreurs de connexion à la base de données ou d'exécution de la requête
                } finally {
                        // Fermer la connexion, les déclarations et le résultat
                        SQLiteConnection.closeConnection(conn);
                        try {
                                if (statement != null) statement.close();
                                if (resultSet != null) resultSet.close();
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                }
        }

        @FXML
        private void insertInto() {
                // Récupérer les nouvelles valeurs des champs
                String newSujet = sujet.getText();
                String newStructure = structure.getText();
                String newDebut = debut.getValue();
                Integer newDuree = duree.getValue();
                String newPromotion = promotion.getValue();

                // Vérifier que toutes les informations ne sont pas nulles
                if (newSujet != null && !newSujet.isEmpty() &&
                        newStructure != null && !newStructure.isEmpty() &&
                        newDebut != null && newDuree != null && newPromotion != null) {
                        // Créer une boîte de dialogue de confirmation
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation d'insertion");
                        alert.setHeaderText(null);
                        alert.setContentText("Êtes-vous sûr de vouloir insérer ces modifications ?");

                        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                        alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                        // Établir la connexion à la base de données
                                        Connection conn = SQLiteConnection.connect();
                                        if (conn != null) {
                                                try {
                                                        // Préparer la requête SQL pour mettre à jour les données
                                                        String query = "UPDATE OffresStage SET nomStructure=?, moisDebut=?, duree=?, promotionEtudiant=?, sujet=? WHERE id=?";
                                                        PreparedStatement statement = conn.prepareStatement(query);
                                                        statement.setString(1, newStructure);
                                                        statement.setString(2, newDebut);
                                                        statement.setInt(3, newDuree);
                                                        statement.setString(4, newPromotion);
                                                        statement.setString(5, newSujet);
                                                        statement.setString(6, idStage.getText()); // Utilisez l'ID pour identifier la ligne à mettre à jour

                                                        // Exécuter la requête
                                                        int rowsAffected = statement.executeUpdate();
                                                        if (rowsAffected > 0) {
                                                                System.out.println("Données mises à jour avec succès !");
                                                                // Fermer la fenêtre après la mise à jour des données
                                                                Stage stage = (Stage) closeButton.getScene().getWindow();
                                                                stage.close();
                                                        } else {
                                                                System.out.println("Aucune donnée mise à jour.");
                                                        }

                                                        // Fermer la déclaration et la connexion
                                                        statement.close();
                                                        SQLiteConnection.closeConnection(conn);
                                                } catch (SQLException e) {
                                                        System.out.println("Erreur lors de la mise à jour des données : " + e.getMessage());
                                                }
                                        }
                                }
                        });
                } else {
                        erreurText.setText("Veuillez remplir tous les champs.");
                }
        }
}
