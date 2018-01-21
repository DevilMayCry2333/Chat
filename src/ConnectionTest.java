
import org.json.JSONObject;

import java.sql.*;
import java.util.regex.Pattern;

public class ConnectionTest{
    /**
     * @author joker
     * @serialData 2018-01-21
     * @param s
     * @return
     * @throws Exception
     */
    public JSONObject execSQL(String s) throws Exception {
        //注册MySQL JDBC驱动
        Class.forName("com.mysql.jdbc.Driver");
        //创建对数据库的SSL连接
        Connection cn = DriverManager.getConnection("jdbc:mysql://jokerl.com:3306/Chat_Test?characterEncoding=utf8&useSSL=true","test","YKY12345");
        //创建实例
        Statement st = cn.createStatement();
        //正则表达式匹配含有查询操作的。
        final String c = ".*select.*";
        //返回结果为JSON形式的
        JSONObject jo = new JSONObject();
        //结果集
        ResultSet rs = null;
        //返回受影响的行数
        int row= 0;
        //是否匹配select
        boolean isMath = false;
        //进行正则表达式匹配
        isMath = Pattern.matches(c, s);

        System.out.println("isMath="+isMath);
        if(!isMath){
            //是insert,update,delete操作
            try {
                row = st.executeUpdate(s);
            }catch (Exception e){
                System.out.println(s);
                e.printStackTrace();
            }
        }else {
            //进行数据库内的查询(精确匹配)
            st.executeQuery(s);
            System.out.println(s);
            rs =st.getResultSet();
            while (rs.next()){
                jo.put("id",rs.getString("id"));
                jo.put("question",rs.getString("question"));
                jo.put("answer",rs.getString("answer"));
            }
            System.out.println(jo.toString());
            rs.close();
        }
        //关闭实例和数据库连接
        st.close();
        cn.close();
       return jo;

    }
}
