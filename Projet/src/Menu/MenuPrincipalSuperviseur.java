package Menu;

import java.sql.Connection;
import java.util.Scanner;

import abonnement.Abonne;
import abonnement.Bornette;
import abonnement.Reservation;
import abonnement.Station;
import abonnement.Vehicule;

public class MenuPrincipalSuperviseur implements Menu {

	private boolean aQuitte;
	private MenuPrincipal menuPrincipal;
	
	public MenuPrincipalSuperviseur(MenuPrincipal menuPrincipal) 
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
		System.out.println("Menu Superviseur ! ");
		System.out.println("Que desirez-vous ? ");
		System.out.println("---------------------------------------------");
		System.out.println("1 : Consulter les routines d un vehicule !");
		System.out.println("2 : Consulter l ordre actuel dans la routine d un vehicule !");
		System.out.println("3 : Consulter le nombre de velo dans chaque station !");
		System.out.println("4 : Consulter les plages horaires Vmoins et Vplus d une station !");
		System.out.println("5 : Modifier la classification d une plage horaire d une station !");
		System.out.println("6 : Consulter les actions prevues sur une station !");
		System.out.println("7 : Retourner au menu principal !");
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
		Vehicule vehicule;
		Bornette bornette;
		Station station;
		
		switch (choix) 
		{
		case 1:
			vehicule = new Vehicule(base);
			vehicule.AfficherRoutines();
			break;

		case 2:
			vehicule = new Vehicule(base);
			vehicule.AfficherOrdreActuelDansRoutineDunVehicule();
			break;

		case 3:
			bornette = new Bornette(base);
			bornette.AfficherNbVeloDansStation();
			break;

		case 4:
			station = new Station(base);
			station.AfficherPlagesHorairesVmoinsVplus();
			break;

		case 5:
			station = new Station(base);
			station.ModifierClassificationPlageHoraireStation();
			break;

		case 6:
			station = new Station(base);
			station.AfficherVehiculeEtOrdrePrevusPourUneStation();
			break;
			
		case 7:
			this.aQuitte = true;
			System.out.println("Merci d'avoir utiliser l'application !");
			System.out.println("A très bientot !");
			break;
			
		default:
			break;
		}
	}

}
