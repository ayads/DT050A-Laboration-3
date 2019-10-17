package se.miun.distsys.listeners;

import se.miun.distsys.messages.ElectionResultMessage;

public interface ElectionResultMessageListener {
    public void onIncomingElectionResultMessage(ElectionResultMessage electionResultMessage);
}