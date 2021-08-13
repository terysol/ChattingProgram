package work.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

// Ű����� ���۹��ڿ� �Է¹޾� ������ �����ϴ� ������
class WriteThread{
	Socket socket;
	ChattingGUI gui;
	String message;
	String id;
	ArrayList<String> participant = new ArrayList<String>();
	public WriteThread(ChattingGUI gui) {
		this.gui = gui;
		this.socket=gui.socket;
	}
	
	public void sendMsg() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter writer = null;
		try {
			// ������ ���ڿ� �����ϱ� ���� ��Ʈ�� ��ü ����
			writer = new PrintWriter(socket.getOutputStream(),true);
			// ù��° �����ʹ� id�̴�. ���濡�� id�� �Բ� �� ip�� ����
			
			if(gui.isFirst == true) {
				InetAddress iaddr = socket.getLocalAddress();
				String ip = iaddr.getHostAddress();
				id= gui.getId();
				participant.add(id);
				message="[" + id + "] �� �α��� (" + ip + ")"; 
				gui.isFirst=false;
				
				gui.who_area.append(id + "\n");
			}else {
				message="[" + id + "] " + gui.chat_field.getText();
			}
			
			writer.println(message);
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if(reader != null) reader.close();
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
}

// ������ ������ ���ڿ��� ���۹޴� ������
class ReadThread extends Thread{
	Socket socket;
	ChattingGUI gui;
	public ReadThread(Socket socket, ChattingGUI gui) {
		this.socket = socket;
		this.gui = gui;
	}
	@Override
	public void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true) {
				String message = reader.readLine();
				if(message == null) {
					System.out.println("������ ������");
					break;
				}
				// ���۹��� ���ڿ� ȭ�鿡 ���
				gui.chat_area.append(message + "\n");
			}
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if(reader != null) reader.close();
				if(socket != null) socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
public class ChattingClient {
	public static void main(String[] args) {
		final int PORT = 5000;
		Socket socket = null;
		ChattingGUI gui;
		try {
			socket = new Socket("127.0.0.1",PORT);
			System.out.println("���Ἲ�� !");
			gui = new ChattingGUI(socket);
			new ReadThread(socket, gui).start();
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
