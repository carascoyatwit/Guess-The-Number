package app.packets;

import java.io.Serializable;

public class PlayerConnected extends Message implements Serializable{

	private static final long serialVersionUID = 2L;

    private int clientID;
    private String name;
	
	public PlayerConnected(int clientID, String name) {
		 this.clientID = clientID;
		 this.name = name;
	}
	
	public int getClientID() {
		return clientID;
	}
	
	public String getName() {
		return name;
	}

	//cuz why not for debugging purposes teehee.
	@Override
    public String toString() {
        return "PlayerConnected [ID=" + clientID + ", Name=" + name + "]";
    }

}
