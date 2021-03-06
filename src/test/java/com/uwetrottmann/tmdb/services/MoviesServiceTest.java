
package com.uwetrottmann.tmdb.services;

import com.uwetrottmann.tmdb.BaseTestCase;
import com.uwetrottmann.tmdb.TestData;
import com.uwetrottmann.tmdb.entities.*;
import com.uwetrottmann.tmdb.enumerations.AppendToResponseItem;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

public class MoviesServiceTest extends BaseTestCase {

    @Test
    public void test_summary() throws ParseException, IOException {
        Movie movie = getManager().moviesService().summary(TestData.MOVIE_ID, null, null).execute().body();
        assertMovie(movie);
        assertThat(movie.original_title).isEqualTo(TestData.MOVIE_TITLE);
    }

    @Test
    public void test_summary_language() throws ParseException, IOException {
        Movie movie = getManager().moviesService().summary(TestData.MOVIE_ID, "pt", null).execute().body();
        assertThat(movie).isNotNull();
        assertThat(movie.title).isEqualTo("Um Sonho de Liberdade");
    }

    @Test
    public void test_summary_with_collection() throws ParseException, IOException {
        Movie movie = this.getManager().moviesService().summary(TestData.MOVIE_WITH_COLLECTION_ID, null, null).execute().body();
        assertThat(movie.title).isEqualTo(TestData.MOVIE_WITH_COLLECTION_TITLE);
        assertThat(movie.belongs_to_collection).isNotNull();
        assertThat(movie.belongs_to_collection.id).isEqualTo(1241);
        assertThat(movie.belongs_to_collection.name).isEqualTo("Harry Potter Collection");
    }

    private void assertMovie(Movie movie) {
        assertThat(movie).isNotNull();
        assertThat(movie.id).isEqualTo(TestData.MOVIE_ID);
        assertThat(movie.title).isEqualTo(TestData.MOVIE_TITLE);
        assertThat(movie.overview).isNotEmpty();
        assertThat(movie.tagline).isNotEmpty();
        assertThat(movie.adult).isFalse();
        assertThat(movie.backdrop_path).isNotEmpty();
        assertThat(movie.budget).isEqualTo(25000000);
        assertThat(movie.imdb_id).isEqualTo(TestData.MOVIE_IMDB);
        assertThat(movie.poster_path).isNotEmpty();
        assertThat(movie.release_date).isEqualTo("1994-09-14");
        assertThat(movie.revenue).isEqualTo(28341469);
        assertThat(movie.runtime).isEqualTo(142);
        assertThat(movie.vote_average).isPositive();
        assertThat(movie.vote_count).isPositive();
    }

    @Test
    public void test_summary_append_videos() throws IOException {
        Movie movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null,
                new AppendToResponse(
                        AppendToResponseItem.VIDEOS)).execute().body();

