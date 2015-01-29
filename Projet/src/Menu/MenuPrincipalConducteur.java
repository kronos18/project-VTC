package Menu;

import java.sql.Connection;
import java.util.Scanner;

import abonnement.Action;
import abonnement.OrdreRegulation;
import abonnement.Regulation;
import abonnement.Velo;

public class MenuPrincipalConducteur implements Menu {
	
	private boolean aQuitte;
	private MenuPrincipal menuPrincipal;

	public MenuPrincipalConducteur(MenuPrincipal menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
	}

	@Override
	public void lancer() {
		while (!this.aQuitte) 
		{
			afficherMenu();
			traiterLeChoix();
		}	
	}

	@Override
	public String getNomMenu() {
		return null;
	}

	@Override
	public void afficherMenu() 
	{
		System.out.println("Menu Conducteur ! ");
		System.out.println("Que desirez-vous ? ");
		System.out.println("---------------------------------------------");
		System.out.println("1 : Consulter les routines a effectuer !");
		System.out.println("2 : Demarrer une routine !");
		System.out.println("3 : Valider un ordre de regulation !");
		System.out.println("4 : Ajouter une action !");
		System.out.println("5 : Declarer un velo endommage !");
		System.out.println("6 : Quitter !");
		System.out.println("--------------------------------------------");
		System.out.print("Votre choix : ");
	}

	@Override
	public void traiterLeChoix() 
	{
		int choix = 0;
		Scanner scan = new Scanner(System.in);
		choix = scan.nextInt();
		Connection base = menuPrincipal.getBase();

		Regulation regulation = new Regulation(base);
		Action a = new Action(base);
		OrdreRegulation or = new OrdreRegulation(base);
		Velo v = new Velo(base);
		
		switch (choix) 
		{
		case 1:
			regulation.AfficherRoutines();
			break;
		case 2:
			regulation.DemarrerRoutines();
			break;
		case 3:
			or.ValiderOrdre();
			break;
		case 4:
			a.SaisirAction();
			break;
		case 5:
			v.DeclarerVeloEndommage();
			break;
		case 6:
			this.aQuitte = true;
			System.out.println("Merci d'avoir utiliser l'application !");
			System.out.println("A tres bientot !");
			break;
			
		default:
			break;
		}		
	}

}
