package vue;

import tournoi.Joueur;
import tournoi.Tournoi;
import controleur.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FenetreModifierJoueur extends JFrame {

	private Tournoi tournoi;
	FenetrePrincipale vue;
	private int id;

	private JTextField nom;
	private JTextField prenom;
	private JComboBox niveau;
	private JSpinner age;
	private JRadioButton fem;
	private JRadioButton hom;
	private JCheckBox nouv;

	/**
	 * constructeur de la fenêtre d'ajout d'un joueur
	 * @param titre le titre à donner à la fenêtre
	 * @param tournoi le tournoi dans lequel on veut ajouter un joueur
	 * @param vue la vue qui crée la fenêtre
     */
	public FenetreModifierJoueur(String titre, Tournoi tournoi, FenetrePrincipale vue, int id){

		this.tournoi = tournoi;
		this.vue = vue;
		this.id = id;

		//Les différents champs de saisie
		Joueur joueur = this.tournoi.getJoueur(id);

		nom = new JTextField(joueur.getNom());
		prenom = new JTextField(joueur.getPrenom());
		niveau = new JComboBox(new String[]{"Debutant","Intermediaire", "Confirme"});
		System.out.println(joueur.getNiveau());
		niveau.setSelectedIndex(joueur.getNiveau());
		age = new JSpinner(new SpinnerNumberModel(joueur.getAge(),1,120,1));
		fem = new JRadioButton("Femme");
		hom = new JRadioButton("Homme");
		if(joueur.getSexe())
		{
			hom.setSelected(false);
			fem.setSelected(true);
		}
		else
		{
			hom.setSelected(true);
			fem.setSelected(false);
		}

		ButtonGroup grSexe = new ButtonGroup();
		grSexe.add(hom);
		grSexe.add(fem);
		nouv = new JCheckBox("Nouveau");
		if(joueur.getNouveau())
		{
			nouv.setSelected(true);
		}
		else
		{
			nouv.setSelected(false);
		}
		
		JButton modifier = new JButton("Modifier le joueur");
		modifier.addActionListener(new ModifierJoueurBoutonControlleur(this, id));
		JButton supprimer = new JButton("Supprimer le joueur");
		supprimer.addActionListener(new SupprimerJoueurBoutonControlleur(this, id));
		
		JPanel corePanel = new JPanel();
		corePanel.setLayout(new BorderLayout());
		
		JPanel gauche = new JPanel();
		
		gauche.setLayout(new GridLayout(3,2));
		gauche.add(new JLabel("Nom :"));
		gauche.add(nom);
		
		JPanel panelSexe = new JPanel();
		panelSexe.setLayout(new GridLayout(2,1));
		panelSexe.add(hom);
		panelSexe.add(fem);
		
		gauche.add(new JLabel("Sexe :"));
		gauche.add(panelSexe);
		
		gauche.add(new JLabel("Niveau :"));
		gauche.add(niveau);
		
		corePanel.add(gauche,BorderLayout.WEST);
		
		JPanel droite = new JPanel();
		
		droite.setLayout(new GridLayout(3,2));
		droite.add(new JLabel("Prenom :"));
		droite.add(prenom);
		
		JPanel panelAnc = new JPanel();
		panelAnc.setLayout(new GridLayout(2,1));
		panelAnc.add(nouv);
		
		droite.add(new JLabel("Anciennete :"));
		droite.add(panelAnc);
		
		droite.add(new JLabel("Age :"));
		droite.add(age);
		
		corePanel.add(droite,BorderLayout.EAST);

		JPanel panelBouton = new JPanel();
		panelBouton.setLayout(new GridLayout(2,1));
		panelBouton.add(modifier);
		panelBouton.add(supprimer);

		corePanel.add(panelBouton,BorderLayout.SOUTH);
		
		this.setContentPane(corePanel);
		this.pack();
		this.setVisible(true);
		this.setTitle(titre);
		int tailleX = 600, tailleY = 200;
		this.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-tailleX)/2,(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-tailleY)/2);
		this.setSize(tailleX,tailleY);
		this.setResizable(false);
	}

	/**
	 *
	 * @return le champ de saisi du nom
     */
	public JTextField getNom() {
		return nom;
	}

	/**
	 *
	 * @param str la chaine de caractère à écrire dans le champ de saisie du nom
     */
	public void setNom(String str){
		this.nom.setText(str);
	}

	/**
	 *
	 * @returnle champ de saisie du prénom
     */
	public JTextField getPrenom() {
		return prenom;
	}

	/**
	 *
	 * @param str la chaine de caractère à écrire dans le champ de saisie du nom
     */
	public void setPrenom(String str){
		this.prenom.setText(str);
	}

	public JComboBox getNiveau() {
		return niveau;
	}

	/**
	 *
	 * @return l'outil de séléction de l'âge
     */
	public JSpinner getAge() {
		return age;
	}

	/**
	 * réinitialise l'age
	 */
	public void setAge(){
		this.age.setValue(0);
	}

	public JRadioButton getFem() {
		return fem;
	}


	public JRadioButton getHom() {
		return hom;
	}

	/**
	 *
	 * @return la case qui indique l'ancienneté
     */
	public JCheckBox getNouv() {
		return nouv;
	}

	/**
	 * pour ajouter un joueur dans le tournoi et dans la liste de la fenetre principale
	 */
	public void modifierJoueur(int id)
	{
		int age = ((int)this.age.getValue());
		String nom = this.nom.getText(), prenom = this.prenom.getText();
		boolean sexe = fem.isSelected();
		boolean nouve = nouv.isSelected();
		int niveau = this.niveau.getSelectedIndex();
		this.tournoi.modifierJoueur(id, nom, prenom, age, sexe, nouve, niveau);
		this.vue.actualiserJoueurs();
		dispose();
	}

	public void supprimerJoueur(int id)
	{
		Joueur jou = this.tournoi.getJoueur(id);
		boolean trouve = false;
		int i = 0;
		Joueur aSupprimer = new Joueur(id, true, true);

		if(jou.getNouveau())
		{
			ArrayList nouveauxJoueurs = this.tournoi.getNouveauxJoueurs();
			int tailleNouveauxJoueurs = nouveauxJoueurs.size();

			while(!trouve && i < tailleNouveauxJoueurs)
			{
				Joueur j = (Joueur) nouveauxJoueurs.get(i);
				int a = j.getId();
				if (a == id)
				{
					aSupprimer = j;
					trouve = true;
				}
				i++;
			}
		}

		else
		{
			ArrayList anciensJoueurs = this.tournoi.getAnciensJoueurs();
			int tailleAnciensJoueurs = anciensJoueurs.size();

			while(!trouve && i < tailleAnciensJoueurs)
			{
				Joueur j = (Joueur) anciensJoueurs.get(i);
				int a = j.getId();
				if (a == id)
				{
					aSupprimer = j;
					trouve = true;
				}
				i++;
			}
		}

		this.tournoi.supprimerJoueur(aSupprimer);
		this.vue.supprimerJoueurTable();
		this.vue.actualiserJoueurs();
		this.vue.actualiserNoms();
		dispose();

	}


}
