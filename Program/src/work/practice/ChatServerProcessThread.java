package work.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread{
	private String nickname= null;
	private Socket socket= null;
	List<PrintWriter> listWriters =null;	// 채팅 서버에 연결된 모든 클라이언트들을 저장하고 있는 list
	
	public ChatServerProcessThread(Socket socket, List<PrintWriter> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;	
	}
	
	@Override
	public void run() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
			
			while(true) {
				String request = bufferedReader.readLine();
				
				if(request == null ) {
					consoleLog("클라이언트로부터 연결 끊김");
					doQuit(printWriter);
					break;
				}
				
				String [] tokens = request.split(":");
				if("join".equals(tokens[0])) {
					doJoin(tokens[1],printWriter);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void doQuit(PrintWriter writer) {
		removeWriter(writer);
		String data =this.nickname + "님이 퇴장하셨습니다.";
		broadcast(data);
	}
	
	private void removeWriter(PrintWriter writer) {
		synchronized (listWriters) {
			listWriters.remove(writer);
		}
	}
	private void doMessage(String data) {
		broadcast(this.nickname + ": " + data);
	}
	
	private void doJoin(String nickname,PrintWriter writer) {
		this.nickname=nickname;
		
		String data= nickname+"님이 입장하셨습니다.";
		broadcast(data);
		
		addWriter(writer);
	}
	private void addWriter(PrintWriter writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}
	
	private void broadcast(String data) {	// 서버에 연결된 모든 클라이언트들에게 메세지를 전달
		synchronized (listWriters) {
			for(PrintWriter writer: listWriters) {
				writer.println(data);
				writer.flush();
			}
		}
	}
	private void consoleLog(String log) {
		System.out.println(log);
	}
	
}
