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
import javax.servlet.http.HttpSession;

import banking.DBConnection;

@WebServlet("/UpdateName")
public class UpdateName extends HttpServlet {
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
                int accountNumber = Integer.parseInt(request.getParameter("accountNumberUpdateName").trim());
                String newName = request.getParameter("newName").trim();

                String query = "UPDATE createaccount SET name = ? WHERE accoutnumber = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, newName);
                ps.setInt(2, accountNumber);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    pw.print("<h3 align='center'>Name Updated Successfull  y</h3>");
                } else {
                    pw.print("<h3 align='center'>Invalid Account Number</h3>");
                }
                RequestDispatcher rd = request.getRequestDispatcher("/user.html");
                rd.include(request, response);

            } catch (SQLException e) {
                pw.print("<h3 align='center'>Error occurred during name update - Try Again</h3>");
                request.getRequestDispatcher("/user.html").include(request, response);
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
