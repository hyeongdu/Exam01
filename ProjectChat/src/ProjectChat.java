import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Map;

public class ProjectChat
{
	Map<String, PrintWriter> room1;
	
	
	static 	
	{
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(ClassNotFoundException cnfe) 
		{
			cnfe.printStackTrace();
		}
		catch(StackOverflowError e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public ProjectChat(Map<String, PrintWriter> room1)
	{
		this.room1 = room1;
	}
	
	Connection con = null;
	PreparedStatement pstmt1 = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null;
	PreparedStatement pstmt6 = null;
	PreparedStatement pstmt7 = null;
	PreparedStatement pstmt8 = null;
	PreparedStatement pstmt9 = null;
	PreparedStatement pstmt10 = null;
	PreparedStatement pstmt11= null;
	PreparedStatement pstmt12= null;
	PreparedStatement pstmt13= null;
	PreparedStatement pstmt14= null;
	PreparedStatement pstmt15= null;
	PreparedStatement pstmt16= null;
	PreparedStatement pstmt17= null;
	PreparedStatement pstmt18= null;
	PreparedStatement pstmt19= null;
	PreparedStatement pstmt20= null;
	PreparedStatement pstmt21= null;
	PreparedStatement pstmt22= null;
	PreparedStatement pstmt23= null;
	PreparedStatement pstmt24= null;
	PreparedStatement pstmt25= null;
	PreparedStatement pstmt26= null;
	ResultSet rs ;
	int updateCount ;
	String sql = null;
	
	
	
	
	public String loginMenu(String name, String password)
	{
		connectDatabase();
		sql = "select * from chatinfo where name = ? and password = ? ";
		String result = null;
		String check = "black";		
		try
		{
			pstmt1 = con.prepareStatement(sql);
			pstmt1.setString(1, name);
			pstmt1.setString(2, password);
			rs = pstmt1.executeQuery();
			
				if(rs.next())
				{
					sql = "select * from chatinfo where name = ? and blackinfo = ? ";
					
						pstmt2 = con.prepareStatement(sql);
						pstmt2.setString(1, name);
						pstmt2.setString(2, check);
						rs = pstmt2.executeQuery();
					
					
					if(rs.next())
						result = "블랙으로 접속할 수 없습니다.  ";
					else
					result =  "로그인";
				}
				else
				{
					
					result = "아이디 또는 비밀번호를 확인해주세요";
				}			
				
				rs.close();
				con.close();
				
		}catch(Exception e) {
			System.out.println("로그인 오류" + e);
		}
		return result;
	}
	
	public String joinchat(String name, String password)
	{
		connectDatabase();
		String result = null;
			sql = "insert into chatinfo(name,password) values(?,?)";
			
			try {
				pstmt3 = con.prepareStatement(sql);
				pstmt3.setString(1, name);
				pstmt3.setString(2, password);
				pstmt3.executeUpdate();
				result = "회원가입 완료";
				
			}catch(SQLIntegrityConstraintViolationException e)
			{
				result = "중복된 아이디입니다";

			}catch(Exception e)
			{
				e.printStackTrace();
				result = "회원가입 오류." + e;
			}
		try {
			con.close();
		}catch(Exception e)
		{
		
		}
		
		return result;
	}
	
	public String addroom(String name, String roomname, String password)
	{
		connectDatabase();
		String result = "1111";
		try
		{	sql = "insert into rlist values (?,?,?)";
			
				pstmt18=con.prepareStatement(sql);
				pstmt18.setString(1, roomname);
				pstmt18.setString(2, password);
				pstmt18.setString(3, name);
				pstmt18.executeUpdate();
				result = "방개설";
				if(result.equals("방개설"))
				{
					
					sql = "update room1 set name = ? , super = ? , roomname = ? where name = ?";
					pstmt5=con.prepareStatement(sql);
					pstmt5.setString(1, name);
					pstmt5.setString(2, "방장");
					pstmt5.setString(3, roomname);
					pstmt5.setString(4, name);
					pstmt5.executeUpdate();
				}
				else
				{
					result = "빈 방이 없습니다.";
				}
		}catch(SQLIntegrityConstraintViolationException e)
		{
			result = "중복된 방이름입니다.";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(" 방만들기 오류(DB)" + e);
		}
		try {
			con.close();
		}catch(Exception e)
		{
		
		}
		return result;
		
		
	}
	
	public String youWhere(String name)
	{
		String check1 ="1111";
		String check2 ="2222";
		connectDatabase();
		sql = "select * from room1 where name = ?";
		try
		{
			pstmt24 = con.prepareStatement(sql);
			pstmt24.setString(1, name);
			rs = pstmt24.executeQuery();
			while(rs.next())
			{
				check1 = rs.getString("name");
				 check2 = rs.getString("roomname");
				
			}
			
		}catch(Exception e)
		{
			System.out.println("위치확인 에러" + e);
		}
		try
		{
			con.close();
		}catch(Exception e)
		{
		
		}
		return check1 + "  님은  " + check2  + "  방에 있습니다";
	}

	//메세지 보내기
	public ArrayList<String> sendAllchat(String name)
	{
		connectDatabase();
		ArrayList<String> arr = new ArrayList<>();
		sql = "select name from room1  "
				+ " where name != ? and roomname = (select roomname from room1 where name = ?) ";
		
		try
		{	
			pstmt6 = con.prepareStatement(sql);
			pstmt6.setString(1, name);
			pstmt6.setString(2, name);
			rs = pstmt6.executeQuery();
			while(rs.next())
			{
				arr.add(rs.getString("name"));
			}
			con.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("array 오류" + e);
		}
		return arr;
	}
	public String enterroom(String name,String roomname, String password)
	{
		connectDatabase();
		String result = "";
		
		try
		{
			sql = "select * from rlist where name = ? and password = ?";
			pstmt7=con.prepareStatement(sql);
			pstmt7.setString(1, roomname);
			pstmt7.setString(2, password);
			rs = pstmt7.executeQuery();
			if(rs.next())
			{
				sql = "update room1 set roomname = ? where name = ?";
				pstmt3 = con.prepareStatement(sql);
				pstmt3.setString(1, roomname);
				pstmt3.setString(2, name);
				pstmt3.executeUpdate();
				
				result =  "방입장";
			}
			else
			{
				
				result = "없는 방입니다.";
			}
			rs.close();
			con.close();
		}catch(Exception e)
		{
			System.out.println(" 방들어가기 오류(DB)" + e);
		}
		
		return result;
		
		
	}
	public String npcDb(String npcPassword, String name)
	{
		connectDatabase();
		String result = "";
		sql = "select * from chatinfo where npcPassword = ? and name = ?";
		try
		{
			pstmt8=con.prepareStatement(sql);
			pstmt8.setString(1, npcPassword);
			pstmt8.setString(2, name);
			rs = pstmt8.executeQuery();
			if(rs.next())
			{
				sql = "insert into chatinfo set NPC = ? where name = ?";
				pstmt15 = con.prepareStatement(sql);
				pstmt15.setString(1, name);
				pstmt15.setString(2, name);
				pstmt15.executeUpdate();
				result =  "관리자 확인";
			}
			else
			{
				
				result = "관리자 권한이 없습니다.";
			}
			rs.close();
			con.close();
		}catch(Exception e)
		{
			System.out.println(" 관리자 오류(DB)" + e);
		} 
		return result;
	}
	public void welcome(String name)
	{
		connectDatabase();
			sql = "update chatinfo set roomname = ? where name = ?";
			try 
			{
				pstmt9 = con.prepareStatement(sql);
				pstmt9.setString(1, "로그인");
				pstmt9.setString(2, name);
				pstmt9.executeUpdate();
				
				sql = "insert into room1 (name,roomname) values(?,?)";
				pstmt10 = con.prepareStatement(sql);
				pstmt10.setString(1, name);
				pstmt10.setString(2, "대기방");
				pstmt10.executeUpdate();
				
			}
			catch(Exception e)
			{
				System.out.println("대기방 선정" + e);
			}
		try {
			con.close();
			
		}catch(Exception e)
		{}
			
			
			
	}
	public void end(String name)
	{
		connectDatabase();
		sql = "update chatinfo set roomname =?  where name = ? ";
		try 
		{
			pstmt9 = con.prepareStatement(sql);
			pstmt9.setString(1, "로그아웃");
			pstmt9.setString(2, name);
			pstmt9.executeUpdate();
			
			sql = "delete room1 where name = ? ";
			pstmt16 = con.prepareStatement(sql);
			pstmt16.setString(1, name);
			pstmt16.executeUpdate();
			
			con.close();
		}catch(Exception e)
		{
			System.out.println("로그아웃 오류" + e);
		}
		
	}
	public String addblacklist(String name)
	{
		connectDatabase();
		String result = null;
			sql = "update chatinfo set blackinfo = ? where name = ?";
			
			try {
				pstmt10 = con.prepareStatement(sql);
				pstmt10.setString(1, "black");
				pstmt10.setString(2, name);
				pstmt10.executeUpdate();
				result = "블랙 추가";
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				result = "서버 문제 입니다." + e;
			}
		try {
			con.close();
		}catch(Exception e)
		{
			System.out.println("블랙 추가 오류 " + e);
		}
		
		return result;
	}
	public String delblacklist(String name)
	{
		connectDatabase();
		String result = null;
			sql = "update chatinfo set blackinfo = ? where name = ?";
			
			try {
				pstmt10 = con.prepareStatement(sql);
				pstmt10.setString(1, "");
				pstmt10.setString(2, name);
				pstmt10.executeUpdate();
				result = "블랙 제거";
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				result = "서버 문제 입니다." + e;
			}
		try {
			con.close();
		}catch(Exception e)
		{
			System.out.println("블랙 추가 오류 " + e);
		}
		
		return result;
	}
	public boolean npccheck(String name)
	{
		boolean result = false;
		connectDatabase();
		sql = "select * from chatinfo where npcpassword = ? and name = ? ";
		try
		{
			pstmt11 = con.prepareStatement(sql);
			pstmt11.setString(1, "12345");
			pstmt11.setString(2, name);
			rs = pstmt11.executeQuery();
			if(rs.next())
			{
				result =  true;
				
			}
			else
			{
				result =  false;
			}
		}catch(Exception e)
		{
			System.out.println("npc확인 오류" + e);
		}
		return result ;
		
	}
	public void exitDb(String name)
	{
		connectDatabase();
		try 
		{
			sql = "update room1 set roomname = ?  , super = ' ' where name =? ";
			pstmt12 = con.prepareStatement(sql);
			pstmt12.setString(1, "대기방");
			pstmt12.setString(2, name);
			pstmt12.executeUpdate();
			
			sql = "select * from rlist where super = ?";
			pstmt20 = con.prepareStatement(sql);
			pstmt20.setString(1, name);
			rs = pstmt20.executeQuery();
			
			
			
			
			if(rs.next())
			{
				String result = rs.getString(name);
				sql = "update rlist set super =  "
						+ " (select name from room1 where roomname = '호호호' and rownum = 1) where name = '호호호'";
				pstmt21 = con.prepareStatement(sql);
//				pstmt21.setString(1, "호호");
//				pstmt21.setString(2, "호호");
				pstmt21.executeUpdate();
			}
		}catch(Exception e )
		{
			System.out.println("방 나가기 오류 " + e);
		}
		try {
			con.close();
		}catch(Exception e) {}
	}
	public boolean membercheckDb(String name)
	{
		boolean result = true;
		connectDatabase();
		sql = "select * from chatinfo where name = ? and roomname = '로그인'";
		try 
		{
			pstmt13 = con.prepareStatement(sql);
			pstmt13.setString(1, name);
			rs = pstmt13.executeQuery();
			if(rs.next())
			{
				result = true;
			}
			else
			{
				result = false;
			}
		}catch(Exception e)
		{
			System.out.println("귓속말 db 오류" + e);
		}
		try {
			con.close();
		}catch(Exception e )
		{
			
		}
		return result;
		
	}
	public String bomb(String roomname)
	{
		String result = "1111";
		connectDatabase();
		sql = "delete rlist where name = ?";
		try
		{
			pstmt22 = con.prepareStatement(sql);
			pstmt22.setString(1, roomname);
			pstmt22.executeUpdate();
			
			sql = "update room1 set roomname = '대기방' where roomname = ?";
			pstmt23 = con.prepareStatement(sql);
			pstmt23.setString(1, roomname);
			pstmt23.executeUpdate();
		}catch(Exception e)
		{
			System.out.println("폭탄 오류" + e );
		}
		try
		{
			con.close();
		}catch(Exception e ) {}
		return roomname + "방이 폭파되었습니다.";
	}
	public String ban(String name,String banname)
	{
		String result = "1111";
		connectDatabase();
		sql = "select * from rlist where super = ?";
		try 
		{
			pstmt17 = con.prepareStatement(sql);
			pstmt17.setString(1, name);
			rs = pstmt17.executeQuery();
			if(rs.next())
			{
				sql = " update room1 set roomname = '대기방' where name = ? ";
				pstmt19 = con.prepareStatement(sql);
				pstmt19.setString(1, banname);
				pstmt19.executeUpdate();
				System.out.println(name+ "님이 " +banname + "을 강퇴");	
				result =  "강퇴";
			}
			else
			{
				result =   "강퇴 권한이 없습니다";
			}
		}catch(Exception e)
		{
			System.out.println("귓속말 db 오류" + e);
		}
		try
		{
			con.close();
		}catch(Exception e)
		{}
		return result;
	}

	
		
	public ArrayList<String> listcheck(String name)
	{
		connectDatabase();
		ArrayList<String> arr = new ArrayList<>();
		sql = "select name from room1  "
				+ " where roomname = (select roomname from room1 where name = ?) ";
		
		try
		{	
			pstmt25 = con.prepareStatement(sql);
			pstmt25.setString(1, name);
			rs = pstmt25.executeQuery();
			while(rs.next())
			{
				arr.add(rs.getString("name"));
			}
			con.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("array 오류" + e);
		}
		return arr;
	}
	public void connectDatabase()
	{
		try {
			con = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:xe",
				"scott",
				"tiger");
		}
		catch(Exception e) {
		
		}
	
	}


	
}

