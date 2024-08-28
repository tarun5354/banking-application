

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import banking.DBConnection;


@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		Connection con=null;
		try {
			con=DBConnection.get();
			
			String user= request.getParameter("username");
			String password=request.getParameter("password");
			String query="INSERT INTO register VALUES(?,?)";
			try(PreparedStatement ps=con.prepareStatement(query)) {
				ps.setString(1, user);
				ps.setString(2, password);
				int count=ps.executeUpdate();
				if (count > 0) {
				   
				    RequestDispatcher rd = request.getRequestDispatcher("/Login.html");
				    rd.include(request, response);
				} else {
				    
				    RequestDispatcher rd = request.getRequestDispatcher("/register.html");
				    rd.include(request, response);
				}

				
			}
			
		}catch(SQLException e) {
			out.println("<h3 style='text-align:center>Database Error:"+e.getMessage()+"</h3>");
			out.println("<h3 style='text-align:center>Registration fialed in database server: </h3>");
			RequestDispatcher rd=request.getRequestDispatcher("/register.html");
			rd.include(request, response);
			
		}catch(Exception e) {
			out.println("<h3 style='text-align:center>Database Error:"+e.getMessage()+"</h3>");
			out.println("<h3 style='text-align:center>Registration fialed in database server: </h3>");
			RequestDispatcher rd=request.getRequestDispatcher("/register.html");
			rd.include(request, response);
			
		}finally {
			if(con!=null) {
				try {
					con.close();
				}
				catch(Exception e) {
					out.println("<h3> data base connection</h3>");
				}
			}
		}
		
		
	}







}
