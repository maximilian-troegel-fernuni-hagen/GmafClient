package de.swa.mmfg;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import de.swa.gc.GraphCode;

/** data type to represent General Metadata **/
public class GeneralMetadata {
	private UUID id;
	private long longitude;
	private long latitude;
	private String cameraModel;
	private String lensModel;
	private float aperture;
	private float exposure;
	private String cityNearBy;
	private long focalLength;
	private int resolution;
	private int width;
	private int height;
	private transient Date date;
	private long fileSize;
	private int iso;
	private long shutterSpeed;
	private String fileName;
	private File fileReference;
	private URL previewUrl;
	private GraphCode gc;
	private byte[] bytes;

	public GeneralMetadata() {
		id = UUID.randomUUID();
	}
	public GeneralMetadata(UUID id, long longitude, long latitude, String camera, String lens, float aperture, float exposure, long focalLength, int resolution, int width, int height, Date date, long fileSize, int iso, long shutterSpeed, String fileName) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.cameraModel = camera;
		this.lensModel = lens;
		this.aperture = aperture;
		this.exposure = exposure;
		this.focalLength = focalLength;
		this.resolution = resolution;
		this.width = width;
		this.height = height;
		this.date = date;
		this.fileSize = fileSize;
		this.iso = iso;
		this.shutterSpeed = shutterSpeed;
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fn) {
		fileName = fn;
	}
	public URL getPreviewUrl() {
		if (previewUrl != null) return previewUrl;
		else {
			String server = "";
			String port = "";
			String preview = "preview";
			String context = "";
			try {
				previewUrl = new URL("http://" + server + ":" + port + "/" + context + "/" + preview + "/" + id.toString() + ".jpg");
				File f = new File(context + "/" + preview + "/" + id.toString() + ".jpg");
				if (!f.exists()) {
					previewUrl = new URL("http://" + server + ":" + port + "/" + context + "/" + preview + "/no_preview.jpg");
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return previewUrl;
	}

	public File getFileReference() {
		return fileReference;
	}
	public void setFileReference(File fileReference) {
		this.fileReference = fileReference;
	}
	public UUID getId() {
		if (id == null) id = UUID.randomUUID();
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public long getLongitude() {
		return longitude;
	}
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
	public long getLatitude() {
		return latitude;
	}
	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}
	public String getCameraModel() {
		return cameraModel;
	}
	public void setCameraModel(String cameraModel) {
		this.cameraModel = cameraModel;
	}
	public String getLensModel() {
		return lensModel;
	}
	public void setLensModel(String lensModel) {
		this.lensModel = lensModel;
	}
	public float getAperture() {
		return aperture;
	}
	public void setAperture(float aperture) {
		this.aperture = aperture;
	}
	public float getExposure() {
		return exposure;
	}
	public void setExposure(float exposure) {
		this.exposure = exposure;
	}
	public String getCityNearBy() {
		return cityNearBy;
	}
	public void setCityNearBy(String cityNearBy) {
		this.cityNearBy = cityNearBy;
	}
	public long getFocalLength() {
		return focalLength;
	}
	public void setFocalLength(long focalLength) {
		this.focalLength = focalLength;
	}
	public int getResolution() {
		return resolution;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public int getIso() {
		return iso;
	}
	public void setIso(int iso) {
		this.iso = iso;
	}
	public long getShutterSpeed() {
		return shutterSpeed;
	}
	public void setShutterSpeed(long shutterSpeed) {
		this.shutterSpeed = shutterSpeed;
	}
	public void generateBytes() {
		if (fileReference != null) {
			try {
				byte[] bytes = new byte[(int) fileReference.length()];
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileReference));
				DataInputStream dis = new DataInputStream(bis);
				dis.readFully(bytes);
				this.setBytes(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public GraphCode getGrapCode() {
		return gc;
	}
	public void setGraphCode(GraphCode gc) {
		this.gc = gc;
	}
}