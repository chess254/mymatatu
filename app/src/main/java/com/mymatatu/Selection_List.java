package com.mymatatu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.FilterSingleton;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Background.CancelBooking;
import com.mymatatu.Background.GetBalance;
import com.mymatatu.CardView_Picking.ItemAdapter;
import com.mymatatu.CustomDataTypes.Item;
import com.mymatatu.CustomDataTypes.Sacco_filter_item;
import com.mymatatu.Global.AppConstant;
import com.mymatatu.Global.GlobalProgressDialogue;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.VolleyEssentials.MySingleton;
import com.mymatatu.model.CountryCityRSM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 03-07-2017.
 */

public class Selection_List extends Fragment {

    GetBalance getBalance;
    private String source, dest;
    private CardView cv;
    private RecyclerView rv;
    private RecyclerView.LayoutManager mLayoutManager;
    private String to, from;
    private FragmentManager fm;
    private ItemAdapter adapter;
    private ArrayList<CountryCityRSM> countyData, cityData;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeContainer;
    String S_name = "1", Stage_name = "1", No_seats = "10", queue = "5", price = "500";
    final String TAG = this.getClass().getSimpleName();
    //    ArrayList<Item> itemlist = new ArrayList<>();
    private ArrayList<Sacco_filter_item> sacconame_all = new ArrayList<>();
    private ArrayList<Sacco_filter_item> sacconame_all_unique = new ArrayList<>();
    private String s_names[] = {"sacco1", "sacco1", "sacco1", "sacco1", "sacco1"};
    private ArrayList<Integer> selected_saccos = new ArrayList<>();
    private FilterSingleton filterSingleton;
    private String screen = "";
    private int s, d;
    //private RegionDatabaseHelper rdb;
    private GlobalProgressDialogue globalProgressDialogue;
    private CancelBooking cb;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.selection_list, container, false);
        if (Validation.checkconnection(getActivity())) {
            Bundle bundle = getArguments();
            countycityrequest();
            cb = new CancelBooking(getActivity());
            cb.sendCancelRequest();
            //  rdb = new RegionDatabaseHelper(getActivity(), null, null, 1);
            getBalance = new GetBalance(getActivity());
            getBalance.checkbalance(getActivity(), this);
            getActivity().setTitle("Select A Matatu");
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootview.getWindowToken(), 0);
            Home ho = (Home) getActivity();
            ho.hidekeyboardontouch();
            if (ho.timerflag == true) {
                ho.endTimer();
            }
            //Params sending
            //String sacconames_filter[] = new String[];
            //price asce or desc to the backend
            source = SaveSharedPreference.getSource(getActivity());
            dest = SaveSharedPreference.getDestination(getActivity());
            screen = bundle.getString("screen");
            s = /*rdb.getCityId(source)*/ Integer.parseInt(findId(source + "", cityData));
            d = Integer.parseInt(findId(dest + "", cityData));
