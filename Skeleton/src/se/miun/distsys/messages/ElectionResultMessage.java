package se.miun.distsys.messages;

public class ElectionResultMessage extends ElectionMessage{
    
    private static final long serialVersionUID = 1L;
    public Integer clientID = null;
    
    public ElectionResultMessage(Integer clientID) {
        this.clientID = clientID;
    }
}