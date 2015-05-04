package cs4r.tools.imagestopdf.converter;

import cs4r.tools.imagestopdf.converter.exception.FailedConversionException;

/**
 * Converts a pdf file into a series of images.
 * 
 * @author cs4r
 */
public interface PdfFileToImagesConverter {

	/**
	 * Transforms a pdf file into a series of images. Each page of the file is
	 * transformed into an image.
	 * 
	 * @param pdfFilePath
	 *            String which represents the path to the pdf file
	 * @param imagesDestinationPath
	 *            String which represents the path to the directory where the
	 *            images will be placed
	 * @param imageFormat
	 *            format of the output images
	 * @pre pdfFilePath is not null and the file represented on it exists
	 * @pre imagesDestinationPath is not null and the path represented on it
	 *      exists
	 * @pre imageFormat is not null
	 * @post Images which are stored in {@source imagesDestinationPath} keep the
	 *       order of the pdf pages in their name by adding an underscore and
	 *       the page number page number
	 * @throws FailedConversionException
	 *             if there is an error reading pdfFile or creating the images
	 */
	void convertPdfFileToImages(String pdfFilePath,
			String imagesDestinationPath, ImageFileFormat imageFormat)
			throws FailedConversionException;

}