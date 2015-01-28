package ressources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Bornette 
{

	private Connection base;

	
	public Bornette(Connection base) 
	{
		this.base = base;
	}
	
	
	public void afficherVplus(Statement requete) throws SQLException 
	{
		// TODO Auto-generated method stub
		String requeteOracle;
		String adresseStation;
		
		requeteOracle = "SELECT distinct(adresseStation), heureDebut,heureFin FROM Classification "
				+ 		"where TO_NUMBER(TO_CHAR(sysdate,'hh24')) >= heureDebut and TO_NUMBER(TO_CHAR(sysdate,'hh24')) < heureFin and typeActuel = 'Vplus'";
		
//		System.out.println("La requete est : "+requeteOracle);
		ResultSet resultat = requete.executeQuery(requeteOracle);
		System.out.println("-------------------------------------\n");
		System.out.println("Les stations vPlus actuelles sont : ");
		System.out.println("===================================\n");
		
		while(resultat.next())
		{ // récupération des résultats
			adresseStation = resultat.getString("ADRESSESTATION");
			System.out.println(adresseStation);
		}
		System.out.println("\n-------------------------------------\n");
	}

	public void afficherVmoins(Statement requete) throws SQLException 
	{
		String requeteOracle;
		String adresseStation;
		requeteOracle = "SELECT distinct(adresseStation), heureDebut,heureFin FROM Classification "
				+ 		"where TO_NUMBER(TO_CHAR(sysdate,'hh24')) >= heureDebut and TO_NUMBER(TO_CHAR(sysdate,'hh24')) < heureFin and typeActuel = 'Vmoins'";
		
//		System.out.println("La requete est : "+requeteOracle);
		ResultSet resultat = requete.executeQuery(requeteOracle);
		System.out.println("-------------------------------------\n");
		System.out.println("Les stations Vmoins actuelles sont : ");
		System.out.println("=====================================\n");
		
		while(resultat.next())
		{ // récupération des résultats
			adresseStation = resultat.getString("ADRESSESTATION");
			System.out.println(adresseStation);
		}
		System.out.println("\n-------------------------------------\n");		
	}


	public void lancerAffichageDesBornesVplusEtVmoins()
	{
		try
		{
			Statement requete = base.createStatement();
			afficherVplus(requete);
			afficherVmoins(requete);
			
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
