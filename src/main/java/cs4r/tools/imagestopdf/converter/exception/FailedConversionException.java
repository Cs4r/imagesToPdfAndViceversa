package cs4r.tools.imagestopdf.converter.exception;

/**
 * My own sort of exception to wrap a set of exceptions that could occur during
 * the conversion of a file into images or vice versa. It points out that there
 * was an important problem so the conversion failed and was not performed.
 * 
 * @author cs4r
 *
 */
public class FailedConversionException extends Exception {

	private static final String EXCEPTION_MESSAGE = "Conversion could not be performed";
	private static final long serialVersionUID = 1L;

	public FailedConversionException() {
		super(EXCEPTION_MESSAGE);
	}

	public FailedConversionException(Throwable cause) {
		super(EXCEPTION_MESSAGE, cause);
	}

	public FailedConversionException(String message, Throwable cause) {
		super(EXCEPTION_MESSAGE, cause);
	}

}
