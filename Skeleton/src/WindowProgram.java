import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.HashMap;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.listeners.CoordinatorMessageListener;
import se.miun.distsys.listeners.ElectionRequestMessageListener;
import se.miun.distsys.listeners.ElectionReplyMessageListener;
import se.miun.distsys.listeners.JoinMessageListener;
import se.miun.distsys.listeners.LeaveMessageListener;
import se.miun.distsys.listeners.SequenceReplyMessageListener;
import se.miun.distsys.listeners.SequenceRequestMessageListener;
import se.miun.distsys.listeners.JoinResponseMessageListener;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.CoordinatorMessage;
import se.miun.distsys.messages.ElectionRequestMessage;
import se.miun.distsys.messages.ElectionReplyMessage;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.LeaveMessage;
import se.miun.distsys.messages.SequenceReplyMessage;
import se.miun.distsys.messages.SequenceRequestMessage;
import se.miun.distsys.messages.JoinResponseMessage;

public class WindowProgram implements ChatMessageListener, JoinMessageListener, LeaveMessageListener, JoinResponseMessageListener, ElectionRequestMessageListener, ElectionReplyMessageListener, CoordinatorMessageListener, SequenceRequestMessageListener, SequenceReplyMessageListener, ActionListener {
	JFrame frame;
	JTextPane txtpnChat = new JTextPane();
	JTextPane txtpnMessage = new JTextPane();
	JTextPane txtpnStatus = new JTextPane();
	JTextPane txtpnCausalOrder = new JTextPane();

	GroupCommuncation gc = null;

	Long previousTime = 0L;

	public WindowProgram() {
		initializeFrame();
		gc = new GroupCommuncation();
		gc.setChatMessageListener(this);
		gc.setJoinMessageListener(this);
		gc.setJoinResponseMessageListener(this);
		gc.setLeaveMessageListener(this);
		gc.setElectionRequestMessageListener(this);
		gc.setElectionResultMessageListener(this);
		gc.setCoordinatorMessageListener(this);
		gc.setSequenceRequestMessageListener(this);
		gc.setSequenceReplyMessageListener(this);
		System.out.println("Group Communcation Started");
	}

	private void initializeFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(txtpnChat);
		txtpnChat.setEditable(false);	
		txtpnChat.setText("--== Group Chat ==--");
		txtpnChat.setFont(new Font("Consolas", Font.PLAIN, 12));

		txtpnMessage.setText(" ➔ ");
		frame.getContentPane().add(txtpnMessage);
		
		JScrollPane scrollPaneStatus = new JScrollPane();
		frame.getContentPane().add(scrollPaneStatus);
		scrollPaneStatus.setViewportView(txtpnStatus);
		txtpnStatus.setEditable(false);
		txtpnStatus.setText("--== Activity Log ==--");
		txtpnStatus.setFont(new Font("Consolas", Font.PLAIN, 12));
		Color backgroundColor = new Color(217, 236, 255);
		txtpnStatus.setBackground(backgroundColor);
		
		JButton btnSendChatMessage = new JButton("Send Chat Message");
		btnSendChatMessage.addActionListener(this);
		btnSendChatMessage.setActionCommand("send");
		frame.getContentPane().add(btnSendChatMessage);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
	            gc.shutdown();
	        }
		});

		JScrollPane scrollPaneClient = new JScrollPane();
		frame.getContentPane().add(scrollPaneClient);
		scrollPaneClient.setViewportView(txtpnCausalOrder);
		txtpnCausalOrder.setEditable(false);
		txtpnCausalOrder.setText("--== Out of Order List ==--");
		txtpnCausalOrder.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtpnCausalOrder.setBackground(backgroundColor);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("send")) {
			gc.sendSequenceRequestMessage(gc.myClient.ID, txtpnMessage.getText());
		}
	}

	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {
		gc.myClientList.put(chatMessage.clientID, false);
	}

	@Override
	public void onIncomingJoinMessage(JoinMessage joinMessage) {
		try {
			gc.myClientList.put(joinMessage.clientID, false);
			txtpnStatus.setText(joinMessage.clientID + " join." + "\n" + txtpnStatus.getText());
			if(joinMessage.clientID != gc.myClient.ID) {
				gc.sendJoinResponseMessage(gc.myClient);
				gc.sendElectionRequestMessage(joinMessage.clientID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingJoinResponseMessage(JoinResponseMessage joinResponseMessage) {
		try {
			if (!gc.myClientList.containsKey(joinResponseMessage.clientID)){
				gc.myClientList.put(joinResponseMessage.clientID, false);
				txtpnStatus.setText(joinResponseMessage.clientID + " join response." + "\n" + txtpnStatus.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingLeaveMessage(LeaveMessage leaveMessage) {
		try {
			if (gc.myClientList.containsKey(leaveMessage.clientID)){
				txtpnStatus.setText(leaveMessage.clientID + " left." + "\n" + txtpnStatus.getText());
				gc.electionCandidateList.remove(leaveMessage.clientID);
				
				//System.out.println("New List: "+gc.myClientList);
				if(gc.myClientList.get(leaveMessage.clientID)){
					gc.myClientList.remove(leaveMessage.clientID);
					if(gc.myClient.ID != leaveMessage.clientID){
						gc.sendElectionRequestMessage(gc.myClient.ID);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingElectionRequestMessage(ElectionRequestMessage electionRequestMessage) {
		try {				
			if (gc.myClient.ID > electionRequestMessage.clientID){
				gc.sendElectionReplyMessage(gc.myClient.ID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingElectionReplyMessage(ElectionReplyMessage electionReplyMessage) {
		try {
			if(gc.bullyMessageHandler.isWithinTimeoutLimit(electionReplyMessage.startElectionTime)){
				gc.electionCandidateList.add(electionReplyMessage.clientID);
				Integer maxClientID = (Collections.max(gc.electionCandidateList));
				gc.sendCoordinatorMessage(maxClientID);
			} else {
				gc.sendCoordinatorMessage(electionReplyMessage.clientID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingCoordinatorMessage(CoordinatorMessage coordinatorMessage) {
		try {
			for (HashMap.Entry<Integer, Boolean> entry : gc.myClientList.entrySet()) {
				gc.myClientList.put(entry.getKey(),false);
			}
			gc.myClientList.put(coordinatorMessage.clientID, gc.bullyMessageHandler.setCoordinator());
			System.out.println("Coordinator: "+gc.myClientList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingSequenceRequestMessage(SequenceRequestMessage sequenceRequestMessage) {
		try {
			if(gc.myClientList.get(gc.myClient.ID)){
				gc.sendSequenceReplyMessage(sequenceRequestMessage.clientID, sequenceRequestMessage.chat, sequenceRequestMessage.startTime);
				System.out.println(sequenceRequestMessage.startTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingSequenceReplyMessage(SequenceReplyMessage sequenceReplyMessage) {
		try {
			if(previousTime<sequenceReplyMessage.time){
				txtpnChat.setText(sequenceReplyMessage.clientID + sequenceReplyMessage.chat + "\n" + txtpnChat.getText());
				previousTime = sequenceReplyMessage.time;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowProgram window = new WindowProgram();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
