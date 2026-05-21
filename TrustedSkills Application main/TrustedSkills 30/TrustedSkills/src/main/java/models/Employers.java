package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
 
import beans.GetConnection;

public class Employers {
	private String userid,otp,otp1  ;
	private String name;
	private String pswd,addr;
	private String usertype;
	private String userstatus;
	private String emailid;
	private String mobileno;  
	private String about,title,path;
	private MultipartFile file; 
	
	  
public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


public String getAddr() {
		return addr;
	}


	public void setAddr(String addr) {
		this.addr = addr;
	}


public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public String getOtp() {
		return otp;
	}


	public void setOtp(String otp) {
		this.otp = otp;
	}


	public String getOtp1() {
		return otp1;
	}


	public void setOtp1(String otp1) {
		this.otp1 = otp1;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPswd() {
		return pswd;
	}


	public void setPswd(String pswd) {
		this.pswd = pswd;
	}


	public String getUsertype() {
		return usertype;
	}


	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}


	public String getUserstatus() {
		return userstatus;
	}


	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}


	public String getEmailid() {
		return emailid;
	}


	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}


	public String getMobileno() {
		return mobileno;
	}


	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public MultipartFile getFile() {
		return file;
	}


	public void setFile(MultipartFile file) {
		this.file = file;
	}


public List<Employers> getEmployerReport(){
		
		GetConnection gc = new GetConnection();
		Connection con;
		List<Employers> lst = new ArrayList<Employers>();
		 
		ResultSet rs;
		
		try {
		
		con= gc.getConnection();
		Statement st=con.createStatement();
		rs=st.executeQuery("select * from employer where usertype='employer' ");
		Employers vs;
		 
		while(rs.next()) {
			
			vs= new Employers();
			vs.setUserid(rs.getString("userid"));
			vs.setTitle(rs.getString("title"));
			vs.setEmailid(rs.getString("emailid"));
			vs.setMobileno(rs.getString("mobileno"));
			vs.setName(rs.getString("contact_per_name"));
			vs.setAbout(rs.getString("about"));  
			vs.setAddr(rs.getString("addr")); 
			vs.setPath(rs.getString("logo"));  
			lst.add(vs);
		}
		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return(lst);
	}
	
	
	public String addNewEmp()
	{
		GetConnection gc = new GetConnection();
		int y=0;
		
		Connection con;
		String st="";
		try {
		con=gc.getConnection();
		PreparedStatement pst;
		
		pst=con.prepareStatement("insert into users values(?,?,?,?,?,?,?);");

		pst.setString(1,userid);
		pst.setString(2,title);
		pst.setString(3,pswd);
		pst.setString(4,usertype);
		pst.setString(5,"pending");
		pst.setString(6,"NA");
		pst.setString(7,emailid);

		int x=pst.executeUpdate();
		
		if(x>0) {
			
			pst=con.prepareStatement("insert into employer values(?,?,?,?,?,?,?,?,?,?  )");
			
			pst.setString(1,userid);
			pst.setString(2,title);
			pst.setString(3,usertype);
			pst.setString(4, about);
			pst.setString(5, addr);
			pst.setString(6,mobileno);
			pst.setString(7,emailid);
			pst.setString(8, name);
			pst.setString(9, path);
			pst.setString(10, "NA"); 
			
			 
			 
			y=pst.executeUpdate();
		}
		else
			st="failure";
		
		if(y>0)
			st="success";
		else
			st="failure";
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return st;
		
		
	}
	  

}
