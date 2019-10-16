package se.miun.distsys.messages;

import se.miun.distsys.clients.Client;

public class ElectionResult extends Election{
    
    private static final long serialVersionUID = 1L;
	public int clientID;
    
    public ElectionResult(Client client) {
        super(client);
    }
}