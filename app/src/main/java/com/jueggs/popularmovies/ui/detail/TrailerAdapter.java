package com.jueggs.popularmovies.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer>
{
    public TrailerAdapter(Context context, int resource, List<Trailer> objects)
    {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Trailer trailer = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_trailer, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.play.setImageResource(R.drawable.ic_vector_play);
        holder.name.setText(trailer.getName());
        holder.size.setText(String.format(getContext().getString(R.string.format_trailer_size), trailer.getSize()));

        return convertView;
    }

    static class ViewHolder
    {
        @Bind(R.id.play) ImageButton play;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.size) TextView size;
        @Bind(R.id.language) TextView language;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
