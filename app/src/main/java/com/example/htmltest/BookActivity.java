package com.example.htmltest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.htmltest.db.Books;
import com.example.htmltest.db.Directory;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
                                                                                     // 触屏监听器和手势监控器
public class BookActivity extends AppCompatActivity implements View.OnClickListener, OnTouchListener, OnGestureListener {

    // 滑动菜单
    public DrawerLayout drawerLayout;
    // 请求数据，展示到界面上。
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private Button   next1;
    private Button   next2;
    private Button   updateString;
    private Button   gototop;

    // 省列表
    private List<Directory> directoryList;
    private Directory selectedDirectory;

    // 每日一图
    private ImageView bingPicImg;
    // 下拉刷新
    public SwipeRefreshLayout swipeRefresh;
    private Button navButton;

    private int positionString;

    String nowTitle;
    GestureDetector mGestureDetector;
    private int verticalMinDistance = 200;
    private int minVelocity = 20;
    protected static final float FLIP_DISTANCE = 50;
     //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
     float x1 = 0;
     float x2 = 0;
     float y1 = 0;
     float y2 = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_book);

        // 滑动菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);

        // 初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        next1 = (Button) findViewById(R.id.newxString);
        next2 = (Button) findViewById(R.id.newxString2);
        gototop = (Button) findViewById(R.id.gototop);
        //updateString = (Button) findViewById(R.id.updateString);
        next1.setOnClickListener(this);
        next2.setOnClickListener(this);
        gototop.setOnClickListener(this);
        //updateString.setOnClickListener(this);

        /*ViewPager mViewPager=(ViewPager) findViewById(R.id.viewPager);
        PagerAdapter mPagerAdapter=new MyPagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);*/

        // 手势Detector：识别
        mGestureDetector = new GestureDetector((OnGestureListener) this);
        ScrollView viewSnsLayout = (ScrollView)findViewById(R.id.weather_layout);
        viewSnsLayout.setOnTouchListener(this); //将主容器的监听交给本activity，本activity再交给mGestureDetector
        viewSnsLayout.setLongClickable(true); //必需设置这为true 否则也监听不到手势

        // 下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        // 下拉刷新

        // 滑动菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        // 下拉刷新
        final String weatherId;
        final String titleText;
        final String directoryText;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);

        String titleString  =  prefs.getString("weather_test", null);
        String directoryString  =  prefs.getString("directory_text", null);
        //getIntent().getStringExtra("directory_text");
        Log.d("BookActivity!!!!!!!", "00000000000");
        if (weatherString != null) {
            Log.d("BookActivity!!!!!!!", "2222333111111111111");
            Log.d("wea2222!!!!!!!", weatherString);
            // Log.d("dir2222!!!!!!!", directoryString);
            weatherId = weatherString;
            String temp = weatherString + "|" + directoryString;
            nowTitle = weatherString;

            // 有缓存时直接解析天气数据
            showWeatherInfo(temp);
        } else {
            Log.d("BookActivity22!!!!!", "2222222222");

            String a  = getIntent().getStringExtra("weather_id");
            String b = getIntent().getStringExtra("directory_text");
            String c = a + "|" + b;
            Log.d("BookActivity22!!!!!!!", a);
            Log.d("BookActivity22!!!!!!!", b);


            // 无缓存时去服务器查询天气

            //weatherLayout.setVisibility(View.INVISIBLE);

            requestWeather(c);

           // showWeatherInfo(weatherString);
        }




        // 下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //requestWeather(weatherId);
                swipeRefresh.setRefreshing(false); // 刷新结束，hide进度条

            }
        });

        // 滑动菜单
        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 打开滑动菜单
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    // 下一页
   // 按钮监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newxString:
                /*backPage(); // 上一页
                gotoTop(); // 回到顶部!!!!!!!!!!!!!
                break;*/
            case  R.id.newxString2:
                nextPage(); // 下一页
               // break;

            case  R.id.gototop:
                 gotoTop(); // 回到顶部!!!!!!!!!!!!!
                break;



            default:
                    break;
        }
    }

 // 回到顶部!!!!!!!!!!!!!
