<?php
require_once __DIR__.'/../config.php';

/**
* Classe qui gère les accès à la base de données
* Il faut renseigner dans le fichier config le nom de la base, le nom de compte et le mot de passe
*/
class Modele{
    private $connexion;

// Constructeur de la classe

    public function __construct(){
        try{
            $chaine="mysql:host=localhost;dbname=".Config::$DB_NAME;  //Va chercher les noms dans le fichier config.php
            $this->connexion = new PDO($chaine,Config::$DB_USER,Config::$DB_PASSWD);
            $this->connexion->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
        }
        catch(PDOException $e){
            throw new ConnexionException("problème de connection à la base de données, essayez de modifier le fichier config.php à la racine du dossier mastermind puis rafraichissez la page");
        }
    }

    // méthode qui permet de se deconnecter de la base
    public function deconnexion(){
        $this->connexion=null;
    }

    /** Retourne tous les joueurs présents dans la base de données
     * @return array Tous les joueurs
     * @throws TableAccesException si problème d'accès à la base
     */
    public function getJoueurs(){
        try{
            $statement=$this->connexion->query("SELECT * FROM ".Config::$DB_tableJoueurs);
            return($statement->fetchAll(PDO::FETCH_ASSOC));
        } catch(PDOException $e){
            $this->deconnexion();
            throw new TableAccesException("problème avec la table joueurs : Veuillez vérifier qu'elle existe bien");
        }
    }

    /** Ajoute un joueur à la base de données
     * @param $prenom Le prénom du joueur à ajouter
     * @param $nom le nom du joueur à ajouter
     * @param $sexe Le sexe du joueur à ajouter
     * @param $anciennete L'ancienneté du joueur à ajouter
     * @param $date La date de naissance du joueur à ajouter
     * @param $niveau Le niveau de jeu du joueur à ajouter
     * @return bool Si l'ajout a bien été effectué
     * @throws TableAccesException si problèmme d'accès à la base
     */
    public function addJoueur($prenom, $nom, $sexe, $anciennete, $date, $niveau) {
        try{

            $statement = $this->connexion->prepare("SELECT count(*) as nbjoueurs FROM ".Config::$DB_tableJoueurs." WHERE nom=? and prenom=?");
            $statement->bindParam(1, $nom);
            $statement->bindParam(2, $prenom);

            $statement->execute();
            $resultQuery=$statement->fetch(PDO::FETCH_ASSOC);

            $result = $resultQuery["nbjoueurs"];

            if($result == 0) {

                $statement = $this->connexion->prepare("INSERT INTO ".Config::$DB_tableJoueurs." VALUES (NULL,?,?,?,?,?,?);");
                $statement->bindParam(1, $prenom);
                $statement->bindParam(2, $nom);
                $statement->bindParam(3, $sexe);
                $statement->bindParam(4, $anciennete);
                $statement->bindParam(5, $date);
                $statement->bindParam(6, $niveau);

                $result = $statement->execute();
                   
                return true;

            } else {

                return false;

            }

        } catch(PDOException $e){
            $this->deconnexion();
            print($e->getMessage());
        }

    }

    /** Supprimer un joueur dans la base de données en utilisant son nom et son prénom
     * @param $prenom Le prénom du joueur à supprimer
     * @param $nom le nom du joueur à supprimer
     * @throws TableAccesException si problèmme d'accès à la base
     */
    public function supprimerJoueur($prenom, $nom){
        try{

            $prenom = addslashes(trim($prenom));
            $nom = addslashes(trim($nom));

            $sql = "DELETE FROM ".Config::$DB_tableJoueurs." WHERE prenom = '".$prenom."' AND nom = '".$nom."'";
            $stmt = $this->connexion->prepare($sql);
            $stmt->execute();
            
        } catch(PDOException $e){
            $this->deconnexion();
            print($e->getMessage());
        }
    }

    /** Supprimer un joueur dans la base de données en utilisant son nom et son prénom
     * @param $prenom Le prénom du joueur à modifier
     * @param $nom le nom du joueur à modifier
     * @param $ddn la date de naissance du joueur à modifier
     * @param $sexe le sexe du joueur à modifier
     * @param $anciennete l'ancienneté du joueur à modifier
     * @param $niveau le niveau du joueur à modifier
     * @throws TableAccesException si problèmme d'accès à la base
     */
    public function modifierJoueur($prenom, $nom, $ddn, $sexe, $anciennete, $niveau) {
        try{

            $sql = "UPDATE ".Config::$DB_tableJoueurs." SET sexe=?, anciennete=?, ddn=?, niveau=? WHERE prenom=? and nom=?";
            $statement = $this->connexion->prepare($sql);

            $statement->bindParam(1, $sexe);
            $statement->bindParam(2, $anciennete);
            $statement->bindParam(3, $ddn);
            $statement->bindParam(4, $niveau);
            $statement->bindParam(5, $prenom);
            $statement->bindParam(6, $nom);

            $statement->execute();
            
        } catch(PDOException $e){
            $this->deconnexion();
            print($e->getMessage());
        }

    }

    public function getPass($pseudo) {
        try{
            $statement = $this->connexion->prepare("SELECT password from " . Config::$DB_tableAdministrateurs . " where login=?;");
            $statement->bindParam(1, $pseudo);
            $statement->execute();
            $result=$statement->fetch(PDO::FETCH_ASSOC);

            return $result["password"];
        }
        catch(PDOException $e){
            $this->deconnexion();
            throw new Exception("problème avec la table " . Config::$DB_tableAdministrateurs);
        }
    }
}

?>
