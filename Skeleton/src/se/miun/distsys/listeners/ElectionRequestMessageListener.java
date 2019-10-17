package se.miun.distsys.listeners;

import se.miun.distsys.messages.ElectionRequestMessage;

public interface ElectionRequestMessageListener {
    public void onIncomingElectionRequestMessage(ElectionRequestMessage electionRequestMessage);
}