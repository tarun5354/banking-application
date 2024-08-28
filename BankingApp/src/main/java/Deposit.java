import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import banking.DBConnection;

@WebServlet("/Deposit")
public class Deposit extends HttpServlet {
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
				// Initialize the connection
				con = DBConnection.get();
				int num = Integer.parseInt(request.getParameter("accountNumberDeposit").trim());
				int amt = Integer.parseInt(request.getParameter("amountDeposit").trim());

				// Query to select account
				String query = "select * from createaccount where accoutnumber=?";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, num);

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					// Update account balance
					query = "update createaccount set balance = balance + ? where accoutnumber = ?";
					ps = con.prepareStatement(query);
					ps.setInt(1, amt);
					ps.setInt(2, num);
					ps.executeUpdate();
					pw.print("<h3 align='center'>Deposit Successful</h3>");
					RequestDispatcher rd = request.getRequestDispatcher("/user.html");
					rd.include(request, response);
				} else {
					// If account number is invalid
					pw.print("<h3 align='center'>Invalid Account Number Given - Try again</h3>");
					RequestDispatcher rd = request.getRequestDispatcher("/user.html");
					rd.include(request, response);
				}
			} catch (Exception e) {
				// Handle any exceptions
				pw.print("<h3 align='center'>An error occurred. Please try again.</h3>");
				request.getRequestDispatcher("/user.html").include(request, response);
			} finally {
				// Close the connection
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			pw.print("<h3 align='center'>Session expired. Please log in again.</h3>");
			RequestDispatcher rd = request.getRequestDispatcher("/Login.html");
			rd.include(request, response);
		}
	}
}
