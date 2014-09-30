/**
 * 
 */
package net.leadware.tools.ui.common.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Classe de gestion des ressources internationalisées
 * 
 * @author Leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  21 août 2014 19:58:59
 */
public class I18NProperties {

	/**
	 * Retourne la ressource localisée associée à un identifiant 
	 * 
	 * @param bundleName	Nom de la collection de ressources
	 * @param resourceID	Identifiant de la ressource
	 * 
	 * @return	Ressource localisée
	 */
	public static String getString(String bundleName, String resourceID) {
	
		return getString(bundleName, Locale.getDefault(), resourceID);
	}	
	
	
	/**
	 * Retourne la ressource localisée associée à un identifiant 
	 * 
	 * @param bundleName	Nom de la collection de ressources
	 * @param resourceID	Identifiant de la ressource
	 * 
	 * @return	Ressource localisée
	 */
	public static String getString(String bundleName, Locale locale, String resourceID) {
	
		return getString(bundleName, locale, resourceID, (Object []) null);
	}	
	
	

	/**
	 * Retourne la ressource localisée associée à un identifiant 
	 * 
	 * @param bundleName	Nom de la collection de ressources
	 * @param resourceID	Identifiant de la ressource
	 * @param params		Paramètres
	 * 
	 * @return	Ressource localisée et formattée
	 */
	public static String getString(String bundleName, Locale locale, String resourceID, Object...params) {
	
		return getString(bundleName, locale, resourceID, getClassLoader() , params);
	}	
	

        		
	/**
	 * Retourne la ressource localisée associée à un identifiant 
	 * 
	 * @param bundleName	Nom de la collection de ressources
	 * @param resourceID	Identifiant de la ressource
	 * @param loader		ClassLoader à utiliser
	 * @param params		Paramètres
	 * 
	 * @return	Ressource localisée et formattée
	 */
	public static String getString(String bundleName, Locale locale, String resourceID, ClassLoader loader, Object...params) {
				
		try {
			
			// Collection de ressources
			ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, locale, loader);
			
			// Obtention du message
			String message = resourceBundle.getString(resourceID);
			
			// S'il y'a des paramètres
			if(message != null && params != null && params.length > 0) {
				// Formatteur du message
				MessageFormat formatter = new MessageFormat(message, locale);
				
				// Formattage du message
				message = formatter.format(params);				
			}
			
			return message;		
		} 
		catch (Exception e) {			
			// Trace
			e.printStackTrace();
		}

		
		return getNotFoundedResourceTemplate(resourceID);		
	}	

	
	/**
	 * Obtention du ClassLoader
	 * 
	 * @return Le ClassLoader
	 */
   public static ClassLoader getClassLoader() {
	   
	   // Obtention du ClassLoader du thread courant
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      
      // S'il n'existe pas, obtention du ClassLoader système
      if (loader == null) loader = ClassLoader.getSystemClassLoader();
      
      return loader;
   }

   
   /**
    * Retourne un template pour les ressources non trouvées via leur identifiant
    * 
    * @param resourceID Identifiant de la ressource
    * 
    * @return ???<code>resourceId</code>???
    */
   public static String getNotFoundedResourceTemplate(String resourceID){
	   
	   return "??? " + resourceID + " ???";
   }   
	
}
