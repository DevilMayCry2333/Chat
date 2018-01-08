import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UpdateDT extends javax.servlet.http.HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String data = request.getParameter("Chat");
//        String data2 = request.getParameter("Chat2");
        ConnectionTest ct = new ConnectionTest();
        JSONObject jo = new JSONObject();
        if(data.contains(";")) {
            String tmp[] = data.split(";");
            String s = "update Chat set answer = '" + tmp[1] + "' where question = '" + tmp[0] + "'";
            try {
                ct.execSQL(s);
                jo.put("answer", "Q :" + tmp[0] + "Successfully update");
            } catch (Exception e) {
                jo.put("answer","UPDATE FAIL");
                e.printStackTrace();
            }

            response.getWriter().write(jo.getString("answer"));
        }else if(data.contains("]")){
            String tmp = data.replace("]","");
            String d = "delete from Chat where question = '"+tmp+"'";
            try {
                ct.execSQL(d);
                jo.put("answer","Successfully delete");

            } catch (Exception e) {
                jo.put("answer","DELETE FAIL");
                e.printStackTrace();
            }
            response.getWriter().write(jo.getString("answer"));

        }
    }
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
        doPost(request,response);
    }
}
