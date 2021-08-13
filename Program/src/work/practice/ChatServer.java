package work.practice;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int PORT = 5000;
	public static void main(String[] args) {
		ServerSocket serversocket=null;
		List<PrintWriter> listWriters = new ArrayList<PrintWriter>();
		
		try {
			serversocket =new ServerSocket();
			
			String HostAddress = InetAddress.getLocalHost().getHostAddress();
			serversocket.bind(new InetSocketAddress(HostAddress, PORT));
			consoleLog("연결 기다림 - " + HostAddress + " : " + PORT);
			
			while(true) {
				Socket socket= serversocket.accept();
				new ChatServerProcessThread(socket, listWriters).start();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(serversocket != null && !serversocket.isClosed()) {
					serversocket.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void consoleLog(String log) {
		System.out.println("[server" + Thread.currentThread().getId() + "]" + log);
	}
}
	