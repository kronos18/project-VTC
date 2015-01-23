package ressources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Abonne 
{

	private Connection base;

	public Abonne(Connection base) 
	{
		this.base = base;
	}

	public void lancerProcedureAbonnement() 
	{
		Scanner scanner = new Scanner(System.in);

		System.out.println("Votre nom :");
		String nom = scanner.nextLine();
		System.out.println("Votre prenom :");
		String prenom = scanner.nextLine();

		System.out.println("Votre date de naissance (jj/mm/dddd) :");

		String dateDeNaissance = scanner.nextLine();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
		try{ 
			Date dateNaisSQL = formatter.parse(dateDeNaissance); 
		} 
		catch (ParseException pe) 
		{
			pe.printStackTrace();
		}
		System.out.println("Votre sexe :");
		String sexe = scanner.nextLine();
		System.out.println("Votre adresse :");
		String adresse = scanner.nextLine();

		System.out.println("Votre code de Carte bancaire:");
		scanner.nextLine();
		int codeCB = scanner.nextInt();

		insererAbonneDansLaBase(nom, prenom, dateDeNaissance,sexe,adresse,codeCB);
	}

	private void insererAbonneDansLaBase(String nom, String prenom, String dateDeNaissance, String sexe, String adresse, int codeCB) 
	{
		try 
		{
			String requeteOracle;
			Random random = new Random();
			ResultSet resultat;
			Statement requete = base.createStatement();

			String codeSecret = genererUnCodeSecret(random);
			
			System.out.println("Le code secret est "+ codeSecret);
			
			resultat = insererUnClient(codeCB, requete, codeSecret);
			//TODO LA DATE COURANTE
			requeteOracle = "insert into ABONNE values ('"+ nom +"','"+ prenom +"',"
					+ "to_date('"+ dateDeNaissance +"', 'yyyy/mm/dd'),'"+ sexe +"','"+ adresse +"',0,to_date('2016/01/20', 'yyyy/mm/dd'),to_date('2015/01/20', 'yyyy/mm/dd'))";

			resultat = requete.executeQuery(requeteOracle);
			System.out.println("On a echoue a lance notre requete");


			//tant  qu'il y a des resultats

			while(resultat.next()) { // récupération des résultats
				resultat.getString("Origine");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private ResultSet insererUnClient(int codeCB, Statement requete, String codeSecret)
			throws SQLException {
		String requeteOracle;
		ResultSet resultat;
		requeteOracle = "insert into CLIENT values ("+ codeSecret +","+codeCB +")";

		resultat = requete.executeQuery(requeteOracle);
		return resultat;
	}

	private String genererUnCodeSecret(Random random) {
		String codeSecret = String.valueOf(random.nextInt(9));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		return codeSecret;
	}

}
