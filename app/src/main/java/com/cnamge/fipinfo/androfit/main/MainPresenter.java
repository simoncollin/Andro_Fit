package com.cnamge.fipinfo.androfit.main;

import android.view.View;

class MainPresenter {

    enum FragmentType {
        SESSIONS,
        MEALS,
        FRIENDS,
        SETTINGS
    }

    private MainInterface mainInterface;
    private View currentButtonSelected;
    private FragmentType currentFragment;

    MainPresenter(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
    }

    void onBottomBarAddButtonClick(){
        switch (this.currentFragment) {
            case SESSIONS:
                mainInterface.addSession();
                break;
            case MEALS:
                mainInterface.addMeal();
                break;
            case FRIENDS:
                mainInterface.showMessage("Bottom Bar button clicked : FRIENDS");
                break;
            case SETTINGS:
                mainInterface.showMessage("Bottom Bar button clicked : SETTINGS");
                break;
        }
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

    public void setCurrentFragment(FragmentType currentFragment) {
        this.currentFragment = currentFragment;
    }
}

