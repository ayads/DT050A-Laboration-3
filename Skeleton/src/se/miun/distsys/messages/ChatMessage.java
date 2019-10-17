package se.miun.distsys.messages;


public class ChatMessage extends Message {
	
	private static final long serialVersionUID = 1L;
	public String chat = "";
	public Integer clientID = null;

	public ChatMessage(Integer clientID, String chat) {
        this.clientID = clientID;
		this.chat = chat;
    }
}