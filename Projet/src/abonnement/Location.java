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


	/**
	 * la fonction permet de lancer la procedure de location d'un abonne
	 */
	private void lanceLocationNonAbonne() 
	{
		try
		{
			String dateRemise,idLocation,idTarif,idVelo,dateLocation;
			String idClient;
			ResultSet resultat;
			Statement requete = base.createStatement();
			Scanner scanner = new Scanner(System.in);
			String codeSecret;
			
			System.out.println("Veuillez entrer votre code de carte bleu svp ! : ");
			String codeCB = scanner.nextLine();
			
			System.out.println("Merci !");
			
			Random random = new Random();
			codeSecret = genererUnCodeSecret(random);
			System.out.println("Votre code secret est le "+codeSecret);

			idTarif = "1";
			insererUnClient(codeCB, requete, codeSecret);
			
			
			System.out.println("Quelle station ?");
			afficherLesStations(requete);
			
			
			
			System.out.println("Votre choix : ");
			String adresseStation = scanner.nextLine();
			
			afficherLesBornettesDisponiblesALaStation(requete,adresseStation);
			System.out.print("Quelle bornette ? : ");
			String idBornette = scanner.nextLine();

			idVelo = getIdVelo(requete,idBornette);
			System.out.println("lid velo est : "+idVelo);
//			insererUneLocation(requete,idTarif,idClient,idVelo, adresseStation);
					
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private String genererUnCodeSecret(Random random)
	{
		String codeSecret = String.valueOf(random.nextInt(9));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		return codeSecret;
	}

	
	private ResultSet insererUnClient(String codeCB, Statement requete, String codeSecret)
			throws SQLException 
	{

		String requeteOracle;
		ResultSet resultat;
		
		
		requeteOracle = "insert into CLIENT values (Client_seq.nextval,"+ codeSecret +","+codeCB +")";

		resultat = requete.executeQuery(requeteOracle);
		System.out.println("On a inserer un client");
		return resultat;
	}
	
	
	/**
	 * la fonction permet de lancer la procedure de location d'un abonne
	 */
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
			afficherLesStations(requete);
			
			
			
			System.out.println("Votre choix : ");
			String adresseStation = scanner.nextLine();
			
			afficherLesBornettesDisponiblesALaStation(requete,adresseStation);
			System.out.print("Quelle bornette ? : ");
			String idBornette = scanner.nextLine();
			
			idVelo = getIdVelo(requete,idBornette);
			System.out.println("lid velo est : "+idVelo);
			insererUneLocation(requete,idTarif,idClient,idVelo, adresseStation);
			
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	/**
	 * La methode affiche toutes les stations de la base de donnee
	 * @param requete
	 * @throws SQLException
	 */
	private void afficherLesStations(Statement requete) throws SQLException 
	{
		String requeteOracle;
		String adresseStation;
		requeteOracle = "SELECT * FROM Station";
		
//		System.out.println("La requete est : "+requeteOracle);
		ResultSet resultat = requete.executeQuery(requeteOracle);
		System.out.println("-------------------------------------\n");
		System.out.println("Les stations sont : ");
		System.out.println("====================\n");
		
		while(resultat.next())
		{ // récupération des résultats
			adresseStation = resultat.getString("ADRESSESTATION");
			System.out.println(adresseStation);
		}
		System.out.println("\n-------------------------------------\n");
		
	}

	private String getIdVelo(Statement requete, String idBornette) throws SQLException 
	{
		String requeteOracle;
		ResultSet resultat;
		String idVelo = null;
		
		requeteOracle = "SELECT idVelo FROM Bornette WHERE idBornette = "+idBornette;
		
//		System.out.println("La requete est : "+requeteOracle);
		resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			idVelo = resultat.getString("idVelo");
			System.out.println(idVelo);
		}
		return idVelo;
	}

	private void afficherLesBornettesDisponiblesALaStation(Statement requete,String adresseStation) throws SQLException 
	{
		String requeteOracle;
		ResultSet resultat;
		
		requeteOracle = "SELECT idBornette,idVelo FROM Bornette WHERE adresseStation = '"+adresseStation+"' AND idVelo is NOT null";
		
//		System.out.println("La requete est : "+requeteOracle);
		resultat = requete.executeQuery(requeteOracle);
		String idBornette;
		System.out.println("Les bornettes qui contient des velos pour la station '"+adresseStation + "' sont : ");
		while(resultat.next())
		{ // récupération des résultats
			idBornette = resultat.getString("idBornette");
			System.out.println(idBornette);
		}
	}

	private void insererUneLocation(Statement requete,String idTarif, String idClient
			, String idVelo,String stationDepart) throws SQLException
	{
		// TODO INSERER LA LOCATION
		String requeteOracle;
		ResultSet resultat;
		
		requeteOracle = "insert into LOCATION values (Location_seq.nextVal,"+ idTarif +","+idClient+","+idVelo+",sysdate ,NULL,'"+ stationDepart +"',NULL)";
		
//		System.out.println("La requete est : "+requeteOracle);
		resultat = requete.executeQuery(requeteOracle);
		System.out.println("Votre location a bien ete pris en compte !");
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
