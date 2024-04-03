module bdd.interface_mini_projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Ajout du module java.sql comme requis

    opens bdd.interface_mini_projet to javafx.fxml;
    exports bdd.interface_mini_projet;
}