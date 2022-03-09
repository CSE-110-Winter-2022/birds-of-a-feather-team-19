package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;

public class Converters {
    @TypeConverter
    public static HashSet<Integer> fromString(String value) {
        Type setType = new TypeToken<HashSet<Integer>>() {}.getType();
        return new Gson().fromJson(value, setType);
    }

    @TypeConverter
    public static String fromHashSet(HashSet<Integer> set) {
        Gson gson = new Gson();
        String json = gson.toJson(set);
        return json;
    }
}
