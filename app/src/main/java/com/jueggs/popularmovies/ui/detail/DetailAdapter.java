package com.jueggs.popularmovies.ui.detail;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.MovieDbContract;
import com.jueggs.popularmovies.model.Review;
import com.jueggs.popularmovies.model.Trailer;

import java.util.List;
import java.util.Locale;

import static com.jueggs.popularmovies.util.Utils.isEmpty;

public class DetailAdapter
{
    private List<Trailer> trailers;
    private List<Review> reviews;
    private ViewGroup container;
    private Context context;

    public DetailAdapter(ViewGroup container, Context context, List<Trailer> trailers, List<Review> reviews)
    {
        this.container = container;
        this.context = context;
        this.trailers = trailers;
        this.reviews = reviews;
    }

    public void createViews()
    {
        LayoutInflater inflater = null;
        container.removeAllViews();
        if (!isEmpty(trailers))
        {
            inflater = LayoutInflater.from(context);
            container.addView(inflater.inflate(R.layout.distinct_divider, container, false));
            container.addView(inflater.inflate(R.layout.trailerlist_header, container, false));

            int size = trailers.size();
            for (int i = 0; i < size; i++)
            {
                View view = inflater.inflate(R.layout.listitem_trailer, container, false);
                bindTrailerView(view, trailers.get(i));
                container.addView(view);
                if (i < size - 1)
                    container.addView(inflater.inflate(R.layout.vague_divider, container, false));
            }
        }

        if (!isEmpty(reviews))
        {
            if (inflater == null)
                inflater = LayoutInflater.from(context);

            container.addView(inflater.inflate(R.layout.distinct_divider, container, false));
            container.addView(inflater.inflate(R.layout.reviewlist_header, container, false));

            int size = reviews.size();
            for (int i = 0; i < size; i++)
            {
                View view = inflater.inflate(R.layout.listitem_review, container, false);
                bindReviewView(view, reviews.get(i));
                container.addView(view);
                if (i < size - 1)
                    container.addView(inflater.inflate(R.layout.vague_divider, container, false));
            }
        }
    }

    private void bindTrailerView(View view, Trailer trailer)
    {
        view.findViewById(R.id.play).setOnClickListener(createOnClickListener(trailer.getKey()));
        ((TextView) view.findViewById(R.id.name)).setText(trailer.getName());
        ((TextView) view.findViewById(R.id.size)).setText(String.format(context.getString(R.string.format_trailer_size), trailer.getSize()));
        String language = new Locale(trailer.getLanguage(), trailer.getRegion()).getDisplayName();
        ((TextView) view.findViewById(R.id.language)).setText(language);
    }

    private View.OnClickListener createOnClickListener(final String key)
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(new Intent(Intent.ACTION_VIEW).setData(MovieDbContract.createYoutubeUri(key)));
            }
        };
    }

    private void bindReviewView(View view, Review review)
    {
        ((TextView) view.findViewById(R.id.author)).setText(String.format(context.getString(R.string.format_review_author), review.getAuthor()));
        ((TextView) view.findViewById(R.id.review)).setText(review.getReview());
    }
}
