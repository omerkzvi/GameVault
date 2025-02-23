package com.example.finalproject.adapters;

import android.content.Context;
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
        holder.gamePublisher.setText(game.getDeveloper());
        holder.gameReleaseDate.setText(game.getReleaseDate());

        // טעינת תמונת המשחק עם Glide
        Glide.with(context)
                .load(game.getImageUrl()) // URL של התמונה מה-API
                .into(holder.gameImage);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public void updateList(List<Game> newList) {
        gameList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle, gameGenre, gamePublisher, gameReleaseDate;
        ImageView gameImage;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameGenre = itemView.findViewById(R.id.gameGenre);
            gamePublisher = itemView.findViewById(R.id.gamePublisher);
            gameReleaseDate = itemView.findViewById(R.id.gameReleaseDate);
            gameImage = itemView.findViewById(R.id.gameImage); // מקשרים לתמונה
        }
    }
}
