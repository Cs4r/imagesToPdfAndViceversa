package cs4r.tools.imagestopdf.converter;

import java.util.List;

import cs4r.tools.imagestopdf.converter.exception.FailedConversionException;

/**
 * Converts a list of images into a pdf file.
 * 
 * @author cs4r
 *
 */
public interface ImagesToPdfFileConverter {
	/**
	 * Transforms a list of images into a pdf file. Each image is transformed
	 * into a page in the pdf file.
	 * 
	 * @param images
	 *            list of paths to the images which will be taken to create the
	 *            pdf file
	 * @param pdfFileDestinationPath
	 *            full path to the pdf file which will be created
	 * 
	 * @pre {@source images} is not null and not empty
	 * @pre {@source pdfFileDestinationPath} is not null
	 * @post The order in {@code images} is kept in the pages of the generated
	 *       pdf file
	 * @throws FailedConversionException
	 *             if there is an error reading any image or creating the pdf
	 */
	public void convertPdfFileToImages(List<String> images,
			String pdfFileDestinationPath) throws FailedConversionException;

}