        assertNotNull(movie.videos);
    }

    @Test
    public void test_summary_append_credits()throws IOException {
        Movie movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null,
                new AppendToResponse(
                        AppendToResponseItem.CREDITS)).execute().body();

        assertNotNull(movie.credits);
    }

    @Test
    public void test_summary_append_releases() throws IOException {
        Movie movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null,
                new AppendToResponse(
                        AppendToResponseItem.RELEASES)).execute().body();

        assertNotNull(movie.releases);
    }

    @Test
    public void test_summary_append_similar() throws IOException {
        Movie movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null,
                new AppendToResponse(
                        AppendToResponseItem.SIMILAR)).execute().body();

        assertNotNull(movie.similar);
    }

    @Test
    public void test_summary_append_all() throws IOException {
        Movie movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null,
                new AppendToResponse(
                        AppendToResponseItem.RELEASES,
                        AppendToResponseItem.CREDITS,
                        AppendToResponseItem.VIDEOS,
                        AppendToResponseItem.SIMILAR)).execute().body();

        assertNotNull(movie.releases);
        assertNotNull(movie.credits);
        assertNotNull(movie.videos);
        assertNotNull(movie.similar);
    }

    @Test
    public void test_alternative_titles() throws IOException {
        MovieAlternativeTitles titles = getManager().moviesService().alternativeTitles(TestData.MOVIE_ID, null).execute().body();
        assertThat(titles).isNotNull();
        assertThat(titles.id).isEqualTo(TestData.MOVIE_ID);
        assertThat(titles.titles).isNotEmpty();
        assertThat(titles.titles.get(0).iso_3166_1).isEqualTo("FI");
        assertThat(titles.titles.get(0).title).isEqualTo("Rita Hayworth - Avain pakoon");
    }

    @Test
    public void test_credits() throws IOException {
        Credits credits = getManager().moviesService().credits(TestData.MOVIE_ID).execute().body();
        assertThat(credits).isNotNull();
        assertThat(credits.id).isEqualTo(TestData.MOVIE_ID);
        assertThat(credits.cast).isNotEmpty();
        assertThat(credits.cast.get(0)).isNotNull();
        assertThat(credits.cast.get(0).name).isEqualTo("Tim Robbins");
        assertThat(credits.crew).isNotEmpty();
    }

    @Test
    public void test_images() throws IOException {
        Images images = getManager().moviesService().images(TestData.MOVIE_ID, null).execute().body();
        assertThat(images).isNotNull();
        assertThat(images.id).isEqualTo(TestData.MOVIE_ID);
        assertThat(images.backdrops).isNotEmpty();
        assertThat(images.backdrops.get(0).file_path).isNotEmpty();
        assertThat(images.backdrops.get(0).width).isEqualTo(1920);
        assertThat(images.backdrops.get(0).height).isEqualTo(1080);
        assertThat(images.backdrops.get(0).iso_639_1).isNull();
        assertThat(images.backdrops.get(0).aspect_ratio).isGreaterThan(1.7f);
        assertThat(images.backdrops.get(0).vote_average).isPositive();
        assertThat(images.backdrops.get(0).vote_count).isPositive();
        assertThat(images.posters).isNotEmpty();
        assertThat(images.posters.get(0).file_path).isNotEmpty();
        assertThat(images.posters.get(0).width).isEqualTo(1000);
        assertThat(images.posters.get(0).height).isEqualTo(1500);
        assertThat(images.posters.get(0).iso_639_1).hasSize(2);
        assertThat(images.posters.get(0).aspect_ratio).isGreaterThan(0.6f);
        assertThat(images.posters.get(0).vote_average).isPositive();
        assertThat(images.posters.get(0).vote_count).isPositive();
    }

    @Test
    public void test_keywords() throws IOException {
        MovieKeywords keywords = getManager().moviesService().keywords(TestData.MOVIE_ID).execute().body();
        assertThat(keywords).isNotNull();
        assertThat(keywords.id).isEqualTo(TestData.MOVIE_ID);
        assertThat(keywords.keywords.get(0).id).isEqualTo(378);
        assertThat(keywords.keywords.get(0).name).isEqualTo("prison");
    }

    @Test
    public void test_releases() throws IOException {
        Releases releases = getManager().moviesService().releases(TestData.MOVIE_ID).execute().body();
        assertThat(releases).isNotNull();
        assertThat(releases.id).isEqualTo(TestData.MOVIE_ID);
        assertThat(releases.countries.get(0).iso_3166_1).isEqualTo("US");
        assertThat(releases.countries.get(0).certification).isEqualTo("R");
        assertThat(releases.countries.get(0).release_date).isEqualTo("1994-09-14");
    }

    @Test
    public void test_videos() throws IOException {
        Videos videos = getManager().moviesService().videos(TestData.MOVIE_ID, null).execute().body();
        assertThat(videos).isNotNull();
        assertThat(videos.id).isEqualTo(TestData.MOVIE_ID);
        assertThat(videos.results.get(0).id).isNotNull();
        assertThat(videos.results.get(0).iso_639_1).isNotNull();
        assertThat(videos.results.get(0).key).isNotNull();
        assertThat(videos.results.get(0).name).isNotNull();
        assertThat(videos.results.get(0).site).isEqualTo("YouTube");
        assertThat(videos.results.get(0).size).isNotNull();
        assertThat(videos.results.get(0).type).isEqualTo("Trailer");
    }

    @Test
    public void test_translations() throws IOException {
        Translations translations = getManager().moviesService().translations(TestData.MOVIE_ID, null).execute().body();
        assertThat(translations).isNotNull();
        assertThat(translations.id).isEqualTo(TestData.MOVIE_ID);
        for (Translations.Translation translation : translations.translations) {
            assertThat(translation.name).isNotNull();
            assertThat(translation.iso_639_1).isNotNull();
            assertThat(translation.english_name).isNotNull();
        }
    }

    @Test
    public void test_similar() throws IOException {
        MovieResultsPage results = getManager().moviesService().similar(TestData.MOVIE_ID, 3, null).execute().body();
        assertThat(results).isNotNull();
        assertThat(results.page).isNotNull().isPositive();
        assertThat(results.total_pages).isNotNull().isPositive();
        assertThat(results.total_results).isNotNull().isPositive();
        assertThat(results.results).isNotEmpty();
        assertThat(results.results.get(0).adult).isEqualTo(false);
        assertThat(results.results.get(0).backdrop_path).isNotNull();
        assertThat(results.results.get(0).id).isNotNull().isPositive();
        assertThat(results.results.get(0).original_title).isNotNull();
        assertThat(results.results.get(0).release_date).isNotNull();
        assertThat(results.results.get(0).poster_path).isNotNull();
        assertThat(results.results.get(0).popularity).isNotNull().isPositive();
        assertThat(results.results.get(0).title).isNotNull();
        assertThat(results.results.get(0).vote_average).isNotNull().isPositive();
        assertThat(results.results.get(0).vote_count).isNotNull().isPositive();
    }

    @Test
    public void test_reviews() throws IOException {
        ReviewResultsPage results = getManager().moviesService().reviews(49026, 1, null).execute().body();
        assertThat(results).isNotNull();
        assertThat(results.id).isNotNull();
        assertThat(results.page).isNotNull().isPositive();
        assertThat(results.total_pages).isNotNull().isPositive();
        assertThat(results.total_results).isNotNull().isPositive();
        assertThat(results.results).isNotEmpty();
        assertThat(results.results.get(0).id).isNotNull();
        assertThat(results.results.get(0).author).isNotNull();
        assertThat(results.results.get(0).content).isNotNull();
        assertThat(results.results.get(0).url).isNotNull();
    }

    @Test
    public void test_lists() throws IOException {
        ListResultsPage results = getManager().moviesService().lists(49026, 1, null).execute().body();
        assertThat(results).isNotNull();
        assertThat(results.id).isNotNull();
        assertThat(results.page).isNotNull().isPositive();
        assertThat(results.total_pages).isNotNull().isPositive();
        assertThat(results.total_results).isNotNull().isPositive();
        assertThat(results.results).isNotEmpty();
        assertThat(results.results.get(0).id).isNotNull();
        assertThat(results.results.get(0).description).isNotNull();
        assertThat(results.results.get(0).favorite_count).isNotNull().isPositive();
        assertThat(results.results.get(0).item_count).isNotNull().isPositive();
        assertThat(results.results.get(0).iso_639_1).isNotNull();
        assertThat(results.results.get(0).name).isNotNull();
        assertThat(results.results.get(0).poster_path).isNotNull();
    }

    @Test
    public void test_latest() throws IOException {
        Movie movie = getManager().moviesService().latest().execute().body();
        // Latest movie might not have a complete TMDb entry, but should at least some basic properties.
        assertThat(movie).isNotNull();
        assertThat(movie.id).isPositive();
        assertThat(movie.title).isNotEmpty();
    }

    @Test
    public void test_upcoming() throws IOException {
        MovieResultsPage page = getManager().moviesService().upcoming(null, null).execute().body();
        assertThat(page).isNotNull();
        assertThat(page.results).isNotEmpty();
    }

    @Test
    public void test_nowPlaying() throws IOException {
        MovieResultsPage page = getManager().moviesService().nowPlaying(null, null).execute().body();
        assertThat(page).isNotNull();
        assertThat(page.results).isNotEmpty();
    }

    @Test
    public void test_popular() throws IOException {
        MovieResultsPage page = getManager().moviesService().popular(null, null).execute().body();
        assertThat(page).isNotNull();
        assertThat(page.results).isNotEmpty();
    }

    @Test
    public void test_topRated() throws IOException {
        MovieResultsPage page = getManager().moviesService().topRated(null, null).execute().body();
        assertThat(page).isNotNull();
        assertThat(page.results).isNotEmpty();
    }

}
