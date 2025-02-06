package com.booksharing.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage<T> {
    private final String filePath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Enable Pretty Printing

    public JsonStorage(String filePath) {
        this.filePath = filePath;
    }

    public List<T> loadData(Type type) {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void saveData(List<T> data) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
