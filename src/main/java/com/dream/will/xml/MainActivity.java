package com.dream.will.xml;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_BASEURL = "http://www.qiushibaike.com";
    int page = 1;

    List<JokeBean> data = new ArrayList<>();
    MyAdapter myAdapter;
    LinearLayoutManager linearLayoutManager;
    boolean isClear = false;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;
    private FloatingActionButton floatingActino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();
    }

    public void getData() {

        Retrofit re = new Retrofit.Builder()
                .baseUrl(KEY_BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        IJoke iJoke = re.create(IJoke.class);
        Call<String> call = iJoke.getData(page);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String value = response.body();
//                Log.i("TAG", "onResponse: response.body---------" + value);
//                text.setText(value);
                //通过Jsoup解析成Doucment
                Document document = Jsoup.parse(value);
                //从doc中拿到class="article block untagged mb15" class中有空格后面是继承关系
                Elements elementsByClass = document.getElementsByClass("article");
                if (isClear) {
                    data.clear();
                }
                //取出每个元素分装成Bean
                for (Element ele : elementsByClass) {
                    data.add(new JokeBean(ele));
                }
                myAdapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void initView() {
//        text = (TextView) findViewById(R.id.text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(data, this);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClick(new MyAdapter.ContentSetOnClickItem() {
            @Override
            public void OnClickListener(View view, int position) {
                JokeBean jokeBean = data.get(position);
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                i.putExtra("userImages", jokeBean.userImage);
                i.putExtra("userNames", jokeBean.userName);
                i.putExtra("contents", jokeBean.content);
                i.putExtra("hrefs", jokeBean.href);
                i.putExtra("contentImage", jokeBean.image);
                startActivity(i);
            }
        });
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setColorSchemeResources(android.R.color.holo_orange_light,android.R.color.holo_blue_light,
                android.R.color.holo_red_light,  android.R.color.holo_green_light);
        refresh.setRefreshing(true);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isClear = true;
                page = 1;
                getData();
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int e = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int r = linearLayoutManager.findFirstVisibleItemPosition();
                int t = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int y = linearLayoutManager.findLastVisibleItemPosition();
                if (e!=0){
                    floatingActino.setVisibility(View.VISIBLE);
                }else {
                    floatingActino.setVisibility(View.GONE);
                    //按下更新按钮 更新数据
                    if (buttonPress){
                        page = 1;
                        getData();
                    }
                }
                Log.i("TAG", "onScrollStateChanged: ---findFirstCompletelyVisibleItemPosition------" + e);
                Log.i("TAG", "onScrollStateChanged: ---findFirstVisibleItemPosition------" + r);
                Log.i("TAG", "onScrollStateChanged: ---findLastCompletelyVisibleItemPosition------" + t);
                Log.i("TAG", "onScrollStateChanged: ----findLastVisibleItemPosition-----" + y);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int t = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE && t == data.size() - 1) {
                    page++;
                    getData();
                    Log.i("TAG", "onScrolled: page---------" + page);
                    isClear = false;
                }
            }
        });
        floatingActino = (FloatingActionButton) findViewById(R.id.floatingActino);
        floatingActino.setVisibility(View.GONE);
        floatingActino.setOnClickListener(this);
    }


    boolean buttonPress= false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActino:
//                floatingActino.setVisibility(View.GONE);
                //按钮按下
                buttonPress = true;
                //清除数据
                isClear = true;
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        //平滑的移动到指定item 带动画
                        //然后在滑动监听中 更新数据操作
                        //好处  解决数据更新和recyclerView滑动 冲突带来画面卡顿
                        recyclerView.smoothScrollToPosition(0);
                    }
                });
                refresh.setRefreshing(true);

                break;
        }
    }
}
