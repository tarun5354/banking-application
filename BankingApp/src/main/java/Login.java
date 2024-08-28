

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import banking.DBConnection;


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		Connection con=null;
		try {
			con=DBConnection.get();
			String user=request.getParameter("username");
			String password=request.getParameter("password");
			
			String query="select password from register where username=? ";
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, user);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				if(password.equals(rs.getString("password"))) {
					HttpSession session=request.getSession();
					session.setAttribute("name", user);
					out.println("<h1 style='text-aglin:center'>Welcome "+user+"</h1>");
					RequestDispatcher rd=request.getRequestDispatcher("/user.html");
					rd.include(request,response);
					
				}
				else {
					out.println("<h3 style='text-align:center'>Invalid username and password- try agin</h3>");
					RequestDispatcher rd=request.getRequestDispatcher("/Login.html");
					rd.include(request,response);
				}
			}
			
			
			
		}catch(Exception e) {
			out.println("<h3> the data base exception in Login html</h3>");
		}
		
	}



}
