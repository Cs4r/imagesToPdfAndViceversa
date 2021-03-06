package cs4r.tools.imagestopdf.converter.impl;

import cs4r.tools.imagestopdf.converter.ImageFileFormat;

/**
 * Set of image file formats supported by {@link PdfFileToImagesConverterImpl}
 * and {@link ImagesToPdfFileConverterImpl}.
 * 
 * @author cs4r
 *
 */
public enum BasicImageFormat implements ImageFileFormat {

	JPG("jpg"), GIF("gif"), JPEG("jpeg"), PNG("png");

	private String format;

	private BasicImageFormat(String format) {
		this.format = format;
	}

	@Override
	public String getFormat() {
		return format;
	}

}
