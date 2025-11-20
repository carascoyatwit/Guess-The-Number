package app.packets;

import java.io.Serializable;

public class PlayerWin extends Message implements Serializable {

	private static final long serialVersionUID = 7L;
	
	private boolean winStatus;
	private int winningClientID;
	private String target;

	public PlayerWin(boolean winStatus, int winningClientID, String target) {
		
		this.winStatus = winStatus;
		this.winningClientID = winningClientID;
		this.target = target;
		
	}
	
	public boolean getWinStatus() {
		return winStatus;
	}
	
	public int getWinningClientID() {
		return winningClientID;
	}
	
	public String getTarget() {
		return target;
	}
	
}
