package se.miun.distsys.messages;

public class ElectionRequestMessage extends ElectionMessage {
    
    private static final long serialVersionUID = 1L;
    public Integer clientID = null;
    public long startElectionTime = System.currentTimeMillis();
    
    public ElectionRequestMessage(Integer clientID) {
        this.clientID = clientID;
    }
}