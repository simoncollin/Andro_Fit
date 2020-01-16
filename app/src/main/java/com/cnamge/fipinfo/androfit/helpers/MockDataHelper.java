package com.cnamge.fipinfo.androfit.helpers;

import android.util.Log;

import com.cnamge.fipinfo.androfit.model.FriendRequest;
import com.cnamge.fipinfo.androfit.model.Meal;
import com.cnamge.fipinfo.androfit.model.Session;
import com.cnamge.fipinfo.androfit.model.User;
import com.orm.SugarRecord;

public class MockDataHelper {

    public static User currentUser;

    public static void setup(){
        try {
        deleteAllData();
        mockUsers();
        mockSessions();
        mockMeals();
        } catch (Exception e){
            Log.e("MockDataHelper", "erreur durant le setup");
        }
    }

    private static void setCurrentUser(User currentUser) {
        MockDataHelper.currentUser = currentUser;
    }

    private static void mockSessions() throws Exception{
            new Session(
                    "Jogging",
                    "Coulée verte",
                    1580549400,
                    1580553000,
                    "30 mn fractionné",
                    0,
                    MockDataHelper.currentUser
            ).save();
            new Session(
                    "Natation",
                    "Piscine des Thiolettes",
                    1581231600	,
                    1581235200,
                    "30 mn crawl & 30mn palmes",
                    0,
                    MockDataHelper.currentUser
            ).save();
            new Session(
                    "Musculation",
                    "Basic Fit Cormontreuil",
                    1583348400	,
                    1583352000	,
                    "Full abdo et biceps",
                    0,
                    MockDataHelper.currentUser
            ).save();
    }

    private static void mockMeals(){
            new Meal(
                    "pâte bolognese",
                    1579523400	,
                    1579523400	,
                    "Faire la bolognese maison",
                    "",
                    MockDataHelper.currentUser
            ).save();
            new Meal(
                    "Salade césar",
                    1579636800	,
                    1579636800	,
                    "Vinaigrette a base d'aïoli",
                    "",
                    MockDataHelper.currentUser
            ).save();
            new Meal(
                    "Céréales maison",
                    1579676400	,
                    1579676400	,
                    "Ne PAS mettre de chocolat !",
                    "",
                    MockDataHelper.currentUser
            ).save();
    }

    private static void mockUsers()  throws Exception{
        User u1 = new User("Toto");
        u1.save();
        setCurrentUser(u1);

        User u2 = new User("Matthieu");
        User u3 = new User("Alix");
        User u4 = new User("Yacine");
        User u5 = new User("Simon");

        u2.save();
        u3.save();
        u4.save();
        u5.save();

        mockSessionsForFriend(u2);
        mockSessionsForFriend(u3);
        mockSessionsForFriend(u4);
        mockSessionsForFriend(u5);

        mockMealsForFriend(u2);
        mockMealsForFriend(u3);
        mockMealsForFriend(u4);
        mockMealsForFriend(u5);

        mockFriendRequests(u5, u2, u3, u4);
    }

    private static void mockFriendRequests(User u1, User u2, User u3, User u4){
        new FriendRequest(MockDataHelper.currentUser, null, u1).save();
        new FriendRequest(MockDataHelper.currentUser, null, u2).save();
        new FriendRequest(MockDataHelper.currentUser, null, u3).save();
        new FriendRequest(MockDataHelper.currentUser, null, u4).save();
    }

    private static void mockSessionsForFriend(User user) throws Exception{
        new Session(
                "Natation",
                "Piscine des Thiolettes",
                1581231600	,
                1581235200,
                "30 mn crawl & 30mn palmes",
                0,
                user
        ).save();
        new Session(
                "Musculation",
                "Basic Fit Cormontreuil",
                1583348400	,
                1583352000	,
                "Full abdo et biceps",
                0,
                user
        ).save();
    }

    private static void mockMealsForFriend(User user){
        new Meal(
                "Riz Cantonais",
                1579523400	,
                1579523400	,
                "Commander chez Intercuisto",
                "",
                user
        ).save();
        new Meal(
                "Salade césar",
                1579636800	,
                1579636800	,
                "Vinaigrette a base d'aïoli",
                "",
                user
        ).save();
    }

    private static void deleteAllData(){
        SugarRecord.deleteAll(Meal.class);
        SugarRecord.deleteAll(FriendRequest.class);
        SugarRecord.deleteAll(Session.class);
        SugarRecord.deleteAll(User.class);
    }
}
