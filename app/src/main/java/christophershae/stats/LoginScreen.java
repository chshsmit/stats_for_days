package christophershae.stats;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class LoginScreen extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    public FirebaseAuth mAuth;
    private DatabaseReference mFireBaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private EditText mEmailField;
    private EditText mPasswordField;
    public String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //Views
        mEmailField = (EditText) findViewById(R.id.emailTextEntry);
        mPasswordField = (EditText) findViewById(R.id.passwordTextEntry);


        mFirebaseInstance = FirebaseDatabase.getInstance();

        //Get reference to user nodes
        mFireBaseDatabase = mFirebaseInstance.getReference("users");


        mAuth = FirebaseAuth.getInstance();
    }

    private void createAccount(final String email, String password){
        Log.d(TAG, "createAccount:" +email);

        if(!validateForm()){
            return;
        }

        //Start to create a user with email
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Sign in success
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    userId = user.getUid();
                    addNewUserToDatabase(userId, email);

                } else {
                    //If sign in fails, display a message to the user
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(LoginScreen.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void addNewUserToDatabase(String newUserId, String email){
        User user = new User(email);
        mFireBaseDatabase.child(userId).setValue(user);
    }

    private void signIn(String email, String passsword) {
        Log.d(TAG, "signIn:" + email);
        if(!validateForm()){
            return;
        }

        //Start sign in with email
        mAuth.signInWithEmailAndPassword(email, passsword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Sign in success
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    //If sign in fails, display a message to the user
                    Log.w(TAG, "signInWithEmail:FAILED", task.getException());
                    Toast.makeText(LoginScreen.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void signOut() {
//        mAuth.signOut();
//    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if(TextUtils.isEmpty(email)){
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if(TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;


    }

    public void buttonClicked(View v){
        int i = v.getId();
        if(i == R.id.loginButton){
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if(i == R.id.newUserButton){
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }


}
