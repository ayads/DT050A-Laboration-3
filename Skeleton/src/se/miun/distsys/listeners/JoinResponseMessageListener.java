package se.miun.distsys.listeners;

import se.miun.distsys.messages.JoinResponseMessage;

public interface JoinResponseMessageListener {
    public void onIncomingJoinResponseMessage(JoinResponseMessage joinResponseMessage);
}