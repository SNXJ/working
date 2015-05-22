/**
 * Copyright 2013 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.badou.mworking.net.bitmap;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements
		ImageCache {
	public BitmapLruCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
		if(url==null||"".equals(url)){
			return null;
		}
		return get(url);
	}

	public Bitmap getCircleBitmap(String url) {
		if(url==null||"".equals(url)){
			return null;
		}
		return get(url + "Circle");
	}

	public void putCircleBitmap(String url, Bitmap bitmap) {
		put(url + "Circle", bitmap);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

	public static void init(Context context) {
		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mBitmapLruCache = new BitmapLruCache(cacheSize);
	}

	public static BitmapLruCache getBitmapLruCache() {
		if (mBitmapLruCache == null) {
			throw new IllegalStateException("ImageLoader not initialized");
		} else {
			return mBitmapLruCache;
		}
	}

	private static BitmapLruCache mBitmapLruCache;
}