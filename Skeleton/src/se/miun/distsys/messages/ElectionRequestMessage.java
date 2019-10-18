package se.miun.distsys.messages;

public class ElectionRequestMessage extends ElectionMessage {
    
    private static final long serialVersionUID = 1L;
	public Integer clientID = null;
    
    public ElectionRequestMessage(Integer clientID) {
        this.clientID = clientID;
    }
}