import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class login extends javax.servlet.http.HttpServlet  {

    //本机的脚本地址
    private static final String L_File = "/Users/youkaiyu/Desktop/php/testGrab";
    //服务器的脚本地址
    private static final String U_File = "/home/ubuntu";
    public void doGet(HttpServletRequest request, HttpServletResponse response ) throws IOException {
        /* 设置网页编码防止乱码 */
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        doPost(request,response);
    }

    /**
     * @author joker
     * @serialData 2018-01-21
     * @param request
     * @param response
     * @throws IOException
     */
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        /* 设置网页编码防止乱码 */
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
//        response.getWriter().write(user + pass);
        try {
            //进行注册
            Class.forName("com.mysql.jdbc.Driver");
            //打开连接
            Connection cn = DriverManager.getConnection("jdbc:mysql://***.com/***?characterEncoding=utf8&useSSL=true", "***", "****");
            //创建两个实例化
            Statement st = cn.createStatement();
            Statement st2 = cn.createStatement();
            //向数据库查询摘要后的密码
            String xc = "select Dpass from User where Duser ='"+user+"'";
            //用户发来的密码用md5摘要
            String md_pass  = MD5Encrypt.MD5(pass);
            System.out.println(md_pass);
            //查询返回结果集
            ResultSet res = st.executeQuery(xc);
            if (res.next()){
                //获得服务器上该用户的密码的md5值
                String tmp = res.getString("Dpass");
                res.close();
                //认证成功
                if(tmp.compareTo(md_pass) ==0) {
                    //出发日期
                    String date = request.getParameter("date");
                    //出发地点
                    String from = request.getParameter("from");
                    //到达的地点
                    String to = request.getParameter("to");
                    //邮箱的地址
                    String addr = request.getParameter("addr");
                    //进行用户的审计(记录操作)
                    st.execute("INSERT INTO log VALUES ('"+user+"','"+from+"','"+to+"','"+addr+"','"+date+"')");
                    //查询出发城市的缩写
                    ResultSet tfrom = st.executeQuery("SELECT 缩写 FROM Sheet1 WHERE 站名='" + from + "'");
                    //查询到达城市的缩写
                    ResultSet tto = st2.executeQuery("SELECT 缩写 FROM Sheet1 WHERE 站名='" + to + "'");
                    //以下两个变量用来存储缩写
                    String dfrom = "";
                    String dto = "";

                    if (tfrom.next() && tto.next()) {
                        dfrom = tfrom.getString("缩写");
                        dto = tto.getString("缩写");
                    }
                    System.out.println(dfrom);
                    System.out.println(dto);
                    //关闭两个结果集
                    tfrom.close();
                    tto.close();
                    //用来控制脚本执行的时间
                    long t1 = System.currentTimeMillis();
                    Process ps = Runtime.getRuntime().exec("php "+U_File+"/test.php " + date + " " + dfrom + " " + dto + " " + addr + " ");
                    //用来读取脚本的输出
                    BufferedReader input = new BufferedReader(new InputStreamReader(ps.getInputStream()));
                    //暂存脚本的输出
                    String line = "";
                    //保存脚本完整的输出
                    List<String> processList = new ArrayList<String>();
                    //进行读取
                    while ((line = input.readLine()) != null) {
                        //获取当前时间
                        long t2 = System.currentTimeMillis();
                        //如果脚本已经运行了6个小时或者找到了有票的动车
                        if (t2 - t1 >= 21600000 || processList.toString().contains("fin")) {
                            break;
                        }
                        processList.add(line);
                        System.out.println(line);
                    }
                    //关闭输入并且终止进程
                    input.close();
                    ps.destroy();
                    //进行邮件的发送
                    Process pa = Runtime.getRuntime().exec("java -classpath "+U_File+":"+U_File+"/lib/javax.mail.jar"+":"+U_File+"/lib/activation.jar Main " + processList.toString() + " " + addr);

                    //返回输入的信息以及票务相关的信息
                    response.getWriter().write("Authentication Success" + date + from + to + addr + processList.toString());

                }else {
                    //授权认证失败
                        response.getWriter().write("Authentication Failed");
                    }
                }
                //关闭两个实例化和连接
                st.close();
            st2.close();
                cn.close();
        }catch (SQLException se){
            se.printStackTrace();
            System.out.println("SQL 异常");
        }catch (ClassNotFoundException cl){
            cl.printStackTrace();
            System.out.println("找不到类");
        }


    }
}
