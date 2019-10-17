package se.miun.distsys.helpers;

import se.miun.distsys.messages.BullyMessage;
import se.miun.distsys.messages.ElectionRequestMessage;
import se.miun.distsys.messages.ElectionResultMessage;
import se.miun.distsys.messages.SequenceRequestMessage;
import se.miun.distsys.messages.SequenceReplyMessage;

public class BullyMessageHandler{
	
	public BullyMessageHandler(){
    }

    public void classifyBullyMessage(BullyMessage bullyMessage){
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