//            s = bundle.getInt("s");
//            d = bundle.getInt("d");
            selected_saccos = bundle.getIntegerArrayList("selected");
            filterSingleton = FilterSingleton.getInstance(getActivity());
            //final ListView listview_filter = (ListView) rootview.findViewById(R.id.lv1);
            //Recycler View COde Here
            cv = (CardView) rootview.findViewById(R.id.cvitem);
            rv = (RecyclerView) rootview.findViewById(R.id.my_rvitem);
            fab = (FloatingActionButton) rootview.findViewById(R.id.floatingActionButton);
            swipeContainer = (SwipeRefreshLayout) rootview.findViewById(R.id.swipeContainer);


            if (screen.equals("route")) {
                showDailogue();
                filterSingleton.allFilters = new ArrayList<>();
                filterSingleton.allItem = new ArrayList<>();
                filterSingleton.allItem_filter = new ArrayList<>();
//           clearData();
                // callAPi();
            } else if (screen.equals("filter")) {
                Collections.sort(filterSingleton.allItem, new Comparator<Item>() {
                    public int compare(Item s1, Item s2) {
                        return s1.t5.compareToIgnoreCase(s2.t5);
                    }
                });
                if (filterSingleton.allItem_filter.size() == 0) {
                    if (filterSingleton.asec == true) {
                        Collections.sort(filterSingleton.allItem, new Comparator<Item>() {
                            public int compare(Item s1, Item s2) {
                                return s1.t5.compareToIgnoreCase(s2.t5);
                            }
                        });
                    } else {
                        Collections.sort(filterSingleton.allItem, new Comparator<Item>() {
                            public int compare(Item s1, Item s2) {
                                return -1 * (s1.t5.compareToIgnoreCase(s2.t5));
                            }
                        });
                    }
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    adapter = new ItemAdapter(getActivity(), filterSingleton.allItem);
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(mLayoutManager);

                } else {
                    if (filterSingleton.asec == true) {
                        Collections.sort(filterSingleton.allItem_filter, new Comparator<Item>() {
                            public int compare(Item s1, Item s2) {
                                return s1.t5.compareToIgnoreCase(s2.t5);
                            }
                        });
                    } else {
                        Collections.sort(filterSingleton.allItem_filter, new Comparator<Item>() {
                            public int compare(Item s1, Item s2) {
                                return -1 * (s1.t5.compareToIgnoreCase(s2.t5));
                            }
                        });
                    }
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    adapter = new ItemAdapter(getActivity(), filterSingleton.allItem_filter);
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(mLayoutManager);
                }

            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (filterSingleton.allItem.size() != 0) {
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.main_frame, new Filter_fragment()).addToBackStack("filters").commit();
                    }
                }
            });
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    showDailogue();
                    filterSingleton.asec = false;
                    filterSingleton.desc = false;
                    filterSingleton.allFilters = new ArrayList<>();
                    filterSingleton.allItem = new ArrayList<>();
                    filterSingleton.allItem_filter = new ArrayList<>();
                    callAPi();

                }
            });


            //  rv.setHasFixedSize(true);
            //JSON VOLLEY CODE

            //populating the List View for the filters

        } else {
            CommonMethod.showconnectiondialog(getActivity(), this);
        }
        return rootview;
    }


    public void countycityrequest() {

        try {
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

                }
            }

            if (cityArray.length() > 0) {
                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject jo_countycity = cityArray.getJSONObject(i);
                    cityData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name"), jo_countycity.getString("id_c")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String findId(String county_string, ArrayList<CountryCityRSM> countyData) {

        String id = "";
        for (int i = 0; i < countyData.size(); i++) {
            if (county_string.equals(countyData.get(i).name)) {
                id = countyData.get(i).id;
                break;
            }
        }
        return id;
    }


    public void callAPi() {
        if (Validation.checkconnection(getActivity()))
            sendrequest(s, d);
        else
            CommonMethod.showconnectiondialog(getActivity(), Selection_List.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        filterSingleton.allItem.clear();
        callAPi();
    }

    public void clearData() {
        filterSingleton.allItem.clear(); //clear list
        adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
    }

    public void sendrequest(final int s, final int d) {
        String url = AppConstant.BASE_URL + "Matatu/viewall";
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Log.d(TAG, response.toString());
                        try {
                            JSONObject response_object = new JSONObject(response);
                            JSONArray responseja = new JSONArray();
                            dismissProgressDailogue();
                            if (response_object.getBoolean("success")) {
                                responseja = response_object.getJSONArray("user_data");
                                filterSingleton.allFilters = new ArrayList<>();
                                for (int i = 0; i < responseja.length(); i++) {
                                    try {
                                        JSONObject data = responseja.getJSONObject(i);
                                        Item item = new Item();
                                        Sacco_filter_item sfc = new Sacco_filter_item();
                                        item.sapc_id = data.getInt("sacp_id");
                                        item.id = data.getInt("sacco_id");
                                        item.m_id = data.getInt("matatu_id");
                                        item.t1 = data.getString("matatu_name");
                                        item.t2 = data.getString("sacco_name");
                                        String path = data.getString("source") + " To " + data.getString("destination");
                                        item.t3 = path;
                                        item.seats = data.getInt("matatu_seats");
                                        String seatsavailable = String.valueOf(data.getInt("available")) + " / " + String.valueOf(data.getInt("matatu_seats")) + " Seats Available";
                                        item.t4 = seatsavailable;
                                        item.t5 = data.getString("sacco_price");
                                        sfc.id = data.getInt("sacco_id");
                                        sfc.s_name = data.getString("sacco_name");
                                        sfc.isFilterSelected = true;

                                        if (filterSingleton.allFilters.size() > 0) {
                                            for (int j = 0; j < filterSingleton.allFilters.size(); j++) {
                                                if (filterSingleton.allFilters.get(j).id != sfc.id) {
                                                    filterSingleton.allFilters.add(sfc);
                                                }
                                            }
                                        } else {
                                            filterSingleton.allFilters.add(sfc);
                                        }
                                        filterSingleton.allItem.add(item);

                                    } catch (JSONException e) {
                                     //   Log.d(TAG, "error in parsing");
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                fab.setVisibility(View.GONE);
                              //  Toast.makeText(getActivity(), response_object.getString("message"), Toast.LENGTH_LONG).show();
                            }

                                /*for (int j = 1; j <= 25; j++) {
                                    Sacco_filter_item sfi = new Sacco_filter_item();
                                    sfi.id = j;
                                    sfi.s_name = "sacco" + j;
                                    //sacconame_all_unique.add(sfi);
                                    filterSingleton.allFilters.add(sfi);
                                }*/
                            if (filterSingleton.allItem.size() > 0) {
                                mLayoutManager = new LinearLayoutManager(getActivity());
                                adapter = new ItemAdapter(getActivity(), filterSingleton.allItem);
                                rv.setAdapter(adapter);
                                rv.setLayoutManager(mLayoutManager);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDailogue();
                Toast.makeText(getActivity(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
              //  Log.e(TAG, error.toString());
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
                params.put("source", String.valueOf(s));
                params.put("destination", String.valueOf(d));
             //   Log.d(String.valueOf(s), String.valueOf(d));
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
        swipeContainer.setRefreshing(false);
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
