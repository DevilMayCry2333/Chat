import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DataProcess {

    public void Process(ConnectionTest ct,String text,String APIKEY,JSONObject jo,String tmpb[],boolean studyFlag,boolean myStudy) throws Exception {
        String question = text;//这是上传给云机器人的问题
        //String INFO = URLEncoder.encode("北京今日天气", "utf-8");
        String INFO = URLEncoder.encode(question, "utf-8");
        String userid = "12345";
        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO + "&userid=" + userid;
        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        JSONObject j2 = new JSONObject(sb.toString());
        System.out.println(j2);
        jo.put("answer", j2.get("text"));
//                    jo.put("answer",sb.toString());
        if (studyFlag || myStudy) {
            if (myStudy) {
                jo.put("answer", tmpb[1]);
            }
            String c = "insert into Chat(question,answer) values('" + text + "','" + jo.get("answer") + "')";
            ct.execSQL(c);

        }

        reader.close();
        // 断开连接
        connection.disconnect();
    }
}
