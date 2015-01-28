package abonnement;

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

	public void AfficherNbVeloDansStation() 
	{
		//Interrogation de la base de donnee
		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;
			int NbVelo = 0;
			String adresseStation = "";

			//Récupère le nombre de velo dans chaque station
			requeteOracle = "select count(*) as NbVelo, adresseStation from bornette where idVelo is not NULL GROUP BY adresseStation";
			resultat = requete.executeQuery(requeteOracle);

			System.out.println("Nombre de Velos disponibles\tStation");
			System.out.println("----------------------------------------------");
			
			while(resultat.next())
			{ 
				NbVelo = resultat.getInt("NbVelo");
				adresseStation = resultat.getString("adresseStation");
				System.out.println(NbVelo + "\t\t\t\t" + adresseStation);
			}
			System.out.println("");
			resultat.close();
			
			

			//Récupère le nombre de velo endommagé dans chaque station
			requeteOracle = "select count(*) as NbVelo, adresseStation from bornette where idVelo in (select idVelo from velo where etatVelo = 'HS') GROUP BY adresseStation";
			resultat = requete.executeQuery(requeteOracle);

			System.out.println("Nombre de Velos endommagés\tStation");
			System.out.println("----------------------------------------------");
			
			while(resultat.next())
			{ 
				NbVelo = resultat.getInt("NbVelo");
				adresseStation = resultat.getString("adresseStation");
				System.out.println(NbVelo + "\t\t\t\t" + adresseStation);
			}
			System.out.println("");
			resultat.close();
			
			

			//Récupère le nombre de places libre dans chaque station
			requeteOracle = "select count(*) as NbPlace, adresseStation from bornette where idVelo is NULL and etatBornette = 'OK' GROUP BY adresseStation";
			resultat = requete.executeQuery(requeteOracle);

			System.out.println("Nombre de places libres\t\tStation");
			System.out.println("----------------------------------------------");
			
			while(resultat.next())
			{ 
				NbVelo = resultat.getInt("NbPlace");
				adresseStation = resultat.getString("adresseStation");
				System.out.println(NbVelo + "\t\t\t\t" + adresseStation);
			}
			System.out.println("");
			resultat.close();			

		}
		catch (SQLException e)
		{
			System.out.println("Impossible de consulter les nombres de velo dans chaque station.");
			System.out.println("Details : "+e.getMessage());
		}
	}
}
