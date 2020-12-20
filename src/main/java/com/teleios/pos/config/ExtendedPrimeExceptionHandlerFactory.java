package com.teleios.pos.config;

import java.util.Map;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import org.eclipse.jdt.annotation.Nullable;
import org.primefaces.application.exceptionhandler.PrimeExceptionHandler;
import org.primefaces.application.exceptionhandler.PrimeExceptionHandlerFactory;

/**
 * Extended primefaces exception handler factory in order to create a exception
 * handler that redirects to the desired error page.
 */
public class ExtendedPrimeExceptionHandlerFactory extends PrimeExceptionHandlerFactory {
	private static final String ERROR_PAGE = "/error/error.xhtml";

	public ExtendedPrimeExceptionHandlerFactory(final ExceptionHandlerFactory wrapped) {
		super(wrapped);
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return new ExtendedPrimeExceptionHandler(getWrapped().getExceptionHandler());
	}

	private static class ExtendedPrimeExceptionHandler extends PrimeExceptionHandler {

		public ExtendedPrimeExceptionHandler(ExceptionHandler wrapped) {
			super(wrapped);
		}

		@Override
		protected String evaluateErrorPage(@SuppressWarnings("null") Map<String, String> errorPages,
				@Nullable Throwable rootCause) {
			return ERROR_PAGE;
		}

	}

}