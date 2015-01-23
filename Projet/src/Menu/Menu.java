package Menu;

import java.sql.Connection;
import java.sql.Statement;

public interface Menu
{
	public void lancer();
	public String getNomMenu();
	public void afficherMenu();
	public void traiterLeChoix();
	

}
