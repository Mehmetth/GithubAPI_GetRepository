package com.mehmet.ingbank_interview_android_project.UserRepo;

public class RepoData
{
    private String reponame;
    private int starImage;
    public RepoData(String description, int imgId)
    {
        this.reponame = description;
        this.starImage = imgId;
    }
    public String GetRepoName()
    {
        return reponame;
    }
    public void SetRepoName(String description)
    {
        this.reponame = description;
    }
    public int GetStarImage()
    {
        return starImage;
    }
    public void SetStarImage(int imgId)
    {
        this.starImage = imgId;
    }
}