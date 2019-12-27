package ru.iovchinnikov.mmtp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class Settings {
    private String filename;
    private JSONObject current;

    Settings(String filename) {
        this.filename = filename;
    }

    public Long readLongSetting(String name) {
        readCurrentOrNew();
        return (Long) current.get(name);
    }

    public String readStringSetting(String name) {
        readCurrentOrNew();
        return current.get(name).toString();
    }

    public void writeIntegerSetting(String name, Integer value) {
        readCurrentOrNew();
        current.put(name, value);
        writeCurrent();
    }

    public void writeStringSetting(String name, String value) {
        readCurrentOrNew();
        current.put(name, value);
        writeCurrent();
    }

    private void writeCurrent() {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(current.toJSONString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readCurrentOrNew() {
        try (Reader reader = new FileReader(filename)) {
            current = (JSONObject) new JSONParser().parse(reader);
        } catch (IOException | ParseException e) {
            current = new JSONObject();
        }
    }
}
