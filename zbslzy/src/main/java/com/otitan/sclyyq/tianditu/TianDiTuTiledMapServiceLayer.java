package com.otitan.sclyyq.tianditu;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.RejectedExecutionException;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import com.esri.core.io.UserCredentials;

/**
 */
public class TianDiTuTiledMapServiceLayer extends TiledServiceLayer {
	private TianDiTuTiledMapServiceType _mapType;
	private TileInfo tiandituTileInfo;

	public TianDiTuTiledMapServiceLayer() {
		this(null, null, true);
	}

	public TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType mapType) {
		this(mapType, null, true);
	}

	public TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType mapType,
			UserCredentials usercredentials) {
		this(mapType, usercredentials, true);
	}

	public TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType mapType,
			UserCredentials usercredentials, boolean flag) {
		super("");
		this._mapType = mapType;
		setCredentials(usercredentials);

		if (flag)
			try {
				getServiceExecutor().submit(new Runnable() {

					public final void run() {
						a.initLayer();
					}

					final TianDiTuTiledMapServiceLayer a;

					{
						a = TianDiTuTiledMapServiceLayer.this;
						// super();
					}
				});
				return;
			} catch (RejectedExecutionException _ex) {
			}
	}

	public TianDiTuTiledMapServiceType getMapType() {
		return this._mapType;
	}

	protected void initLayer() {
		this.buildTileInfo();
		this.setFullExtent(new Envelope(-180, -90, 180, 90));
		this.setDefaultSpatialReference(SpatialReference.create(4490)); // CGCS2000
		// this.setDefaultSpatialReference(SpatialReference.create(4326));
		this.setInitialExtent(new Envelope(117.32, 35.86, 118.31, 37.48));
		super.initLayer();
	}

	public void refresh() {
		try {
			getServiceExecutor().submit(new Runnable() {

				public final void run() {
					if (a.isInitialized())
						try {
							a.b();
							a.clearTiles();
							return;
						} catch (Exception exception) {
							Log.e("ArcGIS",
									"Re-initialization of the layer failed.",
									exception);
						}
				}

				final TianDiTuTiledMapServiceLayer a;

				{
					a = TianDiTuTiledMapServiceLayer.this;
					// super();
				}
			});
			return;
		} catch (RejectedExecutionException _ex) {
			return;
		}
	}

	final void b() throws Exception {

	}

	@Override
	protected byte[] getTile(int level, int col, int row) throws Exception {
		/**
         * 
         * */

		byte[] result = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			URL sjwurl = new URL(this.getTianDiMapUrl(level, col, row));
			HttpURLConnection httpUrl = null;
			BufferedInputStream bis = null;
			byte[] buf = new byte[1024];

			httpUrl = (HttpURLConnection) sjwurl.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());

			while (true) {
				int bytes_read = bis.read(buf);
				if (bytes_read > 0) {
					bos.write(buf, 0, bytes_read);
				} else {
					break;
				}
			}
			;
			bis.close();
			httpUrl.disconnect();

			result = bos.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public TileInfo getTileInfo() {
		return this.tiandituTileInfo;
	}

	/**
	 * ��ȡ��ͼ��Ƭurl
	 * */
	private String getTianDiMapUrl(int level, int col, int row) {

		String url = new TDTUrl(level+1, col, row, this._mapType).generatUrl();
		return url;
	}

	/**
	 * ��ȡ�ٶȵ�ͼ��Ƭurl
	 * */
	private String getBTianDiMapUrl(int level, int col, int row) {

		String url = new BTDTUrl(level, col, row, this._mapType).generatUrl();
		return url;
	}

	private void buildTileInfo() {
		Point originalPoint = new Point(-180, 90);

		double[] res = { 0.703125, 0.351563, 0.175781, 0.0878906, 0.0439453,
				0.0219727, 0.0109863, 0.00549316, 0.00274658,
				0.00137329, 0.000686646, 0.000343323,
				0.000171661, 8.58307e-005,
				4.29153e-005, 2.14577e-005,
				1.07289e-005, 5.36445e-006};

		double[] scale = { 2.95498e+008, 1.47749e+008, 7.38744e+007,
				3.69372e+007, 1.84686e+007, 9.2343e+006,
				4.61715e+006, 2.30857e+006, 1.15429e+006,
				577144, 288572, 144286,
				72143, 36071.5, 18035.7,
				9017.87, 4508.9, 2254.47};

		int levels = 18;
		int dpi = 96;
		int tileWidth = 256;
		int tileHeight = 256;
		this.tiandituTileInfo = new TileInfo(originalPoint, scale, res, levels, dpi, tileWidth, tileHeight);
		this.setTileInfo(this.tiandituTileInfo);
	}

}
