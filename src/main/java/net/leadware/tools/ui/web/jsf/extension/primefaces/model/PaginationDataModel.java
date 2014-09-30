/**
 * 
 */
package net.leadware.tools.ui.web.jsf.extension.primefaces.model;

import java.util.List;
import java.util.Map;

import net.leadware.tools.ui.web.jsf.core.model.pagination.IPagination;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * DataModel spécialisé pour les tableaux DataTable devant assurer une pagination des données
 * @author leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  11 août 2014 14:10:20
 */
public class PaginationDataModel<T> extends LazyDataModel<T> {

	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Composant implémentant la pagination
	 */
	private IPagination<T> paginationImpl;	
	
	/**
	 * Constructeur par défaut
	 */
	public PaginationDataModel() {
		
		super();
		
	}
	
	/**
	 * Constructeur paramétré
	 * @param paginationImpl Composant implémentant la pagination
	 */
	public PaginationDataModel(IPagination<T> paginationImpl) {
		
		super();
		
		this.paginationImpl = paginationImpl;	
	}

	/* (non-Javadoc)
	 * @see org.primefaces.model.LazyDataModel#load(int, int, java.lang.String, org.primefaces.model.SortOrder, java.util.Map)
	 */
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		// Parameters output
		String output = String.format("first=%s, pageSize=%s, sortField=%s", ""+first, ""+pageSize, sortField);

		// Trace
		System.out.println("PaginationDataModel.load( " + output + " )");
		
		if(first > this.getRowCount())
		System.out.println("PaginationDataModel.load(first > rowCount) : " + first + " > " + this.getRowCount());
		
		// Recherche des données
		return paginationImpl.paginate(first, pageSize);
	}
		
}
