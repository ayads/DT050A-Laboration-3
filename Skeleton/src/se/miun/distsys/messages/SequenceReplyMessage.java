package se.miun.distsys.messages;

public class SequenceReplyMessage extends SequenceMessage {
    
    private static final long serialVersionUID = 1L;
    public Integer clientID = null;
    
    public SequenceReplyMessage(Integer clientID) {
        this.clientID = clientID;
    }
}