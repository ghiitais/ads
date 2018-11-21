import java.util.concurrent.atomic.AtomicInteger;

public class Announcement {
	
	private int code;
	private double prix;
	private String descriptif;
	private User owner;
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final int idAnnonce;
	
	public Announcement(int code, double prix, String descriptif){
		this.code = code;
		this.prix = prix;
		this.descriptif = descriptif;
		idAnnonce = count.incrementAndGet();
	}

	
	public int getId(){
		return idAnnonce;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public String getDescriptif() {
		return descriptif;
	}

	public void setDescriptif(String descriptif) {
		this.descriptif = descriptif;
	}

	@Override
	public String toString() {
		return +idAnnonce+" "+code+" "+prix+" "+descriptif;
	}

}
