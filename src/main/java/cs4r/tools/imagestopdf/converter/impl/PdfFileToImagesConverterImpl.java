package cs4r.tools.imagestopdf.converter.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import cs4r.tools.imagestopdf.converter.ImageFileFormat;
import cs4r.tools.imagestopdf.converter.PdfFileToImagesConverter;
import cs4r.tools.imagestopdf.converter.exception.FailedConversionException;

/**
 * Simple implementation of {@link PdfFileToImagesConverter}
 * 
 * @author cs4r
 */
public class PdfFileToImagesConverterImpl implements PdfFileToImagesConverter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PdfFileToImagesConverterImpl.class);

	private static final String OUTPUT_IMAGE_NAME = "%s_%d.%s";

	@Override
	public void convertPdfFileToImages(String pdfFilePath,
			String imagesDestinationPath, ImageFileFormat imageFormat)
			throws FailedConversionException {

		checkPreconditions(pdfFilePath, imagesDestinationPath, imageFormat);

		try {
			PDDocument pdfDocument = PDDocument.load(pdfFilePath);
			String pdfFileName = FilenameUtils.getBaseName(pdfFilePath);
			String outputImageFormat = imageFormat.getFormat();
			@SuppressWarnings("unchecked")
			List<PDPage> pagesList = pdfDocument.getDocumentCatalog()
					.getAllPages();

			convertPagesToImages(pagesList, pdfFileName, imagesDestinationPath,
					outputImageFormat);

			pdfDocument.close();
		} catch (IOException exception) {
			LOGGER.error(
					"Pdf file {} could not be transformed into images inside directory {}",
					pdfFilePath, imagesDestinationPath, exception);
			throw new FailedConversionException(exception);
		}
	}

	private static void checkPreconditions(String pdfFile,
			String imagesDestinationDirectory, ImageFileFormat imageFormat) {
		Preconditions.checkNotNull(pdfFile);
		Preconditions.checkNotNull(imagesDestinationDirectory);
		Preconditions.checkState(new File(pdfFile).exists());
		Preconditions.checkState(new File(imagesDestinationDirectory).exists());
		Preconditions.checkNotNull(imageFormat);
	}

	private static void convertPagesToImages(List<PDPage> pagesList,
			String pdfFileName, String imagesDestinationPath,
			String outputImageFormat) throws IOException {

		int pageNumber = 1;

		for (PDPage page : pagesList) {
			BufferedImage pageAsImage = page.convertToImage();

			String imageName = String.format(OUTPUT_IMAGE_NAME, pdfFileName,
					pageNumber, outputImageFormat);

			File imageFile = new File(imagesDestinationPath, imageName);

			ImageIO.write(pageAsImage, outputImageFormat, imageFile);

			pageNumber++;
		}
	}
}