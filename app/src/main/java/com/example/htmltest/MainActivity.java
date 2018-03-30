package com.example.htmltest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.example.htmltest.db.Books;
import com.example.htmltest.db.Directory;
import com.example.htmltest.db.FirstDb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {
   /* // 滑动菜单
    private DrawerLayout mDrawerLayout;
    // menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
*/

    TextView tvTile;
    TextView tv;
    String sContents;
    private List<FirstDb> firstDbLists;
    FirstDb firstDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs2.getBoolean("firsts", false) != true) {

      if (isNetworkAvailable(this)) {
                // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                new Thread(networkTask).start();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putBoolean("firsts", true);
                editor.apply();
            }
        }



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            Log.d("Main22222!!!!!!!", "6666666" );

            Intent intent = new Intent(this, BookActivity.class);
            startActivity(intent);
            finish();
        }

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 侧滑菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_round);
        }*/


        /*  tvTile = (TextView) findViewById(R.id.tv_title);
        tvTile.setTextSize(25);
        tvTile.setBackgroundColor(Color.parseColor("#E9FAFF"));

        tv = (TextView) findViewById(R.id.tv_content);
        tv.setTextSize(20);
        tv.setBackgroundColor(Color.parseColor("#E9FAFF"));
*/
//        // 判断是否有网络
//        if (isNetworkAvailable(this)) {
//            // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
//            new Thread(networkTask).start();
//        }


    }




    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           /* Bundle data = msg.getData();
            String val = data.getString("value");
            String val2 = data.getString("test");
            String sTitle = data.getString("title");
            String sContents = data.getString("sContents");

//            tvTile.setText("文章标题：。。。");
//            tvTile.setText(sTitle);

            CharSequence charSequence;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                charSequence = Html.fromHtml(sContents,Html.FROM_HTML_MODE_LEGACY);
            } else {
                charSequence = Html.fromHtml(sContents);
            }
            // tv.setText(charSequence);

            Log.d("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
            Log.d("test!!!!!", "test请求结果为-->" + val2);
*/
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
                Log.i("目录!!!!!!!",elements.select("h1").text());
                // 目录
                Elements muLu = doc.select("div.box_con");

                for (int i = 0; i < 1121; i++) {

                    Log.i("mUrl!!!!!!!", muLu.select("dd").get(i).select("a").attr("href"));
                    Log.i("mulu!!!!!!!", muLu.select("dd").get(i).select("a").text());
                    String mulu =muLu.select("dd").get(i).select("a").text();
                    String mUrl =muLu.select("dd").get(i).select("a").attr("href");

                    String url = "http://www.biqiuge.com/book/11048/" + mUrl;
                    // 内容
                    Document doc2 = Jsoup.connect(url).get();
                    Elements sContent = doc2.select("div#wrapper");
                    Log.d("内容!!!!!!!", sContent.select(".content_read").select("#box_con").select("#content").text());


                      sContents = sContent.select(".content_read").select("#box_con").select("#content").toString();


                    // 数据库
                    Directory directory = new Directory();
                    directory.setDirectoryName(mulu); // "目录"
                    directory.setDirectoryContent(sContents);
                    directory.setBooksId(1);
                    directory.save(); // 保存到数据库中。
                }





              /*  String title = doc.title();
                Log.d("title!!!!!!!",title);
                data.putString("sContents", sContents);
                data.putString("title", title);
                data.putString("test", elements.toString());*/
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





    /*Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String val2 = data.getString("test");
            String sTitle = data.getString("title");
            String sContents = data.getString("sContents");

            tvTile.setText("文章标题：。。。");
            tvTile.setText(sTitle);

            CharSequence charSequence;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                charSequence = Html.fromHtml(sContents,Html.FROM_HTML_MODE_LEGACY);
            } else {
                charSequence = Html.fromHtml(sContents);
            }
            tv.setText(charSequence);

            Log.d("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
            Log.d("test!!!!!", "test请求结果为-->" + val2);

        }
    };

    *//**
     * 网络操作相关的子线程
     *//*
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
             *//*   for (int i = 0; i < 10; i++) {
                    Log.i("mulu!!!!!!!", muLu.select("dd").get(i).select("a").attr("href"));
                    Log.i("mulu!!!!!!!", muLu.select("dd").get(i).select("a").text());
                }*//*
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
    }*/

}















