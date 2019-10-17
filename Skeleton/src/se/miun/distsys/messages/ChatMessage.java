package se.miun.distsys.messages;


public class ChatMessage extends Message {
	private static final long serialVersionUID = 1L;
	public String chat = "";
	public Integer myClientID = null;

	public ChatMessage(Integer clientID, String chat) {
		this.myClientID = clientID;
		this.chat = chat;
    }
}