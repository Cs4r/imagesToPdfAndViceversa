package cs4r.tools.imagestopdf.converter.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import cs4r.tools.imagestopdf.converter.ImagesToPdfFileConverter;
import cs4r.tools.imagestopdf.converter.exception.FailedConversionException;

/**
 * Implementation of {@link ImagesToPdfFileConverter}. It only can transform to
 * pdf the image formats defined in {@link BasicImageFormat}.
 * 
 * @author cs4r
 *
 */
public class ImagesToPdfFileConverterImpl implements ImagesToPdfFileConverter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImagesToPdfFileConverterImpl.class);

	private static final List<String> SUPPORTED_IMAGE_FORMATS;

	static {
		SUPPORTED_IMAGE_FORMATS = new ArrayList<>();
		for (BasicImageFormat basicImageFormat : BasicImageFormat.class
				.getEnumConstants()) {
			SUPPORTED_IMAGE_FORMATS.add(basicImageFormat.getFormat());
		}
	}

	@Override
	public void convertPdfFileToImages(List<String> images,
			String pdfFileDestinationPath) throws FailedConversionException {

		checkPreconditions(images, pdfFileDestinationPath);

		try {
			Document pdfDocument = new Document();
			FileOutputStream fileOutputStream = new FileOutputStream(
					pdfFileDestinationPath);
			PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument,
					fileOutputStream);

			pdfWriter.open();
			pdfDocument.open();

			for (String imagePath : images) {
				Image image = Image.getInstance(imagePath);

				float scaler = ((pdfDocument.getPageSize().getWidth()
						- pdfDocument.leftMargin() - pdfDocument.rightMargin()) / image
						.getWidth()) * 100;

				image.scalePercent(scaler);
				pdfDocument.add(image);
				pdfDocument.newPage();
			}

			pdfDocument.close();
			pdfWriter.close();
		} catch (DocumentException | IOException exception) {
			LOGGER.error("Images could not be transformed into a pdf",
					exception);
			throw new FailedConversionException(exception);
		}
	}

	private void checkPreconditions(Collection<String> images,
			String pdfFileDestinationPath) {
		Preconditions.checkNotNull(images);
		Preconditions.checkNotNull(pdfFileDestinationPath);

		Preconditions
				.checkState(!images.isEmpty(), "images could not be empty");

		for (String imagePath : images) {
			Preconditions.checkState(new File(imagePath).exists(),
					"Image %s should exist", imagePath);
			Preconditions.checkState(SUPPORTED_IMAGE_FORMATS
					.contains(FilenameUtils.getExtension(imagePath)
							.toLowerCase()));
		}

	}

}
