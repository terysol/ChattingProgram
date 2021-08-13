package work.gui;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChattingGUI extends JFrame{
	JPanel panel= null;
	JLabel myname = null;		
	JLabel port_text =null;
	JLabel port_num = null;
	JButton send = null;
	JButton setting =null;
	TextField name_field = null;
	TextField chat_field = null;
	TextArea who_area = null;
	JLabel who_label = null;
	TextArea chat_area = null;
	
	JScrollPane pane = new JScrollPane();
	Font font = new Font("맑은 고딕",Font.BOLD,15);
	
	Socket socket;
	String id="";
	boolean isFirst = true;
	WriteThread wt;
	
	public String getId() {
		return id;
	}
	
	public ChattingGUI(Socket socket) {
		super("Chatting Program");
		this.socket=socket;
		wt = new WriteThread(this);
		
		showServer();
		add(panel);
		addEventServer();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void showServer() {
		setSize(600, 500);
		ChattingServer c = new ChattingServer();
		String port = Integer.toString(c.PORT);
		
		panel = new JPanel();
		myname= new JLabel("내 이름(수정x) :");
		name_field = new TextField();
		port_text=new JLabel("< port >");
		port_num = new JLabel(port);
		chat_area = new TextArea();
		who_area=new TextArea();
		chat_field=new TextField();
		send = new JButton("전송");
		setting=new JButton("설정");
		who_label=new JLabel("참가자");
		
		panel.setLayout(null);
		myname.setBounds(22, 30, 135, 15);
		myname.setFont(font);
		name_field.setBounds(160, 30, 200, 20);
		name_field.setFont(font);
		port_text.setBounds(470, 15,70, 20);
		port_text.setFont(font);
		port_num.setBounds(483, 50, 140, 15);
		port_num.setFont(font);
		chat_area.setBounds(22,80,400,300);
		chat_area.setFont(font);
		who_area.setBounds(430, 115, 140, 265);
		who_area.setFont(font);
		who_label.setBounds(470, 50, 100, 100);
		who_label.setFont(font);
		chat_field.setBounds(22, 390, 400, 30);
		chat_field.setFont(font);
		chat_field.setText("채팅을 입력해주세요...");
		send.setBounds(430, 390, 140, 30);
		send.setFont(font);
		setting.setBounds(365, 30, 70, 25);
		setting.setFont(font);
		
		panel.add(myname);
		panel.add(name_field);
		panel.add(port_text);
		panel.add(port_num);
		panel.add(chat_area);
		panel.add(who_area);
		panel.add(chat_field);
		panel.add(send);
		panel.add(setting);
		panel.add(who_label);
	}
	
	public void addEventServer() {
		chat_field.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				chat_field.setText("");
			}
		});

		setting.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(name_field.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "사용자 이름을 설정해 주세요.", "warning",JOptionPane.WARNING_MESSAGE);
				}else {
					id = name_field.getText();
					
					JOptionPane.showMessageDialog(null, "설정되었습니다.", "success",JOptionPane.INFORMATION_MESSAGE);
					setting.setEnabled(false);
					name_field.setEnabled(false);
					wt.sendMsg();
					// who_area.append(id + "\n");
				}	
			}
		});
		
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(chat_field.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "채팅을 입력해주세요.", "warning",JOptionPane.WARNING_MESSAGE);
				}
				chat_area.append("[" + id + "]" + chat_field.getText() + "\n");
				wt.sendMsg();
				chat_field.setText("");
			}
		});
	}
}
