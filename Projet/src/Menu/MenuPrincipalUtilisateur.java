package Menu;

import java.sql.Connection;
import java.util.Scanner;

import ressources.Bornette;
import abonnement.Abonne;
import abonnement.DepotVelo;
import abonnement.Location;
import abonnement.Reservation;


public class MenuPrincipalUtilisateur implements Menu 
{

	private boolean aQuitte;
	private MenuPrincipal menuPrincipal;

	public MenuPrincipalUtilisateur(MenuPrincipal menuPrincipal) 
	{
		this.menuPrincipal = menuPrincipal;
	}

	@Override
	public String getNomMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afficherMenu() 
	{
		System.out.println("---------------------------------------------");
		System.out.println("Menu Utilisateur ! ");
		System.out.println("Que desirez-vous ? ");
		System.out.println("1 : Creer un abonnement !");
		System.out.println("2 : Effectuer une reservation !");
		System.out.println("3 : Effectuer une location !");
		System.out.println("4 : Deposer un velo !");
		System.out.println("5 : Afficher les stations Vplus Vmoins !");
		System.out.println("6 : Retourner au menu principal !");
		System.out.println("---------------------------------------------");
		System.out.print("Votre choix : ");
	}


	@Override
	public void lancer() 
	{
		while (!this.aQuitte) 
		{
			afficherMenu();
			traiterLeChoix();
		}		
	}

	@Override
	public void traiterLeChoix()
	{
		int choix = 0;
		Scanner scan = new Scanner(System.in);
		choix = scan.nextInt();
		Connection base = menuPrincipal.getBase();
		
		switch (choix) 
		{
		case 1:
			Abonne abonne = new Abonne(base);
			abonne.lancerProcedureAbonnement();
			break;
		case 2:
			Reservation reservation = new Reservation(base);
			reservation.lancerProcedureReservation();
			break;
		case 3:
			Location location;
			location = new Location(base);
			location.lancerProcedureLocation();
			break;
		case 4:
			DepotVelo depotVelo;
			depotVelo = new DepotVelo(base);
			depotVelo.lancerProcedureDeDepot();
			break;
		case 5:
			Bornette bornette;
			bornette = new Bornette(base);
			bornette.lancerAffichageDesBornesVplusEtVmoins();
			break;
		case 6:
			this.aQuitte = true;
			break;

		default:
			break;
		}
	}

}
