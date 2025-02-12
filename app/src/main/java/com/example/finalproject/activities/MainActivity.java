package com.example.finalproject.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.Navigation;

import com.example.finalproject.R;
import com.example.finalproject.models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

    }

    public void reg(View view) {
        String email = ((EditText) findViewById(R.id.enterEmailReg)).getText().toString();
        String password = ((EditText) findViewById(R.id.enterPasswordReg)).getText().toString();
        String userName = ((EditText) findViewById(R.id.enterUserNameReg)).getText().toString();

        if (email.isEmpty() || password.isEmpty() || userName.isEmpty()) {

            // Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_fragmentRegister_to_fragmentHome);
                            addData();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Registration Unsuccessful", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
    public void login(View view) {
        String email = ((EditText)findViewById(R.id.enterEmailButton)).getText().toString();
        String password = ((EditText)findViewById(R.id.enterPasswordButton)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_fragmentHome_to_fragmentGames);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    public void addData(){

        String phone = ((EditText) findViewById(R.id.enterPhoneReg)).getText().toString();
        String userName = ((EditText) findViewById(R.id.enterUserNameReg)).getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userName);

        Customer c = new Customer(userName,phone);
        myRef.setValue(c);
    }


}