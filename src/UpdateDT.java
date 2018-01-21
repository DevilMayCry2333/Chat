import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UpdateDT extends javax.servlet.http.HttpServlet{
    /**
     * @author joker
     * @serialData 2018-01-21
     * @param request
     * @param response
     * @throws IOException
     * 本类是负责更新和删除操作的处理
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* 设置编码防止乱码 */
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //用户发来的信息
        String data = request.getParameter("Chat");
        //自封装的数据库连接类
        ConnectionTest ct = new ConnectionTest();
        //返回的JSON数据
        JSONObject jo = new JSONObject();
        //如果有更新操作，进行更新操作
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
            //如果有删除操作,进行删除操作
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
