package com.mehmet.ingbank_interview_android_project;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mehmet.ingbank_interview_android_project.Utils.Constant;
import com.mehmet.ingbank_interview_android_project.Utils.PrefrenceHelper;

import java.util.ArrayList;

public class RepoDetailActivity extends AppCompatActivity
{
    //Tabbar
    TextView reponame_textview;
    ImageButton backbutton_imagebutton;
    ImageButton start_imagebutton;

    //Elements on the page
    ImageView repo_image_imageview;
    TextView repoowner_textview;
    TextView starts_textview;
    TextView openissues_textview;

    String repositoryName;

    boolean starControl = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        reponame_textview = findViewById(R.id.reponame_textview);
        repoowner_textview = findViewById(R.id.repoowner_textview);
        starts_textview = findViewById(R.id.starts_textview);
        openissues_textview = findViewById(R.id.openissues_textview);
        backbutton_imagebutton = findViewById(R.id.backbutton_imagebutton);
        repo_image_imageview = findViewById(R.id.repo_image_imageview);
        start_imagebutton = findViewById(R.id.start_imagebutton);

        if(!Constant.repositoryDetail.isEmpty())
        {
            String[] repositoryDetailArr = Constant.repositoryDetail.split("\\|");
            repositoryName = repositoryDetailArr[0];
            String startCount = repositoryDetailArr[1];
            String openIssuesCount = repositoryDetailArr[2];

            reponame_textview.setText(repositoryName);
            repoowner_textview.setText(Constant.username);
            starts_textview.setText(starts_textview.getText() +" "+ startCount);
            openissues_textview.setText(openissues_textview.getText() +" "+ openIssuesCount);
            Glide.with(RepoDetailActivity.this).load(Constant.avatar).into(repo_image_imageview);
        }
        if(PrefrenceHelper.GetArrayList("starSave") != null )
        {
            for (String star : PrefrenceHelper.GetArrayList("starSave"))
            {
                String[] starArr = star.split("\\|");
                String username = starArr[0];
                String reponame = starArr[1];

                if(username.equals(Constant.username) && reponame.equals(repositoryName))
                {
                    start_imagebutton.setBackgroundResource(R.drawable.star_selected);
                    starControl = true;
                }
            }
        }

        //The repository favorites added to the local memory with SharedPreference
        start_imagebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if(starControl)
                    {
                        start_imagebutton.setBackgroundResource(R.drawable.star_unselected);

                        ArrayList<String> saveStar = PrefrenceHelper.GetArrayList("starSave");
                        saveStar.remove(Constant.username+"|"+repositoryName);
                        PrefrenceHelper.SaveArrayList(saveStar,"starSave");
                        starControl = false;
                    }
                    else
                    {
                        start_imagebutton.setBackgroundResource(R.drawable.star_selected);

                        ArrayList<String> arrayList;
                        if(PrefrenceHelper.GetArrayList("starSave") != null)
                        {
                            arrayList = PrefrenceHelper.GetArrayList("starSave");
                        }
                        else
                        {
                            arrayList = new ArrayList<>();
                        }
                        arrayList.add(Constant.username+"|"+repositoryName);
                        PrefrenceHelper.SaveArrayList(arrayList,"starSave");
                        starControl = true;
                    }
                }
                catch (Exception ex)
                {
                    Log.e("start_imagebutton","Exception: " + ex.toString());
                }
            }
        });
        backbutton_imagebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}