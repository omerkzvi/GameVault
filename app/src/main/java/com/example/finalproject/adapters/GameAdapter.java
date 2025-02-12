package com.example.finalproject.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> gameList;

    public GameAdapter(List<Game> gameList) {
        this.gameList = gameList;
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
        holder.title.setText(game.getTitle());
        holder.genre.setText(game.getGenre());
        holder.publisher.setText(game.getPublisher());
        holder.releaseDate.setText(game.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView title, genre, publisher, releaseDate;

        public GameViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.gameTitle);
            genre = itemView.findViewById(R.id.gameGenre);
            publisher = itemView.findViewById(R.id.gamePublisher);
            releaseDate = itemView.findViewById(R.id.gameReleaseDate);
        }
    }
}
