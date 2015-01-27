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

			System.out.println("Votre nom :");
			String nomAbonne = scanner.nextLine();
			System.out.println("Votre prenom :");
			String prenomAbonne = scanner.nextLine();

			System.out.println("La date de reservation :");

			String dateReservation = scanner.nextLine();
			
			System.out.println("L'adresse de la station a laquelle vous souhaitez reserver :");
			String adresseStation = scanner.nextLine();
			
			System.out.println("Votre code secret :");
			String codeSecret = scanner.nextLine();

			String idClient = getIdAbonne(requete,nomAbonne,prenomAbonne,codeSecret);
			
			System.out.println("On a recuperer l'id de l'abonne : "+idClient);
			
			if (idClient != null)
			{
//			GESTION DE LA RECURRENCE 
				System.out.println("Votre type de recurrence :");
				Recurrence.afficherChoix();
				String typeRecurrence = scanner.nextLine();
				String idRecurrence = null; 
				
				System.out.println("***********  1    *************");
				
				debutRecurrence = dateReservation;
				finRecurrence = getFinAbonnement(requete, idClient);
				System.out.println("On s'apprete a lance l'insertion de la recurrence avec debut recurrence :"+debutRecurrence +"| et finReccurence : "+finRecurrence);
				System.out.println("La date de reservation : "+dateReservation);
				

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
				
//			idRecurrence, #adresseStation, #idClient, idReservation, dateReservation
				
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
		System.out.println("La requete est : "+requeteOracle);
		resultat = requete.executeQuery(requeteOracle);
		System.out.println("On a inserer une reservation");
			
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
		System.out.println("La requete est : "+requeteOracle);
		resultat = requete.executeQuery(requeteOracle);
		System.out.println("On a inserer une recurrence");
		
	}

	private String getIdRecurrence(Statement requete, String idRecurrence)
			throws SQLException 
	{
		
		String requeteOracle = "SELECT recurrence_seq.nextval from dual";
		ResultSet resultat = requete.executeQuery(requeteOracle);
		
		while(resultat.next())
		{ // récupération des résultats
			System.out.println("On recupere les resultats");
			idRecurrence = resultat.getString("nextval");
			System.out.println("idRecurrence :"+idRecurrence);
		}
		return idRecurrence;
	}
	
	private String getFinAbonnement(Statement requete,String idClient) throws SQLException 
	{
		String finAbonnement = null;
		System.out.println("On est dans getFinAbonnement");
		String requeteOracle = "SELECT dateFinAbonnement FROM Abonne"
								+ " WHERE idClient = "+idClient;
		ResultSet resultat = requete.executeQuery(requeteOracle);
		System.out.println("FIN EXEC REQ");
		while(resultat.next())
		{ // récupération des résultats
			System.out.println("On recupere les resultats");
			finAbonnement = resultat.getString("dateFinAbonnement");
			System.out.println("finAbonnement :"+finAbonnement+" ,pour le client :"+idClient);
		}
		System.out.println("On sort de getFinAbonnement");
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
			System.out.println("On recupere l'id de l'abonne");
			idClient = resultat.getString("idClient");
			System.out.println("idclient :"+idClient);
		}
		return idClient;
	}
	
}
