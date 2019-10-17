package se.miun.distsys.messages;

public class SequenceRequestMessage extends SequenceMessage{
    
    private static final long serialVersionUID = 1L;
	public Integer myClientID = null;
    
    public SequenceRequestMessage(Integer clientID) {
        super(clientID);
    }

    
}