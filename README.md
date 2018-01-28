# AndroidMoviesApp
Discover the most popular movies playing.

App features:
- Upon launch, present the user with an grid arrangement of movie posters.
- Allow your user to change sort order via a setting:
- The sort order can be by most popular, or by top rated or favourites
- Allow the user to tap on a movie poster and transition to a details screen with additional information such as:  
    original title
    movie poster image thumbnail
    A plot synopsis (called overview in the api)
    user rating (called vote_average in the api)
    release date
    movie trailers
- User can add movie to favourites
- User can view his/her favourite movies from main activity.
- User can watch any of the movie trailers by clicking on the play icon. It will lauch that intent in YouTube app or browser.



IMP note for runnint this app:
- Add your The movie DB api key in https://github.com/maksumant/AndroidMoviesApp/blob/master/app/src/main/java/com/mymovies/android/popularmovies/utils/NetworkUtils.java. TODO has been marked in this class.
