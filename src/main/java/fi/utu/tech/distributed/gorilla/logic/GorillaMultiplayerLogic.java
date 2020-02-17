package fi.utu.tech.distributed.gorilla.logic;

import fi.utu.tech.distributed.mesh.Mesh;


/**
 * Probably better to just alter GorillaLogic, but let's consider our options
 * @author Tero
 *
 */
public class GorillaMultiplayerLogic extends GorillaLogic {

	protected Mesh<Move> verkko;
	
	/*// Konstruktori peritty ilmeisesti jostain kirjastosta?
	public GorillaMultiplayerLogic() {
		super();
	}
	*/
	
	@Override
	protected void handleMultiplayer() {
		
	}
	
	@Override
	protected void startServer(String port) {
		try {
			Mesh<Move> mesh = new Mesh<Move>(Integer.parseInt(port));			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

