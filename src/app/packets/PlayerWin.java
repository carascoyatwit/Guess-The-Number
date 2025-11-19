package app.packets;

import java.io.Serializable;

public class PlayerWin extends Message implements Serializable {

	private static final long serialVersionUID = 7L;
	
	private boolean winStatus;

	public PlayerWin(boolean winStatus) {
		
		this.winStatus = winStatus;
		
	}
	
	public boolean getWinStatus() {
		return winStatus;
	}

	@Override
	   public String toString() {
        return "PlayerDisconnect [Win=" + winStatus + "]";
    }
	
}
