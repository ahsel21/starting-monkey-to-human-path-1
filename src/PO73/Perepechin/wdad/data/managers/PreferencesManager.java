package PO73.Perepechin.wdad.data.managers;

public class PreferencesManager {
    private static PreferencesManager ourInstance = new PreferencesManager();

    public static PreferencesManager getInstance() {
        return ourInstance;
    }

    private PreferencesManager() {
    }


}
