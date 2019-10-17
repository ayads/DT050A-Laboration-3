package se.miun.distsys.messages;

public class SequenceRequest extends Sequence{
    
    private static final long serialVersionUID = 1L;
	private Integer myClientID = null;
    
    public SequenceRequest(Integer clientID) {
        super(clientID);
    }

    
}