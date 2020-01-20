package ru.iovchinnikov.mmtp.tracker;

import org.json.simple.JSONObject;
import ru.iovchinnikov.mmtp.tracker.entities.Artifact;
import ru.iovchinnikov.mmtp.tracker.entities.User;

import java.util.Map;

public class PropertyBuilder {
    private final StringBuilder sb;
    private final JSONObject json;
    private final Artifact curr;
    private final Artifact prev;

    public PropertyBuilder(JSONObject json) {
        sb = new StringBuilder("Tuleap: ");
        this.json = json;
        curr = PropertyWorker.getArtifact((JSONObject) json.get(PropertyWorker.TRACKER_CURRENT));
        prev = PropertyWorker.getArtifact((JSONObject) json.get(PropertyWorker.TRACKER_PREVIOUS));
        System.out.println(json);
    }

    public PropertyBuilder newLine() {
        sb.append("\n");
        return this;
    }

    public PropertyBuilder appendCustom(String s) {
        sb.append(s);
        return this;
    }

    public PropertyBuilder appendUser() {
        Map user = (Map) json.get(PropertyWorker.TRACKER_USER);
        User trackerUser = PropertyWorker.getUser(user);
        sb.append("user: ")
                .append(trackerUser.toString());
        return this;
    }

    public PropertyBuilder appendAction() {
        sb.append(PropertyWorker.getAction(json));
        this.appendCustom("d ");
        return this;
    }

    public PropertyBuilder appendArtifactRef() {
        sb.append(curr.toString());
        return this;
    }

    public PropertyBuilder appendDetailsIfChanged() {
        boolean isChange = PropertyWorker.isUpdDetails(curr, prev);
        if (isChange) {
            if ("".equals(PropertyWorker.getDetails(curr))) {
                sb.append("removed details");
                return this;
            }
            boolean isNewDetails = "".equals(PropertyWorker.getDetails(prev));
            sb.append(String.format("%s details: '%s'",
                    ((isNewDetails) ? "added" : "changed"),
                    PropertyWorker.getDetails(curr)));
        }
        return this;
    }

    // tried to work around creating and updating comments,
    // found out, that current-previous difference is not about the each comment,
    // but about all artifact's comments (or submit json's)
    public PropertyBuilder appendCommentIfExists() {
        String current = PropertyWorker.getComment(curr);
        if ("".equals(current) || !PropertyWorker.isUpdComment(curr, prev)) return this;
        sb.append(String.format("added/updated a comment: '%s'",
                current));
        return this;
    }

    public PropertyBuilder appendAssigneeIfChanged() {
        User assignee = PropertyWorker.getAssignee(curr);
        boolean isChange = PropertyWorker.isUpdAssignee(curr, prev);
        if (isChange) {
            if (assignee.getLogin() == null) {
                sb.append("removed assignee");
                return this;
            }
            boolean isNewAssignee = PropertyWorker.getAssignee(prev).getLogin() == null;
            sb.append(String.format("%s assignee: %s",
                    ((isNewAssignee) ? "added" : "changed"),
                    assignee.toString()));
        }
        return this;
    }

    public String getResult() {
        return sb.toString();
    }
}
