package se.miun.distsys.messages;

public class Sequence extends BullyMessage {
    
    private static final long serialVersionUID = 1L;
    public Integer myClientID = null;
    
    public Sequence(Integer clientID) {
        super(clientID);
    }
}