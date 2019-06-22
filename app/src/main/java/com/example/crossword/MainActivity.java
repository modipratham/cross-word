package com.example.crossword;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crossword.helpers.Preferences;
import com.example.crossword.models.Crossword;
import com.example.crossword.models.Synonym;
import com.example.crossword.utils.Constants;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout srl;
    private RelativeLayout gameLayout;
    private RelativeLayout summaryLayout;
    ArrayList<Synonym> synonyms = new ArrayList<>();
    ArrayList<Synonym> Newsynonym = new ArrayList<>();
    ArrayList<Crossword> crosswords = new ArrayList<>();
    ProgrammingAdapter programmingAdapter;
    SummaryAdapter summaryAdapter;
    int timer = 0;
    int status = 0;
    RecyclerView programmingList;
    RecyclerView summaryList;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManager1;
    ImageView next;
    Runnable runnable;
    Handler handler;
    int page = 0;
    int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        handler = new Handler();

        progressDialog.setMessage("Please Wait ... ");
        progressDialog.setCancelable(false);

        programmingList = findViewById(R.id.programmingList);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        summaryList = findViewById(R.id.summaryList);
        summaryLayout = findViewById(R.id.summaryLayout);
        gameLayout = findViewById(R.id.gameLayout);
        next= findViewById(R.id.next);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(gameLayout.getVisibility() == View.VISIBLE) {
                    reloadData();
                } else {
                    srl.setRefreshing(false);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(false);
            }
        });

        programmingAdapter = new ProgrammingAdapter(synonyms, this);
        summaryAdapter = new SummaryAdapter(synonyms,this);

        linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        summaryList.setLayoutManager(linearLayoutManager1);
        summaryList.setAdapter(summaryAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                synonyms.remove(viewHolder.getAdapterPosition());
                if(synonyms.size() <= 0){
                    startGame(false);
                } else {
                    summaryList.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(summaryList);

        linearLayoutManager = new GridLayoutManager(this, 4);
        programmingList.setLayoutManager(linearLayoutManager);
        programmingList.setAdapter(programmingAdapter);

        reloadData();

    }

    public void reloadData(){

        String url = "https://crossword-ddd3a.firebaseio.com/crossword.json" ;

        showProgressDialog();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response != null) {

                                JSONArray jsonArray = response.getJSONArray("crossword");

                                crosswords.clear();
                                for (int j=0;j < jsonArray.length(); j++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);

                                    String type = jsonObject.getString("type");
                                    int t = jsonObject.getInt("timer");

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("synonyms");

                                    ArrayList<Synonym> newSynonyms = new ArrayList<>();
                                    for (int i = 0; i < jsonArray1.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        String key1 = jsonObject1.getString("key1");
                                        String key2 = jsonObject1.getString("key2");
                                        String description = jsonObject1.getString("description");

                                        Synonym synonym1 = new Synonym();
                                        synonym1.setKey1(key1);
                                        synonym1.setKey2(key2);
                                        synonym1.setDescription(description);
                                        newSynonyms.add(synonym1);

                                        Synonym synonym2 = new Synonym();
                                        synonym2.setKey1(key2);
                                        synonym2.setKey2(key1);
                                        synonym2.setDescription(description);
                                        newSynonyms.add(synonym2);
                                    }

                                    Crossword crossword = new Crossword();
                                    crossword.setSynonyms(newSynonyms);
                                    crossword.setType(type);
                                    crossword.setTimer(t);
                                    crosswords.add(crossword);

                                }

                                page = 0;
                                startGame(false);

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hideProgressDialog();
                        srl.setRefreshing(false);
                    }
                },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    srl.setRefreshing(false);
                    hideProgressDialog();
                    error.printStackTrace();
                }
            });

        mQueue.add(request);

    }

 public void startGame(boolean retry){

        if (page >= 4){

            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
            startActivity(intent);

        } else {
            handler.removeCallbacks(runnable);
            synonyms.clear();
            Crossword crossword;
            if(!retry){
                crossword = crosswords.get(page);
                currentPage = page;
                page = page + 1;
            } else {
                crossword = crosswords.get(currentPage);
                page = currentPage + 1;
            }
            synonyms.addAll(crossword.getSynonyms());
            final String type = crossword.getType();
            if(type.equals("easy")) {
                TextView TextView = (TextView) findViewById(R.id.TextView);
                TextView.setText("EASY");
                SwipeRefreshLayout background = (SwipeRefreshLayout) findViewById(R.id.srl);
                background.setBackgroundResource(R.drawable.easy);
            }
            else if (type.equals("medium")) {
                TextView TextView = (TextView) findViewById(R.id.TextView);
                TextView.setText("MEDIUM");
                SwipeRefreshLayout background = (SwipeRefreshLayout) findViewById(R.id.srl);
                background.setBackgroundResource(R.drawable.medium);
            }
            else if(type.equals("hard")) {
                TextView TextView = (TextView) findViewById(R.id.TextView);
                TextView.setText("HARD");
                SwipeRefreshLayout background = (SwipeRefreshLayout) findViewById(R.id.srl);
                background.setBackgroundResource(R.drawable.hard);
            }
           else if(type.equals("genius")) {
                TextView TextView = (TextView) findViewById(R.id.TextView);
                TextView.setText("GENIUS");
                TextView.setTextColor(Color.parseColor("#FFFFFF"));
                SwipeRefreshLayout background = (SwipeRefreshLayout) findViewById(R.id.srl);
                background.setBackgroundResource(R.drawable.genius);
            }
            final CircularProgressBar progressBar = findViewById(R.id.pbTimer);
            timer = crossword.getTimer();
            status = 0;
            progressBar.setProgressMax(timer);
            progressBar.setProgress(status);

            runnable = new Runnable() {
                @Override
                public void run() {
                    if (timer > status){
                        status = status + 1;
                        progressBar.setProgress(status);
                        handler.postDelayed(this,1000);
                        TextView textView = (TextView) findViewById(R.id.tvTimer);
                        textView.setText(String.valueOf(timer-status));
                    } else{
                        if (gameLayout.getVisibility()==View.VISIBLE) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setMessage("DO YOU WANT TO RETRY?");
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            startGame(true);
                                        }
                                    });

                            builder1.setIcon(android.R.drawable.ic_dialog_alert);

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                            startActivity(intent);
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();

                            alert11.show();
                            handler.removeCallbacks(this);
                        }
                        else {
                            handler.removeCallbacks(this);
                        }
                    }
                }
            };

            handler.postDelayed(runnable, 1000);

            Collections.shuffle(synonyms);

            summaryAdapter.notifyDataSetChanged();
            programmingAdapter.notifyDataSetChanged();
            Preferences.removePreference(Constants.FIRST_ENTRY);
            Preferences.removePreference(Constants.FIRST_ENTRY_ID);
            gameLayout.setVisibility(View.VISIBLE);
            summaryLayout.setVisibility(View.GONE);
        }

    }

    public void showProgressDialog(){
        if(progressDialog != null && !progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    public void hideProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollHorizontally() {
            return isScrollEnabled && super.canScrollHorizontally();
        }
    }
}

