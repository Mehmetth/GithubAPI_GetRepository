package com.mehmet.ingbank_interview_android_project.UserRepo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mehmet.ingbank_interview_android_project.MainActivity;
import com.mehmet.ingbank_interview_android_project.R;
import com.mehmet.ingbank_interview_android_project.RepoDetailActivity;
import com.mehmet.ingbank_interview_android_project.Utils.Constant;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder>
{
    private List<RepoData> repoData;

    public RepoAdapter(List<RepoData> listdata)
    {
        this.repoData = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.recyclerview_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    //The part where the data is passed to the recycler view.
    //The part that is triggered when the element is clicked in Receyclerview.
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final RepoData myListData = repoData.get(position);
        holder.reponame_textview.setText(repoData.get(position).GetRepoName());
        holder.star_imageview.setImageResource(repoData.get(position).GetStarImage());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                for(String searchRepo : Constant.userRepositoryDeatil)
                {
                    if(searchRepo.startsWith(repoData.get(position).GetRepoName()))
                    {
                        Constant.repositoryDetail = searchRepo;
                        Intent intent2 = new Intent(MainActivity.Current, RepoDetailActivity.class);
                        MainActivity.Current.startActivity(intent2);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return repoData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView star_imageview;
        TextView reponame_textview;
        RelativeLayout relativeLayout;
        ViewHolder(View itemView)
        {
            super(itemView);
            this.reponame_textview = (TextView) itemView.findViewById(R.id.reponame_textview);
            this.star_imageview = (ImageView) itemView.findViewById(R.id.star_imageview);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}