package com.mymatatu.menu_items;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mymatatu.R;

/**
 * Created by deception on 07/08/17.
 */

public class AboutFragment extends Fragment {
    TextView dataView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.aboutfragment ,container,false);
        getActivity().setTitle("Terms And Condition");
        String data = "<ul>" +
                "<li>App download is chargeable at all the time. </li>" +
                "<li>In case of mobile lost, damages or any miss happening the App company has not taken any such losses and in that case you need the buy the App. </li>" +
                "<li>The company will not be responsible for no internet connection, mobile not supporting the format to download the app etc. </li>" +
                "<li>The phone must be the Android to download the app. </li>" +
                "<li>App can be use only the risk of customer. </li>" +
                "<li>To sync new contact, at all the time you need to buy the App and pay for it, during this if any other body if sync your contact with their app, the company will not be responsible for such things. </li>" +
                "<li>The company will do the bookings of destination, the company shall not be responsible any type of other losses may happen before or during or after your journey complete. </li>" +
                "<li>App only can be use when the internet working, otherwise the company will not take any responsibility thereafter. </li>" +
                "<li>The wallet money only can be use for book the ticket, not for cash purposes. </li>" +
                "<li>The commission will be credit your wallet account only and only can be use for ticket booking or company other product buying purposes. </li>" +
                "<li>The company can send you promotional messages, advertising of new products etc to your contact number all the time. </li>" +
                "<li>The wallet can only use for your next of kin. </li>" +
                "<li>The wallet can be only fill after safaricom applicable charges the time of you fill the wallet. </li>" +
                "<li>The prices of the matatus can be up or down at any time without prior notice according to the Matatu price change. </li>" +
                "<li>The ticket amount will be deduct from your wallet immediately after any one book the ticket, can not be transfer to other, but it can transfer to other person same day before the booked matatus checked out from the station. </li>" +
                "<li>There is no refund of any amount at any circumstances. </li>" +
                "<li>The company can change the rules and policies at any time without any prior notice. </li>" +
                "<li>After book the ticket, rest issues only can be short out by SACCO members or matatus owner or current driver, the App company has no responsibilities such as break down middle of the journey, accident, arrest by police, weather disturbances, strikes, over speed, inspection issues, government issues and all other (not captured). </li>" +
                "<li>All the availability of matatu and it seats prior to fast come or fast book vs fast serve basis. </li>" +
                "<li>The company reserves all the rights of admission. </li>"+
                "</ul>";
        dataView = (TextView) v.findViewById(R.id.data);
        dataView.setText(Html.fromHtml(data));


        return v;
    }
}
