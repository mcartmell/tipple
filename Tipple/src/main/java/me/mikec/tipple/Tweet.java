package me.mikec.tipple;

public class Tweet {
    String text;
    String fullName;
    String userName;
    String iconURL;
    public Tweet(String txt, String name, String user, String icon) {
        text = txt;
        fullName = name;
        userName = user;
        iconURL = icon;
    }
}
