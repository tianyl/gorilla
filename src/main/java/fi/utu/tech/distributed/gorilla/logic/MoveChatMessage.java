package fi.utu.tech.distributed.gorilla.logic;

public class MoveChatMessage extends Move {

	private final String message;
	
	public MoveChatMessage(String s) {
		this.message = s;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
}
