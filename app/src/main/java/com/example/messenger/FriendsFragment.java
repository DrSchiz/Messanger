package com.example.messenger;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    ArrayList<User> users;
    RecyclerView friends;
    SearchView search;
    Button all, mine;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        users = new ArrayList<>();
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    RecyclerAdapter adapter = new RecyclerAdapter(getContext(), users);
                    friends.setLayoutManager(new LinearLayoutManager(getContext()));
                    friends.setAdapter(adapter);
                    friends.setHasFixedSize(true);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = new User(
                                document.getString("email"),
                                document.getString("key"),
                                document.getString("firstname"),
                                document.getString("name"),
                                document.getString("nickname"),
                                document.getString("image")
                        );
                        users.add(user);
                        Log.d("sidhf0", "" + user.getKey());

                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friends = view.findViewById(R.id.rvFriends);
        search = view.findViewById(R.id.svSearch);
        all = view.findViewById(R.id.btAll);
        mine = view.findViewById(R.id.btMine);
    }
}