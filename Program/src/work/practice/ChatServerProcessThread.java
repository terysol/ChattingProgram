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
	List<PrintWriter> listWriters =null;	// ä�� ������ ����� ��� Ŭ���̾�Ʈ���� �����ϰ� �ִ� list
	
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
					consoleLog("Ŭ���̾�Ʈ�κ��� ���� ����");
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
		String data =this.nickname + "���� �����ϼ̽��ϴ�.";
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
		
		String data= nickname+"���� �����ϼ̽��ϴ�.";
		broadcast(data);
		
		addWriter(writer);
	}
	private void addWriter(PrintWriter writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}
	
	private void broadcast(String data) {	// ������ ����� ��� Ŭ���̾�Ʈ�鿡�� �޼����� ����
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
