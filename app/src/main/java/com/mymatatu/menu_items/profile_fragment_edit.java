package com.mymatatu.menu_items;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.AppConstants;
import com.mymatatu.Home;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.R;
import com.mymatatu.VolleyEssentials.MySingleton;
import com.mymatatu.model.CountryCityRSM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by anonymous on 21-07-2017.
 */

public class profile_fragment_edit extends Fragment {
    private android.app.FragmentManager fm;
    private EditText name;
    private TextView phone, error, nextofkin, password;
    private String name_s, county_s, city_s, phone_s;
    ImageButton edit_photo;
    ImageView profile;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private Button done;
    ArrayList<CountryCityRSM> countyData, cityData;
    private ByteArrayOutputStream stream;
    private byte[] image_bytes;
    //RegionDatabaseHelper rdb;
    AutoCompleteTextView city, county;
    private ArrayList<String> city_data, county_data;
    private ArrayAdapter cityadapter, countyadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_menuitem_fragment_edit, container, false);
        // rdb = new RegionDatabaseHelper(getActivity(), null, null, 1);
        city_data = new ArrayList<>();
        county_data = new ArrayList<>();

        Bundle bundle = getArguments();
        Home ho = (Home) getActivity();
        ho.hidekeyboardontouch();
        if (ho.timerflag == true) {
            ho.endTimer();
        }
        fm = getFragmentManager();
        done = (Button) v.findViewById(R.id.edit_done);
        name = (EditText) v.findViewById(R.id.profile_edit_name);
        county = (AutoCompleteTextView) v.findViewById(R.id.profile_edit_county);
        city = (AutoCompleteTextView) v.findViewById(R.id.profile_edit_city);
        edit_photo = (ImageButton) v.findViewById(R.id.edit_image_profile);
        profile = (ImageView) v.findViewById(R.id.app_bar_image);
        phone = (TextView) v.findViewById(R.id.phone_edit_profile);
//        nextofkin = (TextView) v.findViewById(R.id.nextofkin_data);
//        password = (TextView) v.findViewById(R.id.password_data);
        //error = (TextView) v.findViewById(R.id.error_profile);
        stream = new ByteArrayOutputStream();
        phone.setText(SaveSharedPreference.getUserName(getActivity()));
        name.setText(bundle.getString("name"));
        county.setText(bundle.getString("county"));
        city.setText(bundle.getString("city"));

        cityadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, city_data);
        city.setAdapter(cityadapter);
        countyadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, county_data);
        county.setAdapter(countyadapter);

        countycityrequest();

        if (SaveSharedPreference.getPrefImage(getActivity()) != null) {
            String image_s = SaveSharedPreference.getPrefImage(getActivity());
            byte[] imagearr;
            imagearr = Base64.decode(image_s, Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(imagearr, 0, imagearr.length);
            profile.setImageBitmap(image);
        }


        if (bundle.getInt("flag") == 1) {
            //Do the work here
            bitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();


            edit_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getdata();
                    if (validate()) {
                        //getActivity().deleteDatabase("profile.db");
                        //pd.add(name_s,phone_s,county_s,city_s,image_bytes);

                        updateEdit();


                    } else {
                        Toast.makeText(getActivity(), "Fields cannot be Empty", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        } else {
            fm.beginTransaction().replace(R.id.main_frame, new profile_fragment()).commit();
        }

        return v;
    }

    private void updateEdit() {

        StringRequest request = new StringRequest(Request.Method.POST, AppConstants.BASE_URL + "Passenger/updateprofile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   Log.d("seats", response);
                        try {
                            JSONObject respose_object = new JSONObject(response);
                            if (respose_object.getBoolean("success")) {
                                SaveSharedPreference.setPrefName(getActivity(), name_s);
                                SaveSharedPreference.setPrefPhone(getActivity(), phone_s);
                                SaveSharedPreference.setPrefCounty(getActivity(), county_s);
                                SaveSharedPreference.setPrefCity(getActivity(), city_s);
                                String encoded = Base64.encodeToString(image_bytes, Base64.DEFAULT);
                                SaveSharedPreference.setPrefimage(getActivity(), encoded);
                                fm.beginTransaction().replace(R.id.main_frame, new profile_fragment()).addToBackStack("edit").commit();
                            } else {
                                Toast.makeText(getActivity(), respose_object.getString("message"), Toast.LENGTH_LONG).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = getActivity().getString(R.string.api_id) +
                        ":" + getActivity().getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
               // Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone_no", phone_s);
                params.put("name", name_s);
                params.put("county", county_s);
                params.put("city", city_s);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                /*SharedPreferences myPrefs = getActivity().getSharedPreferences("matatu", 0);
                SharedPreferences.Editor myPrefsEdit = myPrefs.edit();

                myPrefsEdit.putString("url", uri.toString());
                myPrefsEdit.commit();*/

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                int size = bitmap.getRowBytes() * bitmap.getHeight();

                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getdata() {
        name_s = name.getText().toString().trim();
        phone_s = phone.getText().toString().trim();
        county_s = county.getText().toString().trim();
        city_s = city.getText().toString().trim();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        int size = bitmap.getRowBytes() * bitmap.getHeight();

       // Log.d("sushildlh", size + "");
        image_bytes = stream.toByteArray();
    }

    private boolean validate() {
        if (name_s.isEmpty() || phone_s.isEmpty() || county_s.isEmpty() || city_s.isEmpty()) {
            return false;
        } else if (!county_data.contains(county_s)) {
            county.setError("Country is not present in our system");
            return false;
        } else if (!city_data.contains(city_s)) {
            city.setError("City is not present in our system");
            return false;
        }
        return true;
    }

    public void countycityrequest() {

        try {
            county_data.clear();
            city_data.clear();
            countyData = new ArrayList<>();
            cityData = new ArrayList<>();
            String response = SaveSharedPreference.getString(getActivity(), "all_county");
            JSONObject ja = new JSONObject(response);
            JSONArray countyArray = ja.getJSONArray("county");
            JSONArray cityArray = ja.getJSONArray("city");
            if (countyArray.length() > 0) {
                for (int i = 0; i < countyArray.length(); i++) {
                    JSONObject jo_countycity = countyArray.getJSONObject(i);
                    countyData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name"), jo_countycity.getString("id_c")));
                    county_data.add(jo_countycity.getString("name"));
                }
                countyadapter.notifyDataSetChanged();
            }

            if (cityArray.length() > 0) {
                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject jo_countycity = cityArray.getJSONObject(i);
                    cityData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name"), jo_countycity.getString("id_c")));
                    city_data.add(jo_countycity.getString("name"));
                }
                cityadapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
