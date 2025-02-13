package com.booksharing.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component // ✅ Convert to a Spring Bean
public class JsonStorage<T> {
    private final String filePath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonStorage() {
        this.filePath = System.getProperty("user.dir") + "/data/messages.json"; // ✅ Default path
    }

    public List<T> loadData(Type type) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("⚠️ messages.json not found. Creating a new one.");
            saveData(new ArrayList<>()); // ✅ Create an empty JSON file if missing
        }

        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveData(List<T> data) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }

            try (Writer writer = new FileWriter(filePath, false)) {
                gson.toJson(data, writer);
                System.out.println("✅ Messages saved successfully: " + gson.toJson(data));
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save messages.json: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
