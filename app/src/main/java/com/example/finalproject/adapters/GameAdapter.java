package com.example.finalproject.adapters;

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
    private List<Game> gameList;
    private Context context;

    // Constructor
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

        // הצגת מידע בסיסי
        holder.gameTitle.setText(game.getTitle());
        holder.gameGenre.setText("Genre: " + game.getFirstGenre());
        holder.gameRating.setText("Rating: " + (game.getRating() > 0 ? String.valueOf(game.getRating()) : "Unknown Rating"));
        holder.gameReleaseDate.setText("Release Date: " + game.getReleaseDate());
        // טעינת תמונה
        Glide.with(context)
                .load(game.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.gameImage);

        // לחיצה על כל הכרטיס תפתח/תסגור את הפרטים הנוספים
        holder.itemView.setOnClickListener(v -> {
            if (holder.expandableView.getVisibility() == View.VISIBLE) {
                holder.expandableView.setVisibility(View.GONE);
            } else {
                holder.expandableView.setVisibility(View.VISIBLE);
            }
        });

        // פתיחת הטריילר בלחיצה
        holder.gameTrailerLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(game.getTrailerLink()));
            context.startActivity(browserIntent);
        });
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
        TextView gameTitle, gameGenre, gameRating, gameReleaseDate, gameTrailerLink;
        ImageView gameImage;
        View expandableView;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameGenre = itemView.findViewById(R.id.gameGenre);
            gameRating = itemView.findViewById(R.id.gameRating);
            gameReleaseDate = itemView.findViewById(R.id.gameReleaseDate);
            gameTrailerLink = itemView.findViewById(R.id.gameTrailerLink);
            gameImage = itemView.findViewById(R.id.gameImage);
            expandableView = itemView.findViewById(R.id.expandableView); // האזור שמוסתר כברירת מחדל
        }
    }
}
