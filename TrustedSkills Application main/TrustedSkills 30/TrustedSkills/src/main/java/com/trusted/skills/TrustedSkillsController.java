package com.trusted.skills;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.random.RandomGenerator;
 
import jakarta.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import beans.BranchList;
import beans.GMailer;
import beans.RandomString;
import models.WorkingProfessionals;
import models.ApproveStudents;
import models.CheckUser;
 
import models.Documents;
import models.Employers;
import models.Freelancer;
import models.Institutes;
import models.JavaFuns;
 
import models.Pass;
import models.PasswordRecovery;
 
import models.RegisterBranch;
 
import models.Student;  
import models.ViewStudent;
import models.ViewStudentList; 

@Controller
public class TrustedSkillsController implements ErrorController{
	@RequestMapping("/college")
	public String index()
	{
		return "index.jsp";
	}
   
	 @RequestMapping("/error")
	    public String handleError() {
	        //do something like logging
			return "college";
	    }
	 
	    
 
	@RequestMapping("/home")
	public String home()
	{
		return "index.jsp";
	}
	@RequestMapping("/logout")
	public String logout(HttpSession ses)
	{
		ses.invalidate();
		return "index.jsp";
	}
	 
	@RequestMapping("/admin")
	public String admin()
	{
		return "approvestudentlist";
	}
	
