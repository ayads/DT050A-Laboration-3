package se.miun.distsys.messages;

public class SequenceRequestMessage extends SequenceMessage{
    
    private static final long serialVersionUID = 1L;
	public Integer clientID = null;
    
    public SequenceRequestMessage(Integer clientID) {
        this.clientID = clientID;
    }
}