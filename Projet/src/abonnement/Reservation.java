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
	}

	public void lancerProcedureReservation() 
	{
		ResultSet resultat;
		Statement requete;
		try {
			requete = this.base.createStatement();
			Scanner scanner = new Scanner(System.in);
			
			String finRecurrence;
			String debutRecurrence;
			String jourRecurrence = null;

			System.out.print("Votre nom : ");
			String nomAbonne = scanner.nextLine();
			System.out.print("Votre prenom : ");
			String prenomAbonne = scanner.nextLine();

			System.out.print("La date de reservation : ");

			String dateReservation = scanner.nextLine();
			
			Location.afficherLesStations(requete);
			System.out.print("L'adresse de la station a laquelle vous souhaitez reserver : ");
			String adresseStation = scanner.nextLine();
			
			System.out.print("Votre code secret : ");
			String codeSecret = scanner.nextLine();

			String idClient = getIdAbonne(requete,nomAbonne,prenomAbonne,codeSecret);
			
			
			if (idClient != null)
			{
//			GESTION DE LA RECURRENCE 
				System.out.println("Votre type de recurrence : ");
				Recurrence.afficherChoix();
				System.out.println("Votre choix : ");
				String typeRecurrence = scanner.nextLine();
				String idRecurrence = null; 
				
				debutRecurrence = dateReservation;
				finRecurrence = getFinAbonnement(requete, idClient);
				jourRecurrence = miseAjourDuJourDeLaRecurrence(dateReservation, typeRecurrence);
				
				if (!typeRecurrence.equals("0"))
				{
					idRecurrence = getIdRecurrence(requete, idRecurrence);
					insererRecurence(requete,idRecurrence, typeRecurrence,jourRecurrence,debutRecurrence,finRecurrence);
					
				}
				else
				{
					idRecurrence = "0";
				}
				insererReservation(requete,idRecurrence, adresseStation, idClient,dateReservation);
				
			}
			else
			{
				System.out.println("Desoler mais vous n'avez pas entrer les bonnes informations !");
			}
			
		} 
		catch (SQLException e1) 
		{
			System.out.println(e1.getMessage());
		}
		
		
	}

	private String miseAjourDuJourDeLaRecurrence(String dateReservation, String typeRecurrence) {
		String jourRecurrence;
		if (typeRecurrence.equals("3"))
		{
			jourRecurrence = null;
		}
		else
		{
			jourRecurrence = dateReservation;
			
		}
		return jourRecurrence;
	}
	
	
	private void insererReservation(Statement requete, String idRecurrence, String adresseStation,
			String idClient, String dateReservation) throws SQLException
	{
		String requeteOracle;
		ResultSet resultat;
		
		requeteOracle = "insert into RESERVATION values (Reservation_seq.nextVal,'"+adresseStation+"',"+idClient+","+idRecurrence+",to_date('"+dateReservation+"', 'dd/mm/yyyy'))";
		resultat = requete.executeQuery(requeteOracle);
		System.out.println("Votre reservation a bien ete pris en compte ! ");
			
	}

	
	
	private void insererRecurence(Statement requete, String idRecurrence, String typeRecurrence, String jourRecurrence, String debutRecurrence, String finRecurrence) throws SQLException 
	{
		String requeteOracle;
		ResultSet resultat;
		
		String jourRecurrenceTraitement;
		
		jourRecurrenceTraitement = jourRecurrence;
		if (jourRecurrenceTraitement != null) 
		{
			requeteOracle = "insert into RECURRENCE values ("+idRecurrence+","+typeRecurrence+",to_date('"+ jourRecurrence +"', 'dd/mm/yyyy'),to_date('"+ debutRecurrence +"', 'dd/mm/yyyy'),to_date('"+ finRecurrence +"', 'yyyy-mm-dd:HH24:MI:SS'))";			
		}
		else
		{
			requeteOracle = "insert into RECURRENCE values ("+idRecurrence+","+typeRecurrence+",null,to_date('"+ debutRecurrence +"', 'dd/mm/yyyy'),to_date('"+ finRecurrence +"', 'yyyy-mm-dd:HH24:MI:SS'))";
		}
		resultat = requete.executeQuery(requeteOracle);
	}

	
	
	private String getIdRecurrence(Statement requete, String idRecurrence)
			throws SQLException 
	{
		
		String requeteOracle = "SELECT recurrence_seq.nextval from dual";
		ResultSet resultat = requete.executeQuery(requeteOracle);
		
		while(resultat.next())
		{ // récupération des résultats
			idRecurrence = resultat.getString("nextval");
		}
		return idRecurrence;
	}
	
	private String getFinAbonnement(Statement requete,String idClient) throws SQLException 
	{
		String finAbonnement = null;
		String requeteOracle = "SELECT dateFinAbonnement FROM Abonne"
								+ " WHERE idClient = "+idClient;
		ResultSet resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			finAbonnement = resultat.getString("dateFinAbonnement");
		}
		return finAbonnement;		
	}
	
	private String getIdAbonne(Statement requete, String nomAbonne, String prenomAbonne, String codeSecret) throws SQLException 
	{
		ResultSet resultat;
		String requeteOracle = "SELECT CLIENT.idClient FROM ABONNE INNER JOIN "
				+ "CLIENT ON (ABONNE.idClient = CLIENT.idClient) WHERE NOMABONNE = '"+nomAbonne+"'"
				+ " and  PRENOMABONNE = '"+prenomAbonne+"' and codeSecret = "+codeSecret;
		
		resultat = requete.executeQuery(requeteOracle);
		String idClient = null; 
		
		while(resultat.next())
		{ // récupération des résultats
			idClient = resultat.getString("idClient");
		}
		return idClient;
	}
	
}