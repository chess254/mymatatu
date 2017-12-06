package com.mymatatu.menu_items;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Background.GetBalance;
import com.mymatatu.CustomAdapter;
import com.mymatatu.DataBaseHelper.MyDataBaseHelper;
import com.mymatatu.Global.AppConstant;
import com.mymatatu.Global.GlobalProgressDialogue;
import com.mymatatu.Home;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.PaymentConfirm;
import com.mymatatu.R;
import com.mymatatu.TransferHistory;
import com.mymatatu.VolleyEssentials.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 17-07-2017.
 */

public class Wallet extends Fragment {
    TextView error, mBal;
    EditText mobile_no, amount;
    MyDataBaseHelper mydb;
    Button send_btn;
    int balance;
    private CustomAdapter adapter;
    private RecyclerView mRecycleView;
    ArrayList<TransferHistory> transferHistories = new ArrayList<>();
    private static final String TAG = "Wallet";
    GlobalProgressDialogue globalProgressDialogue;
    boolean flag = false;
    Dialog alertdialog;
    private GetBalance getBalance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wallet_fragment, container, false);
        getActivity().setTitle("My Wallet");
        getBalance = new GetBalance(getActivity());
        getBalance.checkbalance(getActivity(), Wallet.this);
        Home ho = (Home) getActivity();
        if (ho.timerflag == true) {
            ho.endTimer();
        }
        // balance_data = (TextView) v.findViewById(R.id.wallet_balance_data);
        if (Validation.checkconnection(getActivity()))
            walletMoneyTransferHistory();
        else
            CommonMethod.showconnectiondialog(getActivity(), Wallet.this);

        // tv = (TextView) v.findViewById(R.id.textView21);
        error = (TextView) v.findViewById(R.id.error_wallet);
        mBal = ((TextView) v.findViewById(R.id.bal));
        mRecycleView = (RecyclerView) v.findViewById(R.id.recycle);
        mobile_no = (EditText) v.findViewById(R.id.wallet_mobile_no_send);
        amount = (EditText) v.findViewById(R.id.wallet_amount_send);
        send_btn = (Button) v.findViewById(R.id.wallet_send_btn);
        balance = SaveSharedPreference.getBalance(getActivity());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CustomAdapter(getActivity(), transferHistories);
        mRecycleView.setAdapter(adapter);
        //alertdialog = new Dialog(getActivity());
        mydb = new MyDataBaseHelper(getActivity(), null, null, 1);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mobile_no.getText().toString().isEmpty())
                    mobile_no.setError("Enter Mobile number");
                else if (amount.getText().toString().isEmpty())
                    amount.setError("Enter the amount");
                else if (Integer.parseInt(amount.getText().toString()) > balance)
                    amount.setError("Enter amout is more than you balance");
                else
                    dialog("Amount Khs. " + amount.getText().toString() + " is transfer to mobile number " + mobile_no.getText().toString() + ".\nAre you sure ?");

            }
        });

        setBalance();

        return v;
    }


    private void walletMoneyTransfer() {
        showDailogue();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "wallet_transfer/transfermoney"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    if (response_object.getBoolean("success")) {
                        successErrordialog(response_object.getString("message"));
                        mBal.setText(response_object.getString("amount"));
                        amount.setText("");
                        mobile_no.setText("");
                        if (Validation.checkconnection(getActivity())) {
                            walletMoneyTransferHistory();
                            getBalance.checkbalance(getActivity(), Wallet.this);
                        } else
                            CommonMethod.showconnectiondialog(getActivity(), Wallet.this);
                    } else {
                        successErrordialog(response_object.getString("message"));
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
                params.put("tosend_no", mobile_no.getText().toString());
                params.put("amount", amount.getText().toString());
                params.put("phone_no", SaveSharedPreference.getPhone(getActivity()));
                return params;
            }


        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
        //return return_arr;
    }

    public void walletMoneyTransferHistory() {
        showDailogue();
        transferHistories.clear();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Wallet_transfer/history"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    if (response_object.getBoolean("success")) {
                        //  successErrordialog(response_object.getString("message"));
                        JSONArray jsonArray = response_object.getJSONArray("user_data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.getJSONObject(i);
                            TransferHistory transferHistory = new TransferHistory();
                            if (!(object.getString("wth_to").substring(3)).equals(SaveSharedPreference.getPhone(getActivity()))) {
                                transferHistory.data = "To : " + object.getString("wth_to").substring(3);
                                transferHistory.text = "Cash Sent";
                                transferHistory.wth_amount = "-" + object.getString("wth_amount");
                            } else {
                                transferHistory.data = "From : " + object.getString("wth_from").substring(3);
                                transferHistory.text = "Cash Recived";
                                transferHistory.wth_amount = "+" + object.getString("wth_amount");

                            }

                            transferHistory.wth_date = object.getString("wth_date") + "   " + object.getString("wth_time");
                            transferHistories.add(transferHistory);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        error.setVisibility(View.VISIBLE);
                        //successErrordialog(response_object.getString("message"));
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
              //  Log.d("auth", headers.toString());
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

    private void dialog(String msg) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Wallet Transfer")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (Validation.checkconnection(getActivity()))
                            walletMoneyTransfer();
                        else
                            CommonMethod.showconnectiondialog(getActivity(), Wallet.this);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }


    private void successErrordialog(String msg) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Transfer Amount")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
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


    void show_dialog(String title, String body) {
        alertdialog.setContentView(R.layout.dialog_common);
        TextView main = (TextView) alertdialog.findViewById(R.id.dialog_common_textview);
        TextView title_tv = (TextView) alertdialog.findViewById(R.id.dialog_common_title);
        Button close_btn = (Button) alertdialog.findViewById(R.id.dialog_common_button);
        title_tv.setText(title);
        main.setText(body);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.cancel();
            }
        });
        alertdialog.show();
    }

    void setBalance() {
        mBal.setText(String.valueOf(SaveSharedPreference.getBalance(getActivity())));
    }
}

/*
class contacts_check extends AsyncTask<Void, Void, Boolean> {

    private String no, data;
    private ArrayList<String> contacts = new ArrayList<>();

    public contacts_check(ArrayList<String> contacts, String no) {
        this.contacts = contacts;
        this.no = no;

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        for (int i = 0; i < contacts.size(); i++) {
            if (no.trim().compareToIgnoreCase(String.valueOf(contacts.get(i)).trim()) == 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Wallet wallet = new Wallet();
        if (aBoolean) {
            wallet.flag = true;
            Log.d("post", aBoolean.toString());
        }
        super.onPostExecute(aBoolean);
    }

}*/
