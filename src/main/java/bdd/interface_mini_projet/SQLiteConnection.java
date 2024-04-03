package bdd.interface_mini_projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    // Chemin de la base de données SQLite
    private static final String DATABASE_PATH = "C:\\Users\\Noah\\Desktop\\BDD_interface\\BD\\BD_miniprojet";

    // Méthode pour établir une connexion à la base de données
    public static Connection connect() {
        Connection conn = null;
        try {
            // Chargement du pilote SQLite JDBC
            Class.forName("org.sqlite.JDBC");
            // Établissement de la connexion à la base de données
            conn = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
            System.out.println("Connexion à la base de données SQLite établie avec succès.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données SQLite : " + e.getMessage());
        }
        return conn;
    }

    // Méthode pour fermer la connexion à la base de données
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Connexion à la base de données SQLite fermée avec succès.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la fermeture de la connexion à la base de données SQLite : " + e.getMessage());
        }
    }
}