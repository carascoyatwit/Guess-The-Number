package app.packets;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerWin extends Message implements Serializable {

	private static final long serialVersionUID = 7L;
	
	private boolean winStatus;
	private ArrayList<Integer> winningIDs;
	private String target;

	public PlayerWin(boolean winStatus, ArrayList<Integer> winningIDs, String target) {
		
		this.winStatus = winStatus;
		this.winningIDs = winningIDs;
		this.target = target;
		
	}
	
	public boolean getWinStatus() {
		return winStatus;
	}
	
	 public ArrayList<Integer> getWinningIDs() {
	        return winningIDs;
	    }
	
	public String getTarget() {
		return target;
	}
	
}
