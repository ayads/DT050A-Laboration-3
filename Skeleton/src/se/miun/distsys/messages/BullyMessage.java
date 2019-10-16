package se.miun.distsys.messages;

import se.miun.distsys.clients.Client;

public class BullyMessage extends Message {
	private static final long serialVersionUID = 1L;
	public int clientID;	
	public BullyMessage(Client client) {
		this.clientID = client.getID();
	}
}