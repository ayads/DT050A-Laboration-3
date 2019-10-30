package se.miun.distsys.messages;

public class SequenceReplyMessage extends SequenceMessage {
    
    private static final long serialVersionUID = 1L;
    public Integer clientID = null;
    public String chat= "";
    public Long time = null;


    public SequenceReplyMessage(Integer clientID, String chat, Long time) {
        this.clientID = clientID;
        this.chat = chat;
        this.time = time;
    }
}