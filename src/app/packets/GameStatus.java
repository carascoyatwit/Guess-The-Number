package app.packets;

import java.io.Serializable;

public class GameStatus extends Message implements Serializable {

	private static final long serialVersionUID = 5L;

	private boolean gameStarted;
	
	public GameStatus(boolean gameStarted) {
		
		this.gameStarted = gameStarted;
	}
	
	
	@Override
    public String toString() {
        return "Game Status [Status=" + gameStarted + "]";
    }

}
