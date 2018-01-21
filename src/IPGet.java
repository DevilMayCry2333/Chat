import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class IPGet extends javax.servlet.http.HttpServlet {

    public void doGet(HttpServletRequest request,HttpServletResponse response ) throws IOException {
        /* 设置网页编码防止乱码 */
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //用户发送的聊天信息
        String tmp = request.getParameter("Chat");
        try {
            doPost(request,response,tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @author joker
     * @serialData 2018-01-21
     * @param request
     * @param response
     * @throws IOException
     */
    public void doPost(HttpServletRequest request,HttpServletResponse response,String tmp) throws Exception {
        /* 设置网页编码防止乱码 */
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //用户的真实ip(防代理)
        String realip = request.getHeader("x-forwarded-for");
        //用户的ip
        String ip = request.getRemoteAddr();
        //自封装的数据库连接类
        ConnectionTest ct = new ConnectionTest();
        //获取当前时间
        Date dat = new Date();
        //格式化当前时间
        SimpleDateFormat ft =
                new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss E a ");
        String ddat = ft.format(dat);
        System.out.println(tmp + realip+ ip + ddat);
        //IP审计操作，插入数据内
        String up = "insert into IPGet values('"+ddat+"','"+ip+"','"+tmp+"'"+");";
        ct.execSQL(up);
//        response.getWriter().write(tmp + realip+ip + ddat);

    }
}
