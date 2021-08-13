package work.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

// 키보드로 전송문자열 입력받아 서버로 전송하는 스레드
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
			// 서버로 문자열 전송하기 위한 스트림 객체 생성
			writer = new PrintWriter(socket.getOutputStream(),true);
			// 첫번째 데이터는 id이다. 상대방에게 id와 함께 내 ip를 전송
			
			if(gui.isFirst == true) {
				InetAddress iaddr = socket.getLocalAddress();
				String ip = iaddr.getHostAddress();
				id= gui.getId();
				participant.add(id);
				message="[" + id + "] 님 로그인 (" + ip + ")"; 
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

// 서버가 보내온 문자열을 전송받는 스레드
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
					System.out.println("접속이 끊겼음");
					break;
				}
				// 전송받은 문자열 화면에 출력
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
			System.out.println("연결성공 !");
			gui = new ChattingGUI(socket);
			new ReadThread(socket, gui).start();
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
