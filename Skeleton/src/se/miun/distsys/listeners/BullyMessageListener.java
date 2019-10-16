package se.miun.distsys.listeners;

import se.miun.distsys.messages.BullyMessage;

public interface BullyMessageListener {
    public void onIncomingBullyMessage(BullyMessage bullyMessage);
}
