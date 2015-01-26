package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Vehicule 
{

	private Connection base;

	public Vehicule(Connection base) 
	{
		this.base = base;
	}

	public void AfficherRoutines()
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Numero du vehicule :");
		String idVehicule = scanner.nextLine();


		//Interrogation de la base de donnee
		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;

			//requeteOracle = "SELECT idroutines, idVehicule, to_char(dateExecution, 'DAY dd MONTH yyyy') as dateR from regulation where idVehicule = " + idVehicule;			
			requeteOracle = "SELECT * from regulation";
			resultat = requete.executeQuery(requeteOracle);


			System.out.println("Routines du vehicule numero " + idVehicule + " : ");
			String idRoutine = "";
			String dateExecution = "";
			
			while(resultat.next())
			{ 
				//Récupération des résultats
				idRoutine = resultat.getString("idroutines");
			//	dateExecution = resultat.getString("dateR");
				
				System.out.println("Routine numero " + idRoutine);// + " prevue le " + dateExecution + ".");
			}
		}
		catch (SQLException e)
		{
			System.out.println("Impossible d afficher les routines du vehicule.");
			System.out.println("Details : "+e.getMessage());
		}
	}
}
