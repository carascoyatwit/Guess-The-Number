package app.packets;

import java.io.Serializable;

public class PlayerWin extends Message implements Serializable {

	private static final long serialVersionUID = 7L;
	
	private boolean winStatus;
	private int winningClientID;

	public PlayerWin(boolean winStatus, int winningClientID) {
		
		this.winStatus = winStatus;
		this.winningClientID = winningClientID;
		
	}
	
	public boolean getWinStatus() {
		return winStatus;
	}
	
	public int getWinningClientID() {
		return winningClientID;
	}
	@Override
	public String toString() {
        return "PlayerWin [Win=" + winStatus + ", ClientID=" + winningClientID + "]"; 
    }
}
