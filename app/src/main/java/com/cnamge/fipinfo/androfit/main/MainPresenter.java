package com.cnamge.fipinfo.androfit.main;

import android.view.View;

class MainPresenter {

    private MainInterface mainInterface;
    private View currentButtonSelected;

    MainPresenter(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
    }

    void onBottomBarAddButtonClick(){
        mainInterface.showMessage("onBottomBarAddButtonClick");
    }

    void onBottomBarSessionsButtonClick(View view){
        this.currentButtonSelected = view;
        mainInterface.selectView(view);
    }

    void onBottomBarMealsButtonClick(View view){
        this.currentButtonSelected = view;
        mainInterface.selectView(view);
    }

    void onBottomBarFriendsButtonClick(View view){
        this.currentButtonSelected = view;
        mainInterface.selectView(view);
    }

    void onBottomBarSettingsButtonClick(View view){
        this.currentButtonSelected = view;
        mainInterface.selectView(view);
    }

    // Presenter life cycle
    void onResume() {
        mainInterface.selectView(this.currentButtonSelected);
    }

    void onDestroy() {
        mainInterface = null;
    }
}

