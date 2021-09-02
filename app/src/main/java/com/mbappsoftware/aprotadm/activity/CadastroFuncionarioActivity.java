package com.mbappsoftware.aprotadm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.textfield.TextInputEditText;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.util.UUID;

public class CadastroFuncionarioActivity extends AppCompatActivity {

    private TextInputEditText nome, email, senha, confirmaSenha, tipoFuncionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_funcionario);

        iniciaComponentes();
    }

    public void cadastrarFunc(View view) {
        String idUsuario = UUID.randomUUID().toString();
        String textNome = nome.getText().toString().trim().trim();
        String textEmail = email.getText().toString().trim().trim();
        String textSenha = senha.getText().toString().trim().trim();
        String textTipoFunci = tipoFuncionario.getText().toString().trim().toUpperCase();
        String textConfirmarSenha = confirmaSenha.getText().toString().trim();

        if (!textNome.isEmpty()){
            if (!textEmail.isEmpty()){
                if (!textSenha.isEmpty() && textSenha.length() > 5){
                    if (textConfirmarSenha.equals(textSenha) ) {
                        if (!textTipoFunci.isEmpty() && (textTipoFunci.equals("C") || textTipoFunci.equals("P") || textTipoFunci.equals("A"))) {

                            Usuario usuario = new Usuario();
                            usuario.setUidTemporario(idUsuario);
                            usuario.setNome(textNome);
                            usuario.setNomePesquisa(textNome.toUpperCase());
                            usuario.setEmail(textEmail);
                            usuario.setSenha(textSenha);
                            usuario.setQtdNotas(0);
                            if (textTipoFunci.equals("C")){
                                usuario.setTipoDoFuncionario(Usuario.TIPO_CAMPO);
                            } else if (textTipoFunci.equals("A")){
                                usuario.setTipoDoFuncionario(Usuario.TIPO_ADM);
                            }else{
                                usuario.setTipoDoFuncionario(Usuario.TIPO_PROJETO);
                            }

                            usuario.salvarFirestoreUsuarioTemporario(usuario);

                            Toast.makeText(this, "Cadastrado com sucesso!!", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        confirmaSenha.setError("Digite senhas iguais");
                    }
                }else{
                    senha.setError("Digite uma senha maior que 6 numeros ou letras");
                }
            }else{
                email.setError("Digite seu email");
            }
        }else{
            nome.setError("Digite seu nome completo");
        }
    }

    private void iniciaComponentes() {

        //configurando toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Cadastro de funcionario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome = findViewById(R.id.cadastro_et_nome);
        email = findViewById(R.id.cadastro_et_email);
        senha = findViewById(R.id.cadastro_et_senha);
        confirmaSenha = findViewById(R.id.cadastro_et_confirmaSenha);
        tipoFuncionario = findViewById(R.id.cadastro_et_tipoFuncionario);

        SimpleMaskFormatter smfV = new SimpleMaskFormatter("L");
        MaskTextWatcher mtwV = new MaskTextWatcher(tipoFuncionario, smfV);
        tipoFuncionario.addTextChangedListener(mtwV);
    }
}