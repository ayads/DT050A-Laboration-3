package se.miun.distsys.messages;

public class JoinMessage extends Message {
	private static final long serialVersionUID = 1L;
	public Integer myClientID = null;

	public JoinMessage(Integer clientID) {
        this.myClientID = clientID;
    }
}