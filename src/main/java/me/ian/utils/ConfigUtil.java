package me.ian.utils;

import me.ian.Commandlogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class ConfigUtil {

    public static void logMessageToFile(String message){
        try {
            File data = Commandlogger.getInstance().getDataFolder();
            if (!data.exists()) data.mkdir();

            File logs = new File(data, "command.log");
            if (!logs.exists()) logs.createNewFile();

            FileWriter fileWriter = new FileWriter(logs, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
            printWriter.println("[" + dateFormat.format(date) + "] " + message);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e){
            Commandlogger.getInstance().log(Level.SEVERE, "ERROR! Please contact developer.");
            e.printStackTrace();
        }
    }
}
