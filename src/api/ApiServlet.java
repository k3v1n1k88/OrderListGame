/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tungdq
 */
public abstract class ApiServlet extends BaseServlet {

    private static final long serialVersionUID = 874876962332979005L;

    protected abstract ApiOutput execute(HttpServletRequest req, HttpServletResponse resp);

    protected boolean checkValidParam(HttpServletRequest request, String[] params) {
        if (request == null || params == null) {
            return false;
        }

        if (params.length == 0) {
            return true;
        }

        List<String> listParam = new LinkedList<>();
        Enumeration<String> enumParams = request.getParameterNames();
        while (enumParams.hasMoreElements()) {
            String paramName = enumParams.nextElement();
            if (request.getParameter(paramName) != null && !request.getParameter(paramName).isEmpty()) {
                listParam.add(paramName);
            }
        }

        for (int i = 0; i < params.length; i++) {
            if (!listParam.contains(params[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processs(req, resp);
    }

    private void processs(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ApiOutput json = execute(req, resp);
        this.out(json.toJString(), req, resp, "json");
        
    }
}
