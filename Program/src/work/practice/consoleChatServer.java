package work.practice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class consoleChatServer {
	public static void main(String[] args) {
		final int PORT = 6000;
		Scanner scan = new Scanner(System.in);
		try {
			ServerSocket serversocket = new ServerSocket(PORT);
			Socket socket = null;		// 클라이언트 접속 시 사용할 SOCKET
			System.out.println("socket : " + PORT + "으로 서버가 열렸습니다.");
			
			while(true) {
				socket = serversocket.accept();	// 소켓 서버로 접속 시 socket에 접속자 정보 할당
				System.out.println("Client가 접속함 : " + socket.getLocalAddress());
				
				// InputStream - 클라이언트에서 서버로
				InputStream input = socket.getInputStream();	// socket의 inputstream 정보를 inputstream in에 넣기
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));	// BufferedReader에 위 InputStream을 담아 사용
				
				System.out.println(reader.readLine());	// 클라이언트에서 온 메세지 확인
				
				// OutputStream - 서버에서 클라이언트로 
				OutputStream out = socket.getOutputStream();	// socket의 OutputStream 정보를 OutputStream out에 넣은 뒤
				DataOutputStream writer =new DataOutputStream(out);
				String message="";
				message= scan.nextLine();
				writer.writeUTF(message);
				writer.flush();
				
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}

