package Menu;

import java.util.Scanner;

import abonnement.Abonne;


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
		System.out.println("Menu Utilisateur ! ");
		System.out.println("Que desirez-vous ? ");
		System.out.println("---------------------------------------------");
		System.out.println("1 : Creer un abonnement !");
		System.out.println("2 : Effectuer une reservation !");
		System.out.println("3 : Effectuer une location !!");
		System.out.println("4 : Retourner au menu principal !");
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
		switch (choix) 
		{
		case 1:
			Abonne abonne = new Abonne(menuPrincipal.getBase());
			abonne.lancerProcedureAbonnement();
			break;
		case 2:
			/*Menu menuPrincipalConducteur = new MenuPrincipalConducteur();
			menuPrincipalConducteur.lancer();*/
			break;
		case 3:
			
			menuPrincipal.lancer();
			break;
		case 4:
			this.aQuitte = true;
			System.out.println("Merci d'avoir utiliser l'application !");
			System.out.println("A très bientot !");
			break;

		default:
			break;
		}
	}

}
