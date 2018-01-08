
import org.json.JSONObject;

import java.sql.*;
import java.util.regex.Pattern;

public class ConnectionTest{
    public JSONObject execSQL(String s) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection cn = DriverManager.getConnection("jdbc:mysql://XXXXX:3306/XXXX?characterEncoding=utf8&useSSL=true","XXX","XXXX");
        Statement st = cn.createStatement();
        final String c = ".*select.*";
        JSONObject jo = new JSONObject();

        ResultSet rs = null;
        int row= 0;
        boolean isMath = false;
        isMath = Pattern.matches(c, s);

        System.out.println("isMath="+isMath);
        if(!isMath){
            try {
                row = st.executeUpdate(s);
            }catch (Exception e){
                System.out.println(s);
                e.printStackTrace();
            }
        }else {
            st.executeQuery(s);
            rs =st.getResultSet();
            while (rs.next()){
                jo.put("id",rs.getString("id"));
                jo.put("question",rs.getString("question"));
                jo.put("answer",rs.getString("answer"));
            }
            rs.close();
        }
        st.close();
        cn.close();
       return jo;

    }
}
