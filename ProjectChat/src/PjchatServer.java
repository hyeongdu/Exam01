import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;



public class PjchatServer
{
	// 채팅 출력
	Map<String, PrintWriter> room1;
	
	//채팅 금지어
	List<String> list1 = new ArrayList<>();
	
	//공지 목록
	List<String> list2 = new ArrayList<>();
	//데이터베이스 클래스
	ProjectChat pjsql = new ProjectChat(room1);
	ServerSocket serverSocket = null;
	Socket socket = null;

	
	

	

	
	public PjchatServer()
	{
		room1 = new HashMap<String, PrintWriter>();
		Collections.synchronizedMap(room1);
		
	}
	public void init()
	{
		try
		{
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다");
			
			while(true)
			{	
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + " : " + socket.getPort());
	
				Thread msr = new MultiServerT(socket);
				msr.start();
			}
			

			
		}catch(Exception e)
		{
			System.out.println("서버 접속 오류" + e);
		}finally
		{
			try
			{
				serverSocket.close();
			}catch (Exception e )
			{
				System.out.println(" 서버 종료 오류" + e);
			}
		}
	}
	
	
	
	boolean start = true;
	
	public static void main(String[] args)
	{
		PjchatServer pjs = new PjchatServer();
		pjs.init();
		
	}
	class MultiServerT extends Thread
	{	
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;
		String name = "";
		
		
	
		public MultiServerT(Socket socket)
		{
			
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			
			}
			catch(Exception e)
			{
				System.out.println("예외 : " + e);
			
			}
		}
	
		public void run()
		{
			
			String s = "";
			try {
					while(start)
					{
						out.println("서버에 오신걸 환영합니다.");
						out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
						out.println("    1. 로그인   ");
						out.println("    2. 회원가입  ");
						out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
					
						s = in.readLine();
						switch(s)
						{	
						case "1":
							out.println(s + "번을 누르셨습니다 로그인하겠습니다.");
							login();
							start = false;
							break;
						
					
						case "2":
							out.println(s + "번을 누르셨습니다 회원가입.");
							joinchat();
							start = true;
							break;
						}
						
						
					}
					start = true;
					//printwriter
					room1.put(name, out);
					
					//로그인 회원DB
					pjsql.welcome(name);
					
					//금지어 목록 받기
					wordlist();
					
					//공지사항 목록 받기
					noticelist();
					
					System.out.println("현재 접속자 수 : " + room1.size());
					room1Msg(name +"님이 대기방에 입장하셨습니다", "");
					out.println("/명령어 를 입력하시면 명령어를 보실 수 있습니다. ");
					if(!list2.isEmpty())
					{
						out.println("                 공 지 사 항");
						for(String e : list2)
							out.println("        "+e);
					}
					
				
				
					
					while(in!=null)
					{	
						
						s = in.readLine();
						
						
						System.out.println(name + ">" +s);
						try
						{
							
							if(s.charAt(0) == '/')
							{
								StringTokenizer st = new StringTokenizer(s, " ");
							
								String stz = st.nextToken();
								if(stz.equals("/리스트"))
								{
									list(name);
								}
								else if(stz.equals("/명령어"))
								{
									help();
								}
								else if(stz.equals("/귓속말"))
								{
									String stz2 = st.nextToken() ;
									String stz3 = "";
									while(st.hasMoreTokens())
									{
										stz3 += st.nextToken();
									}
									if(pjsql.membercheckDb(stz2))
										oneMsg(stz3,stz2);
									else
										out.println("로그아웃 상태 입니다.");
									
								}
								else if(stz.equals("/방입장"))
								{
									String stz2 = st.nextToken();
									if(st.hasMoreTokens())
									{
										enterroom(stz2, st.nextToken());
										
									}
									else
									{
										enterroom(stz2, "공개방");
										
									}
								}
								else if(stz.equals("/방만들기"))
								{
									String stz2 = st.nextToken();
									if(st.hasMoreTokens())
										addroom(stz2, st.nextToken());
									else
										addroom(stz2, "공개방");
									out.println("방이 개설되었습니다");
									
								}
							
								else if (stz.equals("/운영자"))
								{
									out.println(npc(st.nextToken(), name));
								}
								else if(stz.equals("/블랙추가"))
								{
									out.println(addblack(name, st.nextToken()));
								}
								else if(stz.equals("/블랙제거"))
								{
									out.println(delblack(name, st.nextToken()));
								}
								else if (stz.equals("/공지추가"))
								{
									String stz3 = "";
									while(st.hasMoreTokens())
									{
										stz3 += st.nextToken() + " ";
									}
									list2.add(stz3);
									pjsql.addnotice(stz3);
								}
								else if (stz.equals("/공지게시"))
								{
									chatAllMsg();
								}
								else if (stz.equals("/위치"))
								{
									String stz2 = st.nextToken();
									out.println(pjsql.youWhere(stz2));
								}
								else if(stz.equals("/방나가기"))
								{
									pjsql.exitDb(name);
									out.println("대기방으로 이동");
									list(name);
								}
								else if(stz.equals("/종료"))
								{
									
									room1Msg(name + "님 서버 종료", name);
									pjsql.end(name);
								
								
								}
								else if(stz.equals("/강퇴"))
								{
									String stz3 = st.nextToken();
									String result = pjsql.ban(name, stz3);
									if(result.equals("강퇴"))
									{
										room1Msg(result, name);
										room1Msg(stz3 +"님이 대기방에 입장하셨습니다", stz3);
									}
									else
									{
										out.println(result);
									}
								}
								else if(stz.equals("/방폭파"))
								{
									String stz3 = st.nextToken();
									String result = pjsql.bomb(stz3);
								
								}
								
								else if(stz.equals("/방장위임"))
								{
									String stz2 = st.nextToken();
									out.println(pjsql.change(name, stz2));
								}
								else if(stz.equals("/금지어추가"))
								{
									String stz2 = st.nextToken();
									list1.add(stz2);
									out.println(pjsql.banword(name, stz2));
									
								}
								else if(stz.equals("/금지어제거"))
								{
									String stz2 = st.nextToken();
									list1.remove(stz2);
									out.println(pjsql.removeword(name, stz2));
								}
								
								else if(stz.equals("/금지어목록"))
								{
									for(String e : list1)
										out.println(e);
								}
								else if(stz.equals("/방리스트"))
								{
									Map<String,String> map = pjsql.roomlist();
									out.println(map.values());
								}
								else 
								{
									out.println("잘못된 명령어입니다.");
								}
							}
							else
							{
								for(String e : list1)
								{
									s= s.replace(e, "천사");
								}	
									room1Msg(s,name);
								
							}
								
						}catch(StringIndexOutOfBoundsException sib)
						{
							continue;
						}catch(Exception e)
						{
							System.out.println("서버채팅 오류" + e);
						}

					}
					
				}catch(Exception e)
				{
						System.out.println("서버 접속 오류" + e);
				}finally
				{
					try
					{
					in.close();
					out.close();
					socket.close();
					room1.remove(name);
					}catch (Exception e )
					{
						System.out.println(" 서버 종료 오류" + e);
							
					}
				}
				
		}
		
		
		public void login()
		{
			boolean login = true;
			String password = "";
			String a ="";
			String b = "";
			try {
			while(login)
				{
					out.println("아이디와 비밀번호를 입력해주세요");
					a = in.readLine();
					name = a ;
					out.println("아이디 : " + a);
					out.println("비밀번호 입력하세요");
					b = in.readLine();
					out.println("비밀번호 : " + b);
					password  = b ;
					String result = pjsql.loginMenu(name, password);
					out.println(result);
					if(result.equals("로그인"))
					{
						this.name = name;
						login = false;
					}
					else
					{
						out.println("실패");
						login = true;
					}
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void joinchat()
		{
			String name = "";
			String password = "";
			String a ="";
			String b = "";
			boolean join = true;
		try {
			
			while(join)
				{	
					out.println("회원가입 - 아이디와 비밀번호를 입력하세요.");
					a = in.readLine();
					out.println("아이디 : " + a);
					name = a;
					a = in.readLine();
					out.println("비밀번호 : " + a);
					password = a;
					String result = pjsql.joinchat(name,password);
					out.println(result);
					if(result.equals("회원가입 완료"))
					{
						out.println("로그인");
						join = false;
					}
				}
				}catch(Exception e)
				{
					System.out.println("회원가입 오류" + e);
				}
		}

		public void chatAllMsg()
		{
			if(pjsql.npccheck(name) == true)
			{
				String notice = "                                       *공지사항* ";
				String space = "                               ";
				Iterator it2 = room1.keySet().iterator();
				
				
				while(it2.hasNext())
				{
					try
					{
						PrintWriter it_out = (PrintWriter)room1.get(it2.next());
						
							it_out.println(notice);
							for(String e : list2)
								it_out.println(space + e);
						
					}catch(Exception e)
					{
						System.out.println("예외 : " + e );
					}
				}
			}
			else
			{
				out.println("관리자 권한이 없습니다.");
			}
		}
		
	
		public void oneMsg(String msg, String stz2)

		{
				try
				{
					if(room1.containsKey(stz2))
					{
						PrintWriter it_out = (PrintWriter)room1.get(stz2);
						it_out.println(name+ "님의 귓속말" + ">" + msg);
					}
					else
						out.println("로그아웃 상태입니다.");
					
				}catch(Exception e)
				{
					System.out.println("예외 : " + e );
				}
			
			
			
		}
		public void room1Msg(String msg, String name)
		{
			
			Iterator iter = pjsql.sendAllchat(name).iterator();
			
			try
			{
				while(iter.hasNext())
				{
					PrintWriter it_out = (PrintWriter)room1.get(iter.next());
					
						it_out.println(name +">"+ msg);
				}
			}catch(Exception e)
			{
				System.out.println("전체 채팅 오류" + e);
			}
			
			
		}
		
		
		public void list(String name)
		{
			
			Iterator<String> it = pjsql.sendAllchat(name).iterator();
			String msg = "현재 방 멤버 리스트 [";
			while(it.hasNext())
			{
				msg+= it.next() + ",";
				
			}
			msg = msg.substring(0, msg.length()-1) + "]";
			
			try
			{
				out.println(msg);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		
		
		public void help()
		{
			out.println("----------------------------------------");
			out.println("/리스트 : 현재 있는 방에 인원을 보실 수 있습니다. ");
			out.println("/명령어 : 명령어를 보실 수 있습니다. ");
			out.println("/방만들기 방이름 비밀번호 :  방만들기  ");
			out.println("/귓속말 받는이 내용 : 귓속말 ");
			out.println("/방입장 방이름 비밀번호 : 개설된 방 입장 ");
			out.println("/방나가기 :  방나가기 ");
			out.println("/강퇴 : 방장 기능 ");
			out.println("/위치 : 상대방 위치 확인 ");
			out.println("/종료 : 종료 ");
			out.println("----------------------------------------");
		}
		
		public void addroom(String roomname, String password)
		{	
			
			try
			{
				
				
					String result = pjsql.addroom(name,roomname,password);
					if(result.equals("방개설"))
					{
						
						out.println(roomname + "방 개설");
						
						
						
					}
					else
					{
						out.print("중복된 방 이름입니다. ");
						out.print("다시 입력해주세요");
					}
				
			}catch(Exception e)
			{
				System.out.println("방만들기 오류(server)" + e);
			}
		
		}
		public void enterroom(String roomname, String password)
		{
			
				try
				{
					if(password.equals("공개방"))
					{
						String result = pjsql.enterroom(name, roomname, password);
						if(result.equals("방입장"))
						{
							out.println(result);
						}
						else
						{
							out.println("방이름 또는 비밀번호가  맞지않습니다.");
						}
					}
					else
					{
					
						out.println("방 : " + roomname);
						out.println("비밀번호 : " + password);
						
						String result = pjsql.enterroom(name, roomname, password);
					
						if(result.equals("방입장"))
						{
							room1Msg(name +" 님이 입장했습니다.",name);
						}
						else
						{
							out.println("방이름 또는 비밀번호가 맞지않습니다.");
						}
					}
				}catch(Exception e)
				{
					System.out.println("방 들어가기 오류" + e);
				}
				
		
		}
		
		public void wordlist()
		{
				list1 = pjsql.wordlist();
		}
		
		public void noticelist()
		{
			list2 = pjsql.noticelist();
		}
		public String npc(String password,String name)
		{
			 return pjsql.npcDb(password, name);	
		}
		public String addblack(String name ,String blackname)
		{
			if (pjsql.npccheck(name) == true)
				return pjsql.addblacklist(blackname);
			else
				return "권한이 없습니다.";
		}
		public String delblack(String name, String blackname)
		{
			if (pjsql.npccheck(name) == true)
				return pjsql.delblacklist(blackname);
			else
				return "권한이 없습니다.";
		}
		
		
	}

}	

