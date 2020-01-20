package ru.iovchinnikov.mmtp.tracker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.iovchinnikov.mmtp.tracker.entities.Artifact;
import ru.iovchinnikov.mmtp.tracker.entities.User;

import java.util.Map;

public class PropertyWorker {
    public static final String TRACKER_CURRENT = "current";
    public static final String TRACKER_PREVIOUS = "previous";
    public static final String TRACKER_ACTION = "action";
    public static final String TRACKER_USER = "user";

    public static final String ARTIFACT_SUBMITTED_ON = "submitted_on";
    public static final String ARTIFACT_SUBMITTED_BY = "submitted_by_details";
    public static final String ARTIFACT_VALUES = "values";
    public static final String ARTIFACT_MODIFIED_BY = "last_modified_by";
    public static final String ARTIFACT_MODIFIED_TS = "last_modified_date";
    public static final String ARTIFACT_LAST_COMMENT = "last_comment";

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

    static boolean isUpdAssignee(Artifact curr, Artifact prev) {
        // my eyes bleed when i see this, but it's late already.
        // that's why it's a bad idea to swallow NPE's in frameworks
        String plogin = getAssignee(prev).getLogin();
        String clogin = getAssignee(curr).getLogin();
        if (plogin == null && clogin != null) return true;
        if (plogin != null && clogin == null) return true;
        else return !plogin.equals(clogin);
    }

    // TODO yet works with only one assignee
    static User getAssignee(Artifact artifact) {
        return getUser((Map) ((JSONArray) ((Map) artifact.getValues().get(ASSIGNED_TO)).get("values")).get(0));
    }

    static boolean isUpdComment(Artifact curr, Artifact prev) {
        return !getComment(curr).equals(getComment(prev));
    }

    static String getComment(Artifact json) {
        return (String) json.getComment().get("body");
    }

    static boolean isUpdDetails(Artifact curr, Artifact prev) {
        return !getDetails(curr).equals(getDetails(prev));
    }

    static String getDetails(Artifact json) {
        return (String) ((Map) json.getValues().get(DETAILS)).get("value");
    }

    static String getAction(JSONObject json) {
        return (String) json.get(TRACKER_ACTION);
    }

    static User getChangedBy(JSONObject json) {
        return getUser((JSONObject) json.get(ARTIFACT_MODIFIED_BY));
    }

    static User getSubmittedBy(JSONObject json) {
        return getUser((JSONObject) json.get(ARTIFACT_SUBMITTED_BY));
    }

    public static User getUser(Map user) {
        return new User((String) user.get("ldap_id"),
                (boolean) user.get("has_avatar"),
                (String) user.get("avatar_url"),
                (boolean) user.get("is_anonymous"),
                (String) user.get("user_url"),
                (String) user.get("real_name"),
                (long) user.get("id"),
                (String) user.get("display_name"),
                (String) user.get("uri"),
                (String) user.get("username"));
    }

    public static Artifact getArtifact(JSONObject art) {
        return new Artifact((String) art.get("submitted_on"),
                getUser((Map) art.get("submitted_by_details")),
                (JSONArray) art.get("values"),
                (Map) art.get("last_comment"),
                (long) art.get("id"),
                (String) art.get("last_modified_date"),
                getUser((Map) art.get("last_modified_by")));
    }
}
