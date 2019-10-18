package se.miun.distsys.messages;

public class ElectionResultMessage extends ElectionMessage {
    
    private static final long serialVersionUID = 1L;
    public long startElectionTime = System.currentTimeMillis();
	public Integer clientID = null;
    
    public ElectionResultMessage(Integer clientID) {
        this.clientID = clientID;
    }
}