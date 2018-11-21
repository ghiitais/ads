import java.io.*;
import java.net.*;

public class Client {
	static int portEcouteServeur = 2345;
	static String host = "localhost"; 
	static String message;
	static String msgFromServer;
	static Socket clientSocket;
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	static PrintStream out;
	static BufferedReader in;

	public static void main(String[] args) throws IOException {
		connectToServer();
	}

	public static void connectToServer(){
		try {
			String login = "";
			String mdp = "";
			String port = "";
			String code = "";
			String prix = "";
			String description = "";
			String idAnnonce = "";
			String yesOrNo = "";
			clientSocket = new Socket(host, portEcouteServeur);
			
			 out = new PrintStream(clientSocket.getOutputStream());
			 in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// new Thread(new ClientThreading(in)).start();
			 
			System.out.println("Connection from " + clientSocket.getInetAddress().toString() + ": " + clientSocket.getPort());
			System.out.println("Welcome! Type 1 to Register or 2 to Login or quit to terminate");
		
			
			boolean continu = true;
			while (continu) {

				message = input.readLine();
				
				if ("quit".equals(message)) {
					continu = false;
				}
				
				if(message.equals("1")){
					System.out.println("Login:");
					login = input.readLine();
					System.out.println("Password");
					mdp = input.readLine();
					// Send the command REGI login mdp+++ to server
					out.print("REGI"+" "+login+" "+mdp+"+++");
					msgFromServer = readReq();
					System.out.println(msgFromServer);
					
				}
				else if(message.equals("2")){
					System.out.println("Login:");
					login = input.readLine();
					System.out.println("Password:");
					mdp = input.readLine();
					System.out.println("Port:");
					port = input.readLine();
					out.print("CONE"+" "+login+" "+mdp+" "+port+"+++");
					msgFromServer = readReq();
					System.out.println(msgFromServer);
					if(msgFromServer.equals("OKOK+++")){
						System.out.println("Type -3- to POST an AD, -4- to LIST the ads, -5- to Delete one of your ads, -6- To show interest in an ad, -7- To disconnect");
					}
					
				} else if(message.equals("3")){
					System.out.println("---Catégories---");
					System.out.println("1: Voiture\n2: Moto\n3: Musique\n4: Electroménager\n5: Téléphone\n6: Autre\n---------");
					code = input.readLine();
					System.out.println("Prix:");
					prix = input.readLine();
					System.out.println("Description:");
					description = input.readLine();
					out.print("POST"+" "+code+" "+prix+" "+description+"+++");
					msgFromServer = readReq();
					System.out.println(msgFromServer);
				
				} else if(message.equals("4")) {
					out.print("LIST+++");
					msgFromServer = readReq();
					System.out.println(msgFromServer);
					String[] req = msgFromServer.split(" ");
					int size = Integer.parseInt(req[1].substring(0, req[1].length()-3));
					for(int i=0;i<size;i++)
					{
						System.out.println(readReq());
					}
				} else if(message.equals("5")){
					System.out.println("ID de l'annonce à supprimer:");
					idAnnonce = input.readLine();
					out.print("SUPR "+idAnnonce+"+++");
					msgFromServer = readReq();
					System.out.println(msgFromServer);
				}
				else if(message.equals("6")){
					System.out.println("Enter Ad id that interests you");
					idAnnonce = input.readLine();
					out.print("INTR "+idAnnonce+"+++");
					msgFromServer = readReq();
					System.out.println(msgFromServer);
					if(!msgFromServer.equals("NCON+++")){
						//Owner is online,  to talk to him
						System.out.println("do you want to directly talk to the owner?");
						System.out.println("y/n?:");
						yesOrNo = input.readLine();
						if(yesOrNo.equals("y")){
						// do smthg, second part of protocol.. 
							
							
						}
					}
				}
				else if(message.equals("7")) {
					out.print("DECO+++");
					msgFromServer = readReq();
					System.out.println(msgFromServer);
					clientSocket.close();
					break;
				}
				else if(message.equals("quit")){
					out.print("quit");
					System.out.println("BYE");;
					clientSocket.close();
					break;
				} else {
					//System.out.println("Commande non définie. Réessayez");
				}
			}

			// input.close();
			out.close();
			clientSocket.close();

		 } catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	public static String readReq() throws IOException
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
}