package Menu;

import java.sql.Connection;
import java.util.Scanner;

public class MenuPrincipal implements Menu 
{

	private boolean aQuitte;
	private Connection base;

	public MenuPrincipal(Connection base) 
	{
		this.aQuitte = false;
		this.base = base;
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
	public String getNomMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afficherMenu() 
	{
		System.out.println("Bienvenue dans l'application VePick ! ");
		System.out.println("Que desirez-vous ? ");
		System.out.println("---------------------------------------------");
		System.out.println("1 : Acceder a l'interface utilisateur !");
		System.out.println("2 : Acceder a l'interface Conducteur !");
		System.out.println("3 : Acceder a l'interface Superviseur !");
		System.out.println("4 : Quitter l'application !");
		System.out.println("---------------------------------------------");
		System.out.print("Votre choix : ");
		
		

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
			Menu menuPrincipalUtilisateur = new MenuPrincipalUtilisateur(this);
			menuPrincipalUtilisateur.lancer();
			break;
		case 2:
			Menu menuPrincipalConducteur = new MenuPrincipalConducteur(this);
			menuPrincipalConducteur.lancer();
			break;
		case 3:
			Menu menuPrincipalSuperviseur = new MenuPrincipalSuperviseur(this);
			menuPrincipalSuperviseur.lancer();
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

	public Connection getBase() {
		return base;
	}

}
