package work.practice;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClientApp {
	private static final String SERVER_IP = "192.168.0.8";
	private static final int SERVER_PORT=5000;
	
	public static void main(String[] args) {
		String name=null;
		Scanner scan =new Scanner(System.in);
		while(true) {
			System.out.print("��ȭ���� �Է��ϼ��� >>");
			name=scan.nextLine();
			
			if(name.isEmpty() == false) {
				break;
			}
			System.out.println("��ȭ���� �� ���� �̻� �Է��ؾ� �˴ϴ�.\n");
		}
		scan.close();
		
		Socket socket =new Socket();
		try {
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			consoleLog("ä�ù濡 �����ϼ̽��ϴ�.");
			new ChatWindow(name, socket).show();	// ui ȭ���� ������ 
			
			PrintWriter pw= new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8),true);
			String request = "join: "+ name+"\r\n";
			pw.println(request);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private static void consoleLog(String log) {
		System.out.println(log);
	}
}
