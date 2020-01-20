package ru.iovchinnikov.mmtp.tracker.entities;

import org.json.simple.JSONArray;
import ru.iovchinnikov.mmtp.tracker.PropertyWorker;

import java.util.Map;

public class Artifact {
    private String submittedTs;
    private User submittedBy;
    private JSONArray values;
    private Map comment;
    private long id;
    private String changedTs;
    private User changedBy;

    public Artifact(String submittedTs, User submittedBy, JSONArray values, Map comment, long id, String changedTs, User changedBy) {
        this.submittedTs = submittedTs;
        this.submittedBy = submittedBy;
        this.values = values;
        this.comment = comment;
        this.id = id;
        this.changedTs = changedTs;
        this.changedBy = changedBy;
    }

    public String getSubmittedTs() {
        return submittedTs;
    }

    public void setSubmittedTs(String submittedTs) {
        this.submittedTs = submittedTs;
    }

    public User getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(User submittedBy) {
        this.submittedBy = submittedBy;
    }

    public JSONArray getValues() {
        return values;
    }

    public void setValues(JSONArray values) {
        this.values = values;
    }

    public Map getComment() {
        return comment;
    }

    public void setComment(Map comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChangedTs() {
        return changedTs;
    }

    public void setChangedTs(String changedTs) {
        this.changedTs = changedTs;
    }

    public User getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(User changedBy) {
        this.changedBy = changedBy;
    }

    @Override
    public String toString() {
        return String.format("artifact: '[%s](http://sv-noda.risde.ru:8585/plugins/tracker/?aid=%s)' ",
                ((Map) (getValues().get(0))).get("value"),
                ((Map) (getValues().get(5))).get("value").toString());
    }
}
