package se.miun.distsys.messages;

import se.miun.distsys.clients.Client;

public class LeaveMessage extends Message {
	private static final long serialVersionUID = 1L;
	public int clientID;
	public LeaveMessage(Client client) {
		this.clientID = client.getID();
	}
}