public void gotoTop() {
    weatherLayout.smoothScrollTo(0,20);
    degreeText.setFocusable(false);
    next1.setFocusable(true);
    next1.setFocusableInTouchMode(true);
    next1.requestFocus();
}

 // 上一页
 public void backPage() {
     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
     String nowString = prefs.getString("weather", null);
     Log.d("nowTitle!!!!!!!", nowTitle);

     directoryList = DataSupport.findAll(Directory.class);
     //directoryList = DataSupport.where("directoryName = ?", String.valueOf(selectedDirectory.getDirectoryName())).find(Directory.class);
     if (directoryList.size() > 0) {
         Log.d("books!!!!!!!", "2222");
         for (Directory directory : directoryList) {
             if (nowString.equals(directory.getDirectoryName()))
             {
                 Log.d("nowTitle!!!!!!!",  "id=" + directory.getId() + "目录=" + directory.getDirectoryName());
                 Directory nexts = DataSupport.find(Directory.class, directory.getId() - 1, true);
                 Log.d("id!!!!!!!", "id=" + (directory.getId() - 1));
                 String a,b,c;
                 a = nexts.getDirectoryName();
                 b = nexts.getDirectoryContent();
                 c = a + "|" + b;
                 showWeatherInfo(c);

                 // 回到顶部!!!!!!!!!!!!!
                 weatherLayout.smoothScrollTo(0,20);
                 degreeText.setFocusable(false);
                 next1.setFocusable(true);
                 next1.setFocusableInTouchMode(true);
                 next1.requestFocus();
             }
         }
     }

 }

// 下一页
 public void nextPage() {
     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
     String nowString = prefs.getString("weather", null);
     Log.d("nowTitle!!!!!!!", nowTitle);

     directoryList = DataSupport.findAll(Directory.class);
     //directoryList = DataSupport.where("directoryName = ?", String.valueOf(selectedDirectory.getDirectoryName())).find(Directory.class);
     if (directoryList.size() > 0) {
         Log.d("books!!!!!!!", "2222");
         for (Directory directory : directoryList) {
             if (nowString.equals(directory.getDirectoryName()))
             {
                 Log.d("nowTitle!!!!!!!",  "id=" + directory.getId() + "目录=" + directory.getDirectoryName());
                 Directory nexts = DataSupport.find(Directory.class, directory.getId() + 1, true);
                 Log.d("id!!!!!!!", "id=" + (directory.getId() + 1));

                 String a,b,c;
                 a = nexts.getDirectoryName();
                 b = nexts.getDirectoryContent();
                 c = a + "|" + b;
                 showWeatherInfo(c);

                 // 回到顶部!!!!!!!!!!!!!
                 weatherLayout.smoothScrollTo(0,20);
                 degreeText.setFocusable(false);
                 next1.setFocusable(true);
                 next1.setFocusableInTouchMode(true);
                 next1.requestFocus();
             }
         }
     }

 }

    // 手势监听
 //Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();

     @Override
     public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        /* if (e1.getX() - e2.getX() > 250) {//向左滑，右边显示
             //this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
             //this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
             Log.d("test!!!!!", "向左滑");
         }
         if (e1.getX() - e2.getX() < -250) {//向右滑，左边显示
             Log.d("test!!!!!", "向右滑");
         }*/


            if (e1.getX() - e2.getX() > 200 && Math.abs(velocityX) > 20) {
                //则上面的语句能知道啥意思了吧，就是说向量的水平长度（滑了有多长）必须大于verticalMinDistance，并且水平方向速度大于minVelocity。
                if (e1.getY() - e2.getY() > 100) {
                    Log.d("MYTAG", "向上滑...");
                }else if (e2.getY() - e1.getY() > 100) {
                    Log.d("MYTAG", "向下滑...");
                } else  {
                    Log.d("test!!!!!", "向左滑");
                    nextPage(); // 下一页
                    gotoTop();  // 回到顶部!!!!!!!!!!!!!
                }
            }
            if (e2.getX() - e1.getX() > 200 && Math.abs(velocityX) > 20) {
                //则上面的语句能知道啥意思了吧，就是说向量的水平长度（滑了有多长）必须大于verticalMinDistance，并且水平方向速度大于minVelocity。
                Log.d("MYTAG", "test...");
                if (e2.getY() - e1.getY() > 100) {
                    Log.d("MYTAG", "向下滑...");
                }else if (e1.getY() - e2.getY() > 100) {
                    Log.d("MYTAG", "向上滑...");
                } else  {
                    Log.d("test!!!!!", "向右滑");
                    backPage(); // 上一页
                    gotoTop(); // 回到顶部!!!!!!!!!!!!!

                }
            }

         return false;
     }

