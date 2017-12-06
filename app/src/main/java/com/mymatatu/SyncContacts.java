package com.mymatatu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Global.AppConstant;
import com.mymatatu.Global.GlobalProgressDialogue;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.R;
import com.mymatatu.VolleyEssentials.MySingleton;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.group.Group;
import com.onegravity.contactpicker.picture.ContactPictureType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SyncContacts extends BaseActivity {

    private static final String EXTRA_DARK_THEME = "EXTRA_DARK_THEME";
    private static final String EXTRA_GROUPS = "EXTRA_GROUPS";
    private static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";
    private static final String TAG = "Sync Conatcts";
    GlobalProgressDialogue globalProgressDialogue;

    private static final int REQUEST_CONTACT = 0;
    private boolean mDarkTheme;
    private ArrayList<Contact> mContacts;
    private List<Group> mGroups;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 23;
    View v;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_contacts);
        v = new View(this);
      /*  if(SaveSharedPreference.getPrefSyncContactFlag(this) == 1){
            Intent intent = new Intent(SyncContacts.this,Home.class);
            startActivity(intent);
        }else {*/
        check();
        //}

    }


    private void check() {


        if (ContextCompat.checkSelfPermission(SyncContacts.this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SyncContacts.this, ContactPickerActivity.class)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE,
                                    ContactPictureType.ROUND.name())

                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                                    ContactDescription.ADDRESS.name())
                            .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                            .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT, 0)
                            .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, false)

                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                                    ContactsContract.CommonDataKinds.Email.TYPE_WORK)

                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                                    ContactSortOrder.AUTOMATIC.name())
                            .putExtra("phone", SaveSharedPreference.getPhone(getApplicationContext()));

                    startActivityForResult(intent, REQUEST_CONTACT);
                }
            };
            Thread thread = new Thread(r);
            thread.start();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                 //   Log.d("here", "1");
                    Toast.makeText(SyncContacts.this, "Read contacts permission is required to function app correctly", Toast.LENGTH_LONG).show();
                } else {
                   // Log.d("here", "2");
                    ActivityCompat.requestPermissions(SyncContacts.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            1);
                }

            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null) {
            Intent intent = new Intent(SyncContacts.this, ContactPickerActivity.class)
                    .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE,
                            ContactPictureType.ROUND.name())

                    .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                            ContactDescription.ADDRESS.name())
                    .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                    .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT, 0)
                    .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, false)

                    .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_WORK)

                    .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                            ContactSortOrder.AUTOMATIC.name());

            startActivityForResult(intent, REQUEST_CONTACT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(EXTRA_DARK_THEME, mDarkTheme);
        if (mGroups != null) {
            outState.putSerializable(EXTRA_GROUPS, (Serializable) mGroups);
        }
        if (mContacts != null) {
            outState.putSerializable(EXTRA_CONTACTS, (Serializable) mContacts);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK) {
            if (Validation.checkconnection(SyncContacts.this)) {

                Bundle args = data.getBundleExtra("BUNDLE");
                mContacts = (ArrayList<Contact>) args.getSerializable("ARRAYLIST");
                syncSelectedContacts(mContacts);

               /* for (int i = 0; i < mContacts.size(); i++)
                    Log.d("sushildlh", mContacts.get(i).getFirstName() + " "+mContacts.get(i).getPhone(2));
*/
                /*SaveSharedPreference.setPrefSyncContactFlag(this, 1);
                Intent intent = new Intent(SyncContacts.this, Home.class);
                intent.putExtra("screen", "synccontacts");
                startActivity(intent);*/
            } else {
                CommonMethod.showconnectiondialog(SyncContacts.this, null);
            }

        }
    }

    private void syncSelectedContacts(final List<Contact> mContacts) {
        showDailogue();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Users_sync/contacts"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    if (response_object.getBoolean("success")) {
                        Intent intent = new Intent(SyncContacts.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response_object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDailogue();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = SyncContacts.this.getString(R.string.api_id) +
                        ":" + SyncContacts.this.getString(R.string.api_password);
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

                try {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject object = new JSONObject();
                    //params.put("empl_location", GlobalPreferenceManager.getStringForKey(getActivity(), AppConstant.KEY_EMPLOYEE_LOCATION, ""));
                    for (int i = 0; i < mContacts.size(); i++) {
                        JSONObject JSONestimate = new JSONObject();
                        JSONestimate.put("contact",mContacts.get(i).getPhone(2) );
                        JSONestimate.put("contact_name", mContacts.get(i).getFirstName());
                        jsonArray.put(JSONestimate);
                    }

                    params.put("alldata", jsonArray.toString());
                    // params.put("data", object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("phone_no", SaveSharedPreference.getPhone(SyncContacts.this));
                return params;
            }


        };
        MySingleton.getInstance(SyncContacts.this).addToRequestQueue(request);
        //return return_arr;
    }

    void showDailogue() {
        try {
            globalProgressDialogue = new GlobalProgressDialogue();
            globalProgressDialogue.show(getFragmentManager(), TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void dismissProgressDailogue() {
        try {
            if (globalProgressDialogue != null) {
                globalProgressDialogue.dismissAllowingStateLoss();
                globalProgressDialogue = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


