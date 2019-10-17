package se.miun.distsys.listeners;

import se.miun.distsys.messages.SequenceRequestMessage;

public interface SequenceRequestMessageListener {
    public void onIncomingSequenceRequestMessage(SequenceRequestMessage sequenceRequestMessage);
}