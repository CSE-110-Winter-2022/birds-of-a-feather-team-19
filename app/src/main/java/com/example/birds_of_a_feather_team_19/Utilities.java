package com.example.birds_of_a_feather_team_19;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.BitmapFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Utilities {
    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static boolean invalidName(String name) {
        return name.isEmpty();
    }

    public static boolean invalidPhotoURL(String photoURL) {
        ExecutorService imageExecutor = Executors.newSingleThreadExecutor();

        if (!photoURL.equals("")) {
            Future<Boolean> future = (imageExecutor.submit(() -> BitmapFactory.decodeStream(new URL(photoURL).openStream()) == null));
            try {
                return future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return true;
            }
        }
        return false;
    }

    public static int getCourseAge(String courseYear, String courseQuarter, String currentYear, String currentQuarter) {
        //assumes input will give an output with an appropriate age (0-4)

        int course_year = Integer.parseInt(courseYear);
        //int course_quarter = Integer.parseInt(courseQuarter);
        int current_year = Integer.parseInt(currentYear);
        //int current_quarter = Integer.parseInt(currentQuarter);

        int temp_courseYear = course_year;
        String temp_courseQuarter = courseQuarter;
        if (temp_courseQuarter == "summer session 1" || temp_courseQuarter == "special summer session" || temp_courseQuarter == "summer session 2") {
            temp_courseQuarter = "summer";
        }
        int temp_age = 0;
        while (temp_courseQuarter != currentQuarter && temp_courseYear != current_year) {
            if (temp_age - 1 == 4) {
                break;
            }
            if(temp_courseQuarter == "fall") {
                temp_courseYear = temp_courseYear + 1;
                temp_courseQuarter = "winter";
                temp_age = temp_age + 1;
            } else if (temp_courseQuarter == "winter") {
                temp_courseQuarter = "spring";
                temp_age = temp_age + 1;
            } else if (temp_courseQuarter == "spring") {
                temp_courseQuarter = "summer";
                temp_age = temp_age + 1;
            } else if (temp_courseQuarter == "summer") {
                temp_courseQuarter = "fall";
                temp_age = temp_age + 1;
            }
        }
        return temp_age - 1;

        /*
        Map<String, Integer> yearquarterMap = new HashMap<String, Integer>() {{
            put("winter", 0);
            put("spring", 1);
            put("summer session 1", 2);
            put("summer session 2", 2);
            put("special summer session", 2);
            put("fall", 3);
        }};

        if (current_year - course_year == 0) {
            if (courseQuarter == currentQuarter) {
                return -1;
            } else {
                 return (yearquarterMap.get(currentQuarter) - yearquarterMap.get(courseQuarter)) - 1;
            }
        } else {
            if (current_year - course_year >= 2) {
                return 4;
            }
            else {
                int temp_courseYear = Integer.parseInt(courseYear);
                String temp_courseQuarter = courseQuarter;
                if (temp_courseQuarter == "summer session 1" || temp_courseQuarter == "special summer session" || temp_courseQuarter == "summer session 2") {
                    temp_courseQuarter = "summer";
                }
                int temp_age = 0;
                while (temp_courseQuarter != currentQuarter && temp_courseYear != current_year) {
                    if(temp_courseQuarter == "fall") {
                        temp_courseYear = temp_courseYear + 1;
                        temp_courseQuarter = "winter";
                        temp_age = temp_age + 1;
                    } else if (temp_courseQuarter == "winter") {
                        temp_courseQuarter = "spring";
                        temp_age = temp_age + 1;
                    } else if (temp_courseQuarter == "spring") {
                        temp_courseQuarter = "summer";
                        temp_age = temp_age + 1;
                    } else if (temp_courseQuarter == "summer") {
                        temp_courseQuarter = "fall";
                        temp_age = temp_age + 1;
                    }
                    return temp_age - 1;
            }


*/
    }
}