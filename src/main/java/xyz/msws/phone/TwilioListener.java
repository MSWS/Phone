package xyz.msws.phone;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TwilioServlet", urlPatterns = "/sms")
public class TwilioListener extends HttpServlet {
    private final IPhoneBot bot;

    public TwilioListener(IPhoneBot bot) {
        this.bot = bot;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String from = request.getParameter("From");
        String body = request.getParameter("Body");
        bot.relayMessage(from, body);
    }
}
