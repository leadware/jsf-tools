/**
 * 
 */
package net.leadware.tools.ui.web.jsf.extension.primefaces.view.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.leadware.tools.ui.common.exception.UIExceptionHandler;
import net.leadware.tools.ui.web.jsf.core.model.pagination.IPagination;
import net.leadware.tools.ui.web.jsf.core.view.manager.AbstractCRUDViewManager;
import net.leadware.tools.ui.web.jsf.extension.primefaces.exception.PrimeUIExceptionHandler;
import net.leadware.tools.ui.web.jsf.extension.primefaces.model.PaginationDataModel;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

/**
 * Gestionnaires de base des vues CRUD basées sur primefaces
 * @author leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  16 août 2014 14:08:32
 */
public abstract class PrimeCRUDViewManager<T> extends AbstractCRUDViewManager<T> implements IPagination<T>  {

	/**
	 * ID de sérialisation
	 */	
	private static final long serialVersionUID = 7154992491050436905L;

	/**
	 * Gestionnaire d'exception par défaut pour l'implémentation Primefaces
	 * TODO : Prendre avantage de JSF en fournissant une instance dans le scope application
	 */
	private static UIExceptionHandler exceptionHandler = new PrimeUIExceptionHandler();
	
	
	/**
	 * Constructeur par défaut
	 */
	public PrimeCRUDViewManager() {
		
		System.out.println("PrimeCRUDViewManager.PrimeCRUDViewManager()");
	}


	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.web.jsf.core.view.manager.AbstractCRUDViewManager#postConstruct()
	 */
	@Override
	@PostConstruct
	protected void postConstruct() {
		
		System.out.println("PrimeCRUDViewManager.postConstruct()");
		
		// Appel parent
		super.postConstruct();
		
		// Initialisation du modèle de données du tableau
		this.setDataTableModel(new PaginationDataModel<T>(this));		
	}


	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.web.jsf.core.view.manager.AbstractCRUDViewManager#search()
	 */
	@Override
	public void search() {
		
		// Trace
		System.out.println("PrimeCRUDViewManager.search()");
		
		try{
			
			// Détermination du nombre total de données
			int rowCount = this.businessCountByCriteria();
			
			// Mise à jour du nombre total de données dans le modèle			
			if(this.getDataTableModel() != null && this.getDataTableModel() instanceof LazyDataModel){
				System.out.println("PrimeCRUDViewManager.search() : ==> Mise à jour du nombre total de données : rowCount = " + rowCount);
				((LazyDataModel<T>)dataTableModel).setRowCount(rowCount);
			}
			
			// Réinitialisation de l'offset à 0
			System.out.println("PrimeCRUDViewManager.search() : offset = " + dataTable.getFirst());
			this.getDataTable().setFirst(0);
			
			// Recherche effective des données
			// La pagination est directement gérée par le composant de présentation		
		}
		catch(Exception e){
			// Gestion de l'exception
			this.handleException(e);
		}	
	}
	
	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.web.jsf.core.view.manager.AbstractCRUDViewManager#paginate(int, int)
	 */
	@Override
	public List<T> paginate(int offset, int rows) {
		// Liste des données
		List<T> data_ = this.getDatas();
		
		try{			
			// Recherche effective des données
			data_ = businessSearchByCriteria(offset, rows);
			
			// Mise à jour de la liste des données
			this.setDatas(data_);
		}
		catch(Exception e){				
			// Gestion de l'exception
			this.handleException(e);
		}
		
		// Retour de la liste des données
		return data_;
	}
	
	
	/**
	 * Affiche la vue de sélection des données dans une boite de dialogue
	 */
	public void openSelectionView(){
		
		// Trace
		System.out.println("PrimeCRUDViewManager.displaySelectionView()");
				
		// Affichage de la vue de sélection des données
		RequestContext.getCurrentInstance().openDialog(this.getListViewOutcome(), this.getSelectionViewOptions(), null);		
	}
	
	/**
	 * Retourne les options de présentation de la vue de sélection de données
	 * 
	 * @return Map des options de présentation
	 */
	protected Map<String, Object> getSelectionViewOptions(){
		
		// Options de présentaion de la vue de sélection
		Map<String,Object> options = new HashMap<String, Object>();
		
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentWidth", 1000);
		options.put("contentHeight", 500);

		return options;
	}
	
	
	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.web.jsf.core.view.manager.AbstractCRUDViewManager#select(java.lang.Object)
	 */
	@Override
	public String select(Object object) {
			
		// Si aucun objet n'est sélectionné
		if(object == null) 						
			// L'on lève une exception
			throw new RuntimeException(this.getLocalizedMessage("Veuillez au préalable définir l'objet à sélectionner"));					
		
		// Ferme la vue de sélection en passant en paramètre l'objet sélectionné
		RequestContext.getCurrentInstance().closeDialog(object);
		
		// Aucune navigation
		return null;
	}
	
	/**
	 * Méthode de gestion de l'évènement de sélection
	 * 
	 * @param event Evènement
	 */
	public void handleSelectionEvent(SelectEvent event){
				
		// Post de la donnée sélectionnée pour traitement
		this.handleSelectionData(event.getObject());		
	}
	
	
	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.web.jsf.core.view.manager.AbstractViewManager#getExceptionHandler()
	 */
	@Override
	protected UIExceptionHandler getExceptionHandler() {
		
		return exceptionHandler;
	}
	
}
