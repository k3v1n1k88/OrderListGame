/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author tungdq
 */
public class BaseServlet extends HttpServlet {
    
    private static final long serialVersionUID = 5679792101247229759L;
    
    private static final Logger LOGGER = Logger.getLogger(BaseServlet.class);

    public BaseServlet() {
        super();
    }

    protected void out(String content, HttpServletRequest req, HttpServletResponse resp, String type) {
        try {
            resp.setCharacterEncoding("utf-8");
            if ("json".equalsIgnoreCase(type)) {
                resp.addHeader("Content-Type", "application/json; charset=utf-8");
            } else if ("plain".equalsIgnoreCase(type)) {
                resp.addHeader("Content-Type", "text/plain; charset=utf-8");
            } else {
                resp.addHeader("Content-Type", "text/html; charset=utf-8");
            }
            resp.addHeader("Access-Control-Allow-Origin", "*");

            try (PrintWriter os = resp.getWriter()) {
                os.write(content);
            }
        } catch (Exception ex) {
            LOGGER.error("BaseServlet.out", ex);
        }
    }
}
