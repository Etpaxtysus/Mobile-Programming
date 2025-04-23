package com.example.myloginpage

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isRememberMeChecked by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.keqingsticker), contentDescription = "login image",
                modifier = Modifier.size(300.dp))

            Text(text = "Welcome Back Traveler !", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Login to your account")

            Spacer(modifier = Modifier.height(16.dp))

            // Email input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches() // Validate email
                },
                label = { Text(text = "Email Address") },
                isError = !isEmailValid,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            if (!isEmailValid) {
                Text(text = "Please enter a valid email", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordValid = it.length >= 6 // Validate password length
                },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = !isPasswordValid,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            if (!isPasswordValid) {
                Text(text = "Password must be at least 6 characters", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Remember Me checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Checkbox(
                    checked = isRememberMeChecked,
                    onCheckedChange = { isRememberMeChecked = it }
                )
                Text(text = "Remember Me", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isEmailValid && isPasswordValid) {
                        Log.i("Credential", "Email: $email, Password: $password, Remember Me: $isRememberMeChecked")
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Forgot Password?", modifier = Modifier.clickable { /* Navigate to forgot password */ })

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Or Sign In With")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SocialMediaButton(R.drawable.fb, "Facebook") {
                    Log.i("Login Method", "Facebook Login")
                }
                SocialMediaButton(R.drawable.google, "Google") {
                    Log.i("Login Method", "Google Login")
                }
                SocialMediaButton(R.drawable.twitter, "Twitter") {
                    Log.i("Login Method", "Twitter Login")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button
            Text(
                text = "Don't have an account? Sign Up",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    Log.i("Login", "Navigate to Sign Up screen")
                }
            )
        }
    }
}

@Composable
fun SocialMediaButton(iconId: Int, description: String, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = description,
        modifier = Modifier
            .size(60.dp)
            .clickable(onClick = onClick)
    )
}
