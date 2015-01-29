package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Action {

	private Connection base;
	
	public Action(Connection base) 
	{
		this.base = base;
	}

	public void SaisirAction() {
		ResultSet resultat;
		String requeteOracle = "insert into action values(action_seq.nextval,";
		
		System.out.println(" Menu Ajouter une action !");
		System.out.println("---------------------------");
		System.out.println(" S'agit - il d'un velo (1) ou d'une bornette (2) ?");
		System.out.print("choix :");
		int choix = 0;
		Scanner scan = new Scanner(System.in);
		choix = Integer.parseInt(scan.nextLine());
		
		if(choix ==  1)
		{
			System.out.println("Quel est l'identifiant du velo ?");
			System.out.print("idVelo :");
			int idvelo = 0;
			idvelo = Integer.parseInt(scan.nextLine());
			requeteOracle+=idvelo+",null,";
		}
		else
		{
			System.out.println("Quel est l'identifiant de la bornette ?");
			System.out.print("idBornette :");
			int idBornette = 0;
			idBornette = Integer.parseInt(scan.nextLine());
			requeteOracle+="null," +idBornette+",";
		}
		
		System.out.println("Quel est le nom de l'action ?");
		System.out.print("nom action :");
		String nomAction = "";
		nomAction = scan.nextLine();
		requeteOracle+="'" + nomAction + "',";
		
		System.out.println("Qu'avez vous fait ?");
		System.out.print("rapport action :");
		String rapportAction = "";
		rapportAction = scan.nextLine();
		requeteOracle+="'" + rapportAction + "',";
		
		System.out.println("Y etes vous arrive ?");
		System.out.println("1 - oui");
		System.out.println("2 - non");
		System.out.print("choix :");
		choix = Integer.parseInt(scan.nextLine());
		if(choix == 1)
			requeteOracle+="'effectuee'";
		else
			requeteOracle+="'annule'";

		requeteOracle+=")";
		
		try
		{
			Statement requete = base.createStatement();
			resultat = requete.executeQuery(requeteOracle);
			System.out.print("L'action saisis a ete correctement enregistre");
		}
		catch (SQLException e)
		{
			System.out.println("Impossible d afficher les ordres des routines");
			System.out.println("Details : "+e.getMessage());
			System.out.println("La requête était : "+requeteOracle);
		}
	}
}
