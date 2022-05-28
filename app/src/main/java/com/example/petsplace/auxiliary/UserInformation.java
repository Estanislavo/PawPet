package com.example.petsplace.auxiliary;

import com.example.petsplace.activities.Registration;
import com.google.firebase.auth.FirebaseAuth;

public class UserInformation {

    public static String username = Registration.username;
    public static String addUsername = "none";
    public static String phone;
    public static String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
}
