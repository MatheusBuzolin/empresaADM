package com.mbappsoftware.aprotadm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
import com.mbappsoftware.aprotadm.helper.UsuarioFirebase;
import com.mbappsoftware.aprotadm.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email, senha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponentes();
    }

    private void iniciarComponentes(){

        email = findViewById(R.id.login_et_email);
        senha = findViewById(R.id.login_et_senha);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //verificarUsuarioLogado();
        //deslogarUsuario();
    }

    public void validaLogin(View view) {
        String textEmail = email.getText().toString().trim();
        String textSenha = senha.getText().toString();

        if(!textEmail.isEmpty()){
            if (!textSenha.isEmpty()){

                Usuario usuario = new Usuario();
                usuario.setEmail(textEmail);
                usuario.setSenha(textSenha);
                validarLogin(usuario);

            }else{
                senha.setError("Digite uma senha!!");
            }
        }else{
            email.setError("Digite seu email");
        }
    }

    private void validarLogin(Usuario usuario) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "AGUARDE!!", Toast.LENGTH_SHORT).show();

                    FirebaseFirestore db = ConfiguracaoFirebase.getfirebaseFirestore();
                    db.collection("funcionario")
                            .document(UsuarioFirebase.getIdUsuario())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        Usuario usuario = documentSnapshot.toObject(Usuario.class);

                                        String tipoUsuario = usuario.getTipoDoFuncionario();

                                        if (tipoUsuario.equals(Usuario.TIPO_ADM)){
                                            Toast.makeText(LoginActivity.this, "Bem vindo ", Toast.LENGTH_SHORT).show();
                                            Log.i("tipoUsuario", "tipo do user: " + tipoUsuario );
                                            abrirTelaHome(usuario);

                                        }else{
                                            deslogarUsuario();
                                            senha.setText("");
                                            Toast.makeText(LoginActivity.this, "Você não é cadastrado como passageiro, faça o cadastro", Toast.LENGTH_SHORT).show();
                                        }/*else if (tipoUsuario.equals("B"))
                                        USUARIO BLOQUEADO FAZER TEXTO PARA ABRIR NUMERO DA CENTRAL PARA SABER MOTIVO */
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Você não é cadastrado como passageiro, faça o cadastro", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email e senha nao correspondem a um usuario cadastrado!";
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuario nao esta cadastrado";
                    }catch (Exception e){
                        excecao = " Erro ao cadastrar usuario: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void abrirTelaHome(Usuario usuario){
        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
        //i.putExtra("funcionario", usuario);
        startActivity(i);
        finish();
        //startActivity(new Intent(this, MercadoPagoActivity.class));
    }

    public void abrirTelaCadastro(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void abrirTelaRecuperarSenha(View view) {
        startActivity(new Intent(this, EsqueceuSenhaActivity.class));
    }

    private void deslogarUsuario(){
        try{
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}