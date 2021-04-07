import java.util.*;
import java.io.*;
import java.net.*;

public class PjchatClient
{
	
	PrintWriter out = null;
	BufferedReader in = null;
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		Scanner sc = new Scanner(System.in);

		try
		{
			String ServerIp = "localhost";
			if(args.length >0)
				ServerIp = args[0];
			Socket socket = new Socket(ServerIp, 9999);
			System.out.println("서버와 연결되었습니다.");
			Thread receiver = new Receiver(socket);
			receiver.start();
			Thread sender = new Sender(socket);
			sender.start();
			
		}catch(Exception e)
		{
			System.out.println("서버 접속 오류 " + e );
		}
	}

}

class Receiver extends Thread
{
	Socket socket;
	BufferedReader in = null;
	
	public Receiver(Socket socket)
	{
		this.socket = socket;
		
		try{
			
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		}catch(java.net.SocketException ne) {
			
		}
		catch(Exception e)
		{
			System.out.println("예외1 : " + e);
		}
	}
	
	public void run()
	{
		while (in!=null)
		{
			try 
			{
				System.out.println(in.readLine());
			}catch(java.net.SocketException ne) {
				break;
			}
			catch (Exception e)
			{
				System.out.println("예외2 : " + e);
				System.out.println("예외" + e);
				break;
			}
			
		}
	
		
	}
}


class Sender extends Thread
{
	Socket socket;
	PrintWriter out = null;
	
	//생성자
	public Sender(Socket socket)
	{
		this.socket = socket;
		try {
			out = new PrintWriter(this.socket.getOutputStream(), true);
			
		}catch(Exception e)
		{
			System.out.println("예외S3 : " + e);
		}
	}
	public void run()
	{
		Scanner sc = new Scanner(System.in);
		
		try
		{
			
			while(out!=null)
			{
				try
				{
					String s = sc.nextLine();
					out.println(s);
					
					if(s.equals("q") || s.equals("Q"))
						break;
				}catch(Exception e) {
					System.out.println("예외S1 : " + e);
					
				}
			}
			out.close();
			socket.close();
			
		}catch(Exception e)
		{
			System.out.println("예외S2: " + e);
			
		}
		sc.close();
	}
	
}
