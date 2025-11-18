package app.packets;

import java.io.Serializable;

public class MaxPlayers extends Message implements Serializable{

	private static final long serialVersionUID = 4L;

	private int max;
	
	public MaxPlayers(int max) {
		this.max = max;
	}
	
	public int getMax() {
		return max;
	}
	
	 @Override
	    public String toString() {
	        return "MaxPlayers [Max=" + max + "]";
	    }

}
