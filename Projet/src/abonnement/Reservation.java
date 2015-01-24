package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.DebugGraphics;

public class Reservation 
{

	private Connection base;

	public Reservation(Connection base) {
		this.base = base;
		// TODO Auto-generated constructor stub
	}

	public void lancerProcedureReservation() 
	{
		// TODO Auto-generated method stub
		ResultSet resultat;
		Statement requete;
		try {
			requete = this.base.createStatement();
			Scanner scanner = new Scanner(System.in);
			
			String finRecurrence;
			String debutRecurrence;
			String jourRecurrence;

			System.out.println("Votre nom :");
			String nom = scanner.nextLine();
			System.out.println("Votre prenom :");
			String prenom = scanner.nextLine();

			System.out.println("La date de reservation :");

			String dateReservation = scanner.nextLine();
			
			System.out.println("L'adresse de la station a laquelle vous souhaitez reserver :");
			String adresseStation = scanner.nextLine();
			
			System.out.println("Votre code secret :");
			String codeSecret = scanner.nextLine();

				String idClient = getIdAbonne(requete);
			System.out.println("On a recuperer l'id de l'abonne ");
			
//			GESTION DE LA RECURRENCE 
			System.out.println("Votre type de recurrence :");
			Recurrence.afficherChoix();
			String typeRecurrence = scanner.nextLine();

			
			
			debutRecurrence = dateReservation;
			if (typeRecurrence.equals("3"))
			{
				jourRecurrence = null;
			}
			else
			{
				jourRecurrence = dateReservation;
				
			}
			insererRecurence(requete, typeRecurrence,jourRecurrence,debutRecurrence);
			
			
//			idRecurrence, #adresseStation, #idClient, idReservation, dateReservation
//			insererReservation(nom, prenom, dateDeNaissance,sexe,adresse,codeCB);
		} 
		catch (SQLException e1) 
		{
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
		
		
	}

	private void insererRecurence(Statement requete, String typeRecurrence, String jourRecurrence, String debutRecurrence) 
	{
		//TODO inserer une recurrence
		String requeteOracle;
		ResultSet resultat;
		
		
		requeteOracle = "insert into CLIENT values ("+idClient+","+ codeSecret +","+codeCB +")";

		resultat = requete.executeQuery(requeteOracle);
		System.out.println("On a inserer une recurrence");
	}

	private String getIdAbonne(Statement requete) throws SQLException 
	{
		// TODO Auto-generated method stub
		
		ResultSet resultat;
		String requeteOracle = "SELECT CLIENT.idClient FROM ABONNE INNER JOIN "
				+ "CLIENT ON (ABONNE.idClient = CLIENT.idClient) WHERE NOMABONNE = 'Devis'"
				+ " and  PRENOMABONNE = 'Ivan' and codeSecret = 4277";
		
		resultat = requete.executeQuery(requeteOracle);
		String idClient = null; 
		
		while(resultat.next())
		{ // récupération des résultats
			System.out.println("On recupere l'id de l'abonne");
			idClient = resultat.getString("idClient");
			System.out.println("idclient :"+idClient);
		}
		return idClient;
	}
	
}
