package com.example.studyapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.FirstActivity;
import com.example.studyapp.R;
import com.example.studyapp.recycle.HomeAdapter;
import com.example.studyapp.recycle.HomeData;
import com.example.studyapp.ui.chart.Env;

<<<<<<< HEAD
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class HomeFragment extends Fragment {

    private ArrayList<HomeData> arrayList;
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

=======
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

>>>>>>> master
    private HomeViewModel homeViewModel;

    private Button sub1;
    private TextView tv_data;
    private RequestQueue requestQueue;
    private String today,userID;
    public static String TOTAL_STUDY_TIME;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userID = FirstActivity.userInfo.getString("userId", null);

        //현재 날짜 불러오기
        TimeZone tz;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        tz = TimeZone.getTimeZone("Asia/Seoul");
        dateFormat.setTimeZone(tz);
        Date date = new Date();
        today = dateFormat.format(date);


<<<<<<< HEAD
=======
        //Volley Queue  & request json
        requestQueue = Volley.newRequestQueue(getContext());
        totalStudyTime();

        sub1 = (Button) root.findViewById(R.id.sub1);
        tv_data = (TextView) root.findViewById(R.id.tv_data);

        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //누른 과목 정보 보내주기
                Intent intent = new Intent(getActivity(), StopwatchActivity.class);
                intent.putExtra("subject", sub1.getText());
                startActivity(intent);

            }
        });

>>>>>>> master
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                /*
                onChnaged= 뷰를 눌러서 실행했을때 실행시킬 이벤트 삽입
                 */

            }
        });
        return root;
    }
    private void totalStudyTime() {
        String url = String.format(Env.totalURL, userID, today);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //json object >> {response:[{key : value}, {.....
                            JSONObject jsonObject = new JSONObject(response);

                            //object start name : response  >>>>> array
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject studyObject = jsonArray.getJSONObject(0);
                            String studyTime = studyObject.getString("study_time");

                            if(studyTime.equals("null")){
                                tv_data.setText("00:00:00");
                            }else{
                                TOTAL_STUDY_TIME = makeSortTime(studyTime);
                                tv_data.setText(TOTAL_STUDY_TIME);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }
    private String makeSortTime(String studyTime){
        String [] ar = makeVisibleTime(studyTime).split(":");
        int s = Integer.parseInt(ar[2]);
        int m = Integer.parseInt(ar[1]);
        int h = Integer.parseInt(ar[0]);
        System.out.println(s + " " + m + " " + h);
        if(s >= 60){
            m += 1;
            s %= 60;
        }
        if(m >= 60){
            h += 1;
            h %= 60;
        }
        ar[0] = String.valueOf(h);
        ar[1] = String.valueOf(m);
        ar[2] = String.valueOf(s);

        for(int i = 0; i < ar.length; i++){
            if(ar[i].length() < 2){
                while(ar[i].length() != 2){
                    ar[i] = "0" + ar[i];
                }
            }
        }

        return ar[0]+":"+ar[1]+":"+ar[2];
    }
    private String makeVisibleTime(String study){
        String [] ar = {"0","0","0","0","0","0"};
        int idx = ar.length -1;
        for(int i = study.length()-1; i >= 0; i--){
            char c = study.charAt(i);
            ar[idx--] = String.valueOf(c);
        }
        String time = "";
        for(int i = 0; i < ar.length; i++){
            time += ar[i];

            if(i == 1 || i == 3){
                time += ":";
            }
        }
        return time;
    }
}