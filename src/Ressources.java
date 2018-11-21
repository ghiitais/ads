import java.util.ArrayList;
import java.util.List;

public class Ressources {
	
// Liste des annonces et des utilisateurs
	
	private static volatile List<Announcement> adds = new ArrayList<Announcement>();
	private static volatile List<User> clients = new ArrayList<User>();
	

	public static List<Announcement> getAdds() {
		return adds;
	}

	public static List<User> getClients() {
		return clients;
	}

	public static void setClients(List<User> clients) {
		Ressources.clients = clients;
	}

	public static void setAdds(List<Announcement> adds) {
		Ressources.adds = adds;
	}

}