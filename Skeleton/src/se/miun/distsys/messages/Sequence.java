package se.miun.distsys.messages;

import se.miun.distsys.clients.Client;

public class Sequence extends BullyMessage {
    private static final long serialVersionUID = 1L;
	public int clientID;
    public Sequence(Client client) {
        super(client);
        // TODO Auto-generated constructor stub
    }
    
    public void sequenceRequest(){

    }

    public void sequenceReply(){
        
    }
}