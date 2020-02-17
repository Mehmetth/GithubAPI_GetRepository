package com.mehmet.ingbank_interview_android_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mehmet.ingbank_interview_android_project.UI.UIHelper;
import com.mehmet.ingbank_interview_android_project.UserRepo.RepoAdapter;
import com.mehmet.ingbank_interview_android_project.UserRepo.RepoData;
import com.mehmet.ingbank_interview_android_project.Utils.Constant;
import com.mehmet.ingbank_interview_android_project.Utils.PrefrenceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static Activity Current;

    private static RecyclerView repolist_recyclerView;
    private ProgressBar progressBar;
    private RepoAdapter repoAdapter;

    private List<String> repoName;
    private List<RepoData> repoData;

    private EditText username_edittex;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Current = MainActivity.this;

        PrefrenceHelper.PrefrenceInit(MainActivity.Current);

        repolist_recyclerView = findViewById(R.id.repolist_recyclerView);
        progressBar = findViewById(R.id.progressBar);

        username_edittex = findViewById(R.id.username_edittex);
        Button submit_button = findViewById(R.id.submit_button);

        submit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!username_edittex.getText().toString().isEmpty())
                {
                    BringTheRepoUser(username_edittex.getText().toString());
                    UIHelper.HideKeyboard(MainActivity.Current);
                }
                else
                {
                    Toast.makeText(MainActivity.Current,"You entered the username blank or wrong.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void BringTheRepoUser(final String userName)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    UIHelper.ProgressBar(progressBar,true);

                    String urlString = "https://api.github.com/users/"+userName+"/repos";
                    HttpURLConnection urlConnection = null;
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000 /* milliseconds */ );
                    urlConnection.setConnectTimeout(15000 /* milliseconds */ );
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();

                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    br.close();

                    String jsonString = sb.toString();

                    Constant.userRepositoryDeatil = new ArrayList<String>();
                    repoName = new ArrayList<String>();

                    if(!jsonString.isEmpty())
                    {
                        JSONArray jsonarray = new JSONArray(jsonString);
                        for (int i = 0; i < jsonarray.length(); i++)
                        {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String full_name = jsonobject.getString("name");
                            String stargazers_count = jsonobject.getString("stargazers_count");
                            String open_issues_count = jsonobject.getString("open_issues_count");

                            JSONObject json = new JSONObject(jsonobject.getString("owner"));

                            Constant.avatar = json.getString("avatar_url");
                            Constant.username = json.getString("login");

                            String userRepoDetail = full_name +"|"+ stargazers_count +"|"+ open_issues_count;

                            Constant.userRepositoryDeatil.add(userRepoDetail);

                            String[] userArr = userRepoDetail.split("\\|");
                            repoName.add(userArr[0]);
                            ReloadReposRecyclerView(repoName);
                        }
                    }
                    else
                    {
                        UIHelper.ProgressBar(progressBar,false);
                        Toast.makeText(MainActivity.Current,"No such user was found.",Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ex)
                {
                    UIHelper.ProgressBar(progressBar,false);
                    MainActivity.this.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if(repoData != null)
                            {
                                repoData.clear();
                                repoAdapter.notifyDataSetChanged();
                                username_edittex.setText("");
                            }
                            Toast.makeText(MainActivity.Current,"No such user was found.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    //Where the repositories are taken according to the username.
    public void ReloadReposRecyclerView(final List<String> repoNameList)
    {
        MainActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    repoData = new ArrayList<RepoData>();
                    for (String name : repoNameList)
                    {
                        if(PrefrenceHelper.GetArrayList("starSave") != null)
                        {
                            if(PrefrenceHelper.GetArrayList("starSave").contains(Constant.username+"|"+name))
                            {
                                repoData.add(new RepoData(name,R.drawable.star_selected));
                            }
                            else
                            {
                                repoData.add(new RepoData(name,R.drawable.star_unselected_white));
                            }
                        }
                        else
                        {
                            repoData.add(new RepoData(name,R.drawable.star_unselected_white));
                        }
                    }

                    LoadDataAdapter();

                    UIHelper.ProgressBar(progressBar,false);
                }
                catch (Exception ex)
                {
                    System.out.println("ReloadReposRecyclerView Exception: " + ex.toString());
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (repoData != null)
        {
            MainActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(PrefrenceHelper.GetArrayList("starSave") != null)
                    {
                        for(RepoData repoData : repoData)
                        {
                            if(PrefrenceHelper.GetArrayList("starSave").contains(Constant.username+"|"+repoData.GetRepoName()))
                            {
                                repoData.SetStarImage(R.drawable.star_selected);
                            }
                            else
                            {
                                repoData.SetStarImage(R.drawable.star_unselected_white);
                            }
                        }
                    }

                    LoadDataAdapter();
                }
            });
        }
    }

    public void LoadDataAdapter()
    {
        repoAdapter = new RepoAdapter(repoData);
        repolist_recyclerView.setHasFixedSize(true);
        repolist_recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.Current));
        repolist_recyclerView.setAdapter(repoAdapter);
    }
}