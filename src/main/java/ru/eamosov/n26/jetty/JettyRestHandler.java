package ru.eamosov.n26.jetty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import ru.eamosov.n26.api.EmptyResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JettyRestHandler
 */
public class JettyRestHandler extends AbstractHandler {
    private final Map<String, AbstractRestController> controllers = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public JettyRestHandler() {

    }

    public void registerController(AbstractRestController controller) {
        controllers.put(controller.getPath(), controller);
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        AbstractRestController controller = controllers.get(target);
        if (controller != null) {
            response.setCharacterEncoding("UTF-8");

            try {
                Object ret = controller.handle(baseRequest);
                if (ret instanceof EmptyResult) {
                    response.setStatus(((EmptyResult) ret).code);
                } else {
                    response.setStatus(200);
                    response.setContentType("application/json");
                    response.getOutputStream().write(gson.toJson(ret).getBytes("UTF-8"));
                }
            } catch (Exception e) {
                response.setStatus(500);
                response.setContentType("text/html");
                response.getOutputStream().println(e.getMessage());
            }
            baseRequest.setHandled(true);
        }
    }
}
