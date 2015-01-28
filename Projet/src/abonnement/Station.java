package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Station
{

	private Connection base;

	public Station(Connection base) 
	{
		this.base = base;
	}

	public void AfficherPlagesHorairesVmoinsVplus()
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Choisissez une station :");
		String adresseStation = scanner.nextLine();

		//Interrogation de la base de donnee
		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;

			requeteOracle = "SELECT typeActuel, heureDebut, heureFin FROM classification WHERE adresseStation = '" + adresseStation + "' AND typeActuel != 'Vnul'";			
			resultat = requete.executeQuery(requeteOracle);


			System.out.println("Plages horaires Vmoins/Vplus de la station  " + adresseStation + " : ");
			System.out.println("Plages horaires\t\tClassification");
			System.out.println("----------------------------------------------");
			String typeActuel = "";
			String heureDebut = "";
			String heureFin = "";

			while(resultat.next())
			{ 
				//Récupération des résultats
				typeActuel = resultat.getString("typeActuel");
				heureDebut = resultat.getString("heureDebut");
				heureFin = resultat.getString("heureFin");

				System.out.println(heureDebut + "-" + heureFin + "\t\t\t" + typeActuel);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Impossible d afficher les plages horaires de la station.");
			System.out.println("Details : "+e.getMessage());
		}
	}

	public void ModifierClassificationPlageHoraireStation() 
	{	
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Choisissez une station :");
		String adresseStation = scanner.nextLine();
		
		System.out.println("Choisissez une heure de debut de plage horaire :");
		String heureDebut = scanner.nextLine();

		System.out.println("Choisissez une classification (Vmoins ou Vplus) :");
		String typeProgramme = scanner.nextLine();

		//Interrogation de la base de donnee
		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;

			requeteOracle = "update CLASSIFICATION set typeProgramme = '" + typeProgramme + "' where adresseStation = '" + adresseStation + "' and heureDebut = " + heureDebut;			
			resultat = requete.executeQuery(requeteOracle);


			System.out.println("Mise a jour effectuee. La modification sera prise en compte demain.");
		}
		catch (SQLException e)
		{
			System.out.println("Impossible de modifier la plage horaire de la station.");
			System.out.println("Details : "+e.getMessage());
		}
	}

	public void AfficherVehiculeEtOrdrePrevusPourUneStation() 
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Choisissez une station :");
		String adresseStation = scanner.nextLine();
		

		//Interrogation de la base de donnee
		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;

			//Récupère les véhicules
			requeteOracle = "select idVehicule from regulation where idroutines in (select idroutines from execution where status = 'attente' and idOrdreRegulation in (select idOrdreRegulation from OrdreRegulation where adressestation = '" + adresseStation + "')) group by idvehicule";			
			resultat = requete.executeQuery(requeteOracle);
			
			System.out.println("Vehicule\tNombre de place disponibles\tPriorite\tNom de l ordre");
			System.out.println("------------------------------------------------------------------------------------");
			
			//Pour chaque vehicule, affiche le vehicule, les places dispo, le nom de l'ordre a effectuer et son ordre de priorité
			String idVehicule = "";
			String NbPlaceDispo = "";
			String NomOrdre = "";
			String PrioOrdre = "";
			while (resultat.next())
			{
				idVehicule = resultat.getString("idVehicule");
				
				//Récupère le nombre de place disponible pour le vehicule
				requeteOracle = "select placeDisponible from vehicule where idVehicule = " + idVehicule;
				ResultSet sousResultat = requete.executeQuery(requeteOracle);
				while (sousResultat.next())
					NbPlaceDispo = sousResultat.getString("placeDisponible");
				
				//Récupère le nom de l'ordre ainsi que sa priorité
				requeteOracle = "select o.nomOrdreRegulation, e.ordreExecution from OrdreRegulation o, Execution e where o.adresseStation = 'Meylan' and e.status = 'attente' and o.idOrdreRegulation = e.idOrdreRegulation and e.idroutines in ( select idroutines from regulation where idVehicule = 2 )";
				sousResultat = requete.executeQuery(requeteOracle);
				while (sousResultat.next())
				{
					NomOrdre = sousResultat.getString("nomOrdreRegulation");
					PrioOrdre = sousResultat.getString("ordreExecution");
				}
				sousResultat.close();
				
				System.out.println(idVehicule + "\t\t" + NbPlaceDispo + "\t\t\t\t" + PrioOrdre + "\t\t" + NomOrdre);
			}
			resultat.close();
		}
		catch (SQLException e)
		{
			System.out.println("Impossible de consulter les vehicules et ordre prevus pour la station.");
			System.out.println("Details : "+e.getMessage());
		}
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
