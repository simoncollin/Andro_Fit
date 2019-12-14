package com.cnamge.fipinfo.androfit.model;

import com.orm.SugarRecord;

public class FriendRequest extends SugarRecord<FriendRequest> {
    private User user;
    private Boolean accepted;
    private User friend;

    public FriendRequest() {}

    public FriendRequest(User user, Boolean accepted, User friend) {
        this.user = user;
        this.accepted = accepted;
        this.friend = friend;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
            "user='" + user + '\'' +
            ", accepted='" + accepted + '\'' +
            ", friend=" + user +
            ", id=" + id +
            '}';
    }
}
