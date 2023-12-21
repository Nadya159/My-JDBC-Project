package by.javaguru.servlet;

import by.javaguru.service.OrderService;
import by.javaguru.utils.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private final OrderService orderService = OrderService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        req.setAttribute("orders", orderService.findAll());     //изменить на findByOrder
        req.getRequestDispatcher(JspHelper.getPath("orders")).forward(req,resp);
    }
}
