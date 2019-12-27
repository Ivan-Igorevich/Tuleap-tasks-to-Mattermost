package ru.iovchinnikov.mmtp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class PropertyWorker {
    public static final String CURRENT_VERSION = "current";
    public static final String PREVIOUS_VERSION = "previous";

    public static final String ACTION = "action";
    public static final String CHANGES_BY = "last_modified_by";     //(String) ((Map) json.get(CHANGES_BY)).get("display_name")
    public static final String CHANGES_TS = "last_modified_date";
    public static final String LAST_COMMENT = "last_comment";       //(String) ((Map) json.get(LAST_COMMENT)).get("body")
    public static final String ARTIFACT_VALUES = "values";          //(JSONArray) json.get(ARTIFACT_VALUES);
    public static final String USER_DISPLAY_NAME = "display_name";
    public static final String USER_NAME = "username";

    // artifact value indexes
    public static final int TITLE = 0;             // (String) ((Map) values.get(TITLE)).get("value");
    public static final int TYPE = 1;              // (String) (((Map) ((JSONArray) ((Map) values.get(TYPE)).get("values")).get(0)).get("label"));
    public static final int ASSIGNED_TO = 2;       // (String) (((Map) ((JSONArray) ((Map) values.get(ASSIGNED_TO)).get("values")).get(0)).get("display_name"));
    public static final int STATUS = 3;            // (String) (((Map) ((JSONArray) ((Map) values.get(STATUS)).get("values")).get(0)).get("label"));
    public static final int LINKS = 4;
    public static final int ARTIFACT_ID = 5;       // (String) ((Map) values.get(ARTIFACT_ID)).get("value");
    public static final int LAST_UPDATE_TS = 6;    // (String) ((Map) values.get(LAST_UPDATE_TS)).get("value");
    public static final int CROSS_REFS = 7;
    public static final int SUBMITTED_TS = 8;
    public static final int REMAINING_EFFORT = 9;
    public static final int LAST_UPDATE_BY = 10;
    public static final int DETAILS = 11;          // (String) ((Map) values.get(DETAILS)).get("value");
    public static final int SUBMITTED_BY = 12;

    static String getChangesByDisplayName(JSONObject json) {
        return (String) ((Map) json.get(CHANGES_BY)).get(USER_DISPLAY_NAME);
    }

    static String getChangesByName(JSONObject json) {
        return (String) ((Map) json.get(CHANGES_BY)).get(USER_NAME);
    }

    static String getAction(JSONObject json) {
        return (String) json.get(ACTION);
    }

    static String getArtifactId(JSONObject json) {
        return ((Map) ((JSONArray) json.get(ARTIFACT_VALUES)).get(ARTIFACT_ID)).get("value").toString();
    }

    static String getArtifactTitle(JSONObject json) {
        return (String) ((Map) ((JSONArray) json.get(ARTIFACT_VALUES)).get(TITLE)).get("value");
    }

    static String getDetails(JSONObject json) {
        return (String) ((Map) ((JSONArray) json.get(ARTIFACT_VALUES)).get(DETAILS)).get("value");
    }

    static boolean isUpdDetails(JSONObject curr, JSONObject prev) {
        return getDetails(curr).equals(getDetails(prev));
    }

}
