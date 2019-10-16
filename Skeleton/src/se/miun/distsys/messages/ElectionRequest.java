package se.miun.distsys.messages;
import se.miun.distsys.clients.Client;
//Jetbarin
public class ElectionRequest extends Election{
    private static final long serialVersionUID = 1L;
	public int clientID;
    public ElectionRequest(Client client) {
        super(client);
    }
}