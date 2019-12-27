package ru.iovchinnikov.mmtp;

import org.json.simple.JSONObject;
import org.omg.PortableInterceptor.DISCARDING;

public class PropertyBuilder {
    private final StringBuilder sb;
    private final JSONObject json;
    private final JSONObject curr;
    private final JSONObject prev;

    PropertyBuilder(JSONObject json) {
        sb = new StringBuilder("Tuleap: ");
        this.json = json;
        curr = (JSONObject) json.get(PropertyWorker.CURRENT_VERSION);
        prev = (JSONObject) json.get(PropertyWorker.PREVIOUS_VERSION);
        System.out.println(json);
    }

    PropertyBuilder newLine() {
        sb.append("\n");
        return this;
    }

    PropertyBuilder appendCustom(String s) {
        sb.append(s);
        return this;
    }

    PropertyBuilder appendUser() {
        sb.append(String.format("user: '[%s](http://sv-noda.risde.ru:8585/users/%s)' ",
                PropertyWorker.getChangesByDisplayName(curr),
                PropertyWorker.getChangesByName(curr)));
        return this;
    }

    PropertyBuilder appendAction() {
        sb.append(PropertyWorker.getAction(json));
        this.appendCustom("d ");
        return this;
    }

    PropertyBuilder appendArtifactRef() {
        sb.append(String.format("artifact: '[%s](http://sv-noda.risde.ru:8585/plugins/tracker/?aid=%s)' ",
                PropertyWorker.getArtifactTitle(curr),
                PropertyWorker.getArtifactId(curr)));
        return this;
    }

    PropertyBuilder appendDetailsIfChanged() {
        boolean isChange = PropertyWorker.isUpdDetails(curr, prev);
        if (isChange) {
            boolean isNewDetails = "".equals(PropertyWorker.getDetails(prev));
            sb.append(String.format("%s details: '%s'",
                    ((isNewDetails) ? "added" : "updated"),
                    PropertyWorker.getDetails(curr)));
        }
        return this;
    }

    // tried to work around creating and updating comments,
    // found out, that current-previous difference is not about the each comment,
    // but about all artifact's comments (or submit json's)
    PropertyBuilder appendCommentIfExists() {
        String current = PropertyWorker.getComment(curr);
        if ("".equals(current) || !PropertyWorker.isUpdComment(curr, prev)) return this;
        sb.append(String.format("added/updated a comment: '%s'",
                current));
        return this;
    }

    String getResult() {
        return sb.toString();
    }
}
