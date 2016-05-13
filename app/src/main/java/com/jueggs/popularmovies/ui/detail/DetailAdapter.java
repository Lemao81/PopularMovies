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

import static com.jueggs.popularmovies.data.MovieDbContract.*;
import static com.jueggs.popularmovies.util.Utils.isEmpty;

public class DetailAdapter
{
    private List<Trailer> trailers;
    private List<Review> reviews;
    private ViewGroup container;
    private Context context;
    private LayoutInflater inflater;

    public DetailAdapter(ViewGroup container, Context context, List<Trailer> trailers, List<Review> reviews)
    {
        this.container = container;
        this.context = context;
        this.trailers = trailers;
        this.reviews = reviews;
    }

    public void createViews()
    {
        container.removeAllViews();

        new ListCreator<Trailer>(trailers, R.string.trailer_header, R.layout.listitem_trailer)
        {
            @Override
            void bindView(View view, final Trailer trailer)
            {
                view.findViewById(R.id.play).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(createYoutubeUri(trailer.getKey())));
                    }
                });
                ((TextView) view.findViewById(R.id.name)).setText(trailer.getName());
                String size = String.format(context.getString(R.string.format_trailer_size), trailer.getSize());
                ((TextView) view.findViewById(R.id.size)).setText(size);
                String language = new Locale(trailer.getLanguage(), trailer.getRegion()).getDisplayName();
                ((TextView) view.findViewById(R.id.language)).setText(language);
            }
        }.createList();

        new ListCreator<Review>(reviews, R.string.review_header, R.layout.listitem_review)
        {
            @Override
            void bindView(View view, Review review)
            {
                String author = String.format(context.getString(R.string.format_review_author), review.getAuthor());
                ((TextView) view.findViewById(R.id.author)).setText(author);
                ((TextView) view.findViewById(R.id.review)).setText(review.getReview());
            }
        }.createList();
    }

    abstract class ListCreator<T>
    {
        private List<T> items;
        private int resHeaderId;
        private int resItemId;

        abstract void bindView(View view, T item);

        public ListCreator(List<T> items, int resHeaderId, int resItemId)
        {
            this.items = items;
            this.resHeaderId = resHeaderId;
            this.resItemId = resItemId;
        }

        public void createList()
        {
            if (!isEmpty(items))
            {
                if (inflater == null)
                    inflater = LayoutInflater.from(context);

                container.addView(inflater.inflate(R.layout.divider_distinct, container, false));
                View header = inflater.inflate(R.layout.header_trailerreview_list, container, false);
                ((TextView) header.findViewById(R.id.header)).setText(context.getString(resHeaderId));
                container.addView(header);

                int size = items.size();
                for (int i = 0; i < size; i++)
                {
                    View view = inflater.inflate(resItemId, container, false);
                    bindView(view, items.get(i));
                    container.addView(view);
                    if (i < size - 1)
                        container.addView(inflater.inflate(R.layout.divider_vague, container, false));
                }
            }
        }
    }
}