/* // ！！！！！！！！！！！！！！！！
按下（onDown）： 刚刚手指接触到触摸屏的那一刹那，就是触的那一下。
抛掷（onFling）： 手指在触摸屏上迅速移动，并松开的动作。
长按（onLongPress）： 手指按在持续一段时间，并且没有松开。
滚动（onScroll）： 手指在触摸屏上滑动。
按住（onShowPress）： 手指按在触摸屏上，它的时间范围在按下起效，在长按之前。
抬起（onSingleTapUp）：手指离开触摸屏的那一刹那。

任何手势动作都会先执行一次按下（onDown）动作。
长按（onLongPress）动作前一定会执行一次按住（onShowPress）动作。
按住（onShowPress）动作和按下（onDown）动作之后都会执行一次抬起（onSingleTapUp）动作。
长按（onLongPress）、滚动（onScroll）和抛掷（onFling）动作之后都不会执行抬起（onSingleTapUp）动作。

在这里有个要注意的地方，就是onDown的返回值，如果你设为false，经测试，它就一直只执行onDown-onShowPress-onLongPress；其他的并不会执行。
如果设为true，则正常。
*/



//鼠标手势相当于一个向量（当然有可能手势是曲线），e1为向量的起点，e2为向量的终点，velocityX为向量水平方向的速度，velocityY为向量垂直方向的速度
/* 如果需要设置activity切换效果，在startActivity(intent);之后添加
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);即可，可修改相应参数，
      可参考http://www.iteye.com/topic/1116472其他：
关于activity添加ScrollView后或是外部为RelativeLayout时onFling不起作用，无法滑动问题见http://trinea.iteye.com/blog/1213815
    */

  @Override
  public boolean onTouch(View v, MotionEvent event) {
      // Log.d("touch","touch");
      return mGestureDetector.onTouchEvent(event);
    }

     @Override   // 用户按下屏幕就会触发；
     public boolean onDown(MotionEvent e) {
         return true;
     }

    @Override
     public void onShowPress(MotionEvent e) {

     }

     @Override
     public boolean onSingleTapUp(MotionEvent e) {
         return false;
     }

     @Override
     public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
         return false;
     }

     @Override
     public void onLongPress(MotionEvent e) {

     }

     @Override
     public boolean onTouchEvent(MotionEvent event) {

         return super.onTouchEvent(event);
     }




                                                                                         // 根据天气 id 请求城市天气信息
    public void requestWeather(final String weatherId) {
        Log.d("requestWeather!!!!!!!", "!!!!requestWeather!!!");

          runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherId != null ) {
                            String [] temp = null;
                            temp = weatherId.split("\\|"); //但是要注意的是，如果使用"."、"|"、"^"等字符做分隔符时，要写成s3.split("\\^")的格式，否则不能拆分。
                            String weather = temp[0];
                            String directory_text = temp[1];
                            nowTitle = weather;

                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BookActivity.this).edit();
                            editor.putString("weather", weather);
                            editor.putString("directory_text", directory_text);
                            //Log.d("weatherId!!!!!!!", weatherId);

                            editor.apply();
                            showWeatherInfo(weatherId);
                        } else {
                            Toast.makeText(BookActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false); // 刷新结束，hide进度条
                    }
                });
    }



   /* // 翻页
    private class MyPagerAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<TextView> views;
        public MyPagerAdapter(Context context){

            this.context=context;
            views=new ArrayList<TextView>();
            for(int i = 0; i<30; i++){
                TextView tv=new TextView(context);
                tv.setText(""+i);
                views.add(tv);
            }    }
        @Override
        public int getCount() {
            return views.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));    }
        @Override   public View instantiateItem(ViewGroup container, int position){
            ((ViewGroup) container).addView(views.get(position));
            return views.get(position);    }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;    }
    }*/







    // 处理并展示 Weather 实体类中的数据
    private void showWeatherInfo(String books) {
        Log.d("showWeatherInfo!!!!!!!", "回到地球当神棍");




        titleCity.setTextSize(25);
        //titleCity.setBackgroundColor(Color.parseColor("#E9FAFF"));
        degreeText.setTextSize(20);
        //degreeText.setBackgroundColor(Color.parseColor("#E9FAFF"));



        String [] temp = null;
        temp = books.split("\\|"); //但是要注意的是，如果使用"."、"|"、"^"等字符做分隔符时，要写成s3.split("\\^")的格式，否则不能拆分。
        titleCity.setTextColor(Color.BLACK);//系统自带的颜色类
        titleCity .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        titleCity.setText(temp[0]);


        CharSequence charSequence;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(temp[1],Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml(temp[1]);
        }
        //tv.setText(charSequence);
        degreeText.setTextColor(Color.BLACK);//系统自带的颜色类
        degreeText .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //设置不为加粗
        //degreeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        degreeText.setBackgroundColor(Color.parseColor("#E9FAFF"));
        degreeText.setText(charSequence);






        forecastLayout.removeAllViews();
        weatherLayout.setVisibility(View.VISIBLE);


        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BookActivity.this).edit();
        editor.putString("weather", temp[0]);
        editor.apply();


    }









}




















