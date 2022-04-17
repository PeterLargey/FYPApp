package com.example.fypapplication;

import android.content.Intent;
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
        EditText nameInput = createView.findViewById(R.id.regStaffName);
        EditText contactInput = createView.findViewById(R.id.regStaffContact);
        Button button = createView.findViewById(R.id.createStaffAccount);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roleString = roleInput.getText().toString();
                String usernameString = usernameInput.getText().toString();
                String passwordString = passwordInput.getText().toString();
                String nameString = nameInput.getText().toString();
                String contactString = contactInput.getText().toString();

                if(roleString.isEmpty() || usernameString.isEmpty() || passwordString.isEmpty() || nameString.isEmpty() || contactString.isEmpty()){
                    Toast.makeText(view.getContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else if(passwordString.length() < 6){
                    passwordInput.setError("Please enter at least 6 characters");
                } else {
                    mAuth.createUserWithEmailAndPassword(usernameString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("TAG", "Auth Id " + String.valueOf(mAuth.getUid()));
                                String authId = mAuth.getUid();
                                String wage = "";
                                if(roleString.equalsIgnoreCase("server")){
                                    wage = "14.50";
                                } else {
                                    wage = "11.00";
                                }
                                inputDataIntoStaff(usernameString, roleString, passwordString, nameString, contactString, wage, authId);
                                Toast.makeText(view.getContext(), "New staff member created", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(view.getContext(), ManagerMain.class);
                                view.getContext().startActivity(i);
                            } else {
                                Log.d("TAG", String.valueOf(task.getException()));
                                Toast.makeText(view.getContext(), "An error occurred, New staff member was not created", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });

        return createView;
    }

    public void inputDataIntoStaff(String username, String role, String password, String name, String contact, String wage, String authId){
        String TAG = "TAG";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Staff").document(authId);
        Map<String, Object> newStaff = new HashMap<>();
        newStaff.put("role", role);
        newStaff.put("username", username);
        newStaff.put("password", password);
        newStaff.put("fullName", name);
        newStaff.put("phoneNum", contact);
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