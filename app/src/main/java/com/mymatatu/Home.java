package com.mymatatu;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Background.CancelBooking;
import com.mymatatu.Background.GetBalance;
import com.mymatatu.CustomDataTypes.Item;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.menu_items.AboutFragment;
import com.mymatatu.menu_items.Booking_History;
import com.mymatatu.menu_items.Feedback_fragment;
import com.mymatatu.menu_items.Synced_Contacts;
import com.mymatatu.menu_items.Wallet;
import com.mymatatu.menu_items.profile_fragment;

import java.util.ArrayList;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PaymentConfirm paymentConfirmfragment;
    private CardView cv;
    CancelBooking cb;
    private RecyclerView rv;
    private RecyclerView.LayoutManager mLayoutManager;
    private String to, from, screen = "Home";
    int balance;
    public boolean timerflag = false;
    FrameLayout activity_home;
    //DATA
    String S_name = "1", Stage_name = "1", No_seats = "10", queue = "5", price = "500";
    final String TAG = this.getClass().getSimpleName();
    ArrayList<Item> itemlist;
    private android.app.FragmentManager fm = getFragmentManager();
    DrawerLayout drawer;
    public CountDownTimer ct;
    TextView navbar_name, navbar_phone;
    GetBalance getBalance;
    public TextView navbar_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

    }


    public void init() {
        activity_home = (FrameLayout) findViewById(R.id.main_frame);
        cb = new CancelBooking(Home.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intent = getIntent();
        getBalance = new GetBalance(this);
        screen = intent.getStringExtra("screen");
        //this.deleteDatabase("bookinghistory.db");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if (Validation.checkconnection(Home.this)) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = null;
            //if (navbar_name != null)
            // headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);
            navigationView.setNavigationItemSelectedListener(this);


            // Setting the Nav_header items and call for balance
            navbar_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navbar_name);
            navbar_phone = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navbar_phone);
            navbar_balance = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navbar_wallet_balace);
            navbar_name.setText(SaveSharedPreference.getName(getApplicationContext()));
            navbar_phone.setText("+254 " + SaveSharedPreference.getPhone(getApplicationContext()));
            getBalance.checkbalance(this,null);
            navbar_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.closeDrawer(GravityCompat.START);
                    fm.beginTransaction().replace(R.id.main_frame, new profile_fragment())
                            .addToBackStack("profile").commit();
                }
            });
            navbar_balance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.closeDrawer(GravityCompat.START);
                    fm.beginTransaction().replace(R.id.main_frame, new Wallet())
                            .addToBackStack("Wallet").commit();
                }
            });

            Bundle bundle = new Bundle();
            bundle.putString("screen", "home");
            Route_Selection rs = new Route_Selection();
            rs.setArguments(bundle);
            fm.beginTransaction().replace(R.id.main_frame, new Route_Selection()).commit();
        } else {
            CommonMethod.showconnectiondialog(Home.this, null);
        }
    }

    public void hidekeyboardontouch() {
        activity_home.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                CommonMethod.hideSoftKeyboard(Home.this);
                return false;
            }
        });
    }

    public void setBalance(String s) {
        navbar_balance.setText(s);
    }

    @Override
    public void onBackPressed() {

        Fragment f = getFragmentManager().findFragmentById(R.id.main_frame);

        if (f instanceof SeatingArrangment2) {
            ct.cancel();

        } else if (f instanceof SeatingArrangment1) {

        } else if (f instanceof SeatingArrangment3) {

        } else if (f instanceof SeatingArrangment14) {



        } else if (f instanceof PaymentConfirm) {
            new CancelBooking(this).sendCancelRequest();
        } else if (f instanceof Payment_done) {
            fm.popBackStack("Payment Done", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack("Payment Confirm", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack("Seating Arrangment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
           // fm.popBackStack("Seating Arrangment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
           // fm.popBackStack("filter", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //fm.popBackStack("Selection List", FragmentManager.POP_BACK_STACK_INCLUSIVE); Selection List

        } else if (f instanceof Selection_List) {
            fm.popBackStack("filter", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (f instanceof profile_fragment) {
            fm.popBackStack("changepassword", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack("profile_edit", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            paymentConfirmfragment.backbuttonpressed();

        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.home_menu) {
            removeAllFragments();
            setTitle("My Matatu");
            Intent intent = new Intent(Home.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.profile_menu) {
            removeAllFragments();
            setTitle("My Profile");
            fm.beginTransaction().replace(R.id.main_frame, new profile_fragment())
                    .addToBackStack("profile page").commit();
        } else if (id == R.id.wallet_menu) {
            removeAllFragments();
            setTitle("My Wallet");
            fm.beginTransaction().replace(R.id.main_frame, new Wallet())
                    .addToBackStack("wallter page").commit();

        } else if (id == R.id.booking_history_menu) {
            removeAllFragments();
            setTitle("Booking History");
            fm.beginTransaction().replace(R.id.main_frame, new Booking_History())
                    .addToBackStack("booking history").commit();
        } else if (id == R.id.synced_contacts_menu) {
            removeAllFragments();
            setTitle("Sync Contacts");
            fm.beginTransaction().replace(R.id.main_frame, new Synced_Contacts())
                    .addToBackStack("synced Contacts").commit();

        } else if (id == R.id.feedback_menu) {
            removeAllFragments();
            setTitle("Feedback");
            fm.beginTransaction().replace(R.id.main_frame, new Feedback_fragment())
                    .addToBackStack("feedback").commit();

        } else if (id == R.id.termscondition_menu) {
            removeAllFragments();
            setTitle("Terms and Conditions");
            fm.beginTransaction().replace(R.id.main_frame, new AboutFragment())
                    .addToBackStack("TermsandConditions").commit();

        } else if (id == R.id.help_menu) {
            removeAllFragments();
            setTitle("Help");
            fm.beginTransaction().replace(R.id.main_frame, new Feedback_fragment())
                    .addToBackStack("Help").commit();
        } else if (id == R.id.logout) {
            drawer.closeDrawer(GravityCompat.START);
            dialog();
            return true;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeAllFragments() {
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    private void dialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        SaveSharedPreference.clearUserName(getApplicationContext());
                        Intent intent = new Intent(Home.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        //Log.e("LogoutButtonClicke", "true");

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }


    public void startTimer() {

        timerflag = true;
        ct = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Fragment f = getFragmentManager().findFragmentById(R.id.main_frame);

                if (f instanceof SeatingArrangment1) {
                    SeatingArrangment1 seatingArrangment1 = (SeatingArrangment1) f;
                    seatingArrangment1.timerShow(millisUntilFinished / 1000);
                } else if (f instanceof SeatingArrangment2) {
                    SeatingArrangment2 seatingArrangment2 = (SeatingArrangment2) f;
                    seatingArrangment2.timerShow(millisUntilFinished / 1000);
                } else if (f instanceof SeatingArrangment3) {
                    SeatingArrangment3 seatingArrangment3 = (SeatingArrangment3) f;
                    seatingArrangment3.timerShow(millisUntilFinished / 1000);
                } else if (f instanceof SeatingArrangment14) {
                    SeatingArrangment14 seatingArrangment14 = (SeatingArrangment14) f;
                    seatingArrangment14.timerShow(millisUntilFinished / 1000);
                } else if (f instanceof PaymentConfirm) {
                    PaymentConfirm p = (PaymentConfirm) f;
                    p.timerShow(millisUntilFinished / 1000);

                }
            }

            @Override
            public void onFinish() {
                cb.sendCancelRequest();
                Toast.makeText(getApplication(), "Your timer timed out \n Please try Again", Toast.LENGTH_LONG).show();
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.main_frame, new Route_Selection())
                        .addToBackStack("timeout-route").commit();


            }
        }.start();
    }

    public void endTimer() {
        timerflag = false;
        ct.cancel();
    }

}
