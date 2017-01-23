package main.controleur;

import main.tournoi.Tournoi;
import main.vue.FenetrePrincipale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by E154981H on 03/01/17.
 */
public class ValiderTourControlleur implements ActionListener {
    private FenetrePrincipale vue;
    private Tournoi tournoi;

    public ValiderTourControlleur(FenetrePrincipale vue) {
        this.vue = vue;
        this.tournoi = vue.getTournoi();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            this.tournoi.finirTour();//enregistre le tour et nettoye les paires
            this.tournoi.nouveauTour();
            this.vue.actualiserTerrains();
            //this.main.vue.afficherTournoi();

        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Il n'y a pas de joueurs!! " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }
}