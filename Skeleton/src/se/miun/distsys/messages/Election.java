package se.miun.distsys.messages;

public class Election extends BullyMessage {
	
	private static final long serialVersionUID = 1L;
	private Integer myClientID = null;

	public Election(Integer clientID) {
		super(clientID);
	}
}