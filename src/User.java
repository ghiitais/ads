import java.net.InetAddress;

public class User {

	private static int idUser = 1;

	private int id;
	private String login;
	private String mdp;
	private int port;
	private InetAddress ip;
	private boolean isConnected;
	
	User() {
		//this.id=idUser++;
	}
	
	// REGI id mdp+++
	User(String login, String mdp) {
		this.setLogin(login);
		this.setMdp(mdp);
		this.id = idUser++;
	}
	
	User(int port, InetAddress ip) {
		this.port = port;
		this.ip = ip;
		this.id = idUser++;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public int getIdUser() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


/*	public String toString() {
		return port+":"+ip.toString();
	}*/

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

}