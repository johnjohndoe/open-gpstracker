package nl.sogeti.android.gpstracker.actions;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import nl.sogeti.android.gpstracker.BuildConfig;
import nl.sogeti.android.gpstracker.R;

public class TrackRenamingFragment extends DialogFragment {

    public interface OnTrackRenamedListener {
        void onTrackRenamed(@NonNull String name);
    }

    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".TRACK_RENAMING_FRAGMENT_TAG";

    public static final String TRACK_NAME_BUNDLE_KEY =
            BuildConfig.APPLICATION_ID + ".TRACK_NAME_BUNDLE_KEY";

    private EditText mTrackNameView;

    public static void show(@NonNull FragmentActivity activity, @NonNull String trackName) {
        DialogFragment fragment = new TrackRenamingFragment();
        Bundle extras = new Bundle();
        extras.putString(TRACK_NAME_BUNDLE_KEY, trackName);
        fragment.setArguments(extras);
        fragment.show(activity.getSupportFragmentManager(), FRAGMENT_TAG);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null && !(activity instanceof OnTrackRenamedListener)) {
            throw new AssertionError("Calling activity does not implement OnTrackRenamedListener interface.");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View rootView = layoutInflater.inflate(R.layout.namedialog, contentView, false);
        mTrackNameView = (EditText) rootView.findViewById(R.id.nameField);

        Bundle extras = getArguments();
        if (extras != null) {
            String trackName = extras.getString(TRACK_NAME_BUNDLE_KEY);
            if (!TextUtils.isEmpty(trackName)) {
                mTrackNameView.setText(trackName);
                mTrackNameView.setSelection(0, trackName.length());
            }
        }

        return new AlertDialog.Builder(activity)
                .setTitle(R.string.dialog_routename_title)
                .setMessage(R.string.dialog_routename_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        passBackTrackName();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .setView(rootView)
                .create();
    }

    private void passBackTrackName() {
        Editable trackNameEditable = mTrackNameView.getText();
        if (trackNameEditable == null) {
            return;
        }
        String trackName = trackNameEditable.toString();
        Activity activity = getActivity();
        if (activity != null && activity instanceof OnTrackRenamedListener) {
            ((OnTrackRenamedListener) activity).onTrackRenamed(trackName);
        }
    }

}
