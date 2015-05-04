package cs4r.tools.imagestopdf.converter.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs4r.tools.imagestopdf.converter.ImageFileFormat;
import cs4r.tools.imagestopdf.converter.exception.FailedConversionException;

/**
 * Tests for {@link PdfFileToImagesConverterImpl}
 * 
 * @author cs4r
 *
 */
@RunWith(JUnitParamsRunner.class)
public class PdfFileToImagesConverterImplTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PdfFileToImagesConverterImplTest.class);
	private static final int PDF_FILE_PAGES = 5;
	private static final String PDF_FILE = ClassLoader.getSystemClassLoader()
			.getResource("resources/test-pdf-file.pdf").getPath();
	private static final String IMAGES_DIRECTORY = ClassLoader
			.getSystemClassLoader().getResource("resources/test-images-dir")
			.getPath();

	private static final String FAKE_PDF_FILE = ClassLoader
			.getSystemClassLoader().getResource("resources/fake_pdf.txt")
			.getPath();

	private static final String WRONG_DESTINATION_DIRECTORY = "wrong_destination_directory";
	private static final String WRONG_PDF_FILE = "wrong_pdf_file.pdf";

	@Test(expected = NullPointerException.class)
	@Parameters(method = "nullParametersCombinations")
	public void testConvertPdfFileToImagesWithNullParameters(
			String pdfFilePath, String imagesDestinationPath,
			ImageFileFormat imageFormat) throws FailedConversionException {
		new PdfFileToImagesConverterImpl().convertPdfFileToImages(pdfFilePath,
				imagesDestinationPath, imageFormat);
	}

	private Object[] nullParametersCombinations() {
		return new Object[] { new Object[] { null, null, null },
				new Object[] { PDF_FILE, null, null },
				new Object[] { null, IMAGES_DIRECTORY, null },
				new Object[] { null, null, BasicImageFormat.JPEG },
				new Object[] { PDF_FILE, IMAGES_DIRECTORY, null },
				new Object[] { PDF_FILE, null, BasicImageFormat.JPEG },
				new Object[] { null, IMAGES_DIRECTORY, BasicImageFormat.JPEG } };
	}

	@Test(expected = IllegalStateException.class)
	@Parameters(method = "nonExistentFilesCombinations")
	public void testConvertPdfFileToImagesWithNonExistentFiles(
			String pdfFilePath, String imagesDestinationPath,
			ImageFileFormat imageFormat) throws FailedConversionException {
		new PdfFileToImagesConverterImpl().convertPdfFileToImages(pdfFilePath,
				imagesDestinationPath, imageFormat);
	}

	private Object[] nonExistentFilesCombinations() {
		return new Object[] {
				new Object[] { WRONG_PDF_FILE, IMAGES_DIRECTORY,
						BasicImageFormat.JPEG },
				new Object[] { PDF_FILE, WRONG_DESTINATION_DIRECTORY,
						BasicImageFormat.JPEG },
				new Object[] { PDF_FILE, WRONG_DESTINATION_DIRECTORY,
						BasicImageFormat.JPEG },
				new Object[] { WRONG_PDF_FILE, WRONG_DESTINATION_DIRECTORY,
						BasicImageFormat.JPEG } };
	}

	@Test(expected = FailedConversionException.class)
	public void testConversionWithFakePdfFile()
			throws FailedConversionException {
		new PdfFileToImagesConverterImpl().convertPdfFileToImages(
				FAKE_PDF_FILE, IMAGES_DIRECTORY, BasicImageFormat.JPEG);
	}

	@Test
	@Parameters(method = "supportedImageFormats")
	public void testConversionWithMultipleImageFormats(
			ImageFileFormat imageFileFormat) throws FailedConversionException {

		new PdfFileToImagesConverterImpl().convertPdfFileToImages(PDF_FILE,
				IMAGES_DIRECTORY, imageFileFormat);

		assertConversionFulfillInterfaceContract(imageFileFormat);

		deleteCreatedImages();
	}

	private Object[] supportedImageFormats() {
		return new Object[] { BasicImageFormat.BMP, BasicImageFormat.GIF,
				BasicImageFormat.JPEG, BasicImageFormat.JPG

		};
	}

	private void assertConversionFulfillInterfaceContract(
			ImageFileFormat imageFormat) {

		List<String> imageNames = new ArrayList<>();

		for (File file : new File(IMAGES_DIRECTORY).listFiles()) {
			imageNames.add(file.getName());
		}

		Collections.sort(imageNames);
		int imageNumber = 0;

		for (String imageName : imageNames) {
			++imageNumber;

			String imageExtension = FilenameUtils.getExtension(imageName);
			assertThat(imageExtension,
					equalToIgnoringCase(imageFormat.getFormat()));

			String baseImageName = FilenameUtils.getBaseName(imageName);
			assertTrue(baseImageName
					.endsWith(String.format("_%s", imageNumber)));
		}

		assertThat(imageNumber, equalTo(PDF_FILE_PAGES));
	}

	private void deleteCreatedImages() {
		for (File file : new File(IMAGES_DIRECTORY).listFiles()) {
			String fileName = file.getName();
			if (!file.delete()) {
				LOGGER.error("File {} could not be deleted", fileName);
			} else {
				LOGGER.info("File {} was deleted", fileName);
			}
		}
	}

}