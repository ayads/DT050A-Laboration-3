package se.miun.distsys.messages;

public class CoordinatorMessage extends ElectionMessage {
    
    private static final long serialVersionUID = 1L;
    public Integer clientID = null;
    
    public CoordinatorMessage(Integer clientID) {
        this.clientID = clientID;
    }
}