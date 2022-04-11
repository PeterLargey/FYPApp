package com.example.fypapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateStaffFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View createView = inflater.inflate(R.layout.fragment_create_user, container, false);
        mAuth = FirebaseAuth.getInstance();
        String[] types = new String[2];
        types[0] = "server";
        types[1] = "host";

        ArrayAdapter<String> adapter;


        AutoCompleteTextView roleInput = createView.findViewById(R.id.newStaffType);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.itemtype_dropdown_item, types);
        roleInput.setAdapter(adapter);

        roleInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(view.getContext(), "Type Selected " + item, Toast.LENGTH_LONG).show();
            }
        });


        EditText usernameInput = createView.findViewById(R.id.regStaffUsername);
        EditText passwordInput = createView.findViewById(R.id.regStaffPassword);
        Button button = createView.findViewById(R.id.createStaffAccount);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roleString = roleInput.getText().toString();
                String usernameString = usernameInput.getText().toString();
                String passwordString = passwordInput.getText().toString();

                if(roleString.isEmpty() || usernameString.isEmpty() || passwordString.isEmpty()){
                    Toast.makeText(view.getContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else if(passwordString.length() < 6){
                    passwordInput.setError("Please enter at least 6 characters");
                } else {
                    mAuth.createUserWithEmailAndPassword(usernameString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                double wage = 0.00;
                                if(roleString.equalsIgnoreCase("server")){
                                    wage = 14.50;
                                }
                                if(roleString.equalsIgnoreCase("host")){
                                    wage = 11.00;
                                }
                                inputDataIntoStaff(usernameString, roleString, passwordString, wage);
                                Toast.makeText(view.getContext(), "New staff member created", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(view.getContext(), "An error occurred, New staff member was not created", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });

        return createView;
    }

    public void inputDataIntoStaff(String username, String role, String password, double wage){
        String TAG = "TAG";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Staff").document();
        Map<String, Object> newStaff = new HashMap<>();
        newStaff.put("role", role);
        newStaff.put("username", username);
        newStaff.put("password", password);
        newStaff.put("wage", wage);

        docRef.set(newStaff).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, new staff member added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, new staff member was not added to the database");
            }
        });

    }

}