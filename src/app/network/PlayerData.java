package app.network;

import java.io.Serializable;

public class PlayerData implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
	private String name;


    public PlayerData(String name) {
        this.name = name;
    }

    public String getName() { 
    	return name; 
    }
 
}
