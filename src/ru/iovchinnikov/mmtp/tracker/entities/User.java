package ru.iovchinnikov.mmtp.tracker.entities;

public class User {
    private String ldapId;
    private boolean hasAvatar;
    private String avatarURL;
    private boolean isAnonymous;
    private String userURL;
    private String realName;
    private long id;
    private String displayName;
    private String uri;
    private String login;

    public User(String ldapId, boolean hasAvatar, String avatarURL, boolean isAnonymous, String userURL, String realName, long id, String displayName, String uri, String login) {
        this.ldapId = ldapId;
        this.hasAvatar = hasAvatar;
        this.avatarURL = avatarURL;
        this.isAnonymous = isAnonymous;
        this.userURL = userURL;
        this.realName = realName;
        this.id = id;
        this.displayName = displayName;
        this.uri = uri;
        this.login = login;
    }

    public String getLdapId() {
        return ldapId;
    }

    public void setLdapId(String ldapId) {
        this.ldapId = ldapId;
    }

    public boolean isHasAvatar() {
        return hasAvatar;
    }

    public void setHasAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getUserURL() {
        return userURL;
    }

    public void setUserURL(String userURL) {
        this.userURL = userURL;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return String.format("'[%s](http://sv-noda.risde.ru:8585%s)' ", getDisplayName(), getUserURL());
    }
}
