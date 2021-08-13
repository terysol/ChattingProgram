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
			System.out.print("대화명을 입력하세요 >>");
			name=scan.nextLine();
			
			if(name.isEmpty() == false) {
				break;
			}
			System.out.println("대화명은 한 글자 이상 입력해야 됩니다.\n");
		}
		scan.close();
		
		Socket socket =new Socket();
		try {
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			consoleLog("채팅방에 입장하셨습니다.");
			new ChatWindow(name, socket).show();	// ui 화면이 나오게 
			
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
