package de.danoeh.antennapod.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

import de.danoeh.antennapod.AppConfig;
import de.danoeh.antennapod.R;
import de.danoeh.antennapod.activity.AudioplayerActivity.AudioplayerContentFragment;
import de.danoeh.antennapod.asynctask.ImageLoader;
import de.danoeh.antennapod.util.playback.Playable;

/** Displays the cover and the title of a FeedItem. */
public class CoverFragment extends SherlockFragment implements
		AudioplayerContentFragment {
	private static final String TAG = "CoverFragment";
	private static final String ARG_PLAYABLE = "arg.playable";

	private Playable media;

	private ImageView imgvCover;

	private boolean viewCreated = false;

	public static CoverFragment newInstance(Playable item) {
		CoverFragment f = new CoverFragment();
		if (item != null) {
			Bundle args = new Bundle();
			args.putParcelable(ARG_PLAYABLE, item);
			f.setArguments(args);
		}
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		Bundle args = getArguments();
		if (args != null) {
			media = args.getParcelable(ARG_PLAYABLE);
		} else {
			Log.e(TAG, TAG + " was called with invalid arguments");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.cover_fragment, container, false);
		imgvCover = (ImageView) root.findViewById(R.id.imgvCover);
		viewCreated = true;
		return root;
	}

	private void loadMediaInfo() {
		if (media != null) {
			imgvCover.post(new Runnable() {

				@Override
				public void run() {
					ImageLoader.getInstance().loadCoverBitmap(
							media, imgvCover);
				}
			});
		} else {
			Log.w(TAG, "loadMediaInfo was called while media was null");
		}
	}

	@Override
	public void onStart() {
		if (AppConfig.DEBUG)
			Log.d(TAG, "On Start");
		super.onStart();
		if (media != null) {
			if (AppConfig.DEBUG)
				Log.d(TAG, "Loading media info");
			loadMediaInfo();
		} else {
			Log.w(TAG, "Unable to load media info: media was null");
		}
	}

	@Override
	public void onDataSetChanged(Playable media) {
		this.media = media;
		if (viewCreated) {
			loadMediaInfo();
		}

	}

}
