package com.example.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Game;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> gameList;
    private Context context;

    public GameAdapter(List<Game> gameList, Context context) {
        this.gameList = gameList;
        this.context = context;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.gameTitle.setText(game.getTitle());
        holder.gameGenre.setText(game.getGenre());
        holder.gamePublisher.setText(game.getPublisher());
        holder.gameReleaseDate.setText(game.getReleaseDate());

        // מאזין לכפתור Play
        holder.playTrailerButton.setOnClickListener(v -> {
            String trailerUrl = game.getTrailerUrl();
            if (trailerUrl != null && !trailerUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public void updateList(List<Game> newList) {
        gameList = new ArrayList<>(newList); // מעדכן את הרשימה
        notifyDataSetChanged(); // מעדכן את ה-RecyclerView
    }
    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle, gameGenre, gamePublisher, gameReleaseDate;
        ImageButton playTrailerButton;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameGenre = itemView.findViewById(R.id.gameGenre);
            gamePublisher = itemView.findViewById(R.id.gamePublisher);
            gameReleaseDate = itemView.findViewById(R.id.gameReleaseDate);
            playTrailerButton = itemView.findViewById(R.id.playButtonTr);
        }
    }
}