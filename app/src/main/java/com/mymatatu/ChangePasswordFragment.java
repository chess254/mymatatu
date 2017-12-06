package com.mymatatu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.AppConstants;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.VolleyEssentials.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 03-08-2017.
 */

public class ChangePasswordFragment extends Fragment{
    EditText old,new_password,new_password_confirm;
    ImageView old_show,new_password_show,new_confirm_password_show;
    TextView old_password_error,new_password_error,new_confirm_password_error;
    String old_p_s,new_p_s,confirm_p_s;
    boolean old_show_flag = false,new_password_show_flag = false,new_confirm_password_show_flag = false;
    Button done;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.changelayout_fragment,container,false);
        getActivity().setTitle("Change Password");
        //Initialization
        old = (EditText) v.findViewById(R.id.old_password_change);
        new_password = (EditText) v.findViewById(R.id.password_change);
        new_password_confirm = (EditText) v.findViewById(R.id.confirm_password_change);


        old_show = (ImageView) v.findViewById(R.id.old_password_show);
        new_password_show = (ImageView) v.findViewById(R.id.password_show_btn);
        new_confirm_password_show = (ImageView) v.findViewById(R.id.confirm_password_show_btn);

        old_password_error = (TextView) v.findViewById(R.id.oldpassworderror);
        new_password_error = (TextView) v.findViewById(R.id.passworderror);
        new_confirm_password_error = (TextView) v.findViewById(R.id.confirmpassworderror);

        //Show Password Code
        if(old_show_flag == false){
            old.setTransformationMethod(PasswordTransformationMethod.getInstance());
            old_show_flag = true;
        }else{
            old.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            old_show_flag = false;
        }

        if(new_password_show_flag == false){
            new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            new_password_show_flag = true;
        }else{
            new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            new_password_show_flag = false;
        }

        if(new_confirm_password_show_flag == false){
            new_password_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
            new_confirm_password_show_flag = true;
        }else{
            new_password_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            new_confirm_password_show_flag = false;
        }


        done = (Button) v.findViewById(R.id.change_password_done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               old_p_s = old.getText().toString();
               new_p_s = new_password.getText().toString();
               confirm_p_s = new_password_confirm.getText().toString();
                if(!Validation.checkEmptyfield(old_p_s)){
                    old_password_error.setText("This field cannot be empty");
                    old_password_error.setVisibility(View.VISIBLE);
                }else{
                    old_password_error.setVisibility(View.GONE);
                    if(!Validation.checkEmptyfield(new_p_s)){
                        new_password_error.setText("This field cannot be empty");
                        new_password_error.setVisibility(View.VISIBLE);
                    }else{
                        new_password_error.setVisibility(View.GONE);
                        if(!Validation.checkEmptyfield(confirm_p_s)){
                            new_confirm_password_error.setText("This field cannot be empty");
                            new_confirm_password_error.setVisibility(View.VISIBLE);
                        }else{
                            new_confirm_password_error.setVisibility(View.GONE);
                            if(old_p_s.compareTo(SaveSharedPreference.getPrefPassword(getActivity()))!=0){
                                old_password_error.setText("Old Password Doesnot Match");
                                old_password_error.setVisibility(View.VISIBLE);
                            }else{
                                old_password_error.setVisibility(View.GONE);
                                if(!Validation.checkPassWordLenth(new_p_s)){
                                    new_password_error.setText("New Password need to be atleast 6 Characters");
                                    new_password_error.setVisibility(View.VISIBLE);
                                }else{
                                    new_password_error.setVisibility(View.GONE);
                                    if(Validation.checkPassWordMatch(new_p_s,confirm_p_s)){
                                        //ALl OK
                                        new_confirm_password_error.setVisibility(View.GONE);
                                        sendRequest();



                                    }else{
                                        new_confirm_password_error.setText("Passwords Doesnot Match");
                                        new_confirm_password_error.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });



        return v;
    }

    private void sendRequest() {

        StringRequest str = new StringRequest(Request.Method.POST, AppConstants.BASE_URL + "Login/change_password", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    if(jo.getBoolean("succcess")) {
                        SaveSharedPreference.setPrefPassword(getActivity(), new_p_s);
                        Toast.makeText(getActivity(),jo.getString("message"),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),jo.getString("message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = getActivity().getString(R.string.api_id)+
                        ":"+getActivity().getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
              //  Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone_no",SaveSharedPreference.getPhone(getActivity()));
                params.put("old_password",old_p_s);
                params.put("new_password",new_p_s);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(str);
    }
}
