package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class OrdreRegulation {

	private Connection base;
	
	public OrdreRegulation(Connection base) 
	{
		this.base = base;
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

	public void ValiderOrdre() {
		String nomOrdreRegulation ="";
		String adresseStation ="";
		ResultSet resultat;
		Scanner scan = new Scanner(System.in);
		String requeteOracle = "select idOrdreRegulation from OrdreRegulation where nomOrdreRegulation = ";
				
		System.out.println(" Menu Valider ordre !");
		System.out.println("---------------------------");
		
		
		System.out.print(" Indiquer l'ordre que vous etiez en train de faire :");
		String ordreEnCours = scan.nextLine();
		
		int idOrdreRegulation = 0;
		requeteOracle+="'"+ ordreEnCours + "';";
		try
		{
			Statement requete = base.createStatement();
			resultat = requete.executeQuery(requeteOracle);
			idOrdreRegulation = resultat.getInt("idOrdreRegulation");
		}
		catch (SQLException e)
		{
			System.out.println("Impossible de recupérer l'id de l'ordre de regulation");
			System.out.println("Details : "+e.getMessage());
			System.out.println("La requête était : "+requeteOracle);
		}
		
		// cas echec
		if(idOrdreRegulation == 0)
			return;
		
		requeteOracle = "update Execution set status = ";
		System.out.print(" Indiquer si vous souhaitez annuler (1) ou valider (2) l'ordre :");
		int choix = 0;
		choix = Integer.parseInt(scan.nextLine());
		if(choix == 1)
			requeteOracle += "'annule'";
		else
			requeteOracle += "'effectuee'";	
		
		
		System.out.print(" Indiquer la routine que vous etes en train de faire ?");
		int idRoutine = Integer.parseInt(scan.nextLine());
		requeteOracle += " where idRoutines = " + idRoutine + " and idOrdreRegulation = " + idOrdreRegulation +";";
		try
		{
			Statement requete = base.createStatement();
			resultat = requete.executeQuery(requeteOracle);
		}
		catch (SQLException e)
		{
			System.out.println("Impossible de modifier l'execution");
			System.out.println("Details : "+e.getMessage());
			System.out.println("La requête était : "+requeteOracle);
		}
		
	}
}
