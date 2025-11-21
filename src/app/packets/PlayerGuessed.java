package app.packets;

import java.io.Serializable;

public class PlayerGuessed extends Message implements Serializable {

	private static final long serialVersionUID = 6L;
	
	private int clientID;
	private String guess;

	public PlayerGuessed(int clientID, String guess) {
		
		this.clientID = clientID;
		this.guess = guess;
		
	}
	
	public int getClientID() {
		return clientID;
	}
	
	public String getGuess() {
		return guess;
	}
	
	@Override
    public String toString() {
        return "PlayerGuessed [ID=" + clientID + ", Guess=" + guess + "]";
    }

}
