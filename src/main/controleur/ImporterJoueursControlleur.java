package main.controleur;

import main.exception.ImportExportException;
import main.tournoi.Joueur;
import main.tournoi.Tournoi;
import main.vue.FenetrePrincipale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/** La classe ImporterJoueursControlleur permet d'importer tous les joueurs contenus dans un fichier CSV
 * respectant cet ordre à chaque ligne :
 * [0] : id / [1] : nom / [2] : prenom / [3] : age / [4] : sexe (0 : homme / 1 : femme)
 * [5] : nouveau (0 : ancien / 1 : nouveau) / [6] : niveau  (0 : débutant / 1 : Intérmédiaire / 2 : confirmé)
 * [7] : peutJouer
 * @author DERNONCOURT Cyril , DROUARD Antoine, LE BERT Lea, MARTINEAU Lucas
 */
public class ImporterJoueursControlleur implements ActionListener {

    private Tournoi tournoi;
    private FenetrePrincipale vue;

    /** Constructeur de la classe ImporterJoueursControlleur
     *
     * @param vue la main.vue qu'il faut rafraichir lors de l'ajout des joueurs
     */
    public ImporterJoueursControlleur(FenetrePrincipale vue) {
        this.tournoi = vue.getTournoi();
        this.vue = vue;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Frame fr = new Frame("Choississez un répertoire");
        FileDialog dial = new FileDialog(fr, "Importer un fichier", FileDialog.LOAD);
        dial.setFile("*.csv");
        dial.setVisible(true);
        fr.setVisible(false);
        if (dial.getFile() != null) {
            String cvsFile = dial.getDirectory().concat(dial.getFile());
            try {

                tournoi.importJoueurs(cvsFile);
                vue.actualiserJoueurs();

            } catch (java.io.FileNotFoundException e2) {
                JOptionPane.showMessageDialog(null, "Le fichier demandé n'a pas été trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e4) {
                JOptionPane.showMessageDialog(null, "Fichier erroné", "Erreur", JOptionPane.ERROR_MESSAGE);
                e4.printStackTrace();
            } catch (ImportExportException e4) {
                JOptionPane.showMessageDialog(null, "Fichier erroné : " + e4.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        }
    }


}
