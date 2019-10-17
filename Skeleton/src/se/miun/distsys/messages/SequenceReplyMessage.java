package se.miun.distsys.messages;

public class SequenceReplyMessage extends SequenceMessage{
    
    private static final long serialVersionUID = 1L;
    public Integer myClientID = null;
    
    public SequenceReplyMessage(Integer clientID) {
        super(clientID);
    }
}