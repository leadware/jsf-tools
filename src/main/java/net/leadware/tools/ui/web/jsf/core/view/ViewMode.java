/**
 * 
 */
package net.leadware.tools.ui.web.jsf.core.view;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Différents modes de vues dans une page multi-vues
 * @author leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  13 août 2014 11:10:41
 */
@ManagedBean
@ApplicationScoped
public class ViewMode {
	
	/**
	 * Mode création
	 */
	public static final String CREATION_MODE = "creation";	
	
	/**
	 * Mode consultation
	 */
	public static final String CONSULTATION_MODE = "consultation";
	
	/**
	 * Mode modification
	 */
	public static final String MODIFIFICATION_MODE = "modification";

	/**
	 * Unique instance
	 */
	private static  ViewMode _instance = null;
	
	
	/**
	 * 
	 * @return Instance unique de cette classe
	 */
	public static synchronized ViewMode getInstance(){
		
		// Si l'instance est null, l'on instancie
		if(_instance == null) _instance = new ViewMode();
		
		return _instance;
	}
	
	
	/**
	 * Retourne le mode CREATION_MODE sous forme de <code>String</code> 
	 * @return Mode CREATION_MODE
	 */
	public String getCreationMode(){
		return CREATION_MODE;
	}
	
	/**
	 * 
	 * @param mode Mode
	 * @return Vrai si le mode est celui de la création, faux sinon
	 */
	public boolean isCreationMode(String mode){
		
		return CREATION_MODE.equalsIgnoreCase(mode);
	}
	
	/**
	 * Retourne le mode MODIFICATION sous forme de <code>String</code> 
	 * @return Mode MODIFICATION
	 */
	public String getModificationMode(){
		return MODIFIFICATION_MODE.toString();
	}	
	
	/**
	 * 
	 * @param mode Mode
	 * @return Vrai si le mode est celui de la modification, faux sinon
	 */
	public boolean isModificationMode(String mode){
		
		return MODIFIFICATION_MODE.equalsIgnoreCase(mode);
	}
	
	
	/**
	 * Retourne le mode CONSULTATION sous forme de <code>String</code> 
	 * @return Mode CONSULTATION
	 */
	public String getConsultationMode(){
		return CONSULTATION_MODE.toString();
	}
	

	/**
	 * 
	 * @param mode Mode
	 * @return Vrai si le mode est celui de la consultation, faux sinon
	 */
	public boolean isConsultationMode(String mode){
		
		return CONSULTATION_MODE.equalsIgnoreCase(mode);
	}
	
	
	/**
	 * 
	 * @param mode Mode
	 * @return Vrai si le mode est celui de la creation ou la modification, faux sinon
	 */
	public boolean isEditionMode(String mode){
		
		return (this.isCreationMode(mode) || this.isModificationMode(mode));
	}
	
}
