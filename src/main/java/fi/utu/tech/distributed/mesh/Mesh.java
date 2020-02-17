package fi.utu.tech.distributed.mesh;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Tämän luokan tarkoituksena on hallita verkkotoimintoja lokaalin ja
 * verkkopelien kesken. Pelillä on yksi tällainen luokka.
 *  
 * 
 * Yksi viesti on aina yksi MeshViesti olio, joka ensin lähetetään
 * eteenpäin, ja sen jälkeen tulkitaan.
 * 
 * Tämä verkkototeutus on siis push tyyppinen.
 * 
 * @author Tero
 *
 */

public class Mesh<A> implements Runnable {

	private ArrayList<ClientHandler<A>> clients; // tähän yhdistäneet
	private ServerSocket listening;
	private ClientHandler<A> server;	// mihin itse yhdistetty
	private long omaToken;
	private ArrayList<A> inbox; // tähän tallennetaan itselle osoitetut viestit
									 // toimnnot inboxin kanssa oltava synchronized
	private boolean isConnected;
	
	/**
	 * Luo MeshHandler olion, ja parametrina määritetään portti jota kuunnellaan.
	 * @param port
	 */
	public Mesh(int port) throws Exception {
		
		this.clients = new ArrayList<ClientHandler<A>>();
		this.server = null;
		this.listening = null;
		this.omaToken = new Random().nextLong();
		this.inbox = new ArrayList<A>();
		this.isConnected = false;
		
		try {
			this.listening = new ServerSocket(port);
		}
		catch(Exception e) {
			/*
			 * Kick the can down the road.
			 */
			throw e;
		}


	}

	public ArrayList<A> getInbox() {
		return this.inbox;
	}
	
	public synchronized void addInboxMessage(A message) {
		this.getInbox().add(message);
	}
	
	/**
	 * Palauttaa viestin ja poistaa sen inboxista first-in-first-out logiikalla.
	 * 
	 * @return Palauttaa geneerisen olion, tai null mikäli inbox on tyhjä.
	 */
	public synchronized A readInboxMessage() {
		if(!this.getInbox().isEmpty()) {
			A message = this.getInbox().get(0);
			this.getInbox().remove(0);
			return message;
		}
		else {
			return null;
		}
	}
	
	public long getOmaToken() {
		return this.omaToken;
	}
	
	public ArrayList<ClientHandler<A>> getClients() {
		return this.clients;
	}
	
	public ClientHandler<A> getServer() {
		return this.server;
	}
	
	public void setServer(ClientHandler<A> ch) {
		this.server = ch;
	}
	
	public ServerSocket getServerSocket() {
		return this.listening;
	}

	/**
	 * Rinnakkaisajo mahdollistetaan tällä metodilla.
	 */
	public void run() {
		/*
		 * Kuunnellaan 
		 */
		while(true) {
			try {
				Socket clientSocket = listening.accept();
				ClientHandler<A> ch = new ClientHandler<A>(clientSocket, this);
				Thread t = new Thread(ch);
				t.start();
				this.getClients().add(ch); // lisääkö tämä loputtomasti tyhjiä listaan?
				System.out.println("Guest joined game.");
			}
			catch(Exception e) {
				/*
				 * Ei vielä toteutettu.
				 */
			}
			

		}
	}
	
	/**
	 * Viestin lähetys kaikille. Huom, älä käytä suoraan.
	 * @param o
	 */
	public void broadcast(Serializable o) {
		
		if (!this.getClients().isEmpty()) {
			for(ClientHandler<A> ch : this.getClients()) {
				try {
					System.out.println("Avataan stream clientille");
					Socket client = ch.getSocket();
					InputStream is = client.getInputStream();
					OutputStream os = client.getOutputStream();
					
					ObjectOutputStream oos = new ObjectOutputStream(os);
					ObjectInputStream ois = new ObjectInputStream(is);
					
					try {
						oos.writeObject(o);	// lähetetty kaikille jotka ovat clientteinä yhdistettyinä
					}
					catch(Exception e) {
						/*
						 * Suljetaan streamit.
						 */
						ois.close();
						oos.close(); // is ja os ei tarvitse erikseen sulkea?
					}
					ois.close();
					oos.close();
					
				}
				catch(Exception e) {
					/*
					 * Ei vielä mitään. 
					 */
				}
				
				
			}			
		}
		/*
		 * Pitää lähettää vielä omalle serverille.
		 */
		if (this.isConnected()) {	// ettei yritetä lähettää viestejä nullille
			try {
				System.out.println("Avataan stream serverille");
				InputStream is = this.getServer().getSocket().getInputStream();
				OutputStream os = this.getServer().getSocket().getOutputStream();
				
				ObjectOutputStream oos = new ObjectOutputStream(os);
				ObjectInputStream ois = new ObjectInputStream(is);
				
				try {
					oos.writeObject(o);
				}
				catch(Exception e) {
					/*
					 * Suljetaan streamit
					 */
					ois.close();
					oos.close();
				}
				ois.close();
				oos.close();
				
			}
			catch(Exception e) {
				/*
				 * Ei vielä mitään.
				 */
			}
			
		}
		
	}

	/**
	 * Lähetetään viesti. Jos halutaan tietty vastaanottaja, käytetään sen long osoitetta.
	 * Muutoin osoitteeksi asetetaan 0. Käytetään tätä kun
	 * 
	 * @param o
	 * @param recipient
	 */
	public void send(MeshViesti<A> o, long recipient) {
		o.setOsoite(recipient);
		o.addToken(this.getOmaToken());
		this.broadcast(o);
	}
	
	public void send(A o, long recipient) {
		MeshViesti<A> viesti = new MeshViesti<A>(o, this.getOmaToken(), recipient);
		this.send(viesti, recipient);
	}
	
	
	/**
	 * Suljetaan handleri ja kaikki sen yhteydet.
	 */
	public void close() throws Exception {
		for (ClientHandler<A> ch : this.getClients()) {
			try {
				ch.getSocket().close();				
			}
			catch(Exception e) {
				
				throw e;
			}
		}
	}
	
	
	
	/**
	 * Tällä metodilla liitytään olemassaolevaan verkkoon.
	 * 
	 * @param a
	 * @param portti
	 */
	public void connect(InetAddress a, int portti) {
		try {
			Socket s = new Socket(a, portti);			
			this.setServer(new ClientHandler<A>(s, this));
			this.isConnected = true;
			
		}
		catch(Exception e) {
			/*
			 * Could not connect 
			 */
		}
	}

	public boolean isConnected() {
		return this.isConnected;
	}
	
}
