package se.miun.distsys.messages;

public class SequenceMessage extends BullyMessage {
    
    private static final long serialVersionUID = 1L;
    public Integer myClientID = null;
    
    public SequenceMessage(Integer clientID) {
        super(clientID);
    }
}