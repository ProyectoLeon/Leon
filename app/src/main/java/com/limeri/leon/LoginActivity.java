package com.limeri.leon;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Profesional;
import com.limeri.leon.Models.User;
import com.limeri.leon.common.Application;
import com.limeri.leon.common.DataBase;
import com.limeri.leon.common.Login;
import com.limeri.leon.common.MailSender;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private String mNombre, mCorreo, mProfPassword, mProfMatricula, mProducto;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;
    private AutoCompleteTextView mMatriculaView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Application.setApplicationContext(getApplicationContext());
        // Set up the login form.
        mMatriculaView = (AutoCompleteTextView) findViewById(R.id.matricula);
        progressDialog = new ProgressDialog(this);

//        probarChiper();

        populateAutoComplete();

        if (User.getUserEmail(getBaseContext()) != null) {
            mMatriculaView.setText(User.getUserEmail(getBaseContext()));
        }

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLogInButton = (Button) findViewById(R.id.login_in_button);
        if (mLogInButton != null) {
            mLogInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }
        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        if (mSignInButton != null) {
            mSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog_Prof();
                }
            });
        }
        Button mForgetPass = (Button) findViewById(R.id.forget_pass_button);
        if (mForgetPass != null) {
            mForgetPass.setOnClickListener( new OnClickListener() {
                @Override
                public void onClick(View view) {
                    recuperarContrasena();
                }
            });
        }

        String pass = "equipolimeri";
        String email = "aplicacionleon@gmail.com";
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Verifique que el dispositivo posea una buena conexión a internet",Toast.LENGTH_LONG).show();
                            LoginActivity.this.finish();
                        }
                    }
                });

    }

