# GithubAPI_GetRepository
It provides listing with RecyclerView according to the user name entered using GithubAPI.

Main
This application was developed using the Github API. It lists the "Repository" of the user according to the "Github User" name entered by the user.
Clicking on any of the listed Repository displays on the Repository Detail Page.
The Repository Detail Page displays the user's picture, username, repository name,  repo star count and open issue count
If the user stars the repository, the starred repository is displayed on the page where the repository is listed.

Detail
According to the Github User name that the user has seen, the recyclerView of the user is listed using the "https://api.github.com/users/{username{/repos" api.
In RecyclerView, the details of the selected item are listed in the RepoDetailActivity class.
In the RepoDetailActivity class, the Imageview uses the Glide library to display the user's image as a url.
It uses SharedPreference to keep starred repositories in the local area. The 3rd party "paperdb" and "gson" libraries are used because we keep the ArrayList <> type in the local area.
