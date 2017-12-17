package com.mymovies.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mymovies.android.popularmovies.R;
import com.mymovies.android.popularmovies.domain.Movie;
import com.mymovies.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URLDecoder;

/**
 * Created by makrandsumant on 16/12/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder> {
    private Movie[] movies;

    private MovieListAdapterOnClickHandler adapterOnClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieListAdapterOnClickHandler {
        void onClick(Movie clickedMovie);
    }

    public MovieListAdapter(MovieListAdapterOnClickHandler onClickHandler) {
        this.adapterOnClickHandler = onClickHandler;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForMoviePosterItem = R.layout.movie_poster_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForMoviePosterItem, viewGroup, false);
        return new MoviePosterViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        String relativePosterPath = movies[position].getRelativePosterPath();
        holder.bind(relativePosterPath);
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        }
        return movies.length;
    }

    /**
     * Cache of the children views for a Movie Poster item.
     */
    class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView moviePoster;

        Context context;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link MovieListAdapter#onCreateViewHolder(ViewGroup, int)}
         * @param context
         */
        public MoviePosterViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            moviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         *
         * @param relativePosterPath Position of the item in the list
         */
        void bind(String relativePosterPath) {

            String posterURL = NetworkUtils.buildUrlForImage(relativePosterPath).toString();
            Picasso.with(context).load(URLDecoder.decode(posterURL)).into(moviePoster);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies[adapterPosition];
            adapterOnClickHandler.onClick(movie);
        }
    }

    public void setMovies(Movie[] updatedMovies) {
        this.movies = updatedMovies;
        notifyDataSetChanged();
    }
}
