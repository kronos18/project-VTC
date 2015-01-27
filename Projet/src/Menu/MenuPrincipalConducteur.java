package Menu;

import java.sql.Connection;
import java.util.Scanner;

import abonnement.Action;
import abonnement.OrdreRegulation;
import abonnement.Regulation;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afficherMenu() 
	{
		System.out.println("Menu Conducteur ! ");
		System.out.println("Que desirez-vous ? ");
		System.out.println("---------------------------------------------");
		System.out.println("1 : Consulter les routines ‡ effectuer !");
		System.out.println("2 : Demarrer une routine !");
		System.out.println("3 : Valider un ordre de regulation !");
		System.out.println("4 : Ajouter une action !");
		System.out.println("---------------------------------------------");
		System.out.print("Votre choix : ");
	}

	@Override
	public void traiterLeChoix() 
	{
		int choix = 0;
		Scanner scan = new Scanner(System.in);
		choix = scan.nextInt();
		Connection base = menuPrincipal.getBase();

		Regulation reg = new Regulation(base);
		OrdreRegulation or = new OrdreRegulation(base);
		Action a = new Action(base);
		switch (choix) 
		{
		case 1:
			reg.AfficherRoutines();
			break;
		case 2:
			reg.DemarrerRoutines();
			break;
		case 3:
			or.ValiderOrdre();
			break;
		case 4:
			a.SaisirAction();
			break;
		case 5:
			this.aQuitte = true;
			System.out.println("Merci d'avoir utiliser l'application !");
			System.out.println("A tr√®s bientot !");
			break;
			
		default:
			break;
		}		
	}

}
