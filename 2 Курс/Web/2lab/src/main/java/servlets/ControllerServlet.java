package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String r = request.getParameter("r");

        if (x != null && !x.isEmpty() && y != null && !y.isEmpty() && r != null && !r.isEmpty()) {
            request.getRequestDispatcher("/AreaCheckServlet").forward(request, response);
        } else {
            request.getRequestDispatcher("/form.jsp").forward(request, response);
        }
    }
}