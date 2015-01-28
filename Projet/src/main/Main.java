package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Menu.*;

public class Main
{

	public static void main(String[] args)
	{


		try 
		{

			Connection base = etablirLaConnexionAlaBDD();
			Statement requete = base.createStatement(); // création du descripteur de requête

			
			Menu menuPrincipal = new MenuPrincipal(base);
			menuPrincipal.lancer();
			requete.close();
			base.close();
			
		}
		catch (Exception err)
		{
			System.out.println("Erreur de connexion a la base de donnee");
		} // Attention il faut capturer les exceptions !
	}

	private static Connection etablirLaConnexionAlaBDD() throws SQLException
	{
		System.out.println("Connexion en cours ...");

		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); // Chargement du pilote

		Connection base = DriverManager.getConnection ("jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima","devisi", "bd2015"); // connexion
		//			Connection base = DriverManager.getConnection ("jdbc:oracle:thin:@localhost:1521:ufrima","devisi", "bd2015"); // connexion
		System.out.println("Connexion etablie !\n\n");
		return base;
	}

	private static void afficherMessage() {
		// TODO Auto-generated method stub

	}

	private static void afficherResultats(ResultSet resultat) throws SQLException {
		//tant  qu'il y a des resultats
		while(resultat.next()) { // récupération des résultats
			System.out.println("Numvol = " + resultat.getString("Numvol")
					+ ", Origine = " + resultat.getString("Origine")
					+ ", Destination = " + resultat.getString("Destination"));
		}
	}

}
