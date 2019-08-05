package me.ian.utils;

import me.ian.Commandlogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater {

    public Commandlogger commandlogger;

    private String version;
    private String oldVersion;

    private UpdateResult result = UpdateResult.DISABLED;
    private boolean needUpdate;

    public enum UpdateResult {
        NO_UPDATE,
        DISABLED,
        FAIL_SPIGOT,
        FAIL_NOVERSION,
        BAD_RESOURCEID,
        UPDATE_AVAILABLE
    }

    public Updater(Commandlogger commandlogger, Integer resourceID, boolean disabled) {
        this.commandlogger = commandlogger;
        this.needUpdate = false;
        oldVersion = commandlogger.getDescription().getVersion();

        if (disabled) {
            result = UpdateResult.DISABLED;
            return;
        }

        URL url = null;
        try {
            url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID);
        } catch (MalformedURLException e) {
            result = UpdateResult.BAD_RESOURCEID;
            return;
        }

        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            result = UpdateResult.FAIL_SPIGOT;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (reader.readLine().equals(oldVersion)) {
                result = UpdateResult.NO_UPDATE;
                this.version = reader.readLine();
                needUpdate = false;
            } else {
                result = UpdateResult.UPDATE_AVAILABLE;
                this.version = oldVersion;
                needUpdate = true;
            }
        } catch (IOException e) {
            result = UpdateResult.FAIL_SPIGOT;
        }
    }


    public UpdateResult getResult() {
        return result;
    }

    public String getVersion() {
        return version;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }
}
