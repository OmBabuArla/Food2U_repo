package com.example.omarla.food2u_repo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    Button btnlogin, btnsignup;
    EditText edt_mobile, edt_password;
    String mobile, password;

    AlertDialog.Builder builder;
    Boolean net_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        btnlogin = (Button)findViewById(R.id.login_id);
        btnsignup = (Button)findViewById(R.id.signup_id);
        edt_mobile = (EditText) findViewById(R.id.mobile_id);
        edt_password = (EditText) findViewById(R.id.password_id);


        builder = new AlertDialog.Builder(LogIn.this);
        net_check=netCheck();

        if(net_check== true){                                         //checking currently available data network info
            displayNetwork();
        }
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = edt_mobile.getText().toString();
                password = edt_password.getText().toString();


                if (mobile.length() != 10) {
                    builder.setTitle("Mobile_number");
                    builder.setMessage("Please enter the correct mobile number");
                    displayAlert("Input_error0");
                } else {
                    if (password.length() == 0) {
                        builder.setTitle("Password");
                        builder.setMessage("password no entererd!");
                        displayAlert("Input_error1");
                    } else {

                        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.con_url + URLs.login,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObj = new JSONObject(response);

                                            String error = jsonObj.getString("error");
                                            String message = jsonObj.getString("message");
                                            if (error.equals(0)) {
                                                builder.setMessage(message);
                                                builder.setTitle("Success!");
                                                displayAlert(error);
                                            } else {
                                                builder.setMessage(message);
                                                builder.setTitle("Alert!");
                                                displayAlert(error);
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("mobile_no", mobile);
                                params.put("password", password);

                                return params;
                            }

                        };
                        MySingleton.getInstance(LogIn.this).addToRequestque(stringRequest);

                    }
                }
            }
        });
    }


    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (code.equals("input_error"))
                {
                    edt_mobile.setText("");
                    edt_password.setText("");
                }
                else if (code.equals("input_error1"))
                {
                    edt_password.setText("");
                    edt_mobile.setText("");
                }
                else if (code.equals("0"))
                {
                    // session.createLoginSession(mobile);
                    //startActivity(new Intent(LogIn.this.class));
                }
                else if (code.equals("1")) {
                    edt_mobile.setText("");
                    edt_password.setText("");
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void displayNetwork() {
        new AlertDialog.Builder(this).setMessage("Please check your internet connection")
                .setTitle("Network Error!!").setCancelable(true).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }


    public boolean netCheck() {

        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectionManager.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                && connectionManager.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            net_check = true;
        }
        return net_check;
    }
}


