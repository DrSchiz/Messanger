package com.example.messenger;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    TextView nickname;
    CircleImageView avatar;
    EditText etEmail, etFirstname, etName, etNickname;
    Button edit, editImage;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    public String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nickname = view.findViewById(R.id.tvNickname);
        avatar = view.findViewById(R.id.ivAvatar);
        etEmail = view.findViewById(R.id.etEmail);
        etFirstname = view.findViewById(R.id.etFirstname);
        etName = view.findViewById(R.id.etName);
        etNickname = view.findViewById(R.id.etNickname);
        edit = view.findViewById(R.id.btEdit);
        editImage = view.findViewById(R.id.btEditImage);

        SharedPreferences myPrefs = this.getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        email = myPrefs.getString("email", "");

        firebaseFirestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            User user = documentSnapshot.toObject(User.class);

                            nickname.setText(user.getNickname());
                            etEmail.setText(user.getEmail());
                            etFirstname.setText(user.getFirstname());
                            etName.setText(user.getName());
                            etNickname.setText(user.getNickname());

                            storageReference = FirebaseStorage.getInstance().getReference("users/"+user.getImage()+".jpg");
                            try {
                                File localFile = File.createTempFile("tempfile", ".jpg");
                                storageReference.getFile(localFile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                avatar.setImageBitmap(bitmap);
                                            }
                                        });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AvatarActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.e("meow", "ыыы");
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentID = documentSnapshot.getId();
                                    firebaseFirestore.collection("users")
                                            .document(documentID)
                                            .update(
                                                    "email", etEmail.getText().toString().trim(),
                                                    "firstname", etFirstname.getText().toString().trim(),
                                                    "name", etName.getText().toString().trim(),
                                                    "nickname", etNickname.getText().toString().trim()
                                            )
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Intent intent = new Intent(getActivity(), AuthorizationActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                }
                            }
                        });
            }
        });
    }
}