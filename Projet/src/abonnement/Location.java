package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Location 
{
	private Connection base;

	public Location(Connection base)
	{
		this.base = base;
	}

	public void lancerProcedureLocation() 
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Vous etes :");
		System.out.println("1: Abonne ");
		System.out.println("2: Non abonne ");
		int choix = scanner.nextInt();
		scanner.nextLine();
		traiterLeChoix(choix);

		
	}

	private void traiterLeChoix(int choix) {
		switch (choix)
		{
			case 1:
				lanceLocationAbonne();
				break;
			
			case 2:
				
				lanceLocationNonAbonne();
				break;
	
			default:
				break;
		}
	}

	private void lanceLocationNonAbonne()
	{
		
	}

	private void lanceLocationAbonne() 
	{
		try
		{
			String dateRemise,idLocation,idTarif,idVelo,dateLocation;
			String idClient;
			ResultSet resultat;
			Statement requete = base.createStatement();
			Scanner scanner = new Scanner(System.in);
			
			System.out.println("Votre nom :");
			String nom = scanner.nextLine();
			
			System.out.println("Votre prenom :");
			String prenom = scanner.nextLine();
			
			System.out.println("Votre code secret:");
			String codeSecret = scanner.nextLine();
			idTarif = "2";
			idClient = getIdAbonne(requete,nom,prenom,codeSecret);
			
			System.out.println("Quelle station ?");
			String adresseStation = scanner.nextLine();
			
			System.out.println("Quel borne ?");
			afficherLesVelosDisponiblesALaStation(requete,adresseStation);
			String idBornette = scanner.nextLine();
			idVelo = getIdVelo(requete,idBornette);
			insererUneLocation(requete,idTarif,idClient,idVelo);
			
		
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private String getIdVelo(Statement requete, String idBornette) {
		// TODO Auto-generated method stub
		return null;
	}

	private void afficherLesVelosDisponiblesALaStation(Statement requete,
			String adresseStation) {
		// TODO Auto-generated method stub
		
	}

	private void insererUneLocation(Statement requete, String idLocation,
			String idTarif, String idClient)
	{
		// TODO INSERER LA LOCATION
		
	}

	private String getIdAbonne(Statement requete, String nomAbonne, String prenomAbonne, String codeSecret) throws SQLException 
	{
		// TODO Auto-generated method stub
		
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
