package se.miun.distsys.messages;

public class ElectionResult extends Election{
    
    private static final long serialVersionUID = 1L;
    private Integer myClientID = null;
    
    public ElectionResult(Integer clientID) {
        super(clientID);
    }
}