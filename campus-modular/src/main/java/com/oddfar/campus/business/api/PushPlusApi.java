package com.oddfar.campus.business.api;

import cn.hutool.http.HttpUtil;
import com.oddfar.campus.business.entity.ILog;
import com.oddfar.campus.business.entity.IUser;
import com.oddfar.campus.common.utils.StringUtils;
import com.oddfar.campus.framework.manager.AsyncManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * @author zhiyuan
 */
public class PushPlusApi {


    public static void sendNotice(IUser iUser, ILog operLog) {
        String token = iUser.getPushPlusToken();
        if (StringUtils.isEmpty(token)) {
            //不填默认为我的机器人 改成自己的 把return注释掉可以实现默认通知至企微
//            token = "";
            return;
        }
        String title, content;
        if (operLog.getStatus() == 0) {
            //预约成功
            title = iUser.getRemark() + "-i茅台执行成功";
            content = iUser.getMobile() + System.lineSeparator() + operLog.getLogContent();
            AsyncManager.me().execute(sendNotice(token, title, content, "txt"));

        } else {
            //预约失败
            title = iUser.getRemark() + "-i茅台执行失败";
            content = iUser.getMobile() + System.lineSeparator() + operLog.getLogContent();
            AsyncManager.me().execute(sendNotice(token, title, content, "txt"));
        }


    }

    /**
     * push推送
     *
     * @param token    token
     * @param title    消息标题
     * @param content  具体消息内容
     * @param template 发送消息模板
     */
    public static TimerTask sendNotice(String token, String title, String content, String template) {
        return new TimerTask() {
            @Override
            public void run() {
                if (token.contains("-")) {
                    // 包含- 企微机器人
                    try {
                        sendMessage(token, title + "：" + content);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // 不包含 推送微信
                    String url = "http://www.pushplus.plus/send";
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", token);
                    map.put("title", title);
                    map.put("content", content);
                    if (StringUtils.isEmpty(template)) {
                        map.put("template", "html");
                    }
                    HttpUtil.post(url, map);
                }


            }
        };
    }
    // 企微机器人推送
    public static void sendMessage(String token, String message)  throws IOException  {

        URL url = new URL("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key="+token);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonPayload = "{\"msgtype\":\"text\",\"text\":{\"content\":\"" + message + "\"}}";

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            outputStream.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Message sending failed with response code: " + responseCode);
        }

    }



}
