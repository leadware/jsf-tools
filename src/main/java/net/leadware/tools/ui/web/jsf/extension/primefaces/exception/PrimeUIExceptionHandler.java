/**
 * 
 */
package net.leadware.tools.ui.web.jsf.extension.primefaces.exception;

import org.primefaces.context.RequestContext;

import net.leadware.tools.ui.web.jsf.core.exception.DefaultUIExceptionHandler;

/**
 * Implémentation par défaut spécifique à Primefaces de la classe de gestion des exceptions
 * 
 * @author Leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  21 août 2014 15:02:12
 */
public class PrimeUIExceptionHandler extends DefaultUIExceptionHandler  {
	
	/**
	 * Paramètre indiquant si une erreur est survenue
	 */
	public static  final String IS_ERROR = "isError";

	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.web.jsf.core.exception.DefaultUIExceptionHandler#handle(java.lang.Exception)
	 */
	@Override
	public void handle(Exception e) {
				
		// Appel parent
		super.handle(e);
		
		// Ajout d'un paramètre pour le callback client indiquant qu'il y'a eu une erreur
		RequestContext.getCurrentInstance().addCallbackParam(IS_ERROR, true);		
	}
	
	
}
