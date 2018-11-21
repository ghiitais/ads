import java.net.Socket;

import java.util.Scanner;
//  Juste une classe de test qui contient main
public class Utilities {
	 static User user;
	 static Socket client;
	
	// Creation d'un user: REGI id mdp+++ 
	public static User createUser(String login, String mdp) {
		user = new User(login, mdp);
		Ressources.getClients().add(user);
		return user;
	}
	

	
	public static boolean doesUserExist(String login) {

		for (int i = 0; i < Ressources.getClients().size(); i++) {
			if (Ressources.getClients().get(i).getLogin().equals(login)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isMdpCorrect(String login , String mdp){
		// pour un login donné est ce qu'il a ce mdp
		boolean isCorrect = true;
		for(User temp: Ressources.getClients()){
			if(temp.getLogin().equals(login)){
				if(!temp.getMdp().equals(mdp)){
					isCorrect = false;
				}
			}
		}
		return isCorrect;
		
	}
	//CONE id mdp port+++ : connecter un user sur un port IF EXISTS
	public static void login(String login, String mdp, int port){
		if(doesUserExist(login)) {
			if(isMdpCorrect(login, mdp)){
				user.setConnected(true);
				user.setPort(port);
				
			}
		}
	}
	public static boolean isInteger(String input) {
		boolean valid = true;
	    try {
	        Integer.parseInt(input);
	        valid = true;
	    }
	    catch(Exception e) {
	        valid = false;
	    }
	    return valid;
	}
	
	
	public static void main(String[] args) {
		
		
		Scanner sc = new Scanner(System.in);
		createUser("ghita", "ghita");
		createUser("kenza", "kenza");
		for(int i = 0; i< Ressources.getClients().size(); i++){
			System.out.println(Ressources.getClients().get(i).getLogin());
		}
		while(true){
			System.out.println("Login:");
			String login = sc.nextLine();
			System.out.println("Password:");
			String mdp = sc.nextLine();
			System.out.println("Port:");
			String port = sc.nextLine();
			
			String commande = "CONE "+login+" "+mdp+" "+port+"+++";
			System.out.println(commande);
			String[] adStrs = commande.split(" ");
			
		
			
			if(adStrs[0].equals("CONE")){
				int enteredPort = 0;
				if(adStrs[1].isEmpty() || adStrs[2].isEmpty() || adStrs[3].substring(0, adStrs[3].length() -3).isEmpty() || !isInteger(adStrs[3].substring(0, adStrs[3].length() -3))){
					System.out.println("KOKO 1+++"); // Arguments are empty or port can't be parsed
				} else {
					if(!doesUserExist(adStrs[1])) {
						System.out.println("KOKO 3+++"); //user doesn't exist
					} else {
						if(!isMdpCorrect(adStrs[1], adStrs[2])){
							System.out.println("KOKO 2+++"); //mdp incorrect
						} else {
							enteredPort = Integer.parseInt(adStrs[3].substring(0, adStrs[3].length() -3));
							login(adStrs[1], adStrs[2], enteredPort);
							System.out.println("OKOK+++");
							
						}
					}
			}
				
			}
	
}//while
	}//main
}//class
