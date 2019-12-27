package ru.iovchinnikov.mmtp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class PropertyWorker {
    public static final String CURRENT_VERSION = "current";
    public static final String PREVIOUS_VERSION = "previous";

    public static final String ACTION = "action";
    public static final String CHANGES_BY = "last_modified_by";
    public static final String CHANGES_TS = "last_modified_date";
    public static final String LAST_COMMENT = "last_comment";
    public static final String ARTIFACT_VALUES = "values";
    public static final String USER_DISPLAY_NAME = "display_name";
    public static final String USER_NAME = "username";

    // TODO a ref-library of id's and fields or label-based search
    // artifact value indexes (we need to get rid of this, values are hardcoded for this current tracker)
    public static final int TITLE = 0;
    public static final int TYPE = 1;              // (String) (((Map) ((JSONArray) ((Map) values.get(TYPE)).get("values")).get(0)).get("label"));
    public static final int ASSIGNED_TO = 2;
    public static final int STATUS = 3;            // (String) (((Map) ((JSONArray) ((Map) values.get(STATUS)).get("values")).get(0)).get("label"));
    public static final int LINKS = 4;
    public static final int ARTIFACT_ID = 5;
    public static final int LAST_UPDATE_TS = 6;    // (String) ((Map) values.get(LAST_UPDATE_TS)).get("value");
    public static final int CROSS_REFS = 7;
    public static final int ATTACHMENTS = 8;
    public static final int SUBMITTED_TS = 9;
    public static final int REMAINING_EFFORT = 10;
    public static final int LAST_UPDATE_BY = 11;
    public static final int DETAILS = 12;
    public static final int SUBMITTED_BY = 13;

    static boolean isUpdAssignee(JSONObject curr, JSONObject prev) {
        // my eyes bleed when i see this, but it's late already.
        // that's why it's a bad idea to swallow NPE's in frameworks
        String plogin = getAssigneeLogin(prev);
        String clogin = getAssigneeLogin(curr);
        if (plogin == null && clogin != null) return true;
        if (plogin != null && clogin == null) return true;
        else return !plogin.equals(clogin);
    }

    // TODO yet works with only one assignee
    static String getAssigneeLogin(JSONObject json) {
        return (String) (((Map) ((JSONArray) ((Map) ((JSONArray) json.get(ARTIFACT_VALUES)).get(ASSIGNED_TO)).get("values")).get(0)).get("username"));
    }

    static String getAssigneeDisplayName(JSONObject json) {
        return (String) (((Map) ((JSONArray) ((Map) ((JSONArray) json.get(ARTIFACT_VALUES)).get(ASSIGNED_TO)).get("values")).get(0)).get("display_name"));
    }

    static boolean isUpdComment(JSONObject curr, JSONObject prev) {
        return !getComment(curr).equals(getComment(prev));
    }

    static String getComment(JSONObject json) {
        return (String) ((Map) json.get(LAST_COMMENT)).get("body");
    }

    static boolean isUpdDetails(JSONObject curr, JSONObject prev) {
        return !getDetails(curr).equals(getDetails(prev));
    }

    static String getDetails(JSONObject json) {
        return (String) ((Map) ((JSONArray) json.get(ARTIFACT_VALUES)).get(DETAILS)).get("value");
    }

    static String getArtifactTitle(JSONObject json) {
        return (String) ((Map) ((JSONArray) json.get(ARTIFACT_VALUES)).get(TITLE)).get("value");
    }

    static String getArtifactId(JSONObject json) {
        return ((Map) ((JSONArray) json.get(ARTIFACT_VALUES)).get(ARTIFACT_ID)).get("value").toString();
    }

    static String getAction(JSONObject json) {
        return (String) json.get(ACTION);
    }

    static String getChangedByLogin(JSONObject json) {
        return (String) ((Map) json.get(CHANGES_BY)).get(USER_NAME);
    }

    static String getChangesByDisplayName(JSONObject json) {
        return (String) ((Map) json.get(CHANGES_BY)).get(USER_DISPLAY_NAME);
    }
}
