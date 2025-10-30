package servlets;

import beans.ResultBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String xStr = request.getParameter("x");
        String yStr = request.getParameter("y");
        String rStr = request.getParameter("r");

        if (xStr == null || xStr.isEmpty() || yStr == null || yStr.isEmpty() || rStr == null || rStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Параметры x, y, r не могут быть пустыми");
            return;
        }

        try {
            double x = Double.parseDouble(xStr);
            double y = Double.parseDouble(yStr);
            double r = Double.parseDouble(rStr);

            if (r <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Радиус должен быть положительным");
                return;
            }

            boolean inside = checkArea(x, y, r);

            ResultBean resultBean = (ResultBean) request.getSession().getAttribute("resultBean");
            if (resultBean == null) {
                resultBean = new ResultBean();
                request.getSession().setAttribute("resultBean", resultBean);
            }
            resultBean.addResult(x, y, r, inside, LocalDateTime.now());

            request.setAttribute("latestX", x);
            request.setAttribute("latestY", y);
            request.setAttribute("latestR", r);
            request.setAttribute("latestInside", inside);

            request.getRequestDispatcher("/results.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Параметры x, y, r должны быть числами");
        }
    }

    private boolean checkArea(double x, double y, double r) {
        return (x <= 0 && y >= 0 && x<= r && y<= r) ||
                (x >= 0 && y <= 0 && 2*x -2 <= y) ||
                (x <= 0 && y <= 0 && x*x+y*y <= (r/2)*(r/2));
    }
}