package com.example.finalproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.models.Game;
import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> gameList; // list to hold game data
    private Context context; // context to access resources and start new activities

    // Constructor
    public GameAdapter(List<Game> gameList, Context context) {
        this.gameList = gameList;
        this.context = context;
    }

    // called when a new ViewHolder is created. It inflates the layout for each item in the RecyclerView.
    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    // called to bind data to the ViewHolder for each item
    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        if (game == null) return; // הגנה מפני ערכים חסרים

        holder.gameTitle.setText(game.getTitle() != null ? game.getTitle() : "Unknown Title");
        holder.gameGenre.setText("Genre: " + (!game.getFirstGenre().isEmpty() ? game.getFirstGenre() : "Unknown Genre"));
        holder.gameRating.setText("Rating: " + (game.getRating() > 0 ? String.valueOf(game.getRating()) : "Unknown Rating"));
        holder.gameReleaseDate.setText("Release Date: " + (!game.getReleaseDate().isEmpty() ? game.getReleaseDate() : "Unknown Date"));
        holder.gamePlatform.setText("Platform: " + (!game.getPlatform().isEmpty() ? game.getPlatform() : "Unknown Platform"));

        // טעינת תמונה בצורה בטוחה
        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(game.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.gameImage);
        } else {
            holder.gameImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // הצגת פרטי משחק בלחיצה
        holder.itemView.setOnClickListener(v -> {
            if (holder.expandableView.getVisibility() == View.VISIBLE) {
                holder.expandableView.setVisibility(View.GONE);
            } else {
                holder.expandableView.setVisibility(View.VISIBLE);
            }
        });

        // פתיחת הטריילר רק אם יש קישור תקף
        if (game.getTrailerLink() != null && !game.getTrailerLink().isEmpty()) {
            holder.gameTrailerLink.setVisibility(View.VISIBLE);
            holder.gameTrailerLink.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(game.getTrailerLink()));
                context.startActivity(browserIntent);
            });
        } else {
            holder.gameTrailerLink.setVisibility(View.GONE);
        }
    }


    // return the total number of items in the list
    @Override
    public int getItemCount() {
        return gameList.size();
    }

    // method to update the list of games and notify the adapter to refresh the view
    public void updateList(List<Game> newList) {
        gameList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    // ViewHolder class that holds references to the views in each item layout
    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle, gameGenre, gameRating, gameReleaseDate, gamePlatform, gameTrailerLink;
        ImageView gameImage;
        View expandableView; // view containing additional details that can be expanded or collapsed


        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize the views in the item layout
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameGenre = itemView.findViewById(R.id.gameGenre);
            gameRating = itemView.findViewById(R.id.gameRating);
            gameReleaseDate = itemView.findViewById(R.id.gameReleaseDate);
            gameTrailerLink = itemView.findViewById(R.id.gameTrailerLink);
            gameImage = itemView.findViewById(R.id.gameImage);
            gamePlatform = itemView.findViewById(R.id.gamePlatform);
            expandableView = itemView.findViewById(R.id.expandableView);
        }
    }

}
