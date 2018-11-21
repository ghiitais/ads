import java.io.*;
import java.net.*;

public class ServiceThread implements Runnable {
	
	private Socket client;
	private PrintStream out;
	private BufferedReader in;
	private User user; // This is the user who has logged in, also the owner of an ad
	private boolean connected = false; // to see if the socket connected
	
	public ServiceThread(Socket client) throws IOException {
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintStream(client.getOutputStream());
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
	
			boolean flag = true;
			/*Print the menu options 
			 * 1: Register using login and password
			 * 2: Login with already created account
			 * if user is authentified: Show this menu
			 * You 're now connected, you can-->
			 * 3: Post an ad
			 * 4: List the ads
			 * 5: Delete one of your ads
			 * 6: Show interest in an ad
			 * 7: diconnect
			 */
			// Everything hat the server sends has to be finished with a +++ at the end
			//out.print("Welcome! Type 1 to Register or 2 to Login or quit to terminate+++");
			
			while(flag) {
			
				String messageClient = readReq();
				System.out.println(messageClient);
				String [] adStrs = messageClient.split(" ");
				if(messageClient.equals("quit")){
					flag = false;
		        	break;
		        }
			     /* server reads the commandeClient, 
			      * splits it and checks if valid then and starts if [0].equalsTo("REGI") then call the function that registers a user for example...*/  
			   
			    	 if(adStrs[0].equals("REGI")){
							if(adStrs[1].isEmpty() || adStrs[2].substring(0, adStrs[2].length() -3).isEmpty()){
								out.print("KOKO 1+++"); // Arguments are empty or contain whitespace
							} else {
								if(doesUserExist(adStrs[1])) {
									out.print("KOKO 0+++"); //Login already exists
								} else {
									createUser(adStrs[1], adStrs[2].substring(0, adStrs[2].length() -3));
									out.print("OKOK+++");		
								}
						}
							
						} else if(adStrs[0].equals("CONE")){
							int enteredPort = 0;
							if(adStrs[1].isEmpty() || adStrs[2].isEmpty() || adStrs[3].substring(0, adStrs[3].length() -3).isEmpty() || !isInteger(adStrs[3].substring(0, adStrs[3].length() -3))){
								out.print("KOKO 1+++"); // Arguments are empty or port can't be parsed
							} else {
								if(!doesUserExist(adStrs[1])) {
									out.print("KOKO 3+++"); //user doesn't exist
								} else {
									if(!isMdpCorrect(adStrs[1], adStrs[2])){
										out.print("KOKO 2+++"); //mdp incorrect
									} else {
										enteredPort = Integer.parseInt(adStrs[3].substring(0, adStrs[3].length() -3));
										login(adStrs[1], adStrs[2], enteredPort);
										out.print("OKOK+++");
										//out.print("connecté: "+connected+" "+user.getLogin()+ " "+user.getIp()+" +++");
										//out.print("Type -3- to POST an AD, -4- to LIST the ads, -5- to Delete one of your ads, -6- To show interest in an ad, -7- To disconnect+++");
									}
								}
						}
						} else if(adStrs[0].equals("POST")){
								// POST code prix descriptif+++ code 1 ou 4
								// parse the code and check it's between 1 and 6
								// parse the price 
								//check not empty
								//remove space from description
								int enteredCode;
								double enteredPrice;
								String desc="";
								if(adStrs[1].isEmpty() || adStrs[2].isEmpty()){
									out.print("KOKO 1+++"); 
								} else {
									if(!isInteger(adStrs[1]) || !isDouble(adStrs[2])){
										out.print("KOKO 4+++"); 
									} else {
										enteredCode = Integer.parseInt(adStrs[1]);
										enteredPrice = Double.parseDouble(adStrs[2]);
										if(enteredCode <= 6 && enteredCode >= 1){
											// DESC											
											for(int i=3; i<adStrs.length; i++){
												desc+=adStrs[i];
										}
											
											if(connected){
												Announcement ad = addAnnouncement(enteredCode, enteredPrice, desc.substring(0, desc.length()-3));
												out.print("OKOK "+ad.getId()+"+++");
											} else {
												//out.print("Vous devez être connecté+++");
											}
										}else{
											out.print("KOKO 4+++"); 
										}
									}
								}
							} else if(messageClient.equals("LIST+++")){
								if(connected){
									int nbAnnonces = Ressources.getAdds().size();
									out.print("NBAN "+nbAnnonces+"+++");
									for(int i=0; i<Ressources.getAdds().size(); i++){
										out.print("ANNO "+Ressources.getAdds().get(i).toString()+"+++");
											}
								}else {
									out.print("KOKO 7+++");
								}
								
							} else if(adStrs[0].equals("SUPR")){
								if(!connected){
									out.print("KOKO 7+++");
								} else {
		
								if(adStrs[1].substring(0, adStrs[1].length() -3).isEmpty()){
									out.print("KOKO 1+++");
								}else{
									if(!isInteger(adStrs[1].substring(0, adStrs[1].length() -3))){
										out.print("KOKO 4+++");
									}else{
										int enteredID = Integer.parseInt(adStrs[1].substring(0, adStrs[1].length() -3));
										if(!doesAnnonceExist(enteredID)){
											out.print("KOKO 5+++");
										}else{
											if(!isOwnerOfAd(enteredID)){
												out.print("KOKO 6+++");
											}else{
												deleteAnnouncement(enteredID);
												out.print("OKOK+++");
												
											}
										}
									}
								}
								
							}
							}else if(adStrs[0].equals("INTR")){
								if(!connected){
									out.print("KOKO 7+++");
								} else {
		
								if(adStrs[1].substring(0, adStrs[1].length() -3).isEmpty()){
									out.print("KOKO 1+++");
								}else{
									if(!isInteger(adStrs[1].substring(0, adStrs[1].length() -3))){
										out.print("KOKO 4+++");
									}else{
										int enteredID = Integer.parseInt(adStrs[1].substring(0, adStrs[1].length() -3));
										if(!doesAnnonceExist(enteredID)){
											out.print("KOKO 5+++");
										}else{
											// Si le proprio est connecté
											if(ownerOfAd(enteredID).isConnected()){
												//owner's ip address and port's address
												out.print("CONT "+ownerOfAd(enteredID).getIp()+" "+ownerOfAd(enteredID).getPort()+"+++");
											}else{
												out.print("NCON+++");
											}
										}
									} 
						
								}
			
								}
							}else if(adStrs[0].equals("DISP")){
								//start messaging thread
							}
							else if(messageClient.equals("DECO+++")){
								if(connected){
									connected = false;
									user.setConnected(false);
									out.print("GBYE+++");
									client.close();
									break;
									
								}else{
									out.print("KOKO 7+++");
								}
								
							}else{
								out.print("KOKO 7+++");
							}
					}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	public String readReq() throws IOException
	{
		String response="";
		int l,c;
		while(true)
		{
			c = in.read();
			response+=(char) c;
			l=response.length();
			if(l>=3 && response.substring(l-3, l).equals("+++")){
				break;
			}
				
		}

		return response;
	}
	// Creation d'un user: REGI id mdp+++ 
	public void createUser(String login, String mdp) {
		User addedUser = new User(login, mdp);
		Ressources.getClients().add(addedUser);
	}
	
	// Check if client exists
	public boolean doesUserExist(String login) {
		boolean exists = false;
			for (int i = 0; i < Ressources.getClients().size(); i++) {
				if (Ressources.getClients().get(i).getLogin().equals(login)) {
					exists = true;
				}
			}
			return exists;
		}
	
	public boolean isMdpCorrect(String login , String mdp){
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
	public void login(String login, String mdp, int port){
		// Get the user object from the arraylist 
	for(int i=0; i< Ressources.getClients().size(); i++){
			if(Ressources.getClients().get(i).getLogin().equals(login)){
				user = Ressources.getClients().get(i);
				user.setConnected(true);
				connected = true;
				user.setPort(port);
				user.setIp(client.getLocalAddress());
				user.setLogin(login);
				user.setMdp(mdp);
			}
		}
		
	}
	
	public Announcement addAnnouncement(int code, double prix, String description) {
		Announcement ad = new Announcement(code, prix, description);
		Ressources.getAdds().add(ad);
		ad.setOwner(user);
		return ad;		
	}
	
	public void deleteAnnouncement(int idAnnonce){
		if(doesAnnonceExist(idAnnonce)){
			int j = 0;
		for(int i=0;i<Ressources.getAdds().size();i++) {   		    		
        		if(Ressources.getAdds().get(i).getId() == idAnnonce) {
        			j=i;
        		}
        	}
		Ressources.getAdds().remove(j);
		}
	}
	
	public boolean isOwnerOfAd(int idAnnonce){
		boolean isOwner = false;
		Announcement ad;
		int j =0;
		for(int i = 0; i<Ressources.getAdds().size(); i++){
			if(Ressources.getAdds().get(i).getId() == idAnnonce){
				 j = i;
			}
			ad = Ressources.getAdds().get(j);
			if(ad.getOwner().getLogin().equals(user.getLogin())){
				isOwner = true;
			}
	}
		return isOwner;
	}
	
	public User ownerOfAd(int idAnnonce){
		User owner = null;
		Announcement ad;
		int j =0;
		for(int i = 0; i<Ressources.getAdds().size(); i++){
			if(Ressources.getAdds().get(i).getId() == idAnnonce){
				 j = i;
			}
			ad = Ressources.getAdds().get(j);
			owner = ad.getOwner();
	}
		return owner;
	}
	
	public boolean doesAnnonceExist(int idAnnonce){
		boolean exists = false;
		for(int i = 0; i<Ressources.getAdds().size(); i++){
			if(Ressources.getAdds().get(i).getId() == idAnnonce){
				exists = true;
			}
		}
		return exists;
	}
	
	public boolean isDouble(String s){
		boolean valid = true;
	    try {
	        Double.parseDouble(s);
	        valid = true;
	    }
	    catch( Exception e ) {
	        valid = false;
	    }
	    return valid;
	}

	public boolean isInteger(String input) {
		boolean valid = true;
	    try {
	        Integer.parseInt(input );
	        valid = true;
	    }
	    catch( Exception e ) {
	        valid = false;
	    }
	    return valid;
	}
	
}
