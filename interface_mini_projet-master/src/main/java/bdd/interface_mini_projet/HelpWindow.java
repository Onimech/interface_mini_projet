package bdd.interface_mini_projet;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HelpWindow extends Stage {

    public HelpWindow() {
        TextArea helpText = new TextArea();
        helpText.setEditable(false);
        helpText.setWrapText(true);
        helpText.setText("Guide d'utilisateur\n" +
                "\n" +
                "Bienvenue dans le guide utilisateur. Sur notre application, vous pouvez gérer tous les stages des étudiants de la filière GPHY. La liste de l'ensemble des stages est disponible dans le tableau présent en page principale. Sur cette page, vous pouvez ajouter un stage depuis le bouton « ajouter offre de stage ». Vous pouvez modifier les informations d'une offre de stage en ayant sélectionné l’offre dans le tableau au préalable. Lors de l’ajout ou de la modification d’un stage, pensez à appuyer sur le bouton « actualiser liste de stages » afin que la modification ou l’ajout du stage soit pris en compte. \n"+
                "\n" +
                "Si vous rencontrez des difficultés, veuillez contacter le +(33)(0)5 49 45 30 00, ou au 15 rue de l'Hôtel Dieu, TSA 71117, 86073 POITIERS Cedex 9, France.");


        Scene scene = new Scene(helpText, 400, 300);
        setScene(scene);
        setTitle("Aide");
    }
}