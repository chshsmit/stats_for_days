package christophershae.stats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class LoginScreen extends AppCompatActivity {


    //Decalring global variables
    private static final String TAG = "EmailPassword";
    public FirebaseAuth mAuth;
    private DatabaseReference mFireBaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    //This is the user object that will reference whichever account is logged in
    public User user;

    //My input views
    private EditText mEmailField;
    private EditText mPasswordField;
    public String userId;

    //----------------------------------------------------------------------------------------------
    //This is just the on create method
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        //Views
        mEmailField = (EditText) findViewById(R.id.emailTextEntry);
        mPasswordField = (EditText) findViewById(R.id.passwordTextEntry);

        //Get an instance of my firebase
        mFirebaseInstance = Utils.getDatabase();

        //Get reference to user nodes
        mFireBaseDatabase = mFirebaseInstance.getReference("users");
        //Current Authorized fire base user
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            System.out.println("You are not signed in");
        } else {
            System.out.println("You are signed in");
            changeToMainActivity(mAuth.getUid());


        }


        //signIn("chshsmit@gmail.com", "Password");
    }



    //----------------------------------------------------------------------------------------------
    //This is code handles adding a new profile to the database along with sign-in methods
    //----------------------------------------------------------------------------------------------

    //Adding the new user object to the database
    public void addNewUserToDatabase(String newUserId, String email){
        user = new User(email);
        mFireBaseDatabase.child(newUserId).setValue(user);
    }

    private void createAccount(final String email, String password){
        Log.d(TAG, "createAccount:" +email);

        //Make sure both fields are filled out correctly and that an email isnt being re-used
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
                    FirebaseUser newUser = mAuth.getCurrentUser();
                    userId = newUser.getUid();                           //Getting the userId
                    addNewUserToDatabase(userId, email);                 //Adding newly created user to database
                    changeToMainActivity(userId);                //Changing activity to create a roster

                } else {
                    //If sign in fails, display a message to the user
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(LoginScreen.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


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
                    FirebaseUser newUser = mAuth.getCurrentUser();
                    userId = newUser.getUid();

                    //addUserChangeListener();               //Once the user is signed in, this line gets a reference to their profile information stored in the database


                    changeToMainActivity(userId);


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

        String email = mEmailField.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString().trim();
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


    //------------------------------------------------------------------------------------------------
    //This is the code to change activities
    //------------------------------------------------------------------------------------------------


    public void changeToMainActivity(String currentUserId){
        Intent changingActivities = new Intent(getApplicationContext(),MainActivity.class);
        changingActivities.putExtra("userId", currentUserId);
        changingActivities.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changingActivities);
    }


}
