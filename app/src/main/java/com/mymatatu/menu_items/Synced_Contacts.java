package com.mymatatu.menu_items;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.Global.AppConstant;
import com.mymatatu.Global.GlobalProgressDialogue;
import com.mymatatu.Home;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.R;
import com.mymatatu.VolleyEssentials.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 19-07-2017.
 */

public class Synced_Contacts extends Fragment {
    private ListView lv;
    private ArrayList<String> contacts = new ArrayList<>();
    private static final String TAG = "Wallet";
    private GlobalProgressDialogue globalProgressDialogue;
    private ArrayList<String> names = new ArrayList<>();
    private TextView error;
    private listviewadapter listviewadapter;
    private  ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.synced_contacts_fragment, container, false);
        Home ho = (Home) getActivity();

        error = (TextView) v.findViewById(R.id.nosycn);

         list = (ListView) v.findViewById(R.id.listview_synced_contacts);



        syncAllSelectedContacts();

        return v;
    }


    private void syncAllSelectedContacts() {
        showDailogue();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Users_sync/contactlist"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    if (response_object.getBoolean("success")) {
                        JSONArray responseja = new JSONArray();
                        responseja = response_object.getJSONArray("alldata");
                        for (int i = 0; i < responseja.length(); i++) {
                            JSONObject data = responseja.getJSONObject(i);
                            names.add(data.getString("commansync_username"));
                            contacts.add(data.getString("commansync_usercontact").substring(3));
                        }

                        listviewadapter = new listviewadapter(getActivity(), contacts, names);

                        list.setAdapter(listviewadapter);


                    } else {
                        error.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), response_object.getString("message"), Toast.LENGTH_LONG).show();
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
                String credentials = getActivity().getString(R.string.api_id) +
                        ":" + getActivity().getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                //Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone_no", SaveSharedPreference.getPhone(getActivity()));
                return params;
            }


        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
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


class listviewadapter extends BaseAdapter {

    Context c;
    ArrayList<String> contact_list = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();

    public listviewadapter(Context c, ArrayList<String> contact_list, ArrayList<String> names) {
        this.c = c;
        this.contact_list = contact_list;
        this.names = names;
    }

    @Override
    public int getCount() {
        return contact_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView contact = (TextView) rowView.findViewById(R.id.contact);
        name.setText(names.get(position));
        contact.setText(contact_list.get(position));
        return rowView;
    }


}
