package tournoi;

import liste.Liste;

/**Tournoi est la classe représentant un Tournoi.
 *
 * @author OUAKRIM Yanis, RICHARD Nicolas, ORHON Paul, RIALET Yohann, NIVELAIS Quentin
 *
 * @version 0.1
 */

public class Tournoi{

	private Liste nouveauxJoueurs;
	private Liste anciensJoueurs;
	private Liste terrains;
	private Liste paires;
	private int nbrTerrains;
/** Constructeur de la classe tournoi
	*
	* @param nbrTerrains
	*
	*/
	public Tournoi(int nbrTerrains){
		this.nouveauxJoueurs= new Liste();
		this.anciensJoueurs= new Liste();
		this.terrains= new Liste();
		this.paires= new Liste();
		this.nbrTerrains= nbrTerrains;
	}

/** Retourne la liste de nouveaux joueurs
	*
	* @return Une liste de Tournoi, qui correspond à celle de tous les nouveaux joueurs
	*
	*/
	public Liste getNouveauxJoueurs() {
		return this.nouveauxJoueurs;
	}

	/** Retourne la liste des anciens joueurs
		*
		* @return Une liste de Tournoi, qui correspond à celle de tous les anciens joueurs
		*
		*/
	public Liste getAnciensJoueurs() {
		return this.anciensJoueurs;
	}

	/** Retourne la liste des terrains
		*
		* @return Une liste de Tournoi, qui correspond à celle de tous les terrains
		*
		*/
	public Liste getTerrains() {
		return this.terrains;
	}

	/** Initialise tous les terrains
		*
		*
		*
		*/
	public void initialiserTerrains(){
		for (int i=0; i<this.nbrTerrains; i++){
			this.terrains.add(new Terrain(i+1));
		}
	}

	/** Retourne le nombre de terrains
		*
		* @return Un entier de Tournoi, correspondant au nombre de terrains
		*
		*/
	public int getNbrTerrains() {
		return this.nbrTerrains;
	}

	/** Ajoute un joueur à la liste des joueurs d'un tournoi
		*
		* @param joueur
		*
		*/
	public void ajouterjoueur(Joueur joueur){
		if (joueur.getNouveau()){
			this.nouveauxJoueurs.add(joueur);}
		else{
			this.anciensJoueurs.add(joueur);
		}
	}

	/** Appelée pour démarrer un tour
		*
		*
		*
		*/
	public void demarrerTour(){
		this.creerPaires();
		for (int i=0; i<this.nbrTerrains; i++){
			((Terrain)this.terrains.get(i)).setMatch(null);
		}
		this.attribuerMatchs();
		//On remet tous les joueurs en attente d'une paire
		for (int i=0; i<this.anciensJoueurs.size(); i++){
			((Joueur)this.anciensJoueurs.get(i)).setJoue(false);
		}
		for (int i=0; i<this.nouveauxJoueurs.size(); i++){
			((Joueur)this.nouveauxJoueurs.get(i)).setJoue(false);
		}
	}

	/** Appelée pour créer la liste des paires d'un tournoi
		*
		*
		*
		*/
	private void creerPaires(){
		//On parcourt les deux listes de joueurs et on crée les paires en conséquence
		int tailleMin, tailleMax;
		Joueur joueur;
		//On vérifie si le nombre d'anciens est supérieur au nombre de nouveaux
		if ( this.anciensJoueurs.size()>=this.nouveauxJoueurs.size() ){
			tailleMin=this.nouveauxJoueurs.size();
			tailleMax=this.anciensJoueurs.size();
			//On cherche à créer le maximum de paires ancien/nouveau
			for (int j=0; j<tailleMin; j++)
			// On parcourt les nouveaux joueurs
			{
				joueur = ((Joueur)this.nouveauxJoueurs.get(j));
				for (int i=0; i<tailleMax; i++){
					//On cherche un ancien joueur compatible qui ne joue pas
					if (joueur.estCompatibleAvec(((Joueur)this.anciensJoueurs.get(i))) && (!((Joueur)this.anciensJoueurs.get(i)).getJoue())){
						//Si on trouve un partenaire possible, on les met ensemble et on les rend non disponibles
						this.paires.add(new Paire(joueur,((Joueur)this.anciensJoueurs.get(i)),i,i));
						joueur.setJoue(true);
						((Joueur)this.anciensJoueurs.get(i)).setJoue(true);
						break;
					}
				}
			}
		}
		else{
			tailleMin=this.anciensJoueurs.size();
			tailleMax=this.nouveauxJoueurs.size();
			for (int j=0; j<tailleMin; j++)
			{
				joueur = ((Joueur)this.anciensJoueurs.get(j));
				for (int i=0; i<tailleMax; i++){
					if (joueur.estCompatibleAvec(((Joueur)this.nouveauxJoueurs.get(i))) && (!((Joueur) this.nouveauxJoueurs.get(i)).getJoue())){
						this.paires.add(new Paire(joueur,((Joueur)this.nouveauxJoueurs.get(i)),i,i));
						joueur.setJoue(true);
						((Joueur)this.nouveauxJoueurs.get(i)).setJoue(true);
						break;
					}
				}
			}
		}
	}

