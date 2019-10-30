package se.miun.distsys.messages;

public class SequenceRequestMessage extends SequenceMessage {
    
    private static final long serialVersionUID = 1L;
    public Integer clientID = null;
    public String chat = "";
    public long startTime = System.currentTimeMillis();
    public SequenceRequestMessage(Integer clientID, String chat) {
        this.clientID = clientID;
        this.chat = chat;
    }
}