package pl.foxcode.firebaseauthrealtimedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        button_sign_up.setOnClickListener {
            createAccount(editText_email.text.trim().toString(), editText_password.text.toString(), editText_password_confirmation.text.toString())
        }

        button_sign_in.setOnClickListener {
            mAuth.signInWithEmailAndPassword(editText_email.text.toString(),editText_password.text.toString())
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        if(task.isSuccessful)
                        {
                            val user = mAuth.currentUser

                            Toast.makeText(applicationContext,user?.displayName,Toast.LENGTH_LONG).show()

                        }
                       else
                            Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()

                    }

                })
        }


        button_sign_in_google.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()


        }

    }


    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        Toast.makeText(applicationContext,currentUser.toString(),Toast.LENGTH_LONG).show()
    }


    fun createAccount(email : String, password : String, password2 : String){

        if(password.length<6)
            editText_password.error = getString(R.string.password_error)
        if(!email.contains("@"))
            editText_email.error = getString(R.string.email_error)
        if(password != password2)
            editText_password_confirmation.error = getString(R.string.password_confirmation_error)

        if(password.length>=6 && email.contains("@") && password == password2)
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful)
                    {
                        Toast.makeText(applicationContext,"New account created",Toast.LENGTH_LONG).show()
                    }
                    else
                        Toast.makeText(applicationContext,"Account with this email already exists",Toast.LENGTH_LONG).show()

                }
            })
        }




    }

}