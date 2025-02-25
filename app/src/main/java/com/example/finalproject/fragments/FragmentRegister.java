package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRegister#newInstance} factory method to
 * create an instance of this fragment.
 */

// fragment for registering a new user

public class FragmentRegister extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;


    public FragmentRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRegister newInstance(String param1, String param2) {
        FragmentRegister fragment = new FragmentRegister();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // get references to UI elements
        Button button1 = view.findViewById(R.id.registrationButton);
        EditText firstEmailEditText = view.findViewById(R.id.enterEmailReg);
        EditText passwordEditText = view.findViewById(R.id.enterPasswordReg);
        EditText userNameEditText = view.findViewById(R.id.enterUserNameReg);
        EditText phoneEditText = view.findViewById(R.id.enterPhoneReg);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // retrieve the input values
                String firstEmail = firstEmailEditText.getText().toString().trim();
                String firstPassword = passwordEditText.getText().toString().trim();
                String userName = userNameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                // validation: Check if any field is empty
                if (firstEmail.isEmpty() || firstPassword.isEmpty() || userName.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                // get a reference to the Firebase Realtime Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users").child(userName);

                // check if the username already exists in the database
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // username is taken
                            Toast.makeText(getContext(), "Username is already taken, please choose another", Toast.LENGTH_LONG).show();
                        } else {
                            // username is available, proceed with registration
                            mAuth.createUserWithEmailAndPassword(firstEmail, firstPassword)
                                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // User created successfully, add to database
                                                Customer c = new Customer(userName, phone);
                                                myRef.setValue(c).addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                                        Navigation.findNavController(view).navigate(R.id.action_fragmentRegister_to_fragmentHome);
                                                    } else {
                                                        Toast.makeText(getContext(), "Error saving data", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getContext(), "Registration Unsuccessful", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors
                        Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;


    }
}