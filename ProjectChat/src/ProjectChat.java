import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
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
	PreparedStatement pstmt27= null;
	PreparedStatement pstmt28 = null;
	PreparedStatement pstmt29= null;
	PreparedStatement pstmt30= null;
	PreparedStatement pstmt31= null;
	PreparedStatement pstmt32= null;
	PreparedStatement pstmt33= null;
	PreparedStatement pstmt34= null;
	PreparedStatement pstmt35= null;
	PreparedStatement pstmt36= null;
	PreparedStatement pstmt37= null;
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
						result = "???????????? ????????? ??? ????????????.  ";
					else
					result =  "?????????";
				}
				else
				{
					
					result = "????????? ?????? ??????????????? ??????????????????";
				}			
				
				rs.close();
				con.close();
				
		}catch(Exception e) {
			System.out.println("????????? ??????" + e);
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
				result = "???????????? ??????";
				
			}catch(SQLIntegrityConstraintViolationException e)
			{
				result = "????????? ??????????????????";

			}catch(Exception e)
			{
				e.printStackTrace();
				result = "???????????? ??????." + e;
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
				result = "?????????";
				if(result.equals("?????????"))
				{
					
					sql = "update room1 set name = ? , super = ? , roomname = ? where name = ?";
					pstmt5=con.prepareStatement(sql);
					pstmt5.setString(1, name);
					pstmt5.setString(2, "??????");
					pstmt5.setString(3, roomname);
					pstmt5.setString(4, name);
					pstmt5.executeUpdate();
				}
				else
				{
					result = "??? ?????? ????????????.";
				}
		}catch(SQLIntegrityConstraintViolationException e)
		{
			result = "????????? ??????????????????.";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(" ???????????? ??????(DB)" + e);
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
		String result = "";
		connectDatabase();
		sql = "select * from room1 where name = ?";
		try
		{
			pstmt24 = con.prepareStatement(sql);
			pstmt24.setString(1, name);
			rs = pstmt24.executeQuery();
			if(rs.next())
			{
				check1 = rs.getString("name");
				 check2 = rs.getString("roomname");
				 result = check1 + " ??????  " + check2  + " ?????? ????????????";
			}
			else
			{
				result = "???????????? ???????????????. ";
			}
			
			
		}catch(Exception e)
		{
			System.out.println("???????????? ??????" + e);
		}
		try
		{
			con.close();
		}catch(Exception e)
		{
		
		}
		return result;
	}

	//????????? ?????????
	public ArrayList<String> sendAllchat(String name)
	{
		connectDatabase();
		ArrayList<String> arr = new ArrayList<>();
		sql = "select name from room1  "
				+ " where  roomname = (select roomname from room1 where name = ?) ";
		
		try
		{	
			pstmt6 = con.prepareStatement(sql);
			pstmt6.setString(1, name);
			rs = pstmt6.executeQuery();
			while(rs.next())
			{
				arr.add(rs.getString("name"));
			}
			con.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("array ??????" + e);
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
				
				result =  "?????????";
			}
			else
			{
				
				result = "?????? ????????????.";
			}
			rs.close();
			con.close();
		}catch(Exception e)
		{
			System.out.println(" ??????????????? ??????(DB)" + e);
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
				result =  "????????? ??????";
			}
			else
			{
				
				result = "????????? ????????? ????????????.";
			}
			rs.close();
			con.close();
		}catch(Exception e)
		{
			System.out.println(" ????????? ??????(DB)" + e);
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
				pstmt9.setString(1, "?????????");
				pstmt9.setString(2, name);
				pstmt9.executeUpdate();
				
				sql = "insert into room1 (name,roomname) values(?,?)";
				pstmt10 = con.prepareStatement(sql);
				pstmt10.setString(1, name);
				pstmt10.setString(2, "?????????");
				pstmt10.executeUpdate();
				
			}
			catch(Exception e)
			{
				System.out.println("????????? ??????" + e);
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
			pstmt9.setString(1, "????????????");
			pstmt9.setString(2, name);
			pstmt9.executeUpdate();
			
			sql = "delete room1 where name = ? ";
			pstmt16 = con.prepareStatement(sql);
			pstmt16.setString(1, name);
			pstmt16.executeUpdate();
			
			con.close();
		}catch(Exception e)
		{
			System.out.println("???????????? ??????" + e);
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
				result = "?????? ??????";
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				result = "?????? ?????? ?????????." + e;
			}
		try {
			con.close();
		}catch(Exception e)
		{
			System.out.println("?????? ?????? ?????? " + e);
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
				result = "?????? ??????";
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				result = "?????? ?????? ?????????." + e;
			}
		try {
			con.close();
		}catch(Exception e)
		{
			System.out.println("?????? ?????? ?????? " + e);
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
			System.out.println("npc?????? ??????" + e);
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
			pstmt12.setString(1, "?????????");
			pstmt12.setString(2, name);
			pstmt12.executeUpdate();
			
			sql = "select name from rlist where super = ?";
			pstmt20 = con.prepareStatement(sql);
			pstmt20.setString(1, name);
			rs = pstmt20.executeQuery();
			
			
			
			
			if(rs.next())
			{
				String result = rs.getString(1);
				sql = "update rlist set super =  "
						+ " (select name from room1 where roomname = ? and rownum = 1) where name = ? ";
				pstmt21 = con.prepareStatement(sql);
				pstmt21.setString(1, result);
				pstmt21.setString(2, result);
				pstmt21.executeUpdate();
				
				sql = "update room1 set super = '??????' where roomname =  ? and rownum =1";
				pstmt31 = con.prepareStatement(sql);
				pstmt31.setString(1, result);
				pstmt31.executeUpdate();
				
			}
		}catch(Exception e )
		{
			System.out.println("??? ????????? ?????? " + e);
		}
		try {
			con.close();
		}catch(Exception e) {}
	}
	public boolean membercheckDb(String name)
	{
		boolean result = true;
		connectDatabase();
		sql = "select * from chatinfo where name = ? and roomname = '?????????'";
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
			System.out.println("????????? db ??????" + e);
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
		
		connectDatabase();
		sql = "delete rlist where name = ?";
		try
		{
			pstmt22 = con.prepareStatement(sql);
			pstmt22.setString(1, roomname);
			pstmt22.executeUpdate();
			
			sql = "update room1 set roomname = '?????????' where roomname = ?";
			pstmt23 = con.prepareStatement(sql);
			pstmt23.setString(1, roomname);
			pstmt23.executeUpdate();
		}catch(Exception e)
		{
			System.out.println("?????? ??????" + e );
		}
		try
		{
			con.close();
		}catch(Exception e ) {}
		return roomname + "?????? ?????????????????????.";
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
				sql = " update room1 set roomname = '?????????' where name = ? ";
				pstmt19 = con.prepareStatement(sql);
				pstmt19.setString(1, banname);
				pstmt19.executeUpdate();
				System.out.println(name+ "?????? " +banname + "??? ??????");	
				result =  "??????";
			}
			else
			{
				result =   "?????? ????????? ????????????";
			}
		}catch(Exception e)
		{
			System.out.println("????????? db ??????" + e);
		}
		try
		{
			con.close();
		}catch(Exception e)
		{}
		return result;
		
		
	}
	
	public String change(String name,String newname)
	{
		String result = "1111";
		connectDatabase();
		sql = "select * from rlist where super = ? ";
		try 
		{
			pstmt33 = con.prepareStatement(sql);
			pstmt33.setString(1, name);
			rs = pstmt33.executeQuery();
			if(rs.next())
			{
				sql = " update rlist set super= ? where super = ? ";
				pstmt34 = con.prepareStatement(sql);
				pstmt34.setString(1, newname);
				pstmt34.setString(2, name);
				pstmt34.executeUpdate();
				
				sql = " update room1 set super = '??????' where name = ?";
				pstmt34 = con.prepareStatement(sql);
				pstmt34.setString(1, newname);
				pstmt34.executeUpdate();
				
				sql = "update room1 set super = '' where name = ?";
				pstmt35 = con.prepareStatement(sql);
				pstmt35.setString(1, name);
				pstmt35.executeUpdate();
				
				
				result =  "????????????";
			}
			else
			{
				result =   "?????? ????????? ????????????.";
			}
		}catch(Exception e)
		{
			System.out.println("???????????? ??????" + e);
		}
		try
		{
			con.close();
		}catch(Exception e)
		{}
		return result;
		
		
	}

	
	public String banword(String name,String banword)

	{
		String result = "";
		connectDatabase();
		sql = "select * from chatinfo where npc = ?";
		try 
		{
			pstmt26 = con.prepareStatement(sql);
			pstmt26.setString(1, name);
			rs = pstmt26.executeQuery();
			if(rs.next())
			{
				sql = " insert into wordlist values (?)";
				pstmt27 = con.prepareStatement(sql);
				pstmt27.setString(1, banword);
				pstmt27.executeUpdate();
				
				result =  "???????????? ?????????????????????.";
			}
			else
			{
				result =   "????????? ???????????????. ";
			}
		}catch(SQLIntegrityConstraintViolationException e)
		{
			result = "?????? ????????? ??????????????????. ";

		}
		catch(Exception e)
		{
			System.out.println("????????? db ??????" + e);
		}
		try
		{
			con.close();
		}catch(Exception e)
		{}
		return result;
	}
	
	public String removeword(String name,String removeword)
	{
		String result = "";
		connectDatabase();
		sql = "select * from chatinfo where npc = ?";
		try 
		{
			pstmt26 = con.prepareStatement(sql);
			pstmt26.setString(1, name);
			rs = pstmt26.executeQuery();
			if(rs.next())
			{
				sql = " delete wordlist where ????????? = ?";
				pstmt28 = con.prepareStatement(sql);
				pstmt28.setString(1, removeword);
				pstmt28.executeUpdate();
				
				result =  "???????????? ?????????????????????.";
			}
			else
			{
				result =   "????????? ???????????????. ";
			}
		}
		catch(Exception e)
		{
			System.out.println("????????? db ??????" + e);
		}
		try
		{
			con.close();
		}catch(Exception e)
		{}
		return result;
	}
		
	
	public ArrayList<String>  wordlist() 
	{
		ArrayList<String> arr = new ArrayList<>();
		connectDatabase();
		sql = "select * from wordlist";
		try {
			pstmt29 = con.prepareStatement(sql);
			rs = pstmt29.executeQuery();
			
			while(rs.next())
			{
				arr.add(rs.getString(1));
			}
				
		}catch(Exception e)
		{
			System.out.println("????????? ????????? ??????" + e);
			
		}
		return arr;
	}
	
	public String addnotice(String msg)
	{
		String result = "";
		connectDatabase();
		sql = "insert into notice values(?)";
		try 
		{
			pstmt30= con.prepareStatement(sql);
			pstmt30.setString(1, msg);
			pstmt30.executeUpdate();
			result = "????????????";
			
		}catch(SQLIntegrityConstraintViolationException e)
		{
			result = "?????? ????????? ???????????????. ";

		}
		catch(Exception e)
		{
			System.out.println("???????????? ??????" + e);
		}
		return result;
	}
	
	public ArrayList<String>  noticelist() 
	{
		ArrayList<String> arr = new ArrayList<>();
		connectDatabase();
		sql = "select * from notice";
		try {
			pstmt30 = con.prepareStatement(sql);
			rs = pstmt30.executeQuery();
			
			while(rs.next())
			{
				arr.add(rs.getString(1));
			}
				
		}catch(Exception e)
		{
			System.out.println("???????????? ????????? ??????" + e);
			
		}
		return arr;
	}
	
	public Map<String,String>  roomlist() 
	{
		Map<String,String> arr = new HashMap<>();
		connectDatabase();
		sql = "select * from rlist";
		try {
			pstmt36 = con.prepareStatement(sql);
			rs = pstmt36.executeQuery();
			
			while(rs.next())
			{
				arr.put(rs.getString(1),rs.getString(2));
			}
				
		}catch(Exception e)
		{
			System.out.println("???????????? ????????? ??????" + e);
			
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

