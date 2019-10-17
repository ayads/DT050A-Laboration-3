package se.miun.distsys.messages;

public class SequenceReply extends Sequence{
    
    private static final long serialVersionUID = 1L;
    private Integer myClientID = null;
    
    public SequenceReply(Integer clientID) {
        super(clientID);
    }
}