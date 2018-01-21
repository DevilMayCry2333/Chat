<?php
    /**
     * @author joker
     * @serialDate 2018-01-21
     * 本函数实现的功能是模拟浏览器请求
     * 一个https网址，并且将它的数据返回
     *
     */
    function curl_get_https($url){
        $curl = curl_init(); // 启动一个CURL会话
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_HEADER, 0);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false); // 跳过证书检查
        curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 2);  // 从证书中检查SSL加密算法是否存在
        $tmpInfo = curl_exec($curl);     //返回api的json对象
        //关闭URL请求
        curl_close($curl);
        return $tmpInfo;    //返回json对象
    }

    while(1){
        //存储列车出发的时间
        $time = $argv[1];
        //列车出发的地点
        $left = $argv[2];
        //列车到达的地点
        $to = $argv[3];
        //电子邮箱地址
        $addr = $argv[4];
        //调用函数模拟GET请求，并且返回数据包
        $get_data=curl_get_https("https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=".$time."&leftTicketDTO.from_station=".$left."&leftTicketDTO.to_station=".$to."&purpose_codes=ADULT");
//        var_dump($get_data);
        //通过json解析模块返回一个二维数组
        $dec_data = json_decode($get_data,true);
        //我们只关心返回的结果数据(即列车有无票)
        $res = $dec_data['data']['result'];
        //标记是否有票
        $flag = 0;
        //返回的结果
        $test = "START EXECUTING...";
        //如果有结果
        if($res) {
            //对每一行进行解析(对应每一辆列车)
            for ($j = 0; $j < count($res); $j++) {
                $resE = $res[$j];
                //根据'|'进行分割数据
                $out = explode('|', $resE);
                // var_dump($out);
                // if(strcmp($out[34],"O040") == 0)
                if ($out[23] == '有' || $out[30] == '有' || $out[31] == '有') {
                    $flag = 1;
                    $test = $test . "<==Successfully Discovery ==>";
                    for ($i = 0; $i < count($out); $i++) {
                        //这几行数据12306会进行变更，需要截获数据进行实际的分析。。
                        if ($i == 3 || $i == 4 || $i == 5 || $i == 8 || $i == 9 || $i == 10 || $i == 13) {
                            if ($i == 3) {
                                $a = "(Train No.)";
                            }
                            if ($i == 4) {
                                $a = "(From Site:)";
                            }
                            if ($i == 5) {
                                $a = "(To Site:)";
                            }
                            if ($i == 8) {
                                $a = "(Left time:)";
                            }
                            if ($i == 9) {
                                $a = "(arrive time:)";
                            }
                            if ($i == 10) {
                                $a = "(Time used:)";
                            }
                            if ($i == 13) {
                                $a = "(Date:)";
                            }
                            $test = $test . $a;
                            $test = $test . '***';
                            $test = $test . $out[$i];
                            $test = $test . ' ';
                        }

                    }

                }
            }
        }
        //结束标记
        $test = $test."fin";

        if($flag==1) {
            var_dump($test);
           //  system("java -classpath .:lib/javax.mail.jar:lib/activation.jar Main ".$test." ".$addr);
        }

        //让进程进入睡眠，防止触发12306的报警系统
        sleep(1);
    }
?>
