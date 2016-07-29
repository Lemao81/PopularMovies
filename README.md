# PopularMovies Android App

Description
-
The App implements the API of the movie website www.themoviedb.org. It shows lists of the currently most popular movies and the best rated movies of all time. Movies can be selected as favourites and selected favourites can be seen in an extra view. The app offers details about the movies like rating and release year, also showing the movie poster. Users can login with their website login to rate movies and load their online favourite lists.

Website
-
www.themoviedb.org

![thoughtbot](https://lh4.ggpht.com/HuziCvAK2bC5P0y3lsco4avotFW8O_3xg7ONwVOUXf0f7qm06RzfdSX6NACSP8ebpg=w300)

About themoviedb.org
-

The Movie Database (TMDb) was started as a side project in 2008 to help the media center community serve high resolution posters and fan art. What started as a simple image sharing community has turned into one of the most actively user edited movie database on the Internet.

With an initital data contribution from a project called omdb (thank you!), the goal was to create our own product and service. We launched the first version of the database in early 2009. Along with the website we also launched one of first and only free movie data API's.

Today, our service is used by tens of millions of people every week and is often regarded as the single best place to get movie data and images. Whether you're interested in personal movie and TV recommendations, what movies have won the Oscar for best picture, maintaining a personal watchlist, or like to develop applications of your own, we hope you'll love everything our service has to offer.

Versions
-

Two versions of the app are available, a free version which includes advertising banners and a paid version, where advertising banners are removed

To run the app
-

To run the app you need to retrieve an API key from https://www.themoviedb.org/ and insert it in the local gradle.properties file:

---shell
MovieDbApiKey="<your API key>"
---
