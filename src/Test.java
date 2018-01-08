import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.regex.Pattern;

public class Test extends javax.servlet.http.HttpServlet {


    private boolean updateFlag = false;
    private String[] tmpb = null;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
       boolean studyFlag = false;
        boolean myStudy = false;
        int update = 0;
        String []tmpUpdates = new String[2];
        ConnectionTest ct = new ConnectionTest();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        JSONObject jo =new JSONObject();
        String text = request.getParameter("ChatK");
        response.getWriter().write(text);
//            tmpUpdate[0] = tmpUpdate[0].replace("&","");
//            tmpUpdate[1] = tmpUpdate[1].replace("&","");
        String APIKEY = "XXXX";
        int row;
      if (text.contains("##")) {
            text = text.replace("##", "");
            studyFlag = true;
        }else if (text.contains(",")) {
          tmpb = text.split(",");
          text = tmpb[0];
          myStudy = true;
        }


            //存放答案的
//        }else if(text.contains(";")){
//          String tmp2[];
//          tmp2 = text.split(";");
//          update = 1;
//          request.setAttribute("Chat1",tmp2[0]);
//          request.setAttribute("Chat2",tmp2[1]);
//          RequestDispatcher rqd = request.getRequestDispatcher("UpdateDT");
//      }
        if(myStudy == false) {

            try {
                jo = ct.execSQL("select * from Chat where question like '" + text + "'");
            } catch (Exception e) {
                System.out.println(update);
                System.out.println(tmpUpdates[0]);
                System.out.println(tmpUpdates[1]);
                e.printStackTrace();
            }

        }

            /* 如果我的数据库内没有答案，则调用图灵api接口 */
            if (jo.toString().equals("{}")) {
                DataProcess dp = new DataProcess();
                try {
                    dp.Process(ct, text, APIKEY, jo, tmpb, studyFlag, myStudy);
                } catch (Exception e) {
                    System.out.println(update);
                    System.out.println(tmpUpdates[0]);
                    System.out.println(tmpUpdates[1]);
                    e.printStackTrace();
                }
            }

                /*访问我的数据库寻找答案 */
                if (!myStudy) {
                    response.getWriter().write(jo.getString("answer"));
                } else
                    response.getWriter().write("成功学习!" + jo.getString("answer"));

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}