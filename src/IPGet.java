import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class IPGet extends javax.servlet.http.HttpServlet {
    public void doGet(HttpServletRequest request,HttpServletResponse response ) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        doPost(request,response);

    }
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String realip = request.getHeader("x-forwarded-for");
        String ip = request.getRemoteAddr();
        String tmp = request.getParameter("Chat");
        response.getWriter().write(tmp +realip + ip);


    }
}
