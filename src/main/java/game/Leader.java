package game;

import java.io.Serializable;

import client.Player;

public class Leader implements Serializable {

	private static final long serialVersionUID = -2250624168799764473L;
	private Player leader;

	public Player getLeader() {
		return leader;
	}

	public void setLeader(Player leader) {
		this.leader = leader;
	}	
	

	
}
