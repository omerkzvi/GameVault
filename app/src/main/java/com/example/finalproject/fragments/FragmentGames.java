package com.example.finalproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.adapters.GameAdapter;
import com.example.finalproject.models.Game;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FragmentGames extends Fragment {

    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private List<Game> gameList;
    private DatabaseReference databaseReference;

    public FragmentGames() {
        // Required empty public constructor
    }

    public static FragmentGames newInstance(String param1, String param2) {
        FragmentGames fragment = new FragmentGames();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameList = new ArrayList<>();
        gameAdapter = new GameAdapter(gameList);
        recyclerView.setAdapter(gameAdapter);

        // קריאת נתונים מ-Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("VideoGames");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameList.clear();
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    if (game != null) {
                        gameList.add(game);
                    }
                }
                gameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        return view;
    }
}
