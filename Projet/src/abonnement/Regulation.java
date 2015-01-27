package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Regulation {

	private Connection base;
	
	public Regulation(Connection base) 
	{
		this.base = base;
	}
	
	public void AfficherRoutines()
	{
		String requeteOracle="";
		try
		{
		
			Statement requete = base.createStatement();
			ResultSet resultat;
			Boolean resultFound = false;
	
			requeteOracle= "select distinct idroutines from execution where idroutines not in (select idRoutines from regulation where trunc(dateExecution) = trunc(sysdate));";
			
			resultat = requete.executeQuery(requeteOracle);
			
			String idRoutine = "";
			while(resultat.next())
			{ 
				if(!resultFound)
				{
					System.out.println("La (les) routine(s) qui n'a pas ete effectue est la suivante :");
					resultFound = true;
				}

				idRoutine = resultat.getString("idroutines");
				afficherOrdreRegulation(idRoutine);
			}
			if (!resultFound)
				System.out.println("Aucune routine à effectuer");
		}
		catch (SQLException e)
		{
			System.out.println("Impossible d afficher les routines");
			System.out.println("Details : "+e.getMessage());
			System.out.println("La requête était : "+requeteOracle);
		}
	}
	
	public void afficherOrdreRegulation(String idRoutine)
	{
		String nomOrdreRegulation ="";
		String adresseStation ="";
		ResultSet resultat;
		String requeteOracle = "";
		try
		{

			Statement requete = base.createStatement();
			requeteOracle = "Select * from OrdreRegulation inner join Execution on execution.idOrdreRegulation = OrdreRegulation.idOrdreRegulation where idRoutines = "+idRoutine+";";
			
			resultat = requete.executeQuery(requeteOracle);
	
			System.out.println("Routine numero " + idRoutine);
			while(resultat.next())
			{
				adresseStation = resultat.getString("idroutines");
				nomOrdreRegulation = resultat.getString("nomOrdreRegulation");
				System.out.println("	Station : " + adresseStation);
				System.out.println("	Ordre : " + nomOrdreRegulation);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Impossible d afficher les ordres des routines");
			System.out.println("Details : "+e.getMessage());
			System.out.println("La requête était : "+requeteOracle);
		}
	}

	
	public void DemarrerRoutines() {
		
		System.out.print("La routine que vous souhaitez réaliser :");
		int idRoutine = 0;
		Scanner scan = new Scanner(System.in);
		idRoutine = scan.nextInt();
		
		int idVehicule = choixVoitureDispo();
		
		System.out.print("Nous vous reservons le vehicule et la routine pour aujourd'hui");
		
		String requeteOracle = "insert into regulation values(" + idRoutine + "," + idVehicule + ", sysdate);";
		
		Statement requete;
		try {
			requete = base.createStatement();
			ResultSet resultat = requete.executeQuery(requeteOracle);
		} catch (SQLException e) {
			System.out.println("Impossible de demarrer la routine : creation de la regulation echec");
			System.out.println("Details : "+e.getMessage());
			System.out.println("La requête était : "+requeteOracle);
		}
		
		
		System.out.print("Rentrez votre identifiant employe :");
		int idEmp = 0;
		Scanner scan2 = new Scanner(System.in);
		idEmp = scan2.nextInt();
		
		requeteOracle = "insert into Conduit values(" + idVehicule + "," + idEmp + ", sysdate);";

		try {
			requete = base.createStatement();
			ResultSet resultat = requete.executeQuery(requeteOracle);
		} catch (SQLException e) {
			System.out.println("Impossible de demarrer la routine :  : creation de la conduite echec");
			System.out.println("Details : "+e.getMessage());
			System.out.println("La requête était : "+requeteOracle);
		}

	}
	
	public int choixVoitureDispo()
	{
		String requeteOracle ="";
		Boolean resultFound = false;
		String idVehicule = "";
		String modeleVehicule = "";
		String placeDisponible = "";
		try {
			Statement requete = base.createStatement();
			requeteOracle ="select * from vehicule where idVehicule not in(select idVehicule from regulation where trunc(dateExecution) = trunc(sysdate));";
			ResultSet resultat = requete.executeQuery(requeteOracle);

			while(resultat.next())
			{ 
				if(!resultFound)
				{
					System.out.println("Les voitures disponibles sont :");
					resultFound = true;
				}
				
				idVehicule = resultat.getString("idVehicule");
				modeleVehicule = resultat.getString("modeleVehicule");
				placeDisponible = resultat.getString("placeDisponible");
				System.out.println("id : "+idVehicule);
				System.out.println("	modele : "+modeleVehicule);
				System.out.println("	place dispo : "+placeDisponible);
			}
		
		} catch (SQLException e) {
			System.out.println("Impossible d afficher les voitures disponible pour aujourd'hui");
			System.out.println("Details : "+e.getMessage());
			System.out.println("La requête était : "+requeteOracle);
		}
		
		System.out.print("Le vehicule que vous souhaitez prendre :");
		int idVehiculeChoix = 0;
		Scanner scan = new Scanner(System.in);
		idVehiculeChoix = scan.nextInt();
		
		return idVehiculeChoix;
	}
}
