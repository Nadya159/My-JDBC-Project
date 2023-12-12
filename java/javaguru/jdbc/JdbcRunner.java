package javaguru.jdbc;

import javaguru.jdbc.dao.*;
import javaguru.jdbc.dao.CustomerDao;
import javaguru.jdbc.entity.*;

public class JdbcRunner {
    public static void main(String[] args) {
        var customerDao = CustomerDao.getInstance();
        var productDao = ProductDao.getInstance();
        var orderDao = OrderDao.getInstance();
        var itemDao = ItemDao.getInstance();

        Customer customer = customerDao.findById(2).get();
        Product product = productDao.findById(3).orElseThrow();
        Order order = orderDao.findById(202312001).orElseThrow();
        Item item = itemDao.findById(order).orElseThrow();
        System.out.println("-->" + customer + "\n-->" + product + "\n-->" + order + "\n-->" + item);
    }
}
