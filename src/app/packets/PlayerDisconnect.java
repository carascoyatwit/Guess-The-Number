package app.packets;

import java.io.Serializable;

public class PlayerDisconnect extends Message implements Serializable {

	private static final long serialVersionUID = 3L;

	private int clientID;
	
	public PlayerDisconnect(int clientID) {
		this.clientID = clientID;
	}
	
	public int getClientID() {
		return clientID;
	}
	
	 @Override
	    public String toString() {
	        return "PlayerDisconnect [ID=" + clientID + "]";
	    }

}
