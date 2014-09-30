/**
 * 
 */
package net.leadware.tools.ui.web.jsf.core.view.manager;

import java.io.Serializable;

import net.leadware.tools.ui.common.exception.UIExceptionHandler;
import net.leadware.tools.ui.web.jsf.core.exception.DefaultUIExceptionHandler;
import net.leadware.tools.ui.web.jsf.core.i18n.LocaleBean;
import net.leadware.tools.ui.web.jsf.core.util.JSFUtils;

/**
 * Gestionnaire (Controleur) de base des vues
 * @author leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  12 août 2014 20:54:12
 */
public abstract class AbstractViewManager implements Serializable {

	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = -7200238132608843715L;

	/**
	 * Identifiant des gestionnaires de vues par défaut
	 */
	public static final String DEFAULT_VIEW_MANAGER_IDENTIFIER = "Bean";
	
	/**
	 * Identifiant de la vue
	 */
	private String viewIdentifier = null;
	
	/**
	 * Gestionnaire d'exception par défaut
	 * TODO : Prendre avantage de JSF en fournissant une instance dans le scope application
	 */
	private static UIExceptionHandler exceptionHandler = new DefaultUIExceptionHandler();
	
	/**
	 * Retourne l'identifiant des gestionnaires de vues
	 * @return Identifiant des gestionnaires des vues
	 */
	protected String getViewManagerIdentifier(){
		
		return DEFAULT_VIEW_MANAGER_IDENTIFIER;
	}
	
	/**
	 * Retourne l'identifiant la vue <br/>
	 * Par défaut le nom de la classe privé de l'idenitifiat des gestionnaires de vues
	 * @return Identifiant de la vue
	 */
	public String getViewIdentifier(){
		
		// Si l'identifiant de la vue n'est pas défini
		if(this.viewIdentifier == null){

			// Nom de la classe
			String _viewIdentifier = this.getClass().getSimpleName();
			
			// Index de l'identifiant des gestionnaires de vues
			int index = _viewIdentifier.indexOf(this.getViewManagerIdentifier());
						
			// Si l'identifiant des gestionnaires de  vues existe bel et bien
			if(index > -1){
				// L'on  extrait l'identifiant
				_viewIdentifier = _viewIdentifier.replaceFirst(this.getViewManagerIdentifier(), "");
			}
			
			// Conversion de la première lettre en miniscule			
			String firstLetter = _viewIdentifier.substring(0, 1);
			_viewIdentifier = _viewIdentifier.replaceFirst(firstLetter, firstLetter.toLowerCase());
				
			// Mise à jour
			this.viewIdentifier = _viewIdentifier;			
		}
		
		return viewIdentifier;
	}	
	
	/**
	 * Ouvre la vue par défaut <br/>
	 * 
	 * @return La navigation vers la vue appropriée
	 */
	public abstract String openDefaultView();	
	
	/**
	 * Ferme la vue courante
	 * @return La navigation vers la vue appropriée
	 */
	public abstract String closeView();
	
	/**
	 * Retourne la règle navigation vers la vue d'acceuil de l'application <br/>
	 * Par défaut retourne la valeur <code>home</code>
	 * @return 
	 */
	public String getApplicationHomeViewOutcome(){
		return "home";
	}
	
	
	
	/**
	 * Gère les exceptions devant être propagées au client <br/>
	 * L'implémentation effective est déléguée au gestionnaire d'exceptions fourni par la méthode <code>getExceptionHandler</code>
	 * 
	 * @param e Exception
	 */
	protected void handleException(Exception e){
		
		this.getExceptionHandler().handle(e);	
	}
	
	
	/**
	 * Retourne l'instance du gestionnaire d'exceptions
	 * 
	 * @return Gestionnaire d'exceptions
	 */
	protected UIExceptionHandler getExceptionHandler(){
		
		return exceptionHandler;
	}	
	
	/**
	 * Retourne la valeur localisée d'une ressource à partir de son ID
	 * 
	 * @param resourceID ID de la ressource
	 * @param params	 Paramètres
	 * 
	 * @return Valeur localisée de la ressource
	 */	
	protected String getLocalizedMessage(String resourceID, Object...params){
				
		return JSFUtils.getManagedBean(LocaleBean.BEAN_NAME, LocaleBean.class).getLocalizedMessage(resourceID, params);		
	}
	
	/**
	 * Retourne la valeur localisée d'une ressource à partir de son ID
	 * 
	 * @param resourceID ID de la ressource
	 * 
	 * @return Valeur localisée de la ressource
	 */	
	protected String getLocalizedMessage(String resourceID){
		
		return this.getLocalizedMessage(resourceID, (Object []) null);
	}	
		
}