	/** Attribue un match à deux paires
		*
		*
		*
		*/
	private void trierPaires(int gauche, int droite){
		//Algorithme de tri rapide adapté pour ranger les performances des paires
		droite = droite-1;
		int pivot = 0;
		Paire tmp;
		if(droite > gauche){
			pivot = (gauche+droite)/2;
			tmp = ((Paire) this.paires.get(gauche));
			this.paires.set(gauche, ((Paire) this.paires.get(pivot))) ;
			this.paires.set(pivot, tmp) ;
			pivot = gauche;
			for(int i = gauche; i<droite;i++){
				if(((Paire)this.paires.get(i)).getPerf() < ((Paire)this.paires.get(gauche)).getPerf()){
					pivot++;
					tmp = (Paire) this.paires.get(i);
					this.paires.set(i, ((Paire) this.paires.get(pivot)) );
					this.paires.set(pivot, tmp);
				}
			}
			tmp = (Paire) this.paires.get(pivot);
			this.paires.set(pivot, ((Paire) this.paires.get(gauche)));
			this.paires.set(gauche, tmp);
			trierPaires(gauche, pivot-1);
			trierPaires(pivot+1, droite);
		}
	}
	private void attribuerMatchs(){
		Paire paire1,paire2;
		trierPaires(0, this.paires.size());
		for(int i=0;i<this.paires.size();i++){
			System.out.println(((Paire) this.paires.get(i)).toString());
		}
		//On parcourt les terrains et on leur attribue des matchs que l'on crée à partir des paires
		for (int i=0; i<Math.floor(Math.min(this.nbrTerrains,this.paires.size()/2)); i++){
			paire1=((Paire)this.paires.get(2*i));
			paire2=((Paire)this.paires.get(2*i+1));
			((Terrain)this.terrains.get(i)).setMatch(new Match(i+1, paire1, paire2));
		}
		String res= "";
		for(int i=0; i<Math.floor(Math.min(this.nbrTerrains,this.paires.size()/2));i++){
			res +=((Terrain)this.terrains.get(i)).getMatch().toString()+"\n";
		}
		System.out.println(res);
	}

	/** Appelée pour finir un tour du tournoi
		*
		*
		*
		*/
	public void finirTour(){
		//On demande le score des équipes pour chaque terrain
		for (int i=0; i<this.nbrTerrains; i++){
			if (((Terrain)this.terrains.get(i)).getMatch()!=null){
				((Terrain)this.terrains.get(i)).getMatch().getPaire1().setScore(0);
				((Terrain)this.terrains.get(i)).getMatch().getPaire2().setScore(0);
				//On détermine les vainqueurs de chaque match
				((Terrain)this.terrains.get(i)).getMatch().setVainqueur(((Terrain)this.terrains.get(i)).getMatch().determinerVainqueur());
				//On modifie le score des joueurs en conséquence
				((Terrain)this.terrains.get(i)).getMatch().modifierScores();
			}

			}
	}
	public String toString(){
		String res= "";
		for(int i=0; i<this.paires.size();i++){
			res +=((Paire)this.paires.get(i)).toString()+"\n";
		}
		return res;
	}

}
