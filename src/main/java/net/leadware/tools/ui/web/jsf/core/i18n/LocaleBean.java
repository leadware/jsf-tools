/**
 * 
 */
package net.leadware.tools.ui.web.jsf.core.i18n;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import net.leadware.tools.ui.common.i18n.I18NProperties;
import net.leadware.tools.ui.web.jsf.core.util.JSFUtils;

/**
 * Classe en charge de la gestion de la localisation
 * 
 * @author Leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  22 août 2014 10:28:05
 */
@ManagedBean
@SessionScoped
public class LocaleBean implements Serializable {
	
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Nom du bean managé
	 */
	public static final String BEAN_NAME = "localeBean";
	
	/**
	 * Locale courante
	 */
	private Locale locale;
		
	
	/**
	 * Constructeur par défaut
	 */
	public LocaleBean() {
				
	}

	/**
	 * Initialisation 
	 */
	@PostConstruct
	protected void init(){
		
		// Initialisation de la locale
		this.locale = getDefaultLocale();
		
		// Trace
		System.out.println("LocaleBean.init() locale = " + locale);
	}
	
	/**
	 * Obtention de la locale par défaut
	 * 
	 * @return Locale par défaut
	 */
	public Locale getDefaultLocale(){
		
		// Locale par défaut
		Locale defaultLocale = null;
		
		// Si nous sommes dans le contexte d'une requete
		if(JSFUtils.isFacesContextExist()) 
			// Initialisation avec la locale par défaut de l'application  
			defaultLocale =  JSFUtils.getFacesApplication().getDefaultLocale();
		
		// Si la locale est indéfinie, initialisation avec la locale de la JVM
		if(defaultLocale == null) defaultLocale = Locale.getDefault();
		
		return defaultLocale;
	}

	/**
	 * Retourne la valeur de la propriété locale
	 * @return La propriété locale
	 */
	public Locale getLocale() {
		
		// Si par mégarde la locale est indéfinie, l'on retourne la locale par défaut 
		if(locale == null) return this.getDefaultLocale();
		
		return locale;
	}


	/**
	 * Met à jour la propriété locale
	 * 
	 * @param locale Nouvelle valeur de la propriété locale
	 */
	public void setLocale(Locale locale) {
		
		// Si la locale est indéfinie, l'on définie une valeur par défaut
		if(locale == null) this.locale = this.getDefaultLocale();
		
		// Sinon mise à jour
		else this.locale = locale;
	}
	
	
	/**
	 * Retourne la valeur de la propriété language
	 * @return La propriété language
	 */
	public String getLanguage() {
		
		return this.getLocale().getLanguage();
	}


	/**
	 * Met à jour la propriété language
	 * @param language Nouvelle valeur de la propriété language
	 */
	public void setLanguage(String language) {
		
		try{
			this.setLocale(new Locale(language));
		}
		catch(Exception e){
			// Trace
			e.printStackTrace();
		}
	}


	/**
	 * Modifie la locale de l'application
	 * 
	 * @param locale Nouvelle valeur de la locale
	 */
	public void changeLocale(Locale locale){
		
		// Mise à jour de la locale
		this.setLocale(locale);
		
		// Si nous sommes dans le contexte d'une requete
		if(FacesContext.getCurrentInstance() != null)
			// Mise à jour de la locale de la vue associée
			FacesContext.getCurrentInstance().getViewRoot().setLocale(this.getLocale());
	}
	
	/**
	 * Modifie la locale de l'application
	 * 
	 * @param language Langue de la locale
	 */
	public void changeLocale(String language){
		
		try{
			this.changeLocale(new Locale(language));
		}
		catch(Exception e){
			// Trace
			e.printStackTrace();
		}
	}
	
	/**
	 * Retourne la valeur localisée d'une ressource à partir de son ID
	 * 
	 * @param resourceID ID de la ressource
	 * 
	 * @return Valeur localisée de la ressource
	 */
	public String getLocalizedMessage(String resourceID){
		
		return this.getLocalizedMessage(resourceID, (Object []) null);
	}
	
	
	/**
	 * Retourne la valeur localisée d'une ressource à partir de son ID
	 * 
	 * @param resourceID ID de la ressource
	 * @param params	 Paramètres
	 * 
	 * @return Valeur localisée de la ressource
	 */
	public String getLocalizedMessage(String resourceID, Object...params){
						
		// Retour de la valeur associée à l'ID
		return I18NProperties.getString(this.getMessageBundle(), this.getLocale(), resourceID, params);
	}	
	
	/**
	 * Retourne le nom de base du fichier de messages <br/>
	 * Par défaut retourne le nom du fichier configuré dans la propriété messageBundle du faces-config
	 * 
	 * @return Nom de base du fichier de messages
	 */
	protected String getMessageBundle(){
		
		// Fichier de messages
		String messageBundle = null;
		
		// Si nous sommes dans le contexte d'une requete, obtention du messageBundle configuré par défaut
		if(JSFUtils.isFacesContextExist()) messageBundle = JSFUtils.getFacesApplication().getMessageBundle();
		
		// Trace
		System.out.println("LocaleBean.getMessageBundle() : messageBundle = " + messageBundle);
		
		return messageBundle;
	}
}
