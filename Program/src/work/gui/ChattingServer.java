package work.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

// client로 부터 전송된 문자열을 받아서 다른 클라이언트에게 문자열을 보내주는 스레드
class ServerThread extends Thread{
	Socket socket;
	Vector<Socket> vector;
	public ServerThread(Socket socket, Vector<Socket> vector) {
		this.socket = socket;
		this.vector = vector;
	}
	
	@Override
	public void run() {
		BufferedReader reader=null;
		try {
			reader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message=null;
			
			while(true) {
				message = reader.readLine();
				if(message == null) {
					vector.remove(socket);
					break;
				}
				sendMsg(message);
			}
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if(reader != null) reader.close();
				if(socket != null) socket.close();
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	// 전송받은 문자열 다른 클라이언트들에게 보내주는 메서드
	public void sendMsg(String message) {
		try {
			for(Socket socket:vector) {
				if(socket != this.socket) {
					PrintWriter writer= new PrintWriter(socket.getOutputStream(),true);
					writer.println(message);
					writer.flush();
				}
			}
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}

public class ChattingServer {
	final static int PORT = 5000;
	public static void main(String[] args) {
		
		ServerSocket server = null;
		Socket socket = null;
		
		Vector<Socket> vector = new Vector<Socket>();
		try {
			server = new ServerSocket(PORT);
			while(true) {
				System.out.println("접속 대기중..");
				socket = server.accept();
				vector.add(socket);
				new ServerThread(socket,vector).start();
			}
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
