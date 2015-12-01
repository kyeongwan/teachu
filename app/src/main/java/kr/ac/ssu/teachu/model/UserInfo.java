package kr.ac.ssu.teachu.model;

public class UserInfo {

    public String email;
    public String password;
    public String deviceId;
    public String gcmKey;
    public String token;

    static UserInfo g_userInfo = new UserInfo();

    private UserInfo() {
    }

    public static UserInfo getInstance() {
        return g_userInfo;
    }
}