package com.example.administrator.canvasdemo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener{
    private RecyclerView rvMovie;
    private MyAdapter myAdapter;
    private List<String>urls=new ArrayList<>();
    private HashMap<String, Drawable> bgCacheMap = new HashMap<>();
    private ImageView cinemaDetailMoviebg;
    private boolean scrollState;
    private int firstVisibleItemPosition;
    private int lastVisibleItemPosition;
    private int visibleCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvMovie = (RecyclerView) findViewById(R.id.rv_movie);
        cinemaDetailMoviebg = (ImageView) findViewById(R.id.cinema_detail_moviebg);
        urls.add("http://img5.mtime.cn/mt/2018/10/19/141458.50154485_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/09/25/113254.40208555_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/10/25/170940.95506397_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/10/29/103941.24479370_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/10/15/100101.58856075_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/10/22/103110.71321890_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/09/30/153647.85396352_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/09/18/171005.34108810_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/10/22/154652.68494916_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/09/11/174027.67550491_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/09/28/165519.90243063_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/09/29/162642.59988398_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/10/18/095330.29294375_1280X720X2.jpg");
        urls.add("http://img31.mtime.cn/mt/2016/01/04/174628.64585634_1280X720X2.jpg");
        urls.add("http://img31.mtime.cn/mt/2015/03/24/091623.70794150_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/09/05/100442.95551597_1280X720X2.jpg");
        urls.add("http://img5.mtime.cn/mt/2018/09/10/101934.45689324_1280X720X2.jpg");
        myAdapter=new MyAdapter(urls,this);
        rvMovie.setLayoutManager(new CenterLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rvMovie.addItemDecoration(new GalleryItemDecoration(this));
        LinearSnapHelper movieSnapHelper = new LinearSnapHelper();
        movieSnapHelper.attachToRecyclerView(rvMovie);
        rvMovie.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(this);
        rvMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    //滚动停止
                    case RecyclerView.SCROLL_STATE_IDLE:
                        scrollState=false;
                        Log.i("newState","滚动停止");
                        autoplay();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        scrollState=true;
                        Log.i("newState","手指拖动");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING: //惯性滚动
                        scrollState = true;
                        Log.i("newState","惯性滚动");
                        break;

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
                 firstVisibleItemPosition=linearLayoutManager.findFirstVisibleItemPosition();
                 lastVisibleItemPosition=linearLayoutManager.findLastVisibleItemPosition();
                 visibleCount=lastVisibleItemPosition-firstVisibleItemPosition;

            }
        });
    }

    private void autoplay() {
        RecyclerView.LayoutManager layoutManager = rvMovie.getLayoutManager();
        for (int i = 0; i < visibleCount; i++) {
            ImageView imageView=layoutManager.getChildAt(i).findViewById(R.id.movie_pic);
            int[] location = new  int[2] ;
            imageView.getLocationInWindow(location);
            if (location[0]+imageView.getWidth()/2==rvMovie.getWidth()/2){
                Log.i("autoplay","location[0]"+location[0]+"   pos:"+rvMovie.getChildAdapterPosition(layoutManager.getChildAt(i)));
                setMovieRecBg(rvMovie.getChildAdapterPosition(layoutManager.getChildAt(i)));
                return;
            }
        }
    }


    @Override
    public void onItemClick(int pos) {
        rvMovie.smoothScrollToPosition(pos);
        setMovieRecBg(pos);
    }

    public void setMovieRecBg(final int pos){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ImageUtils imageUtils=new ImageUtils(MainActivity.this);
                try {
                    final Bitmap bitmap= Glide.with(MainActivity.this).asBitmap().load(urls.get(pos)).submit(300,520).get();
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void run() {
                            Drawable curBg=new BitmapDrawable(getResources(),imageUtils.gaussianBlur(25, bitmap));
                            Drawable preBg;
                            if (bgCacheMap.get("PRE_BG")==null){
                                preBg=curBg;
                            }else {
                                preBg= bgCacheMap.get("PRE_BG");
                            }
                            RequestOptions options=new RequestOptions().placeholder(preBg).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
                            Glide.with(MainActivity.this).load(bitmap).apply(options).transition(DrawableTransitionOptions.withCrossFade(1000)).into(cinemaDetailMoviebg);
                            bgCacheMap.put("PRE_BG",curBg);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
