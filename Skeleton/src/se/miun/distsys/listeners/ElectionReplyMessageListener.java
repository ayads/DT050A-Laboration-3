package se.miun.distsys.listeners;

import se.miun.distsys.messages.ElectionReplyMessage;

public interface ElectionReplyMessageListener {
    public void onIncomingElectionReplyMessage(ElectionReplyMessage electionReplyMessage);
}