import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DataProcess {
    /**
     * @author joker
     * @serialData 2018-01-21
     * @param ct
     * @param text
     * @param APIKEY
     * @param jo
     * @param tmpb
     * @param studyFlag
     * @param myStudy
     * @throws Exception
     */

    public void Process(ConnectionTest ct,String text,String APIKEY,JSONObject jo,String tmpb[],boolean studyFlag,boolean myStudy) throws Exception {
        //这是上传给云机器人的问题
        String question = text;
        //把问题用UTF-8格式编码，防止乱码
        String INFO = URLEncoder.encode(question, "utf-8");
        //用户上下文语境有关的变量,一次会话默认为同一用户
        String userid = "12345";
        //通过GET方法调用图灵API的接口。
        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO + "&userid=" + userid;
        //打开URL链接
        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        //使用StringBuffer 拼接读取Reader的内容
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        //返回JSON数据
        JSONObject j2 = new JSONObject(sb.toString());
        jo.put("answer", j2.get("text"));

        //是否缓存或者自主学习（都要进行插入操作)
        if (studyFlag || myStudy) {
            //如果是自主学习的话,更新json字符串，答题的答案
            if (myStudy) {
                jo.put("answer", tmpb[1]);
            }
            //进行插入操作，执行sql
            String c = "insert into Chat(question,answer) values('" + text + "','" + jo.get("answer") + "')";
            ct.execSQL(c);

        }

        reader.close();
        // 断开连接
        connection.disconnect();
    }
}
