/**
 * 
 */
package net.leadware.tools.ui.web.jsf.core.model.pagination;

import java.util.List;

/**
 * Interface de pagination <br/>
 * Définit un ensemble de méthodes pour la pagination (càd chargement le partiel des données)
 * @author leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  12 août 2014 10:13:57
 */
public interface IPagination<T> {

	/**
	 * Retourne partiellement un ensemble de données à partir d'une position de début et d'un nombre maximal
	 * 
	 * @param offset Index de début de la pagination 
	 * @param size Nombre maximal/limite de données	
	 * 	
	 * @return Liste des données
	 */
	public List<T> paginate(int offset, int size);
	
}
