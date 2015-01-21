package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main
{

	public static void main(String[] args)
	{
		//ESSAI GIT HUB
		//on a joute ca
		
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				System.out.println("On test la connection a la base de donnee");
				//ESSAI GIT HUB
				//toto

				try 
				{
					System.out.println("Connexion en cours ...");

					DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); // Chargement du pilote
					System.out.println("On test la connection a la base de donnee");
		                        
					Connection base = DriverManager.getConnection ("jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima",	"devisi", "bd2015"); // connexion
					System.out.println("On s'est connecte ...");
					Statement requete = base.createStatement(); // création du descripteur de requête
					System.out.println("On s'est connecte ...");
					
					String requeteOracle;
					requeteOracle = "SELECT * FROM STATION";
					// exécution d'une requête
					ResultSet resultat = requete.executeQuery(requeteOracle);
					afficherResultats(resultat);
					
					//on ferme la requete
					requete.close();
					//on ferme le resultat
					resultat.close();
					base.close(); // fermeture de la connexion
					
					afficherMessage();
				}
				catch (Exception err)
				{
					System.out.println("Une erreur, Oh Oh!");
				} // Attention il faut capturer les exceptions !
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
