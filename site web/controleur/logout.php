<?php
	session_start();
	$_SESSION=array();
	session_unset(); //Detruit toutes les variables de la session en cours
	session_destroy();//Destruit la session en cours
	header("location: ../index.php"); // redirige l'utilisateur
?>
