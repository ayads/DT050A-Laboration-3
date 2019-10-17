package se.miun.distsys.messages;

public class ElectionRequest extends Election{
    
    private static final long serialVersionUID = 1L;
	private Integer myClientID = null;
    
    public ElectionRequest(Integer clientID) {
        super(clientID);
    }
}