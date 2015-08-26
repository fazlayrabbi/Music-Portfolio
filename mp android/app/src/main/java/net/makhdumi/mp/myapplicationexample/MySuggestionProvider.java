package net.makhdumi.mp.myapplicationexample;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Fazlay on 7/26/2015.
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "net.makhdumi.mp.myapplicationexample.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
