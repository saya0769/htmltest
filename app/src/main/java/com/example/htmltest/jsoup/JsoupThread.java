package com.example.htmltest.jsoup;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupThread extends Application{
            // 判断是否有网络
            // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
          //  new Thread(networkTask).start();




    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String val2 = data.getString("test");
            String sTitle = data.getString("title");
            String sContents = data.getString("sContents");


            CharSequence charSequence;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                charSequence = Html.fromHtml(sContents,Html.FROM_HTML_MODE_LEGACY);
            } else {
                charSequence = Html.fromHtml(sContents);
            }


            Log.d("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
            Log.d("test!!!!!", "test请求结果为-->" + val2);

        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "请求结果");
            msg.setData(data);

            try {
                //从一个URL加载一个Document对象。
                Document doc = Jsoup.connect("http://www.biqiuge.com/book/11048/").get();
                //选择“美食天下”所在节点
                Elements elements = doc.select("div#info");
                //打印 <a>标签里面的title
                Log.i("mytag!!!!!!!",elements.select("h1").text());
                // 目录
                Elements muLu = doc.select("div.box_con");
             /*   for (int i = 0; i < 10; i++) {
                    Log.i("mulu!!!!!!!", muLu.select("dd").get(i).select("a").attr("href"));
                    Log.i("mulu!!!!!!!", muLu.select("dd").get(i).select("a").text());
                }*/
                // 内容
                Document doc2 = Jsoup.connect("http://www.biqiuge.com/book/11048/7850503.html").get();
                Elements sContent = doc2.select("div#wrapper");
                Log.d("sContent!!!!!!!", sContent.select(".content_read").select("#box_con").select("#content").text());

                String sContents = sContent.select(".content_read").select("#box_con").select("#content").toString();




                String title = doc.title();
                Log.d("title!!!!!!!",title);
                data.putString("sContents", sContents);
                data.putString("title", title);
                data.putString("test", elements.toString());
            }catch(Exception e) {
                Log.d("Exception!!!!!!!!!", e.toString());
            }


            handler.sendMessage(msg);
        }
    };


    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            //return (info!=null && info.isConnected());//
            if(info != null){
                return info.isConnected();
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


}


















