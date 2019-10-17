package se.miun.distsys.messages;

public class BullyMessage extends Message {
	
	private static final long serialVersionUID = 1L;
	public Integer clientID = null;

	public BullyMessage(Integer clientID) {
		this.clientID = clientID;
	}

	public void handleBullyMessage(BullyMessage bullyMessage){
		if(bullyMessage instanceof ElectionRequestMessage) {
			ElectionRequestMessage electionRequestMessage = (ElectionRequestMessage) bullyMessage;

		} else if(bullyMessage instanceof ElectionResultMessage) {
			ElectionResultMessage electionResultMessage = (ElectionResultMessage) bullyMessage;

		} else if(bullyMessage instanceof SequenceRequestMessage) {
			SequenceRequestMessage sequenceRequestMessage = (SequenceRequestMessage) bullyMessage;

		} else if(bullyMessage instanceof SequenceReplyMessage) {
			SequenceReplyMessage sequenceReplyMessage = (SequenceReplyMessage) bullyMessage;

		} else {
			System.out.println("Unknown bullyMessage type");
		}
	}
}