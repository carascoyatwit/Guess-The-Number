package app.packets;

import java.io.Serializable;

public class NextRound extends Message implements Serializable {
    private static final long serialVersionUID = 8L;
    
	private final String target;

    public NextRound(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}

