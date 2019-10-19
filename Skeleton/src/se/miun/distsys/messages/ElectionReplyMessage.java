package se.miun.distsys.messages;

public class ElectionReplyMessage extends ElectionMessage {
    
    private static final long serialVersionUID = 1L;
    public Integer clientID = null;
    public long startElectionTime = System.currentTimeMillis();
    
    public ElectionReplyMessage(Integer clientID) {
        this.clientID = clientID;
    }
}