	@RequestMapping("/SendOTP")
	public String SendOTP(HttpServletRequest request,HttpSession ses)
	{
		String m="OTP sending Failed!!";
		 String filepath=request.getServletContext().getRealPath("/gmail_api/");
			try {
		GMailer mail=new GMailer(filepath);
		RandomString rnd=new RandomString();
		String otp=rnd.getAlphaNumericString(4);
		System.out.println("otp="+otp);
		ses.setAttribute("otp", otp);
		
		try
		{
			String msg="Dear student, your one time password is "+otp;
			if(mail.sendMail(msg, request.getParameter("email").trim(), "OTP")) 
			{
				m="OTP sent on specified email id...";
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		return "otpmsg.jsp?m="+m;
	}
	@RequestMapping("/registerstudent")
	public String registerstudent()
	{
		return "Register.jsp";
	}
	@RequestMapping("/studentHome")
	public String studentHome()
	{
		return "Student.jsp";
	}
	@RequestMapping("/student")
	public String student()
	{
		return "Student.jsp";
	}
	
	@RequestMapping("/WorkingProfessional")
	public String WorkingProfessional()
	{
		return "WorkingProfessional.jsp";
	}
	@RequestMapping("/freelancer")
	public String freelancer()
	{
		return "freelancer.jsp";
	}
	@RequestMapping("/RegInstitute")
	public String RegInstitute()
	{
		return "RegInstitute.jsp";
	}
	@RequestMapping("/RegEmployer")
	public String RegEmployer()
	{
		return "RegEmployer.jsp";
	}
	 
	@RequestMapping("/RegSkills")
	public ModelAndView RegSkills(HttpServletRequest request,HttpSession ses)
	{
		String[] str=request.getParameterValues("selectedSkills");
		JavaFuns jf=new JavaFuns();
		String qr;
		if(jf.execute("delete from stud_skills where userid='"+ses.getAttribute("userid").toString().trim()+"'")) {}
		for(int i=0;i<str.length;i++)
		{
		qr="insert into stud_skills(userid,skill,utype) values('"+ses.getAttribute("userid").toString().trim()+"','"+str[i].trim()+"','"+ses.getAttribute("usertype").toString().trim()+"')";
		if(jf.execute(qr)) {}
		}
		ModelAndView mv=new ModelAndView();
		mv.setViewName("Success.jsp");
		mv.addObject("activity","skillsReg");
		return mv;
	}
	@RequestMapping("/staff")
	public String staff1()
	{
		return "Staff.jsp";
	}
	@RequestMapping("/RegGrad")
	public String RegGrad(HttpServletRequest request,HttpSession ses)
	{
		String xschool=request.getParameter("grad-school");
	double xpercent=Double.parseDouble(request.getParameter("grad-percentage").trim());
		String xyear=request.getParameter("grad-year");
		String branch=request.getParameter("branch");
		JavaFuns jf=new JavaFuns();
		int mxid=jf.FetchMax("acid", "academics");
		String qr="insert into academics values("+mxid+",'"+ses.getAttribute("userid").toString().trim()+"',";
		qr+="'"+xschool+"',"+xpercent+",'"+xyear+"','"+request.getParameter("grad-degree").toString().trim()+"','"+request.getParameter("branch").toString().trim()+"',0,0)";
		System.out.println(qr);
		if(jf.execute(qr)) {}
		return "viewProfile.jsp";
	}
	@RequestMapping("/RegXIIDetails")
	public String RegXIIDetails(HttpServletRequest request,HttpSession ses)
	{
		String xschool=request.getParameter("xii-school");
	double xpercent=Double.parseDouble(request.getParameter("xii-percentage").trim());
		String xyear=request.getParameter("xii-year");
		JavaFuns jf=new JavaFuns();
		int mxid=jf.FetchMax("acid", "academics");
		String qr="insert into academics values("+mxid+",'"+ses.getAttribute("userid").toString().trim()+"',";
		qr+="'"+xschool+"',"+xpercent+",'"+xyear+"','HSC','NA',0,0)";
		System.out.println(qr);
		if(jf.execute(qr)) {}
		return "viewProfile.jsp";
	}
	@RequestMapping("/RegXDetails")
	public String RegXDetails(HttpServletRequest request,HttpSession ses)
	{
		String xschool=request.getParameter("x-school");
	double xpercent=Double.parseDouble(request.getParameter("x-percentage").trim());
		String xyear=request.getParameter("x-year");
		JavaFuns jf=new JavaFuns();
		int mxid=jf.FetchMax("acid", "academics");
		String qr="insert into academics values("+mxid+",'"+ses.getAttribute("userid").toString().trim()+"',";
		qr+="'"+xschool+"',"+xpercent+",'"+xyear+"','SSC','NA',0,0)";
		System.out.println(qr);
		if(jf.execute(qr)) {}
		return "viewProfile.jsp";
	}
	 
	@SessionScope
	@RequestMapping("/RegDocs")
	public ModelAndView RegDocs(Documents eobj,HttpServletRequest request,HttpSession ses)
	{
		ModelAndView mv=new ModelAndView();
		eobj.getId();
		 int mx=eobj.getDocId();
		 String filepath="NA",fileName="NA";
		 try
		 {MultipartFile file=eobj.getFile();
		  filepath=request.getServletContext().getRealPath("/")+"/Uploads/";
		  
		 System.out.println("path="+filepath);
		 File f=new File(filepath);
		 f.mkdir();
		 f=new File(filepath);
		 f.mkdir();
		
			 
		  fileName=mx+"."+ file.getOriginalFilename().split("\\.")[1];
			 file.transferTo(new File(filepath+"/"+fileName));
		 } catch (Exception e) {
			 
		 }
			 try {
				 if(eobj.getCategory().equals("Link"))
				 {
					 fileName=request.getParameter("link").trim();
				 }
			 eobj.setDocpath(fileName);
			 eobj.setUserid(ses.getAttribute("userid").toString().trim());
			 if(eobj.registration() )
			 { 
				mv.setViewName("Success.jsp");
				mv.addObject("activity","DocumentReg");
			 }
			 else
			 { 
				 mv.setViewName("Failure.jsp?type=DocumentReg");
				 mv.addObject("activity","DocumentReg");
			 }
			 
		 
			 
		// mv.setViewName("Success.jsp?type=DocumentReg");
		 }
		 catch (Exception e) {
			// TODO: handle exception
			 System.out.println("err="+e.getMessage());
			 mv.setViewName("Failure.jsp?type=DocumentReg");
		}
		 return mv;
	}
	
	 
	 @RequestMapping("/uploadResume")
		public ModelAndView uploadResume(HttpSession ses, Student stu,HttpServletRequest request)
		{
			ModelAndView mv=new ModelAndView();
			    
			 try
			 {  MultipartFile file1=stu.getFile1();
			 String filepath1=request.getServletContext().getRealPath("/")+"/Resume/";
			    
			 File f1=new File(filepath1);
			 f1.mkdir();
			 try { 
				 String fileName1=stu.getUserid()+"."+ file1.getOriginalFilename().split("\\.")[1];
				 file1.transferTo(new File(filepath1+"/"+fileName1));
				 ses.setAttribute("resume", fileName1);
				 JavaFuns jf=new JavaFuns();
				 if(jf.execute("update studentpersonal set stud_resume='"+fileName1.trim()+"' where userid='"+ses.getAttribute("userid").toString().trim()+"'")) {}
						mv.setViewName("Success.jsp");
					 
			 }
			 catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
				 mv.setViewName("Failure.jsp");
			}
			 
			 }
			 catch (Exception e) {
					// TODO: handle exception
				 mv.setViewName("Failure.jsp");
				}
			 mv.addObject("activity","ResumeReg");
			  
			 return mv;
			
		}	 
	@RequestMapping("/registeruser")
	public ModelAndView registeruser(HttpSession ses, Student stu,HttpServletRequest request)
	{
		ModelAndView mv=new ModelAndView();
		// String otp=request.getParameter("otp").toString().trim();
		// String otp1=request.getParameter("otp1").toString().trim();
		  
		// if(otp.trim().equals(otp1.trim()))
		// { 
	//	System.out.println(stu.getSelectedCategories());
		 try
		 {MultipartFile file=stu.getFile();
		 String filepath=request.getServletContext().getRealPath("/")+"/Uploads/";
		 MultipartFile file1=stu.getFile1();
		 String filepath1=request.getServletContext().getRealPath("/")+"/Resume/";
		 System.out.println("path="+filepath);
		 File f=new File(filepath);
		 f.mkdir();
		 File f1=new File(filepath1);
		 f1.mkdir();
		 try {
			  
			 String fileName=stu.getUserid()+"."+ file.getOriginalFilename().split("\\.")[1];
			 file.transferTo(new File(filepath+"/"+fileName));
			 stu.setPath(fileName);
			 String fileName1=stu.getUserid()+"."+ file1.getOriginalFilename().split("\\.")[1];
			 file1.transferTo(new File(filepath1+"/"+fileName1));
			 stu.setPath(fileName);
			 stu.setResume(fileName1);
			 String st=stu.addNewStudent();
				if(st.equals("success"))
					mv.setViewName("Success.jsp");
				else
					mv.setViewName("Failure.jsp");
		 }
		 catch (Exception e) {
			// TODO: handle exception
			 mv.setViewName("Failure.jsp");
		}
		 
		 }
		 catch (Exception e) {
				// TODO: handle exception
			 mv.setViewName("Failure.jsp");
			}
		 mv.addObject("activity","StudReg");
		// }
		// else
		// { mv.setViewName("Failure.jsp");
		//	 mv.addObject("activity","EmailVerificationFailed");
		// } 
		 return mv;
		
	}
	@RequestMapping("/RegInst")
	public ModelAndView RegInst( Institutes stu,HttpServletRequest request)
	{
		ModelAndView mv=new ModelAndView();
		/// String otp=request.getParameter("otp").toString().trim();
		// String otp1=request.getParameter("otp1").toString().trim();
		  
		 //if(otp.trim().equals(otp1.trim()))
		// { 
		//System.out.println(stu.getSelectedCategories());
		 try
		 {MultipartFile file=stu.getFile();
		 String filepath=request.getServletContext().getRealPath("/")+"/Uploads/";
		 
		 
		 System.out.println("path="+filepath);
		 File f=new File(filepath);
		 f.mkdir();
		  
		 try {
			  
			 String fileName=stu.getUserid()+"."+ file.getOriginalFilename().split("\\.")[1];
			 file.transferTo(new File(filepath+"/"+fileName));
			 stu.setPath(fileName);
			 String st=stu.addNewInst();
				if(st.equals("success"))
					mv.setViewName("Success.jsp");
				else
					mv.setViewName("Failure.jsp");
		 }
		 catch (Exception e) {
			// TODO: handle exception
			 mv.setViewName("Failure.jsp");
		}
		 
		 }
		 catch (Exception e) {
				// TODO: handle exception
			 mv.setViewName("Failure.jsp");
			}
		 mv.addObject("activity","InstReg");
		// }
		// else
		// { mv.setViewName("Failure.jsp");
		//	 mv.addObject("activity","EmailVerificationFailed");
		// } 
		 return mv;
		
	}
	@RequestMapping("/RegEmp")
	public ModelAndView RegEmp( Employers stu,HttpServletRequest request)
	{
		ModelAndView mv=new ModelAndView();
		/// String otp=request.getParameter("otp").toString().trim();
		// String otp1=request.getParameter("otp1").toString().trim();
		  
		 //if(otp.trim().equals(otp1.trim()))
		// { 
		//System.out.println(stu.getSelectedCategories());
		 try
		 {MultipartFile file=stu.getFile();
		 String filepath=request.getServletContext().getRealPath("/")+"/Uploads/";
		 
		 
		 System.out.println("path="+filepath);
		 File f=new File(filepath);
		 f.mkdir();
		  
		 try {
			  
			 String fileName=stu.getUserid()+"."+ file.getOriginalFilename().split("\\.")[1];
			 file.transferTo(new File(filepath+"/"+fileName));
			 stu.setPath(fileName);
			 String st=stu.addNewEmp();
				if(st.equals("success"))
					mv.setViewName("Success.jsp");
				else
					mv.setViewName("Failure.jsp");
		 }
		 catch (Exception e) {
			// TODO: handle exception
			 mv.setViewName("Failure.jsp");
		}
		 
		 }
		 catch (Exception e) {
				// TODO: handle exception
			 mv.setViewName("Failure.jsp");
			}
		 mv.addObject("activity","RegEmp");
		// }
		// else
		// { mv.setViewName("Failure.jsp");
		//	 mv.addObject("activity","EmailVerificationFailed");
		// } 
		 return mv;
		
	}
	@RequestMapping("/registeruser1")
	public ModelAndView registeruser1( WorkingProfessionals stu,HttpServletRequest request)
	{
		ModelAndView mv=new ModelAndView();
		/// String otp=request.getParameter("otp").toString().trim();
		// String otp1=request.getParameter("otp1").toString().trim();
		  
		 //if(otp.trim().equals(otp1.trim()))
		// { 
		//System.out.println(stu.getSelectedCategories());
		 try
		 {MultipartFile file=stu.getFile();
		 String filepath=request.getServletContext().getRealPath("/")+"/Uploads/";
		 
		 
		 System.out.println("path="+filepath);
		 File f=new File(filepath);
		 f.mkdir();
		  
		 try {
			  
			 String fileName=stu.getUserid()+"."+ file.getOriginalFilename().split("\\.")[1];
			 file.transferTo(new File(filepath+"/"+fileName));
			 stu.setPath(fileName);
			 String st=stu.addNewStudent();
				if(st.equals("success"))
					mv.setViewName("Success.jsp");
				else
					mv.setViewName("Failure.jsp");
		 }
		 catch (Exception e) {
			// TODO: handle exception
			 mv.setViewName("Failure.jsp");
		}
		 
		 }
		 catch (Exception e) {
				// TODO: handle exception
			 mv.setViewName("Failure.jsp");
			}
		 mv.addObject("activity","StudReg");
		// }
		// else
		// { mv.setViewName("Failure.jsp");
		//	 mv.addObject("activity","EmailVerificationFailed");
		// } 
		 return mv;
		
	}
	@RequestMapping("/registeruser2")
	public ModelAndView registeruser2( Freelancer stu,HttpServletRequest request)
	{
		ModelAndView mv=new ModelAndView();
		/// String otp=request.getParameter("otp").toString().trim();
		// String otp1=request.getParameter("otp1").toString().trim();
		  
		 //if(otp.trim().equals(otp1.trim()))
		// { 
		//System.out.println(stu.getSelectedCategories());
		 try
		 {MultipartFile file=stu.getFile();
		 String filepath=request.getServletContext().getRealPath("/")+"/Uploads/";
		 
		 
		 System.out.println("path="+filepath);
		 File f=new File(filepath);
		 f.mkdir();
		  
		 try {
			  
			 String fileName=stu.getUserid()+"."+ file.getOriginalFilename().split("\\.")[1];
			 file.transferTo(new File(filepath+"/"+fileName));
			 stu.setPath(fileName);
			 String st=stu.addNewStudent();
				if(st.equals("success"))
					mv.setViewName("Success.jsp");
				else
					mv.setViewName("Failure.jsp");
		 }
		 catch (Exception e) {
			// TODO: handle exception
			 mv.setViewName("Failure.jsp");
		}
		 
		 }
		 catch (Exception e) {
				// TODO: handle exception
			 mv.setViewName("Failure.jsp");
			}
		 mv.addObject("activity","StudReg");
		// }
		// else
		// { mv.setViewName("Failure.jsp");
		//	 mv.addObject("activity","EmailVerificationFailed");
		// } 
		 return mv;
		
	}
	@RequestMapping("/updateuser")
	public ModelAndView updateuser(Student stu,HttpServletRequest request,HttpSession ses)
	{String fileName="NA";
		
	ModelAndView mv=new ModelAndView();
	try
		 {
			 stu.setUserid(ses.getAttribute("userid").toString().trim());
			 
		  
		 try {
			 MultipartFile file=stu.getFile();
			 String filepath=request.getServletContext().getRealPath("/")+"/Uploads/";
			 
			 
			 System.out.println("path="+filepath);
			 File f=new File(filepath);
			 f.mkdir();
			  fileName=stu.getUserid()+"."+ file.getOriginalFilename().split("\\.")[1];
			 file.transferTo(new File(filepath+"/"+fileName));
			 
		 }
		 catch (Exception e) {
			// TODO: handle exception
			// return "UserRegFailure.jsp";
		}
		 if(!fileName.equals("NA"))
		 {
			 ses.setAttribute("photo", fileName);
		 }
		 stu.setPath(fileName);
		 String st=stu.updateStudent(stu.getUserid());
		 if(st.equals("success"))
				mv.setViewName("Success.jsp");
			else
				mv.setViewName("Failure.jsp");
		 }
		 catch (Exception e) {
			 System.out.println("in update="+e.getMessage());
				// TODO: handle exception
			 mv.setViewName("Failure.jsp");
			}
		 mv.addObject("activity","StudProfile");
		 return mv;
	}
	 
	@RequestMapping("/ChangePass")
	public String ChangePass()
	{
		return "ChangePass.jsp";
	}
	@RequestMapping("/ChangePassService")
	public ModelAndView ChangePassService(Pass eobj,HttpSession ses)
	{
		ModelAndView mv=new ModelAndView();
		 try
		 {
			 
			 eobj.setUserid(ses.getAttribute("userid").toString().trim());
			 if(eobj.changePassword())
			 { 
				 mv.setViewName("Success.jsp");
			 }
			 else
			 { 
				 mv.setViewName("Failure.jsp");
			 }
		 }
		 catch (Exception e) {
			// TODO: handle exception
			 System.out.println("err="+e.getMessage());
			 mv.setViewName("Failure.jsp");
		}
		 mv.addObject("activity","changePass");
		 return mv;
		 
	}

	 
	@RequestMapping("/registerbranch")
	@SessionScope
	public String registerbranch() {
		
		return("RegisterBranch.jsp");
		
	}
	 
	@RequestMapping("/registernewbranch")
	public ModelAndView registernewbranch(RegisterBranch rb) {
		
		String st=rb.registerBranch();
		ModelAndView mv=new ModelAndView();
		mv.setViewName(st);
		mv.addObject("activity","branchReg");
		return mv;
	}
	 
	 
	@RequestMapping("/deactivatestudent")
	public ModelAndView deactivatestudent(String uid) {
		ModelAndView mv=new ModelAndView();
		ApproveStudents ap=new ApproveStudents();
		String sts=ap.updateStudentStatus1(uid);
		if(sts.equals("success"))
			 mv.setViewName("Success.jsp");
		else
			 mv.setViewName("Failure.jsp");
		mv.addObject("activity","studDeActivation");
		 return mv;
	}
	
	 
	@RequestMapping("/activatestudent")
	public ModelAndView activatestudent(HttpServletRequest request, ApproveStudents ap) {
		ModelAndView mv=new ModelAndView();
		 
		String filepath=request.getServletContext().getRealPath("/");
		ap.setPath(filepath);
		String sts=ap.updateStudentStatus();
		if(sts.equals("success"))
			 mv.setViewName("Success.jsp");
		else
			 mv.setViewName("Failure.jsp");
		mv.addObject("activity","studActivation");
		 return mv;
	}
	@RequestMapping("/viewstudent")
	@SessionScope
	public ModelAndView viewstudent() {
		
		List<Student> lst = new ArrayList<Student>();
		Student vs = new Student();
		lst=vs.getStudentReport();
		ModelAndView mv=new ModelAndView();
		mv.addObject("stal",lst);
		mv.setViewName("ViewStudentReport.jsp");
		return mv;
	}
	@RequestMapping("/viewAlumni")
	@SessionScope
	public ModelAndView viewalumni() {
		
		List<WorkingProfessionals> lst = new ArrayList<WorkingProfessionals>();
		WorkingProfessionals vs = new WorkingProfessionals();
		lst=vs.getAlumniReport();
		ModelAndView mv=new ModelAndView();
		mv.addObject("stal",lst);
		mv.setViewName("ViewAlumniReport.jsp");
		return mv;
	}
	@RequestMapping("/viewFreelancers")
	@SessionScope
	public ModelAndView viewFreelancers() {
		
		List<Freelancer> lst = new ArrayList<Freelancer>();
		Freelancer vs = new Freelancer();
		lst=vs.getAlumniReport();
		ModelAndView mv=new ModelAndView();
		mv.addObject("stal",lst);
		mv.setViewName("ViewFreelancerReport.jsp");
		return mv;
	}
	@RequestMapping("/viewInstitutes")
	@SessionScope
	public ModelAndView viewInstitutes() {
		
		List<Institutes> lst = new ArrayList<Institutes>();
		Institutes vs = new Institutes();
		lst=vs.getInstReport();
		ModelAndView mv=new ModelAndView();
		mv.addObject("stal",lst);
		mv.setViewName("viewInstitutes.jsp");
		return mv;
	}
	@RequestMapping("/viewEmployers")
	@SessionScope
	public ModelAndView viewEmployers() {
		
		List<Employers> lst = new ArrayList<Employers>();
		Employers vs = new Employers();
		lst=vs.getEmployerReport();
		ModelAndView mv=new ModelAndView();
		mv.addObject("stal",lst);
		mv.setViewName("viewEmployers.jsp");
		return mv;
	}
	@RequestMapping("/viewstudent1")
	@SessionScope
	public ModelAndView viewstudent1(String year) {
		
		List<ViewStudent> lst = new ArrayList<ViewStudent>();
		ViewStudent vs = new ViewStudent();
		lst=vs.getStudentReport1(year);
		ModelAndView mv=new ModelAndView();
		mv.addObject("std",lst);
		mv.setViewName("ViewStudentReport1.jsp");
		return mv;
	}
	@RequestMapping("/editProfile")
	@SessionScope
	public ModelAndView editProfile(HttpSession ses) {
		ModelAndView mv=new ModelAndView();
		try
		{
		List<Student> lst = new ArrayList<Student>();
		Student vs = new Student();
		lst=vs.getStudentReport(ses.getAttribute("userid").toString().trim());
		
		mv.addObject("std",lst);
		}
		catch (Exception e) {
		System.out.println("errr in edit="+e.getMessage());
		}
		mv.setViewName("EditProfileStud.jsp");
		return mv;
	}
	 
	@RequestMapping("/verifyEmailOTP")
	@SessionScope
	public ModelAndView verifyEmailOTP(HttpServletRequest request) {
		
		 String color="red"; 
		 String msg="Email Verification Failed!!";
		 String otp=request.getParameter("otp").toString().trim();
		 String otp1=request.getParameter("otp1").toString().trim();
		 String filepath=request.getServletContext().getRealPath("/");
		 try {
		 if(otp.trim().equals(otp1.trim()))
		 {
			 color="green";
			 msg="Email Verified Successfully..";
		 }
		 else {
		 color="red";
		 msg="Email Verification Failed!!";
		 }
		 }
		 catch (Exception e) {
			// TODO: handle exception
		}
		ModelAndView mv=new ModelAndView();
		mv.addObject("color",color);
		mv.addObject("msg",msg); 
		mv.setViewName("email_result1.jsp");
		return mv;
	
	}
	@RequestMapping("/VerifyEmail")
	@SessionScope
	public ModelAndView VerifyEmail(HttpServletRequest request) {
		
		 String color="red";
		 String otp="na";
		 String msg="Otp Sending Failed!!";
		 String email=request.getParameter("email").toString().trim();
		 String filepath=request.getServletContext().getRealPath("/");
		 try {
		 GMailer gmail=new GMailer(filepath);
		  otp=RandomString.getAlphaNumericString(4);
		 String msg1="Dear User, OTP for email verification is "+otp;
		 gmail.sendMail("Email Verification", msg1, email);
		 color="green";
		 msg="OTP sent on specified email id!!";
		 }
		 catch (Exception e) {
			// TODO: handle exception
		}
		ModelAndView mv=new ModelAndView();
		mv.addObject("color",color);
		mv.addObject("msg",msg);
		mv.addObject("otp",otp);
		mv.setViewName("email_result.jsp");
		return mv;
	
	}
	@RequestMapping("/approvestudentlist")
	@SessionScope
	public ModelAndView approvestudentlist() {
		
		List<Student> lst = new ArrayList<Student>();
		Student vls= new Student();
		
		lst=vls.getPendingRegistrations();
		System.out.println("lst="+lst.size());
		ModelAndView mv=new ModelAndView();
		mv.addObject("stal",lst);
		mv.setViewName("UpdateStudentStatus.jsp");
		return mv;
	
	}
	 
	@RequestMapping("/approvestudentlist_al")
	@SessionScope
	public ModelAndView approvestudentlist_al() {
		
		List<WorkingProfessionals> lst = new ArrayList<WorkingProfessionals>();
		WorkingProfessionals vls= new WorkingProfessionals();
		
		lst=vls.getPendingRegistrations();
		System.out.println("lst="+lst.size());
		ModelAndView mv=new ModelAndView();
		mv.addObject("stal",lst);
		mv.setViewName("UpdateStudentStatus1.jsp");
		return mv;
	
	}
	@RequestMapping("/approvestudentlist_free")
	@SessionScope
	public ModelAndView approvestudentlist_free() {
		
		List<Freelancer> lst = new ArrayList<Freelancer>();
		Freelancer vls= new Freelancer();
		
		lst=vls.getPendingRegistrations();
		System.out.println("lst="+lst.size());
		ModelAndView mv=new ModelAndView();
		mv.addObject("stal",lst);
		mv.setViewName("UpdateStudentStatus2.jsp");
		return mv;
	
	}
	@RequestMapping("/check")
	public String check(CheckUser cu,HttpServletRequest request,HttpSession ses) {
		
		String st=cu.checkUser(request);
		if(st.equals("student") || st.equals("WorkingProfessional")|| st.equals("freelancer"))
		{

			JavaFuns jf=new JavaFuns();
			Vector v=jf.getValue("select currentsts,job_post,job_details from userdetails where userid='"+cu.getUserid().trim()+"'", 3);
		 	ses.setAttribute("currentsts", v.elementAt(0).toString().trim());
			ses.setAttribute("job_post", v.elementAt(1).toString().trim());
			ses.setAttribute("job_details", v.elementAt(2).toString().trim());
		}
		System.out.println(""+st);
		return st;
	}
	
	 
	@RequestMapping("/registerstaff")
	@SessionScope
	public String registerstaff()
	{
		return "RegisterStaff.jsp";
	}
	@RequestMapping("/regCourse")
	@SessionScope
	public String regCourse()
	{
		return "RegCourse.jsp";
	}
	
	   
	   
	@RequestMapping("/viewDocsStud")
	public ModelAndView viewDocsStud(HttpSession ses,HttpServletRequest request)
	{
		
		List<Documents> lst = new ArrayList<Documents>();
		List<Documents> lst1 = new ArrayList<Documents>();
		Documents obj=new Documents();
		obj.setCourse(request.getParameter("course").toString().trim());
		 obj.getDocs1(request.getParameter("course").toString().trim(),"Tutorial");
		 
		 lst=obj.getLstdocs();
		 obj.getDocs11(request.getParameter("course").toString().trim(),"Tutorial");
		 lst1=obj.getLstdocs();
System.out.println("lstsize="+lst.size());
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("viewDocsStud.jsp");
		mv.addObject("lst", lst); 
		mv.addObject("lst1", lst1);
		return mv;
		 
	}
	@RequestMapping("/viewDocs")
	public ModelAndView viewDocs(HttpSession ses)
	{
		
		List<Documents> lst = new ArrayList<Documents>();
		Documents obj=new Documents();
		obj.setUserid(ses.getAttribute("userid").toString().trim());
		 obj.getDocs();
		 lst=obj.getLstdocs();
System.out.println("lstsize="+lst.size());
		ModelAndView mv = new ModelAndView();

		mv.setViewName("viewDocs.jsp");
		mv.addObject("lst", lst); 
		return mv;
		 
	}
	 
	 
	@RequestMapping("/forgetpassword")
	public String forgetpassword() {
		
		return("ForgetPassword.jsp");
	}
	
	@RequestMapping("/recoverpassword")
	public String recoverpassword(PasswordRecovery pr,HttpServletRequest request) {
		 String filepath=request.getServletContext().getRealPath("/gmail_api/");
			
		String sts=pr.getNewPassword(filepath);
		
		return(sts);
	}
	
	 
}
