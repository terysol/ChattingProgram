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
			Socket socket = null;		// Ŭ���̾�Ʈ ���� �� ����� SOCKET
			System.out.println("socket : " + PORT + "���� ������ ���Ƚ��ϴ�.");
			
			while(true) {
				socket = serversocket.accept();	// ���� ������ ���� �� socket�� ������ ���� �Ҵ�
				System.out.println("Client�� ������ : " + socket.getLocalAddress());
				
				// InputStream - Ŭ���̾�Ʈ���� ������
				InputStream input = socket.getInputStream();	// socket�� inputstream ������ inputstream in�� �ֱ�
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));	// BufferedReader�� �� InputStream�� ��� ���
				
				System.out.println(reader.readLine());	// Ŭ���̾�Ʈ���� �� �޼��� Ȯ��
				
				// OutputStream - �������� Ŭ���̾�Ʈ�� 
				OutputStream out = socket.getOutputStream();	// socket�� OutputStream ������ OutputStream out�� ���� ��
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

