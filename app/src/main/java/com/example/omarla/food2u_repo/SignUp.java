package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText ed_username, ed_email, ed_password, ed_mobile, ed_conf_password;
    String username, email, password, mobile, conf_password;
    Button signup;
    TextView login;
    AlertDialog.Builder builder;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ed_username = (EditText) findViewById(R.id.username);
        ed_email = (EditText) findViewById(R.id.email);
        ed_password = (EditText) findViewById(R.id.password);
        ed_conf_password = (EditText) findViewById(R.id.conf_password);
        ed_mobile =(EditText) findViewById(R.id.ed_mobile);
        signup = (Button) findViewById(R.id.btn_signup);
        ed_mobile.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});

        ed_password.setFilters(new InputFilter[] {new InputFilter.LengthFilter(12)});

        ed_conf_password.setFilters(new InputFilter[] {new InputFilter.LengthFilter(12)});


        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        builder = new AlertDialog.Builder(SignUp.this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ed_username.getText().toString();
                email = ed_email.getText().toString().trim();
                password = ed_password.getText().toString().trim();
                conf_password = ed_conf_password.getText().toString().trim();
                mobile = ed_mobile.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (username.length()<1 && email.length()<1 && password.length()<1 && conf_password.length()<1 && mobile.length()<1) {
                    builder.setTitle("Something went wrong");
                    builder.setMessage("Please fill all the fields?");
                    displayAlert("1");
                }
                else if(username.length()<3){
                    builder.setTitle("Invalid Username");
                    builder.setMessage("Username should be minimum 3 character ?");
                    displayAlert("2");
                }
                else if(!email.matches(emailPattern))
                {
                    builder.setTitle("Email Address");
                    builder.setMessage("Please, fill the complete email address");
                    displayAlert("3");
                }
                else if(password.length()<6){
                    builder.setTitle("Password Error");
                    builder.setMessage("Password Length Min 6 Character and Max 12");
                    displayAlert("4");
                }
                else if(password.length()>12){
                    builder.setTitle("Password Error");
                    builder.setMessage("Password Length Min 6 Character and Max 12");
                    displayAlert("4");
                }
                else if(!conf_password.equals(password)){
                    builder.setTitle("Password Error");
                    builder.setMessage("Please, fill password and confirm password same");
                    displayAlert("4");
                }
                else if(mobile.length() != 10)
                {
                    builder.setTitle("Mobile Number");
                    builder.setMessage("Please, enter valid mobile number");
                    displayAlert("6");
                }
                else{
                 ValidateEmail();
                }
            }
        });
    }
    public void displayAlert(final String code) {
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void Perfor_Action(View view)                     // login text in signup activity method

        {
            Intent i=new Intent(SignUp.this,LogIn.class);
            startActivity(i);

        }

    public void ValidateEmail(){
           progress.show();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, URLs.ValidateEmailURL1 + email + URLs.ValidateEmaiURL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("email response",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Log.d("Response",jsonObject+toString());
                            Log.d("URLs",URLs.ValidateEmailURL1 + email + URLs.ValidateEmaiURL2.toString());
                            Boolean smtp_check=jsonObject.getBoolean("smtp_check");
                            if(smtp_check==true){
                                ValidateContact();
                            }
                            else{
                                Toast.makeText(SignUp.this, "Enter email address is wrong", Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUp.this, ""+e.toString(), Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SignUp.this, ""+error.toString(), Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                });
        MySingleton.getInstance(SignUp.this).addToRequestque(stringRequest);
    }

    public void ValidateContact(){

        StringRequest stringRequest=new StringRequest(Request.Method.GET, URLs.ValidateContactURL1 + mobile + URLs.ValidateContactURL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("valid contact",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean valid=jsonObject.getBoolean("valid");
                            if(valid==true){
                                CallWebServices();
//                                progress.dismiss();
//                                Toast.makeText(SignUp.this, ""+jsonObject.toString(), Toast.LENGTH_LONG).show();
                            }
                            else{

                                Toast.makeText(SignUp.this, "Entered Contact no is wrong.", Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(SignUp.this, ""+e.toString(), Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();

                Toast.makeText(SignUp.this, ""+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(SignUp.this).addToRequestque(stringRequest);
    }

    public void CallWebServices(){
         Log.d("register Url",URLs.REGISTER);
                StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.REGISTER, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("signupresponse",response);
                                progress.dismiss();

                                startActivity(new Intent(SignUp.this, LogIn.class));
                                Toast.makeText(SignUp.this, "Successful Registration", Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.dismiss();

                            }
                        })
                        {
                            protected Map<String,String> getParams() throws AuthFailureError
                            {
                                Map<String,String> Params=new HashMap<String,String>();
                                Params.put("name",username);
                                Params.put("email",email);
                                Params.put("mobile_no",mobile);
                                Params.put("password",password);
                                return Params;
                            }



                    };
                        MySingleton.getInstance(SignUp.this).addToRequestque(stringRequest);
    }

}

