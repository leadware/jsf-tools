package net.leadware.tools.ui.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'exception associée aux validations métier
 * 
 * @author Leadware
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Enterprise Architect)</a>
 *
 * @since 3 juin 2014 17:01:37
 */
public class BusinessValidationException extends RuntimeException {

	/**
	 * ID Genere par eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Liste des message d'erreurs
	 */
	private List<String> messages = new ArrayList<String>();
	
	/**
	 * Constructeur par defaut
	 */
	public BusinessValidationException() {}

	/**
	 * Constructeur avec initialisation des parametres
	 * @param messages Liste des messages d'erreurs
	 */
	public BusinessValidationException(List<String> messages) {
		
		// Mise a jour du message
		setMessages(messages);
	}

	/**
	 * Methode permettant d'obtenir le champ messages
	 * @return champ messages
	 */
	public List<String> getMessages() {
	
		// On retourne la valeur du champ
		return messages;
	}

	/**
	 * Methode permettant de modifier le champ messages
	 * @param messages Nouvelle valeur du champ messages
	 */
	public void setMessages(List<String> messages) {
	
		// On modifie la valeur du champ
		this.messages = messages;
		
		// Si la liste de messages est nulle
		if(this.messages == null) this.messages = new ArrayList<String>();
	}
	
	/**
	 * Methode d'ajout des messages d'erreur
	 * @param messages	Tableau des messages d'erreurs
	 */
	public void addMessages(String ... messages) {
		
		// Si la liste est vide
		if(messages == null || messages.length == 0) return;
		
		// Parcours du tableau
		for (String message : messages) {
			
			// Ajout du message
			this.messages.add(message);
		}
	}
	
	/**
	 * Methode d'obtention du message d'erreur
	 */
	@Override
	public String getMessage() {
		
		// Constructeur de chaine
		StringBuilder messageBuilder = new StringBuilder("");
		
		// Ajout d'une ligne
		messageBuilder.append("\n");
		
		// Parcours de la liste de messages
		for (String message : messages) {
			
			// Ajout du message
			messageBuilder.append(message);
			
			// Ajout du retour chariot
			messageBuilder.append("\n");		
		}
		
		// On retourne le message
		return messageBuilder.toString();
	}
	
	/**
	 * Methode d'obtention de l'etat d'erreur
	 * @return	Etat d'erreur
	 */
	public boolean isErrors() {
		
		// On retourne comparaison sur la taille de la liste des erreurs
		return messages.size() > 0;
	}
}
