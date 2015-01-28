package abonnement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Velo 
{

	private Connection base;

	public Velo(Connection base) 
	{
		this.base = base;
	}

	public void DeclarerVeloEndommage()
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Numero  du velo concerne : ");
		String idVelo = scanner.nextLine();

		try
		{
			String requeteOracle;
			Statement requete = base.createStatement();
			ResultSet resultat;
		
			requeteOracle = "Update Velo set etatVelo='HS' where idvelo = " + idVelo;
			resultat = requete.executeQuery(requeteOracle);

			System.out.println("Le velo numero " + idVelo + " est desormais inutilisable dans l'etat actuel.");
		}
		catch (SQLException e)
		{
			System.out.println("Impossible de modifier l'etat du velo selectionne.");
			System.out.println("Details : "+e.getMessage());
		}
	}
}
