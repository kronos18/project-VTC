package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DepotVelo 
{

	private Connection base;


	public DepotVelo(Connection base) 
	{
		this.base = base;
	}

	/**
	 * Lance la procedure pour pouvoir deposer un velo
	 *
	 */
	public void lancerProcedureDeDepot() 
	{
		try
		{
			String adresseStation;
			String idBornette;
			String idVelo;
			String codeSecret;
			String idLocation;
			String idClient;
			Scanner scanner = new Scanner(System.in);
			Statement requete = base.createStatement();
			
			System.out.print("Quel est votre code secret ? : ");
			codeSecret = scanner.nextLine();
			boolean existeClient = Location.getIdClient(requete, codeSecret) != null;
			if (existeClient)
			{
				System.out.println("Quel est votre station ? : ");
				Location.afficherLesStations(requete);
				System.out.print("Votre choix : ");
				adresseStation = scanner.nextLine();
				
				System.out.println("Choissisez la bornette : ");
				boolean bool;
				bool = afficherLesBornettesVide(requete, adresseStation);
				if (bool)
				{
					System.out.print("Votre choix : ");
					idBornette = scanner.nextLine();
					idClient = Location.getIdClient(requete, codeSecret);
					idLocation = getIdLocation(requete, idClient);
					
					mettreAjourLaLocation(requete,adresseStation,idLocation);
					
					if (clientAauMoinsUneAmende(requete,idClient))
					{
						System.out.println("Vous avez rendu le velo en retard !");
						System.out.println("Une amende vient d'etre emise a votre encontre !");
					}
					
					//si le client est non abonne et beneficie d'une remise
					if (clientAbonneARemise(requete,idClient))
					{
						afficherMessageRemise(requete, idClient);
					}
					
					else if (clientNonAbonneAremiseVplus(requete,idClient)) 
					{
						System.out.println("Vous venez de beneficier d'une remise Vplus !");
						afficherCodeRemise(requete,idClient);
					}
				}
				
				else
				{
					System.out.println("Desoler mais la station '"+ adresseStation +"' ne possede aucune bornette libre ");
					System.out.println("Veuillez choisir une autre station :");
				}
				
			}
			else
			{
				System.out.println("Desoler mais le code secret n'est pas attribue !");
			}
		}
		
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * affiche le code de la remise dont le client non abonne vient de beneficier
	 * @param requete
	 * @param idClient
	 * @throws SQLException 
	 */
	private void afficherCodeRemise(Statement requete, String idClient) throws SQLException 
	{
		String requeteOracle;
		String idRemise = null ;
		ResultSet resultat;

		requeteOracle = "SELECT * FROM remise WHERE TO_CHAR(dateExpiration,'dd/mm/yyyy:hh24:mi') = TO_CHAR(sysdate,'dd/mm/yyyy:hh24:mi') ";
		resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			idRemise = resultat.getString("idRemise");
		}
		System.out.println("Le code de la remise Vplus est : "+idRemise);
	}

	
	/**
	 * affiche un message de confirmation que le client beneficie d'une remise Vplus, 
	 * donc d'un credit temps
	 * @param idClient 
	 * @param requete 
	 * @throws SQLException 
	 */
	private void afficherMessageRemise(Statement requete, String idClient) throws SQLException 
	{
		String requeteOracle;
		String creditTemps = null ;
		ResultSet resultat;

		requeteOracle = "SELECT * FROM Abonne WHERE idClient = "+ idClient +" and creditTemps > 0";

		resultat = requete.executeQuery(requeteOracle);
		System.out.println("Vous venez de beneficier d'une remise Vplus !");
		System.out.print("Votre credit temps est de ");
		while(resultat.next())
		{ // récupération des résultats
			creditTemps = resultat.getString("creditTemps");
			System.out.print(creditTemps +" heure(s)");
		}
		System.out.println("\n");
		
	}

	private boolean clientNonAbonneAremiseVplus(Statement requete,String idClient) throws SQLException 
	{
		String requeteOracle;
		String Idremise = null ;
		ResultSet resultat;
		boolean beneficieDuneRemise = false ;

		requeteOracle = "SELECT * FROM remise WHERE TO_CHAR(dateExpiration,'dd/mm/yyyy:hh24:mi') = TO_CHAR(sysdate,'dd/mm/yyyy:hh24:mi')";

		resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			Idremise = resultat.getString("IDREMISE");
		}
		beneficieDuneRemise = Idremise != null;
		
		return beneficieDuneRemise;
	}

	private boolean clientAbonneARemise(Statement requete, String idClient) throws SQLException
	{
		String requeteOracle;
		String resIdClient = null ;
		ResultSet resultat;
		boolean beneficieDuneRemise = false ;

		requeteOracle = "SELECT * FROM Abonne WHERE idClient = "+ idClient +" and creditTemps > 0";

		resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			resIdClient = resultat.getString("idClient");
		}
		beneficieDuneRemise = resIdClient != null;
		
		return beneficieDuneRemise;
	}

	/**
	 * retourne vrai si le client a au moins une amende
	 * @param requete
	 * @param idClient 
	 * @return
	 * @throws SQLException
	 */
	private boolean clientAauMoinsUneAmende(Statement requete, String idClient ) throws SQLException {
		String requeteOracle;
		String idAmende = null ;
		ResultSet resultat;

		requeteOracle = "SELECT idAmende FROM Amende WHERE idClient = "+idClient+" and TO_CHAR(datePenalite,'dd/mm/yyyy') = TO_CHAR(sysdate,'dd/mm/yyyy')";

		resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			idAmende = resultat.getString("idAmende");
		}
		return idAmende != null;
	}


	/**
	 * retourne l'identifiant de la location pour un client donne
	 * @param requete
	 * @param idClient
	 * @return
	 * @throws SQLException
	 */
	private String getIdLocation(Statement requete, String idClient) throws SQLException
	{
		String requeteOracle;
		String idLocation = null;
		ResultSet resultat;

		requeteOracle = "SELECT idLocation FROM Location WHERE idClient = "+ idClient;

		resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			idLocation = resultat.getString("idLocation");
			System.out.println(idLocation);
		}
		return idLocation;
	}


	/**
	 * retourne l'identifiant du velo pour une bornette donnee
	 * @param requete
	 * @param idBornette
	 * @return
	 * @throws SQLException
	 */
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


	/**
	 * La fonction met a jour la location pour prendre en compte le depot du velo
	 * @param requete
	 * @param stationArrivee
	 * @param idLocation
	 * @throws SQLException
	 */
	private void mettreAjourLaLocation(Statement requete, String stationArrivee,String idLocation) throws SQLException 
	{
		String requeteOracle;
		ResultSet resultat;

		requeteOracle = "UPDATE Location "
				+ 		"SET stationArrivee = '"+ stationArrivee +"', dateRemise = sysdate "
				+ 		"WHERE idLocation =  "+ idLocation;

		resultat = requete.executeQuery(requeteOracle);
	}


	public static boolean afficherLesBornettesVide(Statement requete,String adresseStation ) throws SQLException {
		String requeteOracle;
		String idBornette = null;
		boolean isNotNull;
		requeteOracle = "SELECT idBornette FROM bornette WHERE adresseStation = '"+adresseStation+"' and idVelo is NULL and etatBornette = 'OK' ";

		ResultSet resultat = requete.executeQuery(requeteOracle);
		System.out.println("-------------------------------------\n");

		System.out.println("Les bornettes disponibles sont : ");
		System.out.println("==================================\n");

		while(isNotNull = resultat.next())
		{ // récupération des résultats
			idBornette = resultat.getString("idBornette");
			System.out.println(idBornette);
		}
		System.out.println("\n-------------------------------------\n");
		return idBornette != null;
	}

}