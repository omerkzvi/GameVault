package com.example.finalproject.adapters;

import android.content.Context;
import android.util.Log;
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

        // שם המשחק (תמיד קיים)
        holder.gameTitle.setText(game.getTitle());

        // ז'אנר - אם אין ז'אנר, יציג "Unknown Genre"
        String genre = (game.getFirstGenre() != null && !game.getFirstGenre().isEmpty()) ? game.getFirstGenre() : "Unknown Genre";
        holder.gameGenre.setText("Genre: " + genre);

        // דירוג - אם אין דירוג, יציג "Unknown"
        String rating = (game.getRating() > 0) ? String.valueOf(game.getRating()) : "Unknown Rating";
        holder.gameRating.setText("Rating: " + rating);

        // תאריך יציאה - אם אין תאריך, יציג "Unknown"
        String releaseDate = (game.getReleaseDate() != null && !game.getReleaseDate().isEmpty()) ? game.getReleaseDate() : "Unknown Release Date ";
        holder.gameReleaseDate.setText("Release Date: " + releaseDate);

        // טעינת תמונת המשחק עם Glide
        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(game.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background) // תמונה זמנית בזמן טעינה
                    .error(R.drawable.ic_launcher_background) // תמונה במקרה של שגיאה
                    .into(holder.gameImage);
        } else {
            holder.gameImage.setImageResource(R.drawable.ic_launcher_background);
        }
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
        TextView gameTitle, gameGenre, gameRating, gameReleaseDate;
        ImageView gameImage;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameGenre = itemView.findViewById(R.id.gameGenre);
            gameRating = itemView.findViewById(R.id.gameRating);
            gameReleaseDate = itemView.findViewById(R.id.gameReleaseDate);
            gameImage = itemView.findViewById(R.id.gameImage);
        }
    }
}