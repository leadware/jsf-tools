/**
 * 
 */
package net.leadware.tools.ui.web.jsf.core.view;

/**
 * Type de vues
 * 
 * @author Leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 *
 * @since  12 août 2014 20:07:04
 */
public enum ViewType {
	
	/**
	 * Vue de création
	 */
	CREATION("Creation"),

	/**
	 * Vue de consultation ou détails
	 */
	DETAILS("Details"),
	
	/**
	 * Modal panel
	 */
	DIALOG("Dialog"),
	
	/**
	 * Vue d'édition (création / modification)
	 */
	EDITION("Edition"),
	
	/**
	 * Vue de liste
	 */
	LIST("List"),
	
	/**
	 * Vue de modification
	 */
	MODIFICATION("Modification"),
	
	/**
	 * 
	 */
	VIEW("View");
	
	/**
	 * Type de la vue
	 */
	private final String typeName; 
	

	/**
	 * Constructeur paramétré
	 * @param typeName Identifiant de la vue
	 */
	private ViewType(String typeName){
		this.typeName = typeName;
	}
	
	/**
	 * Retourne la valeur de la propriété typeName
	 * @return La propriété typeName
	 */
	public String getTypeName() {
		return typeName;
	}	
	
	/**
	 * Construit et retourne un nom par défaut nom d'une vue
	 * @param viewIdentifier   Identifiant de la vue (role, utilisateur, ...)
	 * @param viewType Type de la vue
	 * @return Un nom par défaut pour la vue
	 */
	public static String buildDefaultViewName(String viewIdentifier, ViewType viewType){
		
		if(viewType != null && viewIdentifier != null && ! viewIdentifier.isEmpty())
			
			return new StringBuilder(viewIdentifier).append(viewType.getTypeName()).toString();
		
		return null;
	}
	
}
