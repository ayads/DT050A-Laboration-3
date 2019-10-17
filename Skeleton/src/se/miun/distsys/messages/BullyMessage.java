package se.miun.distsys.messages;

public class BullyMessage extends Message {
	
	private static final long serialVersionUID = 1L;
	public Integer myClientID = null;

	public BullyMessage(Integer clientID) {
		this.myClientID = clientID;
	}
}