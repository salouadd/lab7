package com.example.starsgallery.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.starsgallery.R;
import com.example.starsgallery.beans.Star;
import com.example.starsgallery.service.StarService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {

    private List<Star> stars;
    private List<Star> starsFilter;
    private Context context;
    private NewFilter mfilter;
    private int lastPosition = -1;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;
        this.stars = stars;
        this.starsFilter = new ArrayList<>(stars);
        this.mfilter = new NewFilter(this);
    }

    public void sortByRatingAsc() {
        Collections.sort(starsFilter, (s1, s2) -> Float.compare(s1.getStar(), s2.getStar()));
        notifyDataSetChanged();
    }

    public void sortByRatingDesc() {
        Collections.sort(starsFilter, (s1, s2) -> Float.compare(s2.getStar(), s1.getStar()));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.star_item, viewGroup, false);
        final StarViewHolder holder = new StarViewHolder(v);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                Star selectedStar = starsFilter.get(position);

                View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null, false);
                final ImageView img = popup.findViewById(R.id.img);
                final RatingBar bar = popup.findViewById(R.id.ratingBar);
                final TextView idss = popup.findViewById(R.id.idss);
                final TextView starName = popup.findViewById(R.id.starName);
                final TextView starDescription = popup.findViewById(R.id.starDescription);
                final TextView starFilmography = popup.findViewById(R.id.starFilmography);

                Glide.with(context)
                        .load(selectedStar.getImg())
                        .placeholder(android.R.drawable.btn_star_big_on)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(img);
                
                idss.setText(String.valueOf(selectedStar.getId()));
                starName.setText(selectedStar.getName());
                starDescription.setText(selectedStar.getDescription());
                starFilmography.setText(selectedStar.getFilmography());
                bar.setRating(selectedStar.getStar());

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Détails : " + selectedStar.getName())
                        .setView(popup)
                        .setPositiveButton("Sauvegarder la note", (dialog1, which) -> {
                            float s = bar.getRating();
                            int ids = Integer.parseInt(idss.getText().toString());
                            Star star = StarService.getInstance().findById(ids);
                            if (star != null) {
                                star.setStar(s);
                                StarService.getInstance().update(star);
                                notifyItemChanged(holder.getAdapterPosition());
                                Toast.makeText(context, "Note mise à jour avec succès", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Fermer", null)
                        .create();
                dialog.show();
            }
        });

        holder.btn_delete.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Star starToRemove = starsFilter.get(pos);
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Supprimer " + starToRemove.getName() + " de la galerie ?")
                        .setPositiveButton("Supprimer", (d, w) -> {
                            StarService.getInstance().delete(starToRemove);
                            starsFilter.remove(pos);
                            stars.remove(starToRemove);
                            notifyItemRemoved(pos);
                            Toast.makeText(context, "Supprimé", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        Star currentStar = starsFilter.get(position);
        
        Glide.with(context)
                .asBitmap()
                .load(currentStar.getImg())
                .placeholder(android.R.drawable.btn_star_big_on)
                .apply(new RequestOptions().circleCrop().override(200, 200))
                .into(holder.img);

        holder.name.setText(currentStar.getName());
        holder.stars.setRating(currentStar.getStar());
        holder.idss.setText(String.valueOf(currentStar.getId()));
        holder.ratingValue.setText(String.format("%.1f / 5", currentStar.getStar()));
        
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            animation.setDuration(500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return starsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    public class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss, name, ratingValue;
        ImageView img, btn_delete;
        RatingBar stars;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
            ratingValue = itemView.findViewById(R.id.ratingValue);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public class NewFilter extends Filter {
        public RecyclerView.Adapter mAdapter;

        public NewFilter(RecyclerView.Adapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filtered = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filtered.addAll(stars);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Star p : stars) {
                    if (p.getName().toLowerCase().startsWith(filterPattern)) {
                        filtered.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            results.count = filtered.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter = (List<Star>) filterResults.values;
            mAdapter.notifyDataSetChanged();
        }
    }
}
