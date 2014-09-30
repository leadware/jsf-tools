/**
 * 
 */
package net.leadware.tools.ui.common.exception;

/**
 * Interface de gestion des exceptions clientes à destination de l'utilisateur
 * 
 * @author Leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  21 août 2014 14:55:49
 */
public interface UIExceptionHandler {

	
	/**
	 * Méthode implémentant la logique de gestion des exceptions à destionation de l'utilisateur
	 * 
	 * @param e Exception à gérer
	 */
	public void handle(Exception e);
}
