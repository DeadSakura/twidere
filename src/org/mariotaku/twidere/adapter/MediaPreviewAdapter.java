package org.mariotaku.twidere.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.mariotaku.twidere.Constants;
import org.mariotaku.twidere.R;
import org.mariotaku.twidere.app.TwidereApplication;
import org.mariotaku.twidere.util.ImageLoaderWrapper;
import org.mariotaku.twidere.util.ImageLoadingHandler;

import java.util.Collection;

public class MediaPreviewAdapter extends ArrayAdapter<String> implements Constants {

	private final ImageLoaderWrapper mImageLoader;
	private final SharedPreferences mPreferences;
	private final ImageLoadingHandler mImageLoadingHandler;

	private boolean mIsPossiblySensitive;

	public MediaPreviewAdapter(final Context context) {
		super(context, R.layout.image_preview_item);
		mImageLoader = ((TwidereApplication) context.getApplicationContext()).getImageLoaderWrapper();
		mPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		mImageLoadingHandler = new ImageLoadingHandler();
	}

	public void addAll(final Collection<String> data, final boolean is_possibly_sensitive) {
		mIsPossiblySensitive = is_possibly_sensitive;
		addAll(data);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);
		final String link = getItem(position);
		final ImageView image_view = (ImageView) view.findViewById(R.id.image_preview_item);
		image_view.setTag(link);
		if (mIsPossiblySensitive && !mPreferences.getBoolean(PREFERENCE_KEY_DISPLAY_SENSITIVE_CONTENTS, false)) {
			view.findViewById(R.id.image_preview_progress).setVisibility(View.GONE);
			image_view.setBackgroundResource(R.drawable.image_preview_nsfw);
		} else if (!link.equals(mImageLoadingHandler.getLoadingUri(image_view))) {
			image_view.setBackgroundResource(0);
			mImageLoader.displayPreviewImage(image_view, link, mImageLoadingHandler);
		}
		return view;
	}

}
