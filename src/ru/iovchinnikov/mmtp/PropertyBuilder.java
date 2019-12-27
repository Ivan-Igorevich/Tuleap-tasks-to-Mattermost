package ru.iovchinnikov.mmtp;

import org.json.simple.JSONObject;

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

    String getResult() {
        return sb.toString();
    }
}
