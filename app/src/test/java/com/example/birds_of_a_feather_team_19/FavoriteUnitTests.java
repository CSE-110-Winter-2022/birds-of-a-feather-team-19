package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birds_of_a_feather_team_19.model.db.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class FavoriteUnitTests {
    @Test
    public void favorite_Test() throws Exception {
        User test_user1 = new User("User1", "");
        assertEquals(test_user1.isFavorite(), false);
        test_user1.setFavorite(true);
        assertEquals(test_user1.isFavorite(), true);
        test_user1.setFavorite(true);
        assertEquals(test_user1.isFavorite(), true);
        test_user1.setFavorite(false);
        assertEquals(test_user1.isFavorite(), false);
    }

}