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

public class Vehicule 
{

	private Connection base;

	public Vehicule(Connection base) 
	{
		this.base = base;
	}

	public void AfficherRoutines()
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Numero du vehicule :");
		String idVehicule = scanner.nextLine();


		//Interrogation de la base de donnee
		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;

			requeteOracle = "SELECT idroutines, idVehicule, to_char(dateExecution, 'DAY dd MONTH yyyy') as dateR from regulation where idVehicule = " + idVehicule;			
			resultat = requete.executeQuery(requeteOracle);


			System.out.println("Routines du vehicule numero " + idVehicule + " : ");
			String idRoutine = "";
			String dateExecution = "";

			while(resultat.next())
			{ 
				//Récupération des résultats
				idRoutine = resultat.getString("idroutines");
				dateExecution = resultat.getString("dateR");

				System.out.println("Routine numero " + idRoutine + " prevue le " + dateExecution + ".");
			}
		}
		catch (SQLException e)
		{
			System.out.println("Impossible d afficher les routines du vehicule.");
			System.out.println("Details : "+e.getMessage());
		}
	}

	public void AfficherOrdreActuelDansRoutineDunVehicule() 
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Numero du vehicule :");
		String idVehicule = scanner.nextLine();


		//Interrogation de la base de donnee
		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;

			//Récupère le plus petit ordre de l'ordreRegulation de la routine qui est en statut attente pour la voiture concernée.
			requeteOracle = "select MIN(ordreExecution) as ordreE from execution where idOrdreRegulation in ( select idOrdreRegulation from execution where status = 'attente' and idRoutines in (select idRoutines from regulation where idVehicule = " + idVehicule + " and trunc(dateExecution) = trunc(sysdate)))";			
			resultat = requete.executeQuery(requeteOracle);

			int numOrdreExec = 0;			
			while(resultat.next())
			{ 
				numOrdreExec = resultat.getInt("ordreE");
			}

			//Selectionne l'idOrdreRegulation dont les statuts sont en attente, et l'id de la routine correspond à la voiture concernée et la date de la routine est aujourd'hui
			requeteOracle = "select nomOrdreRegulation from OrdreRegulation where idOrdreRegulation in (select idOrdreRegulation from execution where status = 'attente' and ordreExecution = " + numOrdreExec + " and idroutines in(select idRoutines from regulation where idVehicule = " + idVehicule + " and trunc(dateExecution) = trunc(sysdate)))";			
			resultat = requete.executeQuery(requeteOracle);

			String nomOrdreRegulation = "";
			while(resultat.next())
			{ 
				nomOrdreRegulation = resultat.getString("nomOrdreRegulation");
			}

			if (nomOrdreRegulation == "")
				System.out.println("La voiture numero " + idVehicule + " n a pas de routine aujourd hui.");
			else
				System.out.println("Ordre en cours pour le vehicule numero " + idVehicule + " : " + nomOrdreRegulation);
		}
		catch (SQLException e)
		{
			System.out.println("Impossible d afficher l ordre actuel de la routine du vehicule.");
			System.out.println("Details : "+e.getMessage());
		}
	}

	public void ModifierRoutine() 
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Numero de la routine :");
		String idRoutine = scanner.nextLine();
		String ordreExecution = "";
		String nomOrdreRegulation = "";


		//Interrogation de la base de donnee
		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;

			//Affiche les ordres de la routine	
			requeteOracle = "select e.ordreExecution, o.nomOrdreRegulation from execution e, OrdreRegulation o where e.idOrdreRegulation = o.idOrdreRegulation and e.idroutines = " + idRoutine + " order by e.ordreExecution";
			resultat = requete.executeQuery(requeteOracle);

			System.out.println("Ordres de la routine " + idRoutine + " : ");
			while(resultat.next())
			{ 
				ordreExecution = resultat.getString("ordreExecution");
				nomOrdreRegulation = resultat.getString("nomOrdreRegulation");
				System.out.println("\t" + ordreExecution + " : " + nomOrdreRegulation);
			}

			System.out.println("");
			System.out.println("Que voulez-vous faire :");
			System.out.println("1 : Changer la priorite d un ordre");
			System.out.println("2 : Ajouter un ordre");
			System.out.println("3 : Supprimer un ordre");
			String choix = scanner.nextLine();

			switch (choix) 
			{
			case "1" :
				this.ModifierPrioDunOrdre(idRoutine, requete);
				break;

			case "2":
				this.AjouterUnOrdreALaRoutine(idRoutine, requete);
				break;
				
			case "3":
				this.SupprimerUnOrdreDeLaRoutine(idRoutine, requete);
				break;
			}
		}
		catch (SQLException e)
		{
			System.out.println("Impossible d afficher la routine du vehicule.");
			System.out.println("Details : "+e.getMessage());
		}
	}

	private void SupprimerUnOrdreDeLaRoutine(String idRoutine, Statement requete) 
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Identifiant de l ordre de regulation : ");
		String idOrdreRegul = scanner.nextLine();

		try
		{
			//Ajoute un ordre a la priorite
			String requeteOracle = "delete from execution where idOrdreExecution = " + idOrdreRegul + " and idroutines = " + idRoutine;
			ResultSet resultat = requete.executeQuery(requeteOracle);
			
			System.out.println("Ordre supprime de la routine.");
		} 
		catch (SQLException e) 
		{
			System.out.println("Impossible de supprimer un ordre de la routine.");
			System.out.println("Details : "+e.getMessage());
		}
	}

	private void AjouterUnOrdreALaRoutine(String idRoutine, Statement requete) 
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Identifiant de l ordre de regulation : ");
		String idOrdreRegul = scanner.nextLine();

		System.out.println("Priorite de l ordre de regulation : ");
		String PrioOrdre = scanner.nextLine();

		try
		{
			//Ajoute un ordre a la priorite
			String requeteOracle = "INSERT INTO execution values (" + idOrdreRegul + "," + idRoutine + "," + PrioOrdre + ",'attente')";
			ResultSet resultat = requete.executeQuery(requeteOracle);
			
			System.out.println("Ordre ajoute a la routine.");
		} 
		catch (SQLException e) 
		{
			System.out.println("Impossible d ajouter un ordre a la routine.");
			System.out.println("Details : "+e.getMessage());
		}
	}

	private void ModifierPrioDunOrdre(String idRoutine, Statement requete) 
	{
		//Recuperation des informations via le menu
		Scanner scanner = new Scanner(System.in);

		System.out.println("Priorite actuelle de l ordre a changer : ");
		String oldPrio = scanner.nextLine();

		System.out.println("Nouvelle priorite de l ordre : ");
		String NewPrio = scanner.nextLine();

		try
		{
			//Modifie un ordre de priorite  NE MARCHE PAS
			String requeteOracle = "update execution set ordreExecution = " + NewPrio + " where ordreExecution = " + oldPrio + " and idroutines = " + idRoutine;
			ResultSet resultat = requete.executeQuery(requeteOracle);
			
			System.out.println("Mise a jour effectuee.");
		} 
		catch (SQLException e) 
		{
			System.out.println("Impossible de modifier la routine.");
			System.out.println("Details : "+e.getMessage());
		}
	}
}
