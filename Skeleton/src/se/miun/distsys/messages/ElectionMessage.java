package se.miun.distsys.messages;

public class ElectionMessage extends BullyMessage {
	
	private static final long serialVersionUID = 1L;
	public Integer clientID = null;

	public ElectionMessage(Integer clientID) {
		super(clientID);
	}
}