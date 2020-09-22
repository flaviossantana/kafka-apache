package br.com.kafka.service;

import br.com.kafka.servlet.NewOrderServlet;
import br.com.kafka.servlet.SendReportServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import static br.com.kafka.constants.HttpConfig.*;

public class EcommerceService {

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath(PATH_CONTEXT);
        context.addServlet(new ServletHolder(new NewOrderServlet()), PATH_NEW_ORDER);
        context.addServlet(new ServletHolder(new SendReportServlet()), PATH_SEND_REPORT);

        server.setHandler(context);
        server.start();
        server.join();
    }
}
