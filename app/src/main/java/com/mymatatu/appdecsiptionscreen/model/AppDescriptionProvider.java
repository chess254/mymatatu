package com.mymatatu.appdecsiptionscreen.model;



import com.mymatatu.Global.AppConstant;
import com.mymatatu.R;

import java.util.ArrayList;

/**
 * Created by S.V. on 8/1/2017.
 */

public class AppDescriptionProvider {
    public ArrayList<AppDesciptionModel> allAppdescription (){
        ArrayList<AppDesciptionModel> allData = new ArrayList<>();

        AppDesciptionModel appDesciptionModel = new AppDesciptionModel();
        appDesciptionModel.desciption = AppConstant.FIRST_APP_DESCRIPTION;
        appDesciptionModel.imageId = R.drawable.search_appdescription;
        appDesciptionModel.highLightText = AppConstant.HEADER_FIRST_APP_DESCRIPTION;

        allData.add(appDesciptionModel);

        AppDesciptionModel appDesciptionModel2 = new AppDesciptionModel();
        appDesciptionModel2.desciption =AppConstant.SECOND_APP_DESCRIPTION;
        appDesciptionModel2.imageId = R.drawable.selectseat_appdescription;
        appDesciptionModel2.highLightText = AppConstant.HEADER_SECOND_APP_DESCRIPTION;

        allData.add(appDesciptionModel2);


        AppDesciptionModel appDesciptionModel3 = new AppDesciptionModel();
        appDesciptionModel3.desciption = AppConstant.THIRD_APP_DESCRIPTION;
        appDesciptionModel3.imageId = R.drawable.safetravel_appdescription;
        appDesciptionModel3.highLightText = AppConstant.HEADER_THIRD_APP_DESCRIPTION;

        allData.add(appDesciptionModel3);

        AppDesciptionModel appDesciptionModel4 = new AppDesciptionModel();
        appDesciptionModel4.desciption =AppConstant.FOURTH_APP_DESCRIPTION;
        appDesciptionModel4.imageId = R.drawable.earnlimitless_appdescription;
        appDesciptionModel4.highLightText = AppConstant.HEADER_FOURTH_APP_DESCRIPTION;

        allData.add(appDesciptionModel4);

        return allData;
    }
}