/*
    private void probarChiper() {
        Present.test();
    }
*/

    private void recuperarContrasena() {

        String matricula = mMatriculaView.getText().toString();

        if (matricula.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, complete su matrícula", Toast.LENGTH_LONG).show();
            return;
        }

        Profesional profesional = Profesional.getProfesional(matricula);

        if (profesional == null) {
            mMatriculaView.setError("El número de matrícula es incorrecto");
        } else {
            enviarMail(profesional);
        }
    }

    private void enviarMail(Profesional profesional){

        String asunto = "LEON - Recuperación de Contraseña";
        String cuerpo = "Hola " + profesional.getNombre() + ",\r\n\r\n" +
                        "Este es un mensaje enviado desde la aplicación LEON porque usted solicitó recuperar su contraseña.\r\n" +
                        "Su contraseña de acceso a la aplicación es: " + profesional.getContrasena() + "\r\n\r\n" +
                        "Gracias por utilizar LEON.";

        if (MailSender.sendMail(profesional, asunto, cuerpo, null)) {
            Toast.makeText(getApplicationContext(), "Se ha enviado un correo a su casilla", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error al enviar el correo", Toast.LENGTH_LONG).show();
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mMatriculaView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Lógica de LOGIN.
     * Si hay errores (matricula inválida, falta de valores, etc.)
     */
    private void attemptLogin() {

        showProgressBar();
        // Reset errors.
        mMatriculaView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String matricula = mMatriculaView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(matricula)) {
            mMatriculaView.setError("Por favor ingrese la matricula");
            focusView = mMatriculaView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Por favor ingrese la contraseña");
            focusView = mPasswordView;
            cancel = true;
        } else {

            Profesional profesional = Profesional.getProfesional(matricula);
            if (profesional == null) {
                mMatriculaView.setError("El número de matrícula es incorrecto");
                focusView = mMatriculaView;
                cancel = true;
            } else {
                if (!Login.isSameDevice(profesional.getLogin()) && !Login.isLoginVencido(profesional.getLogin())) {
                    mMatriculaView.setError("El profesional inició sesión en otro dispositivo");
                    focusView = mMatriculaView;
                    cancel = true;
                } else if (!password.equals(profesional.getContrasena())) {
                    mPasswordView.setError("La contraseña es incorrecta");
                    focusView = mPasswordView;
                    cancel = true;
                } else {
                    Profesional.setProfesionalActual(profesional);
                }
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            hideProgressBar();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            UserLoginTask mAuthTask = new UserLoginTask(matricula);
            mAuthTask.execute();
        }
    }

    private void showProgressBar() {
        progressDialog.setMessage("Por favor, espere...");
        progressDialog.show();
        // put your code which preload with processDialog
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mMatriculaView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Void> {

        private final String mMatricula;

        UserLoginTask(String matricula) {
            mMatricula = matricula;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                cargarBaseDatos();
                login(mMatricula);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            hideProgressBar();
        }

        @Override
        protected void onCancelled() {
            hideProgressBar();
        }

    }

    private void hideProgressBar() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void cargarBaseDatos() {
            DataBase.loadDB();
    }

    public void login(String matricula) {
        try {
            DataBase.setProfesionalLogin();
            User.saveUserEmail(getBaseContext(), matricula);
            Profesional.loadCuentas();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Navegacion.irA(LoginActivity.this, SelecPacienteActivity.class);
    }

    //POPUP PARA REGISTRAR PROFESIONAL
    private void showDialog_Prof() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.datos_profesional_popup, (ViewGroup) this.findViewById(android.R.id.content), false);
        // Set up the input
        final EditText nombre = (EditText) viewInflated.findViewById(R.id.inputNombre);
        final EditText matricula = (EditText) viewInflated.findViewById(R.id.inputMatricula);
        final EditText producto = (EditText) viewInflated.findViewById(R.id.inputProducto);
        final EditText password = (EditText) viewInflated.findViewById(R.id.inputPassword);
        final EditText confPassword = (EditText) viewInflated.findViewById(R.id.inputPassword2);
        final EditText correo = (EditText) viewInflated.findViewById(R.id.inputCorreo);

        builder.setView(viewInflated);

        // Set up the buttons

        builder.setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog = builder.create();

        dialog.show();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNombre = nombre.getText().toString();
                mCorreo = correo.getText().toString();
                mProfPassword = password.getText().toString();
                mProfMatricula = matricula.getText().toString();
                mProducto = producto.getText().toString();
                String confPass = confPassword.getText().toString();
                Profesional profesional = Profesional.getProfesional(mProfMatricula);

                if (mProfMatricula.isEmpty() || mProducto.isEmpty() || mProfPassword.isEmpty() || mProfPassword.isEmpty() || mNombre.isEmpty() || mCorreo.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Por favor ingresar los datos obligatorios",Toast.LENGTH_LONG).show();
                } else if (profesional == null) {
                    matricula.setError("No existe una cuenta con ese número de matricula");
                } else if (profesional.isRegistrado()) {
                    matricula.setError("El número de matrícula ya posee cuenta registrada");
                } else if (!mProducto.equals(profesional.getProducto())) {
                    producto.setError("Código de producto incorrecto");
                } else if (!mProfPassword.equals(confPass)) {
                    password.setError("Las contraseñas no son iguales");
                    confPassword.setError("Las contraseñas no son iguales");
                } else {
                    profesional.setNombre(mNombre);
                    profesional.setCorreo(mCorreo);
                    profesional.setContrasena(mProfPassword);
                    profesional.setRegistrado(true);
                    Profesional.saveProfesional(profesional);

                    dialog.dismiss();
                }
            }
        });
//        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
//        button.setGravity(Gravity.END);
//        button.setGravity(Gravity.CENTER_VERTICAL);
//        button.setBackground(getResources().getDrawable(R.drawable.button));
        button.setPadding(10, 0, 10, 0);

        Button button2 = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//        button2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button2.setTextColor(getResources().getColor(R.color.colorPrimary));
//        button2.setGravity(Gravity.START);
//        button2.setBackground(getResources().getDrawable(R.drawable.button));
//        button2.setGravity(Gravity.CENTER_VERTICAL);
        button2.setPadding(10, 0, 10, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }
}

