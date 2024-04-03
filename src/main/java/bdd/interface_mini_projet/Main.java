package bdd.interface_mini_projet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Chargement du fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("/bdd/interface_mini_projet/main_scene.fxml"));

        System.out.println("FXML Loaded"); // Ajout de ce message de débogage

        // Configuration de la scène
        Scene scene = new Scene(root, 1200, 600);

        // Configuration du style de la scène

        // Configuration de la scène dans la fenêtre principale
        primaryStage.setTitle("Titre de la fenêtre");

        primaryStage.setScene(scene);

        // Affichage de la fenêtre principale
        primaryStage.show();
    }
}
