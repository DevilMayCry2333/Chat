import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class Test extends javax.servlet.http.HttpServlet {


    private boolean updateFlag = false;
    private String[] tmpb = null;

    /**
     * @author joker
     * @serialData 2018-01-21
     * @param request
     * @param response
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* 设置编码防止乱码 */
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        //缓存标志
       boolean studyFlag = false;
       //自主学习标志
        boolean myStudy = false;
        //是否要进行更新的标志
        int update = 0;
    //    String []tmpUpdates = new String[2];
        //自定义实例化类
        ConnectionTest ct = new ConnectionTest();
        //要返回结果的JSON对象
        JSONObject jo =new JSONObject();
        //用户发送来的信息
        String text = request.getParameter("ChatK");
        System.out.println(text);
        //图灵机器人的API KEY
        String APIKEY = "*****";

        //如果有缓存操作
      if (text.contains("##")) {
          //删除"##"字符串来进行正常的查询操作
            text = text.replace("##", "");
            //设置缓存标志位
            studyFlag = true;
        }else if (text.contains(",")) {
          //如果有自主学习操作
          tmpb = text.split(",");
          text = tmpb[0];
          //设置自主学习标志位
          myStudy = true;
        }

        //如果没有自主学习
        if(myStudy == false) {
            try {
                //向数据库中查找答案
                jo = ct.execSQL("select * from Chat where question like '" + text + "'");
            } catch (Exception e) {
                System.out.println(update);
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
                    e.printStackTrace();
                }
            }
            //进行IP审计操作
        IPGet ip = new IPGet();
        try {
            ip.doPost(request,response,text);
        } catch (Exception e) {
            e.printStackTrace();
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