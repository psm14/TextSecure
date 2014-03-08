package org.thoughtcrime.securesms;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.notifications.MessageNotifier;
import org.thoughtcrime.securesms.service.KeyCachingService;
import org.whispersystems.textsecure.crypto.MasterSecret;

public class StandardShortcuts {

    private final Activity activity;
    private final MasterSecret masterSecret;

    public StandardShortcuts(Activity activity, MasterSecret masterSecret) {
        this.activity     = activity;
        this.masterSecret = masterSecret;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_message:       openSingleContactSelection();   return true;
            case R.id.menu_new_group:         createGroup();                  return true;
            case R.id.menu_settings:          handleDisplaySettings();        return true;
            case R.id.menu_clear_passphrase:  handleClearPassphrase();        return true;
            case R.id.menu_mark_all_read:     handleMarkAllRead();            return true;
        }

        return false;
    }

    private void openSingleContactSelection() {
        Intent intent = new Intent(activity, SingleContactSelectionActivity.class);
        intent.putExtra(SingleContactSelectionActivity.MASTER_SECRET_EXTRA, masterSecret);
        activity.startActivity(intent);
    }

    private void createGroup() {
        Intent intent = new Intent(activity, GroupCreateActivity.class);
        intent.putExtra("master_secret", masterSecret);
        activity.startActivity(intent);
    }

    private void handleDisplaySettings() {
        Intent preferencesIntent = new Intent(activity, ApplicationPreferencesActivity.class);
        preferencesIntent.putExtra("master_secret", masterSecret);
        activity.startActivity(preferencesIntent);
    }

    private void handleClearPassphrase() {
        Intent intent = new Intent(activity, KeyCachingService.class);
        intent.setAction(KeyCachingService.CLEAR_KEY_ACTION);
        activity.startService(intent);
    }

    private void handleMarkAllRead() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DatabaseFactory.getThreadDatabase(activity).setAllThreadsRead();
                MessageNotifier.updateNotification(activity, masterSecret);
                return null;
            }
        }.execute();
    }
}
