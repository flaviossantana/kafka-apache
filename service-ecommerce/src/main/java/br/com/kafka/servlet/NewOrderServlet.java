package br.com.kafka.servlet;

import br.com.kafka.OrdersDatabase;
import br.com.kafka.client.ProducerClient;
import br.com.kafka.core.StoreLogger;
import br.com.kafka.dto.CorrelationId;
import br.com.kafka.dto.Order;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.UUID;

import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;


public class NewOrderServlet extends HttpServlet {

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try (ProducerClient<Order> orderProducer = new ProducerClient<>()) {

            String email = req.getParameter("email");
            BigDecimal amount = new BigDecimal(req.getParameter("amount"));
            String uuid = req.getParameter("uuid");

            Order order = new Order(uuid, email, amount);

            try(OrdersDatabase ordersDatabase = new OrdersDatabase()){
                String msg = "Sorry " + email + ", old order. Try again!";
                int httpStatus = HttpStatus.NO_CONTENT_204;

                if(ordersDatabase.save(order)){
                    orderProducer.send(
                            new CorrelationId(NewOrderServlet.class.getSimpleName()),
                            STORE_NEW_ORDER,
                            email,
                            order);

                    httpStatus = HttpStatus.OK_200;
                    msg = "Thanks " + email + ", for your purchase!";
                }

                resp.getWriter().print(msg);
                resp.setStatus(httpStatus);

                StoreLogger.info(msg);
            }



        } catch (Exception e) {
            throw new ServletException(
                    "Email: " + req.getParameter("email") +
                            "/ Amount: " + req.getParameter("amount"), e);
        }
    }
}