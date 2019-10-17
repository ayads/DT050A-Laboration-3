package se.miun.distsys.messages;

public class LeaveMessage extends Message {

	private static final long serialVersionUID = 1L;
	public Integer clientID = null;

	public LeaveMessage(Integer clientID) {
        this.clientID = clientID;
    }
}