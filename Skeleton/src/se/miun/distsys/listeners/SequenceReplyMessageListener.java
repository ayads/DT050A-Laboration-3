package se.miun.distsys.listeners;

import se.miun.distsys.messages.SequenceReplyMessage;

public interface SequenceReplyMessageListener {
    public void onIncomingSequenceReplyMessage(SequenceReplyMessage sequenceReplyMessage);
}