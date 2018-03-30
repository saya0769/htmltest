package com.example.htmltest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
//import android.telecom.Call;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.htmltest.db.Books;
import com.example.htmltest.db.Directory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

// 碎片 choose_area.xml 碎片
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private Button backUpdate;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    // 省列表
    private List<Books> bookList;
    private List<Directory> directoryList;
    //private List<County> countyList;
    // 选中的省
    private Books selectedBooks;
    private Directory selectedDirectory;
    // 当前选中的级别
    private int currentLevel;
    private int postionString;

    // 是否初始化
    private boolean firstInit = true;

    String sContents;

    // 标题
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        Log.d("onCreateView!!!!!!!!", "111111111111111");

        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        backUpdate = (Button) view.findViewById(R.id.back_update);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean weatherString = prefs.getBoolean("firstinit", false);
        Log.d("firstInit!!!!!!!", "id=" + weatherString);
        // new Thread(networkTask).start();
        if (weatherString) {
            Log.d("firstInit!!!!!!!", "!!!!!!!!!!!!!!!!!!");
            new Thread(networkTask).start();
        }*/



        return view;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
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
                // 数据库
                Directory directory = new Directory();
                for (int i = 0; i < 10; i++) {

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


                    directory.setDirectoryName(mulu); // "目录"
                    directory.setDirectoryContent(sContents);
                    directory.setBooksId(1);
                    directory.save(); // 保存到数据库中。

                    Books books = new Books();
                    books.setFirstI(1);
                }






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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedBooks = bookList.get(position);
                    Log.d("ActivityCreated!!!!!!!", "22222222");
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    Log.d("onItemClick!!!!!!!", "LEVEL_CITY");
                    selectedDirectory = directoryList.get(position);
                    Log.d("ActivityCreated!!!!!!!", "3333333333");
                    Log.d("setOnItem!!!", "id=" + id + " p=" + position);

                    //String weatherId = directoryList.get(position).getDirectoryId();
                    //String directoryText = directoryList.get(position).getDirectoryContent();
                    String weatherId = directoryList.get(position).getDirectoryName();
                    String directoryText = directoryList.get(position).getDirectoryContent();
                    String testData = weatherId + "|" + directoryText;

                  /*  MyApplication application = new MyApplication();
                    application.setDirectoryString(weatherId);
                    application.setContentString(directoryText);
                    Log.d("application!!!!!!!",   application.getDirectoryString());
                    Log.d("application!!!!!!!",   application.getContentString());
*/

                    Log.d("weatherId!!!!!!!",   weatherId);
                    //Log.d("directoryText!!!!!!!",   directoryText);

                    if (getActivity() instanceof MainActivity) {
                        Log.d("ActivityCreated!!!!!!!", "444444444=" + weatherId);
                        Log.d("onsBooks!!!!!!!", "onsBooks444444444=" + weatherId);

                        Intent intent = new Intent(getActivity(), BookActivity.class);
                        intent.putExtra("weather_id", weatherId); // 首次进入 books 活动界面
                        intent.putExtra("directory_text", directoryText);
                        intent.putExtra("position", position);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof BookActivity) {
                        Log.d("ActivityCreated!!!!!!!", "55555555555");

                        // instanceof 判断是否在 WeatherActivity 活动中
                        BookActivity activity = (BookActivity) getActivity();
                        activity.drawerLayout.closeDrawers();;
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(testData);

                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_CITY) {
                    Log.d("ActivityCreated!!!!!!!", "44444444");
                    queryBooks();
                }
            }
        });
        backUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //new Thread(networkTask).start(); // 更新
                Log.d("backUpdate!!!!", "1111111111111");
            }
        });
        queryBooks();
    }

    private void queryCounties() {
    }

    // 查询选中的书本内所有的目录。
    private void queryCities() {
        titleText.setText(selectedBooks.getBookName());
        backButton.setVisibility(View.VISIBLE);
        directoryList = DataSupport.where("booksid = ?", String.valueOf(selectedBooks.getId())).find(Directory.class);
        //directoryList = DataSupport.findAll(Directory.class);

        if (directoryList.size() > 0) {
            Log.d("queryCities!!!!!!!", "1111111111");

            dataList.clear();
            for (Directory directory : directoryList) {
                dataList.add(directory.getDirectoryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            Log.d("queryCities!!!!!!!", "2222222222");

            int bookCode = selectedBooks.getBookCode();
            String address = "http://guolin.tech/api/china/" + bookCode;
            queryFromServer(address, "directory");
        }
    }

    private void queryBooks() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        bookList = DataSupport.findAll(Books.class);

        if (bookList.size() > 0) {
            Log.d("books!!!!!!!", "2222");

            dataList.clear();
            for (Books books : bookList) {
                dataList.add(books.getBookName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {

            Log.d("books!!!!!!!", "333333");
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "books");
        }
    }

    private void queryFromServer(String address, final String sbooks) {


        boolean result = false;
        if ("books".equals(sbooks)) {
            Log.d("okhttp!!!!!!!!", "!!!!province!!!!");
            Books books = new Books();
            books.setBookName("回到地球当神棍");
            books.setBookCode(1);
            books.save(); // 保存到数据库中。
            result = true;
        } else if ("directory".equals(sbooks)) {
            Log.d("Directory!!!!!!!!", "!!!!Directory!!!!");
            /*Directory directory = new Directory();
            directory.setDirectoryName("目录");
            directory.setDirectoryCode(2);
            directory.setDirectoryId(1);

            directory.setDirectoryContent("测试测试测试测试");
            directory.setBooksId(selectedBooks.getId());

            directory.save(); // 保存到数据库中。



            Directory directory2 = new Directory();
            directory2.setDirectoryName("第79章 枕边伊人（上架三更求订阅！）");
            directory2.setDirectoryContent("22222测试测试测试测试");
            directory.setDirectoryId(2);
            directory2.setBooksId(selectedBooks.getId());
            directory2.save();*/


            result = true;
        }



        if (result) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("runOnUiThread!!!!!!!!", "!!!!runOnUiThread!!!!");

                    if ("books".equals(sbooks)) {
                        Log.d("runOnUiThread!!!!!!!!", "!!!!queryBooks!!!!");

                        queryBooks();
                    } else if ("directory".equals(sbooks)) {
                        Log.d("runOnUiThread!!!!!!!!", "queryCities");
                        queryCities();
                    }
                }
            });
        }
    }










}


















