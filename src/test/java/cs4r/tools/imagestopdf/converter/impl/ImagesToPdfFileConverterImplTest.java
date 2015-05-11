package cs4r.tools.imagestopdf.converter.impl;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs4r.tools.imagestopdf.converter.exception.FailedConversionException;

/**
 * Unit testing for {@link ImagesToPdfFileConverterImpl}.
 * 
 * @author cs4r
 *
 */
@RunWith(JUnitParamsRunner.class)
public class ImagesToPdfFileConverterImplTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImagesToPdfFileConverterImplTest.class);

	private static final int NUMBER_OF_IMAGES = 2;

	private static final String PDF_DIRECTORY = ClassLoader
			.getSystemClassLoader()
			.getResource("resources/test-pdf-dir/output/").getPath();

	private static final String IMAGE_FULL_PATH_FORMAT = "%s%d.%s";

	private static List<String> LIST_OF_JPG_IMAGES = new ArrayList<>();
	private static List<String> LIST_OF_GIF_IMAGES = new ArrayList<>();
	private static List<String> LIST_OF_JPEG_IMAGES = new ArrayList<>();
	private static List<String> LIST_OF_PNG_IMAGES = new ArrayList<>();

	private ImagesToPdfFileConverterImpl imagesToPdfFileConverterImpl;

	@BeforeClass
	public static void prepareTest() {

		String basePath = ClassLoader.getSystemClassLoader()
				.getResource("resources/test-pdf-dir/input/").getPath();

		basePath = FilenameUtils.getFullPath(basePath);

		for (int i = 1; i <= NUMBER_OF_IMAGES; ++i) {
			LIST_OF_JPG_IMAGES.add(String.format(IMAGE_FULL_PATH_FORMAT,
					basePath, i, BasicImageFormat.JPG.getFormat()));
			LIST_OF_GIF_IMAGES.add(String.format(IMAGE_FULL_PATH_FORMAT,
					basePath, i, BasicImageFormat.GIF.getFormat()));
			LIST_OF_JPEG_IMAGES.add(String.format(IMAGE_FULL_PATH_FORMAT,
					basePath, i, BasicImageFormat.JPEG.getFormat()));
			LIST_OF_PNG_IMAGES.add(String.format(IMAGE_FULL_PATH_FORMAT,
					basePath, i, BasicImageFormat.PNG.getFormat()));
		}
	}

	@Before
	public void setUp() {
		imagesToPdfFileConverterImpl = new ImagesToPdfFileConverterImpl();
	}

	@Test
	@Parameters(method = "supportedImageFormats")
	public void testConvertPdfFileToImages(List<String> images)
			throws FailedConversionException {
		String pdfFileDestinationPath = getPdfDestinationPath();
		imagesToPdfFileConverterImpl.convertPdfFileToImages(images,
				pdfFileDestinationPath);

		try {
			assertConversionFulfillInterfaceContract(pdfFileDestinationPath);
			deleteCreatedPdf();
		} catch (IOException e) {
			fail();
		}
	}

	private Object[] supportedImageFormats() {
		return new Object[] { LIST_OF_JPG_IMAGES, LIST_OF_GIF_IMAGES,
				LIST_OF_JPEG_IMAGES, LIST_OF_PNG_IMAGES };
	}

	private String getPdfDestinationPath() {
		return FilenameUtils.getFullPath(PDF_DIRECTORY) + "output.pdf";
	}

	private void assertConversionFulfillInterfaceContract(
			String pdfFileDestinationPath) throws IOException {
		PDDocument pdfDocument = PDDocument.load(pdfFileDestinationPath);
		assertThat(NUMBER_OF_IMAGES, equalTo(pdfDocument.getDocumentCatalog()
				.getAllPages().size()));
		pdfDocument.close();
	}

	private void deleteCreatedPdf() {
		for (File file : new File(PDF_DIRECTORY).listFiles()) {
			String fileName = file.getName();
			if (!file.delete()) {
				LOGGER.error("File {} could not be deleted", fileName);
			} else {
				LOGGER.info("File {} was deleted", fileName);
			}
		}
	}

	@Test(expected = NullPointerException.class)
	@Parameters(method = "nullParametersCombinations")
	public void testConvertPdfFileToImagesWithNullParameters(
			List<String> images, String pdfFileDestinationPath)
			throws FailedConversionException {
		imagesToPdfFileConverterImpl.convertPdfFileToImages(images,
				pdfFileDestinationPath);
	}

	private Object[] nullParametersCombinations() {
		return new Object[] { new Object[] { null, getPdfDestinationPath() },
				new Object[] { LIST_OF_JPEG_IMAGES, null },
				new Object[] { null, null } };
	}

	@Test(expected = IllegalStateException.class)
	public void testConvertPdfFileToImagesWithEmptyListOfImages()
			throws FailedConversionException {
		imagesToPdfFileConverterImpl.convertPdfFileToImages(
				Collections.emptyList(), getPdfDestinationPath());
	}

	@Test(expected = FailedConversionException.class)
	public void testConvertPdfFileToImagesWithInvalidPdfPath()
			throws FailedConversionException {
		imagesToPdfFileConverterImpl.convertPdfFileToImages(
				LIST_OF_JPEG_IMAGES, "");
	}

}
