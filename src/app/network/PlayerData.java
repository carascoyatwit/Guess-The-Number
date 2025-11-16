package app.network;

import java.io.Serializable;

public class PlayerData implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
	private String name;
    private boolean isHost;

    public PlayerData(String name, boolean isHost) {
        this.name = name;
        this.isHost = isHost;
    }

    public String getName() { 
    	return name; 
    }
    
    public boolean isHost() { 
    	return isHost; 
    }
 
}
