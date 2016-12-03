package com.dream.will.xml;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Main2Activity extends SwipeBackActivity implements View.OnClickListener {

    String hrefs;
    String userImages;
    String userNames;
    String contents;
    String image;

    List<CommetBean> data;
    com.dream.will.xml.ListAdapter Myad;
    private ImageView contentImage;
    private ListView listview;
    private FloatingActionButton floatingActino;
    private ImageView userImage;
    private TextView userName;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //获取  swipeBackLayout
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
//设置 互动的区域
        swipeBackLayout.setEdgeSize(200);
// 设定滑动关闭的方向  下左右
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        Intent i = getIntent();
        userImages = i.getStringExtra("userImages");
        userNames = i.getStringExtra("userNames");
        contents = i.getStringExtra("contents");
        image = i.getStringExtra("contentImage");
        hrefs = i.getStringExtra("hrefs");
        Log.i("TAG", "onCreate: hrefs---------" + hrefs);
        data = new ArrayList<>();
        initView();
        getData();
    }

    private void getData() {
        Log.i("TAG", "onResponse: response.body---------");
        Retrofit re = new Retrofit.Builder()
                .baseUrl(MainActivity.KEY_BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        IJoke iJoke = re.create(IJoke.class);
        Call<String> call = iJoke.getConmment(hrefs.substring(hrefs.lastIndexOf("/") + 1));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String value = response.body();
                Log.i("TAG", "onResponse: response.body---------" + value);
                //通过JSop解析成Doucment
                Document document = Jsoup.parse(value);
//                //从doc中拿到class="article block untagged mb15" class中有空格后面是继承关系
                Elements elementsByClass = document.getElementsByClass("comment-block");
//                    data.clear();
//                //取出每个元素分装成Bean
                for (Element ele : elementsByClass) {
                    data.add(new CommetBean(ele));
                }
                Log.i("TAG", "onResponse: data.size()---------" + data.size());
                Myad.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void initView() {
        contentImage = (ImageView) findViewById(R.id.contentImage);
        listview = (ListView) findViewById(R.id.listview);
        floatingActino = (FloatingActionButton) findViewById(R.id.floatingActino);
        floatingActino.setOnClickListener(this);
        if (image != null) {
            Glide.with(this).load(image).into(contentImage);
        } else {
            contentImage.setVisibility(View.GONE);
        }
        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);
        content = (TextView) findViewById(R.id.content);
        Glide.with(this).load(userImages).into(userImage);
        userName.setText(userNames);
        content.setText(contents);

        Myad = new com.dream.will.xml.ListAdapter<CommetBean>(this, data, R.layout.comment_item) {


            @Override
            public void bindData(int position, ViewHolder viewHolder) {
                CommetBean commetBean = data.get(position);
                ImageView uImage = (ImageView) viewHolder.findViewBid(R.id.userImage);
                TextView uName = (TextView) viewHolder.findViewBid(R.id.userName);
                TextView cText = (TextView) viewHolder.findViewBid(R.id.content);
                Glide.with(Main2Activity.this).load(commetBean.userImage).into(uImage);
                uName.setText(commetBean.userName);
                cText.setText(commetBean.content);
            }
        };
        listview.setAdapter(Myad);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActino:
                finish();
                break;
        }
    }
}
