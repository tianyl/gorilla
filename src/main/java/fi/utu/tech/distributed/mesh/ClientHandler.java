package fi.utu.tech.distributed.mesh;

import java.io.*;
import java.net.*;

public class ClientHandler<A> implements Runnable {

	public Socket socket;
	private final Mesh<A> mesh; // oma verkko
	
	public ClientHandler(Socket s, Mesh<A> mesh) {
		
		this.socket = s;
		this.mesh = mesh;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	private Mesh<A> getMesh() {
		return this.mesh;
	}
	
	public void run(){
		try {
			InputStream is = this.getSocket().getInputStream();		
			OutputStream os = this.getSocket().getOutputStream();
			
			ObjectOutputStream oos = new ObjectOutputStream(os);
			ObjectInputStream ois = new ObjectInputStream(is);
			
			try {
				while(true) {
					MeshViesti<A> v = (MeshViesti<A>)ois.readObject();
					/*
					 * Jos viestissä on jo oma kuittaus, sivuutetaan viesti kokonaan.
					 */
					
					for(Long l : v.getKuittaus()) {
						if(l == this.getMesh().getOmaToken()) {
							// viestistä löytyi oma kuittaus, ei tehdä mitään.
							continue;
						}
					}
					
					/*
					 * Lisätään oma kuittaus ja lähetetään viesti eteenpäin muille.
					 * 
					 */
					v.getKuittaus().add(this.getMesh().getOmaToken());
					this.getMesh().broadcast(v);
					
					/*
					 * Katsotaan kuuluuko viesti meille.
					 * Jos kuuluu, annetaan se gorillapelille.
					 * 
					 * Mainittakoon että on hieman omituista että tässä
					 * toteutuksessa vaaditaan tällaista filtteröintiä,
					 * kun itse peli ei vaadi mitään sen kaltaista toimiakseen,
					 * ei edes privaattiviestijä.
					 */
					
					if(v.getOsoite() == 0 || v.getOsoite() == this.getMesh().getOmaToken()) {
						/*
						 * Viesti on meille. (0 on kaikille suunnattu viesti.)
						 */
						
						this.getMesh().addInboxMessage(v.getViesti());
					}
					
					
					/*
					 * Luetaan toinen viesti.
					 */
					
				}
			}
			catch(Exception e) {
				/*
				 * Meni pieleen. Suljetaan streamit.
				 */
				ois.close();
				oos.close();
				this.getSocket().close();
			}
		}
		catch(Exception e) {
			/*
			 * Ei vielä mitään. 
			 */
		}
		
	}
	

}
