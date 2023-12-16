package by.javaguru.servlet;

import by.javaguru.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            {
                writer.write("<h3>Список продуктов:</h3>");
                writer.write("<ul>");
                productService.findAll().stream().forEach(productDto ->
                        writer.write("""
                                <li>
                                <a href='/products?productId=%d'>%s</a>
                                </li>
                                """.formatted(productDto.id(), productDto.desription())));
                writer.write("<ul>");
            }
        }

    }
}
