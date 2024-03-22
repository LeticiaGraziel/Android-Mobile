package com.example.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister, buttonResetPassword, buttonLogout; // Adicionando o botão de logout
    private TextView textLoginStatus, textToastMessage; // Adicionando o TextView para mensagens Toast
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        buttonLogout = findViewById(R.id.buttonLogout); // Inicializando o botão de logout
        textLoginStatus = findViewById(R.id.text_login_status);
        textToastMessage = findViewById(R.id.text_toast_message); // Inicializando o TextView para mensagens Toast
        firebaseAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                loginUser(email, password);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                registerUser(email, password);
            }
        });

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                resetPassword(email);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            textLoginStatus.setText("Você está logado como: " + user.getEmail());
            buttonLogout.setVisibility(View.VISIBLE); // Torna o botão de logout visível se o usuário estiver logado
        } else {
            textLoginStatus.setText("Você está deslogado");
            buttonLogout.setVisibility(View.GONE); // Torna o botão de logout invisível se o usuário estiver deslogado
        }
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                            textToastMessage.setText("Login bem-sucedido!");
                            Toast.makeText(MainActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                        } else {
                            updateUI(null);
                            textToastMessage.setText("Falha no login. Verifique suas credenciais.");
                            Toast.makeText(MainActivity.this, "Falha no login. Verifique suas credenciais.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                            textToastMessage.setText("Usuário registrado com sucesso!");
                            Toast.makeText(MainActivity.this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            updateUI(null);
                            textToastMessage.setText("Falha ao registrar. Tente novamente mais tarde.");
                            Toast.makeText(MainActivity.this, "Falha ao registrar. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            textToastMessage.setText("E-mail de redefinição de senha enviado com sucesso!");
                            Toast.makeText(MainActivity.this, "E-mail de redefinição de senha enviado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            textToastMessage.setText("Falha ao enviar e-mail de redefinição de senha.");
                            Toast.makeText(MainActivity.this, "Falha ao enviar e-mail de redefinição de senha.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        updateUI(null);
        textToastMessage.setText("Logout bem-sucedido!");
        Toast.makeText(MainActivity.this, "Logout bem-sucedido!", Toast.LENGTH_SHORT).show();
    }
}
