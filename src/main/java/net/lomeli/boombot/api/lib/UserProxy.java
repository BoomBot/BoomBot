package net.lomeli.boombot.api.lib;

import com.google.common.base.Strings;

public class UserProxy {
    private final String userID, userName;
    private String nickName;
    private boolean hasNickName;

    public UserProxy(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public UserProxy(String userID, String userName, String nickname) {
        this(userID, userName);
        this.nickName = nickname;
        this.hasNickName = !Strings.isNullOrEmpty(nickname);
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public boolean hasNickName() {
        return hasNickName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEffectiveName() {
        return hasNickName() ? getNickName() : getUserName();
    }
}
