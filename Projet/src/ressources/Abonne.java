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
		String codeCB = scanner.nextLine();
		insererAbonneDansLaBase(nom, prenom, dateDeNaissance,sexe,adresse,codeCB);
	}

	private void insererAbonneDansLaBase(String nom, String prenom, String dateDeNaissance, String sexe, String adresse, String codeCB) 
	{
		try 
		{
			String requeteOracle;
			Random random = new Random();
			ResultSet resultat;
			Statement requete = base.createStatement();

			String codeSecret = genererUnCodeSecret(random);

			System.out.println("Le code secret est "+ codeSecret);
			
			requeteOracle = "SELECT client_seq.nextval from dual";
			resultat = requete.executeQuery(requeteOracle);
			String idClient = null; 
			
			while(resultat.next())
			{ // récupération des résultats
				System.out.println("On recupere les resultats");
				idClient = resultat.getString("nextval");
				System.out.println("idclient :"+idClient);
			}
			
			
			resultat = insererUnClient(codeCB, requete, codeSecret, idClient);
			//TODO LA DATE COURANTE
			requeteOracle = "insert into ABONNE values ("+idClient+",'"+ nom +"','"+ prenom +"',"
					+ "to_date('"+ dateDeNaissance +"', 'dd/mm/yyyy'),'"+ sexe +"','"+ adresse +"',0,to_date('2016/01/20', 'yyyy/mm/dd'),to_date('2015/01/20', 'yyyy/mm/dd'))";
			afficherLaRequete(requeteOracle);
			resultat = requete.executeQuery(requeteOracle);
			System.out.println("On a inseree l'abonne !");

		} 
		catch (SQLException e)
		{
			System.out.println("Impossible d'inserer un abonne dans la base");
			System.out.println("Details : "+e.getMessage());
		}

	}

	private void afficherLaRequete(String requeteOracle) {
		System.out.println("La requete est : ");
		System.out.println(requeteOracle);
	}

	private ResultSet insererUnClient(String codeCB, Statement requete, String codeSecret, String idClient)
			throws SQLException 
	{

		String requeteOracle;
		ResultSet resultat;
		
		
		requeteOracle = "insert into CLIENT values ("+idClient+","+ codeSecret +","+codeCB +")";

		resultat = requete.executeQuery(requeteOracle);
		System.out.println("On a inserer un client");
		return resultat;
	}

	private String genererUnCodeSecret(Random random) {
		String codeSecret = String.valueOf(random.nextInt(9));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		codeSecret = codeSecret.concat(String.valueOf(random.nextInt(9)));
		return codeSecret;
	}
	
	private static void afficherResultats(ResultSet resultat) throws SQLException {
		//tant  qu'il y a des resultats
		while(resultat.next()) { // récupération des résultats
			System.out.println("Numvol = " + resultat.getString("Numvol")
					+ ", Origine = " + resultat.getString("Origine")
					+ ", Destination = " + resultat.getString("Destination"));
		}
	}

}
