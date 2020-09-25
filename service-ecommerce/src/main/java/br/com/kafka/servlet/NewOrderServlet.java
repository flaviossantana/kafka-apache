package br.com.kafka.servlet;

import br.com.kafka.client.ProducerClient;
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
import static br.com.kafka.constants.TopicConfig.STORE_SEND_EMAIL;


public class NewOrderServlet extends HttpServlet {

    private final ProducerClient<Order> orderProducer = new ProducerClient<>();
    private final ProducerClient<String> emailProducer = new ProducerClient<>();

    @Override
    public void destroy() {
        super.destroy();
        orderProducer.close();
        emailProducer.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {

            String email = req.getParameter("email");
            BigDecimal amount = new BigDecimal(req.getParameter("amount"));
            Order order = new Order(UUID.randomUUID().toString(), email, amount);

            String msg = "Thanks " + email + ", for your purchase!";

            orderProducer.send(
                    new CorrelationId(NewOrderServlet.class.getSimpleName()),
                    STORE_NEW_ORDER, email, order);

            emailProducer.send(
                    new CorrelationId(NewOrderServlet.class.getSimpleName()),
                    STORE_SEND_EMAIL,
                    email,
                    msg);

            resp.getWriter().print(msg);
            resp.setStatus(HttpStatus.OK_200);

            System.out.println(msg);

        } catch (Exception e) {
            throw new ServletException(
                    "Email: " + req.getParameter("email") +
                            "/ Amount: " + req.getParameter("amount"), e);
        }


    }
}
