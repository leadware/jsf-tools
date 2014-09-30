/**
 * 
 */
package net.leadware.tools.ui.web.jsf.core.exception;

import net.leadware.tools.ui.common.exception.UIExceptionHandler;
import net.leadware.tools.ui.web.jsf.core.util.JSFUtils;

/**
 * Implémentation par défaut de la classe de gestion des exceptions
 * 
 * @author Leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  21 août 2014 15:02:12
 */
public class DefaultUIExceptionHandler implements UIExceptionHandler  {

	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.common.exception.UIExceptionHandler#handle(java.lang.Exception)
	 */
	@Override
	public void handle(Exception e) {

		// Si l'exception est nulle, arret
		if(e == null) return;
		
		// Affichage d'un message d'erreur 
		// TODO : Voir comment définir le summary
		JSFUtils.addErrorMessage("", e.getMessage());
		
		// Trace de l'exception
		e.printStackTrace();
	}

}
