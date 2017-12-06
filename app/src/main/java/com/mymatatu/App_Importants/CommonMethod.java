package com.mymatatu.App_Importants;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.mymatatu.Home;
import com.mymatatu.Login;
import com.mymatatu.PaymentConfirm;
import com.mymatatu.R;
import com.mymatatu.RegisterActivity;
import com.mymatatu.Route_Selection;
import com.mymatatu.SeatingArrangment1;
import com.mymatatu.SeatingArrangment14;
import com.mymatatu.Selection_List;
import com.mymatatu.SyncContacts;
import com.mymatatu.menu_items.Booking_History;
import com.mymatatu.menu_items.Wallet;

/**
 * Created by anonymous on 24-07-2017.
 */

public class CommonMethod {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showconnectiondialog(final Context c, final Fragment f) {
        final Dialog nointernetdialog;
        nointernetdialog = new Dialog(c);
        nointernetdialog.setContentView(R.layout.no_internet_dialog);
        final Button refresh = (Button) nointernetdialog.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.checkconnection(c)) {
                    if (f != null) {
                        if (f instanceof SeatingArrangment1 || f instanceof SeatingArrangment14 || f instanceof PaymentConfirm || f instanceof Route_Selection) {
                            ;
                        } else if (f instanceof Booking_History)
                            ((Booking_History) f).callAPi();
                        else if (f instanceof Selection_List)
                            ((Selection_List) f).callAPi();
                        else if (f instanceof Wallet)
                            ((Wallet) f).walletMoneyTransferHistory();

                    } else if (c != null) {
                        if (c instanceof Home)
                            ((Home) c).init();
                        else if (c instanceof RegisterActivity)
                           ;
                        else if (c instanceof Login || c instanceof SyncContacts)
                            ;
                    }
                    nointernetdialog.cancel();

                } else
                    nointernetdialog.show();
            }
        });
        nointernetdialog.setCancelable(false);
        nointernetdialog.show();
    }


}
