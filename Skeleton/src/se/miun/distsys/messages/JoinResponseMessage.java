package se.miun.distsys.messages;

public class JoinResponseMessage extends Message {
	
	private static final long serialVersionUID = 1L;
	public Integer clientID = null;

	public JoinResponseMessage(Integer clientID) {
        this.clientID = clientID;
    }
}