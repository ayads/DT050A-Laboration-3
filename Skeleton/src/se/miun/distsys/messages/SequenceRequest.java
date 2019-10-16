package se.miun.distsys.messages;

import se.miun.distsys.clients.Client;

public class SequenceRequest extends Sequence{
    private static final long serialVersionUID = 1L;
	public int clientID;
    public SequenceRequest(Client client) {
        super(client);
        // TODO Auto-generated constructor stub
    }
}