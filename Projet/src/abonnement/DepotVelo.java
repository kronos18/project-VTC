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
			System.out.println("Quel est votre code secret ? :");
			codeSecret = scanner.nextLine();

			System.out.println("Quel est votre station ? :");
			Location.afficherLesStations(requete);

			adresseStation = scanner.nextLine();
			System.out.println("Choissisez la bornette :");

			boolean bool;
			bool = afficherLesBornettesVide(requete, adresseStation);
			if (bool)
			{
				System.out.print("Votre choix : ");
				idBornette = scanner.nextLine();
				idClient = Location.getIdClient(requete, codeSecret);
				idLocation = getIdLocation(requete, idClient);
				//			idVelo = getIdVelo(requete, idBornette);

				mettreAjourLaLocation(requete,adresseStation,idLocation);
				if (clientAauMoinsUneAmende(requete))
				{
					System.out.println("Vous avez rendu le velo en retard !");
					System.out.println("Une amende vient d'etre emise a votre encontre !");
				}
			}
			else
			{
				System.out.println("Desoler mais la station '"+ adresseStation +"' ne possede aucune bornette libre ");
				System.out.println("Veuillez choisir une autre station :");

			}

		} 
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private boolean clientAauMoinsUneAmende(Statement requete) throws SQLException {
		String requeteOracle;
		String idAmende = null ;
		ResultSet resultat;

		requeteOracle = "SELECT idAmende FROM Amende WHERE idClient = 4 and TO_CHAR(datePenalite,'dd/mm/yyyy') = TO_CHAR(sysdate,'dd/mm/yyyy')";

		//		System.out.println("La requete est : "+requeteOracle);
		resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			idAmende = resultat.getString("idAmende");
		}
		return idAmende != null;
	}


	private String getIdLocation(Statement requete, String idClient) throws SQLException {
		String requeteOracle;
		String idLocation = null ;
		ResultSet resultat;

		requeteOracle = "SELECT idLocation FROM Location WHERE idClient = "+ idClient;

		//		System.out.println("La requete est : "+requeteOracle);
		resultat = requete.executeQuery(requeteOracle);
		while(resultat.next())
		{ // récupération des résultats
			idLocation = resultat.getString("idLocation");
			System.out.println(idLocation);
		}
		return idLocation;
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


	private void mettreAjourLaLocation(Statement requete, String stationArrivee,String idLocation) throws SQLException 
	{
		String requeteOracle;
		ResultSet resultat;

		requeteOracle = "UPDATE Location "
				+ 		"SET stationArrivee = '"+ stationArrivee +"', dateRemise = sysdate "
				+ 		"WHERE idLocation =  "+ idLocation;

		//		System.out.println("La requete est : "+requeteOracle);
		resultat = requete.executeQuery(requeteOracle);
	}


	public static boolean afficherLesBornettesVide(Statement requete,String adresseStation ) throws SQLException {
		String requeteOracle;
		String idBornette = null;
		boolean isNotNull;
		requeteOracle = "SELECT idBornette FROM bornette WHERE adresseStation = '"+adresseStation+"' and idVelo is NULL and etatBornette = 'OK' ";

//				System.out.println("La requete est : "+requeteOracle);
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
		System.out.println("idbornette : " + idBornette);
		return adresseStation != null;
	}

}
