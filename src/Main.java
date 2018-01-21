
public class Main {
    /**
     * @author joker
     * @serialData 2018-01-21
     * @param args
     */
    public static void main(String[] args) {
       //要发送的内容
        StringBuffer a = new StringBuffer();
        int i;
        for (i= 0 ; i < args.length -1 ; i++)
            a.append(args[i]);
        String b= "";
        //收件人地址
        b = args[i];
        MailUtil.sendEmil(b,a.toString());
    }
}
