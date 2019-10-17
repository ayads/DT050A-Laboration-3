package se.miun.distsys;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import se.miun.distsys.listeners.BullyMessageListener;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.listeners.JoinMessageListener;
import se.miun.distsys.listeners.LeaveMessageListener;
import se.miun.distsys.listeners.JoinResponseMessageListener;
import se.miun.distsys.messages.BullyMessage;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.LeaveMessage;
import se.miun.distsys.messages.Message;
import se.miun.distsys.messages.MessageSerializer;
import se.miun.distsys.messages.JoinResponseMessage;

public class GroupCommuncation {
	  
	private int datagramSocketPort = 2019;	
	DatagramSocket datagramSocket = null;
	boolean runGroupCommuncation = true;
	MessageSerializer messageSerializer = new MessageSerializer();
	
	//Message Listeners
	ChatMessageListener chatMessageListener = null;
	JoinMessageListener joinMessageListener = null;
	BullyMessageListener bullyMessageListener = null;
	LeaveMessageListener leaveMessageListener = null;
	JoinResponseMessageListener joinResponseMessageListener = null;

	//Create a new client.
	public Integer clientID = createClient();
	public HashMap<Integer, Integer> electionCandidateList = new HashMap<>();
	public Vector<Integer> activeClientList = new Vector<>();
	public GroupCommuncation() {
		try {
			runGroupCommuncation = true;
			datagramSocket = new MulticastSocket(datagramSocketPort);
			ReceiveThread rt = new ReceiveThread();
			rt.start();
			sendJoinMessage(clientID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		sendLeaveMessage();
		runGroupCommuncation = false;
	}

	class ReceiveThread extends Thread{
		@Override
		public void run() {
			byte[] buffer = new byte[65536];
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
			while(runGroupCommuncation) {
				try {
					datagramSocket.receive(datagramPacket);
					byte[] packetData = datagramPacket.getData();
					Message receivedMessage = messageSerializer.deserializeMessage(packetData);
					handleMessage(receivedMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void handleMessage (Message message) {
			if(message instanceof ChatMessage) {
				ChatMessage chatMessage = (ChatMessage) message;
				if(chatMessageListener != null ){
					chatMessageListener.onIncomingChatMessage(chatMessage);
				}
			} else if (message instanceof JoinMessage) {
				JoinMessage joinMessage = (JoinMessage) message;
				if (joinMessageListener != null) {
					joinMessageListener.onIncomingJoinMessage(joinMessage);
				}
			} else if (message instanceof BullyMessage) {
				BullyMessage bullyMessage = (BullyMessage) message;
				if (bullyMessageListener != null) {
					bullyMessageListener.onIncomingBullyMessage(bullyMessage);
				}
			} else if (message instanceof JoinResponseMessage) {
				JoinResponseMessage joinResponseMessage = (JoinResponseMessage) message;
				if (joinResponseMessageListener != null) {
					joinResponseMessageListener.onIncomingJoinResponseMessage(joinResponseMessage);
				}
			}  else if (message instanceof LeaveMessage) {
				LeaveMessage leaveMessage = (LeaveMessage) message;
				if (leaveMessageListener != null) {
					leaveMessageListener.onIncomingLeaveMessage(leaveMessage);
				}
			} else {
				System.out.println("Unknown message type");
			}
		}
	}
	
	public Integer createClient() {
		try {
			Thread.sleep(250);
			int newClient = ThreadLocalRandom.current().nextInt(0, 100);
			return newClient;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendChatMessage(Integer clientID, String chat) {
		try {
			ChatMessage chatMessage = new ChatMessage(clientID, chat);
			byte[] sendData = messageSerializer.serializeMessage(chatMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendJoinMessage(Integer clientID) {
		try {
			JoinMessage joinMessage = new JoinMessage(clientID);
			byte[] sendData = messageSerializer.serializeMessage(joinMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendBullyMessage() {
		try {
			BullyMessage bullyMessage = new BullyMessage(clientID);
			byte[] sendData = messageSerializer.serializeMessage(bullyMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void sendJoinResponseMessage() {
		try {
			JoinResponseMessage joinResponseMessage = new JoinResponseMessage(clientID);
			byte[] sendData = messageSerializer.serializeMessage(joinResponseMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public void sendLeaveMessage() {
		try {
			LeaveMessage leaveMessage = new LeaveMessage(clientID);
			byte[] sendData = messageSerializer.serializeMessage(leaveMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setChatMessageListener(ChatMessageListener listener) {
		this.chatMessageListener = listener;
	}

	public void setJoinMessageListener(JoinMessageListener listener) {
		this.joinMessageListener = listener;
	}

	public void setBullyMessageListener(BullyMessageListener listener) {
		this.bullyMessageListener = listener;
	}

	public void setJoinResponseMessageListener(JoinResponseMessageListener listener) {
		this.joinResponseMessageListener = listener;
	}

	public void setLeaveMessageListener(LeaveMessageListener listener) {
		this.leaveMessageListener = listener;
	}
}