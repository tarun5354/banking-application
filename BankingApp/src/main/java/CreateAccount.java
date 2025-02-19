import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import banking.DBConnection;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            String user = (String) session.getAttribute("name");
            pw.print("<h1 align='center'>Welcome, " + user + " Continue with your transactions</h1>");
            
            Connection con = null;
             
            
            try {
                con = DBConnection.get();
                int num = Integer.parseInt(request.getParameter("accountNumber"));
                String name = request.getParameter("name");
                int balance = Integer.parseInt(request.getParameter("balance"));

                String query = "INSERT INTO createaccount VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, num);
                ps.setString(2, name);
                ps.setInt(3, balance);
                
                int count = ps.executeUpdate();
                if(count > 0) {
                	
                    pw.print("<h3 align='center'>Account Created Successfully</h3>");
                    RequestDispatcher rd = request.getRequestDispatcher("/user.html");
                    rd.include(request, response);
                } else {
                    
                    RequestDispatcher rd = request.getRequestDispatcher("/user.html");
                    rd.include(request, response);
                }
                } catch(Exception e) {
                    pw.print("<h3 align='center'>Record already existed - Try Again</h3>");
                    request.getRequestDispatcher("/user.html").include(request, response);
                }


        }
          
    }
}

