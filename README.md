# PopularMovies
app listing popular/top rated movies

to run the app you need to retrieve an API key from https://www.themoviedb.org/ and insert it in the app/build.gradle file:

buildTypes.each{
        it.buildConfigField 'String', 'API_KEY', '"<your API key>"'
}
