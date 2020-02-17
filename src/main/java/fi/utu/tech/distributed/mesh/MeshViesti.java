package fi.utu.tech.distributed.mesh;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Tämä luokka kuvastaa viestiä joita verkkojärjestelmä kuljettaa.
 * Viestit ovat plaintext String muotoisia, muutamine otsikkotietoineen
 * (mm. viestin lähde, ja lähettäjä)
 * 
 * @author Tero
 *
 */

public class MeshViesti<A> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long osoite;
	public A viesti;		// siirretty viesti/komento 
	public ArrayList<Long> kuittaus;		/* Tällä kuittauksella tiedetään keneltä viesti saatiin, jottei sitä lähetetä takaisin.
	 										 *
	 										 * Lähetettäessä viestiä eteenpäin, vaihdetaan se omaksi kuittaukseksi
	 										 *
	 										 */

	public MeshViesti() {
		// As I understand, Serializable requires a constructor with no arguments
	}
	
	/**
	 * Luodessa uusi MeshViesti olio on siihen lisättävä oma kuittaus.
	 * 
	 * 
	 * @param v viestin hyötykuorma
	 * @param k oma kuittaus
	 * @param o viestin osoite
	 */
	public MeshViesti(A v, long k, long o) {
		
		this.viesti = v;
		this.kuittaus = new ArrayList<Long>();
		kuittaus.add(k);
		this.osoite = o;
	}
	
	public ArrayList<Long> getKuittaus(){
		return this.kuittaus;
	}
	
	public A getViesti() {
		return this.viesti;
	}
	
	public long getOsoite(){
		return this.osoite;
	}
	
	public void setOsoite(long l) {
		this.osoite = l;
	}
	
	public void addToken(long l) {
		this.getKuittaus().add(l);
	}

}
