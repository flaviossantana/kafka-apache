package br.com.kafka.servlet;

import br.com.kafka.client.ProducerClient;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static br.com.kafka.constants.TopicConfig.*;


public class SendReportServlet extends HttpServlet {

    private final ProducerClient<String> batchProducer = new ProducerClient<>();


    @Override
    public void destroy() {
        super.destroy();
        batchProducer.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {

            batchProducer.send(STORE_SEND_MESSAGE_TO_ALL_USERS, STORE_REPORT_USER,STORE_REPORT_USER);
            resp.setStatus(HttpStatus.OK_200);

        } catch (Exception e) {
            throw new ServletException(
                    "Send Report ERROR: ", e);
        }


    }
}
