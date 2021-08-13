package work.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class consoleChatClient {
	public static void main(String[] args) {
		final int PORT = 6000;
		try {
			Socket socket = new Socket("192.168.0.8",PORT);
			System.out.println("socket ������ ���� ����!");
			
			// OutputStream - Ŭ���̾�Ʈ���� server�� �޼��� �߼�
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out,true);
			
			writer.println("CLIENT TO SERVER");
			
			// InputStream - Server���� ���� �޼��� Ŭ���̾�Ʈ�� ������
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			System.out.println(reader.readLine());
			System.out.println("CLIENT SOKET CLOSE");
			socket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
