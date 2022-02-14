package com.example.birds_of_a_feather_team_19;

import static com.example.birds_of_a_feather_team_19.Utilities.invalidName;
import static com.example.birds_of_a_feather_team_19.Utilities.invalidPhotoURL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birds_of_a_feather_team_19.model.db.Course;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class UtilitiesUnitTest {
    //Empty String should return true
    @Test
    public void invalidName_empty() throws Exception {
        assertTrue(invalidName(""));
    }

    //Not Empty String should return false
    @Test
    public void invalidName_notEmpty() throws Exception {
        assertFalse(invalidName("Team19"));
    }

    //Simple string should return false
    @Test
    public void invalidName_justString() throws Exception {
        assertTrue(invalidPhotoURL("Team19"));
    }

    //Valid Url that is not photo should return false
    @Test
    public void invalidUrlNotPhoto() throws Exception {
        assertTrue(invalidPhotoURL("https://www.google.com/"));
    }

    //valid photo url should return true;
    @Test
    public void validPhotoUrl() throws Exception {
        assertFalse(invalidPhotoURL("https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0"));
    }

}

