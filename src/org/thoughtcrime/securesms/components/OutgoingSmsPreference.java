package org.thoughtcrime.securesms.components;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.util.TextSecurePreferences;

public class OutgoingSmsPreference extends DialogPreference {
  private CheckBox dataUsers;
  private CheckBox askForFallback;
  private CheckBox nonDataUsers;
  public OutgoingSmsPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    setPersistent(false);
    setDialogLayoutResource(R.layout.outgoing_sms_preference);
  }

  @Override
  protected void onBindDialogView(final View view) {
    super.onBindDialogView(view);
    dataUsers      = (CheckBox) view.findViewById(R.id.data_users);
    askForFallback = (CheckBox) view.findViewById(R.id.ask_before_fallback_data);
    nonDataUsers   = (CheckBox) view.findViewById(R.id.non_data_users);

    dataUsers.setChecked(TextSecurePreferences.isSmsFallbackEnabled(getContext()));
    askForFallback.setChecked(TextSecurePreferences.isSmsFallbackAskEnabled(getContext()));
    nonDataUsers.setChecked(TextSecurePreferences.isSmsNonDataOutEnabled(getContext()));

    dataUsers.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        askForFallback.setEnabled(dataUsers.isChecked());
      }
    });

    askForFallback.setEnabled(dataUsers.isChecked());
  }

  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);

    if (positiveResult) {
      TextSecurePreferences.setSmsFallbackEnabled(getContext(), dataUsers.isChecked());
      TextSecurePreferences.setSmsFallbackAskEnabled(getContext(), askForFallback.isChecked());
      TextSecurePreferences.setSmsNonDataOutEnabled(getContext(), nonDataUsers.isChecked());
      if (getOnPreferenceChangeListener() != null) getOnPreferenceChangeListener().onPreferenceChange(this, null);
    }
  }
}
