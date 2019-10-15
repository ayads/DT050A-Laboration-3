import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.listeners.JoinMessageListener;
import se.miun.distsys.listeners.LeaveMessageListener;
import se.miun.distsys.listeners.JoinResponseMessageListener;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.LeaveMessage;
import se.miun.distsys.messages.JoinResponseMessage;

public class WindowProgram implements ChatMessageListener, JoinMessageListener, LeaveMessageListener, JoinResponseMessageListener, ActionListener {
	JFrame frame;
	JTextPane txtpnChat = new JTextPane();
	JTextPane txtpnMessage = new JTextPane();
	JTextPane txtpnStatus = new JTextPane();
	JTextPane txtpnCausalOrder = new JTextPane();

	GroupCommuncation gc = null;

	public WindowProgram() {
		initializeFrame();
		gc = new GroupCommuncation();
		gc.setChatMessageListener(this);
		gc.setJoinMessageListener(this);
		gc.setJoinResponseMessageListener(this);
		gc.setLeaveMessageListener(this);
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
 			for (int i = 0; i < 100; i++) {
				gc.sendChatMessage(gc.activeClient, txtpnMessage.getText());
			}
		}
	}

	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {
		if(gc.vectorClockHandler.isCausalOrder(chatMessage, gc.vectorClock)){
			gc.activeClientList.add(chatMessage.clientID);
			txtpnChat.setText(chatMessage.clientID + " ➔ Hi! This is a generic BOT message!" + "\n" + txtpnChat.getText());
			//txtpnChat.setText(chatMessage.clientID + chatMessage.chat + "\n" + txtpnChat.getText());
		} else {
			txtpnCausalOrder.setText(chatMessage.clientID + " is out of order!" + "\n" + txtpnCausalOrder.getText());
		}
	}

	@Override
	public void onIncomingJoinMessage(JoinMessage joinMessage) {
		try {
			if(gc.vectorClockHandler.isCausalOrder(joinMessage, gc.vectorClock)){
				gc.activeClientList.add(joinMessage.clientID);
				txtpnStatus.setText(joinMessage.clientID + " join." + "\n" + txtpnStatus.getText());
			} else {
				txtpnCausalOrder.setText(joinMessage.clientID + " is out of order!" + "\n" + txtpnCausalOrder.getText());
			}
			if(joinMessage.clientID != gc.activeClient.getID()){				
				gc.sendJoinResponseMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingJoinResponseMessage(JoinResponseMessage joinResponseMessage) {
		try {
			if (!gc.activeClientList.contains(joinResponseMessage.clientID)){
				gc.activeClientList.add(joinResponseMessage.clientID);
				txtpnStatus.setText(joinResponseMessage.clientID + " join response." + "\n" + txtpnStatus.getText());
			}
			else if(!gc.vectorClockHandler.isCausalOrder(joinResponseMessage, gc.vectorClock)) {
				txtpnCausalOrder.setText(joinResponseMessage.clientID + " is out of order!" + "\n" + txtpnCausalOrder.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingLeaveMessage(LeaveMessage leaveMessage) {
		try {
			if (gc.activeClientList.contains(leaveMessage.clientID)){
				txtpnStatus.setText(leaveMessage.clientID + " left." + "\n" + txtpnStatus.getText());
				gc.activeClientList.remove(leaveMessage.clientID);
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
