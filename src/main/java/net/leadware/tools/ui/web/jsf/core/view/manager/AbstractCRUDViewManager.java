/**
 * 
 */
package net.leadware.tools.ui.web.jsf.core.view.manager;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;

import net.leadware.tools.ui.common.exception.BusinessValidationException;
import net.leadware.tools.ui.web.jsf.core.util.JSFUtils;
import net.leadware.tools.ui.web.jsf.core.view.ViewMode;
import net.leadware.tools.ui.web.jsf.core.view.ViewType;


/**
 * Gestionnaire (Controleur) de base des vues de type CRUD
 * @author leadware
 * @author <a href="mailto:lkamhoua@leadware.net">Landry KAMHOUA (J. Enterprise Architect)</a>
 * @since  8 août 2014 13:38:13
 */
public abstract class AbstractCRUDViewManager <T> extends AbstractViewManager {
	
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 3375716956394724265L;

	/**
	 * Type en paramètre de la classe abstraite
	 */
	protected final Class<T> parameterizedType;
	
	/**
	 * Objet courant manipulé sur une vue (Liste, Consultation, ...) à un instant donné
	 */
	protected T currentObject;

	/**
	 * Objet edité sur la vue d'édition (Création, Modificaton)
	 */
	protected T editedObject;
	
	/**
	 * Critères de recherche de la vue de liste
	 */
	protected T criteriaObject;
	
	/**
	 * Objet sélectionné dans le tableau de données de la vue de liste
	 */
	protected T selectedObject;
	
	/**
	 * Liste des données du tableau de la vue de liste
	 */
	protected List<T> datas;
	
	/**
	 * Tableau de données de la vue de liste
	 */
	protected UIData dataTable;
	
	/**
	 * DataModel du tableau de données de la vue de liste
	 */
	protected DataModel<T> dataTableModel;
	
	/**
	 * Mode courant la vue en charge de l'objet courant <br/>
	 * Permet de spécifier la vue courante d'une page multi-vues 
	 * <li>creation</li>
	 * <li>modification</li>
	 * <li>consultation</li>
	 * <li>...</li>
	 */
	protected String multiViewPageMode;
	
//	/**
//	 * Indique s'il y'a un modal un panel multi-vues pour la gestion unifiée de la création, consultation, modification
//	 */
//	protected boolean multiViewDialog = true;
	
	/**
	 * Constructeur par défaut
	 */
	@SuppressWarnings("unchecked")
	protected AbstractCRUDViewManager(){
		
		System.out.println("AbstractCRUDViewManager.AbstractCRUDView()");
		
		// Initialisation de la classe paramétrée
		this.parameterizedType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];						
	}

	/**
	 * Méthode d'initialisation  du contexte
	 */
	@PostConstruct
	protected void postConstruct(){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.postConstruct()");
				
		// Initialisation des critères de recherche
		this.initializeCriteria();
	}
	
	/**
	 * Méthode de destruction du contexte
	 */
	@PreDestroy
	protected void preDestroy(){
		
	}
	
	/**
	 * Méthode de création ou d'ajout 
	 * @param object Objet devant être édité
	 * @return La navigation vers la vue de création
	 */
	protected String create(T object){
		
		// Si le template de création est null, l'on initialise 
		if(object == null) object = this.getCreationTemplateInstance();
		
		// Mise à jour de l'objet à éditer
		this.setEditedObject(object);
		
		// Mise à jour du mode de la vue dans le cas d'une page multi-vues
		this.setMultiViewPageMode(ViewMode.CREATION_MODE);	
		
		// Navigation vers la vue de création
		return this.getCreationViewOutcome();
	}
	
	/**
	 * Action de création ou d'ajout
	 * @return La navigation vers la vue de création
	 */
	public String create(){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.create()");
		
		try{
			
			// Création à partir d'une nouvelle instance d'objet
			return this.create(this.getCreationTemplateInstance());					
		}
		catch(Exception e){
			// Gestion de l'exception			
			this.handleException(e);
		}
		
		return null;
	}
	
	
	/**
	 * Action de création ou d'ajout
	 * @param event Evènement
	 */
	public void create(ActionEvent event){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.create(event)");
		
		try{
			// Création à partir d'une nouvelle instance d'objet
			this.create(this.getCreationTemplateInstance());			
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}	
	}

	/**
	 * Méthode de copie  
	 * @param object Objet devant être copié pour création
	 * @return La navigation vers la vue de création
	 */
	protected String copy(T object){
		
		// Si aucun objet n'est sélectionné
		if(object == null) 						
			// L'on lève une exception
			throw new RuntimeException(this.getLocalizedMessage("Veuillez au préalable définir l'objet à copier"));					
				
		// Création sur la base d'une copie
		return this.create(this.getCreationTemplateInstance(object));
	}
	
	
	/**
	 * Action de copie, création à partir d'un modèle
	 * @return La navigation vers la vue de création
	 */
	public String copy(){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.copy()");
		
		try{
			
			// Création sur la base de l'objet courant
			return this.copy(this.getCurrentObject());					
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}	
		
		return null;
	}
	
	/**
	 * Action de copie, création à partir d'un modèle
	 * @param event Evènement
	 */
	public void copy(ActionEvent event){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.copy(event)");
			
		try{
			// Création sur la base de l'objet courant
			this.copy(this.getCurrentObject());			
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}		
	}
	
		
	/**
	 * Méthode de consultation des données
	 * @param object Objet à consulter
	 * @return La Navigation vers la vue de consultation
	 */
	public String display(T object){
		
		// Si l'objet est indéfini
		if(object == null)			
			// L'on lève une exception
			throw new RuntimeException(this.getLocalizedMessage("Veuillez au préalable définir l'objet à consulter"));		
				
		// Mise à jour de l'objet courant
		this.setCurrentObject(this.reload(object));
		
		// Mise à jour du mode de la vue dans le cas d'une page multi-vues
		this.setMultiViewPageMode(ViewMode.CONSULTATION_MODE);			
		
		// Navigation vers la vue de consultation
		return this.getDetailsViewOutcome();
	}
	
	
	/**
	 * Action de consultation des détails
	 * @return La navigation vers la vue de consultation
	 */
	public String display(){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.display()");
		
		try{			
			// Consultation de l'objet courant
			return this.display(this.getCurrentObject());					
		}
		catch(Exception e){
			// Gestion de l'exception			
			this.handleException(e);
		}
		
		return null;
	}
		
	/**
	 * Action de consultation des détails
	 * @param event Evènement
	 */
	public void display(ActionEvent event){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.display(event)");
		
		try{
			// Consultation de l'objet courant
			this.display(this.getCurrentObject());			
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}	
	}	
	
	/**
	 * Méthode de modification des données
	 * @param object Objet à consulter
	 * @return La Navigation vers la vue de modification
	 */
	public String modify(T object){
		
		// Si l'objet est indéfini
		if(object == null)			
			// L'on lève une exception
			throw new RuntimeException(this.getLocalizedMessage("Veuillez au préalable définir l'objet à modifier"));		
				
		// Mise à jour de l'objet à éditer
		this.setEditedObject(object);
		
		// Mise à jour du mode de la vue dans le cas d'une page multi-vues
		this.setMultiViewPageMode(ViewMode.MODIFIFICATION_MODE);			
		
		// Navigation vers la vue de consultation
		return this.getModificationViewOutcome();
	}
	
	
	/**
	 * Action de modification
	 * @return La navigation vers la vue de modification
	 */
	public String modify(){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.modify()");
		
		try{			
			// Modification de l'objet courant
			return this.modify(this.getCurrentObject());					
		}
		catch(Exception e){
			// Gestion de l'exception			
			this.handleException(e);
		}
		
		return null;
	}
	
	
	/**
	 * Action de modification
	 * @param event Evènement
	 */
	public void modify(ActionEvent event){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.modify(event)");
		
		try{
			// Modification de l'objet courant
			this.modify(this.getCurrentObject());			
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}	
	}	
	
	/**
	 * Méthode d'enregistrement des données en création
	 * @param object Objet à créer
	 * @return La navigation vers la vue de consultation
	 */
	protected String save(T object){
		
		// Validation des données
		this.checkEditionData(object);
				
		// Création éffective
		T savedObject = this.businessSave(object);
		
		// Consultation de l'objet
		String navigation = this.display(savedObject);
		
		// Message
		JSFUtils.addInfoMessage("", this.getLocalizedMessage("Création éffectuée avec succès"));
		
		// Navigation vers la vue de consultation
		return navigation; //this.getDetailsViewOutcome();	
	}	

	/**
	 * Action d'enregistrement des données en création
	 * @return La navigation vers la vue de consultation
	 */
	public String save(){

		// Trace
		System.out.println("AbstractCRUDViewManager.save()");

		try{			
			// Création effective
			 return this.save(this.getEditedObject());			
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}	
		
		return null;
	}
	
	/**
	 * Action d'enregistrement des données en création
	 * @param event Evènement
	 */
	public  void save(ActionEvent event){

		// Trace
		System.out.println("AbstractCRUDViewManager.save()");

		try{
			
			// Création effective
			this.save(this.getEditedObject());			
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}				
	}	

	/**
	 * Méthode d'enregistrement des données en modification
	 * 
	 * @param object Objet à modifier
	 * 
	 * @return La navigation vers la vue de consultation
	 */
	protected String update(T object){
		
		// Validation des données
		this.checkEditionData(object);
				
		// Modification éffective
		T updatedObject = this.businessUpdate(object);
		
		// Met à jour l'objet la liste des données
		this.updateInDatatable(object, updatedObject);
			
		// Consultation de l'objet
		String navigation = this.display(updatedObject);
		
		//Message
		JSFUtils.addInfoMessage("", this.getLocalizedMessage("Modification éffectuée avec succès"));
		
		// Navigation vers la vue de consultation
		return navigation; //this.getDetailsViewOutcome();		
	}	

	/**
	 * Action d'enregistrement des données en modification
	 * @return La navigation vers la vue de consultation
	 */
	public String update(){

		// Trace
		System.out.println("AbstractCRUDViewManager.update()");

		try{
			
			// Modification effective
			this.update(this.getEditedObject());		
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}	
		
		return null;
	}
	
	
	/**
	 * Action d'enregistrement des données en modification
	 * @param event Evènement
	 */
	public  void update(ActionEvent event){

		// Trace
		System.out.println("AbstractCRUDViewManager.update()");

		try{
			
			// Modification effective
			update(this.getEditedObject());		
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}				
	}	
	
	
	/**
	 * Méthode de suppression
	 * @param object Objet à supprimer
	 * @return La navigation vers la vue de liste
	 */
	protected String delete(T object){
		
		// Si l'objet est indéfini
		if(object == null)			
			// L'on lève une exception
			throw new RuntimeException(this.getLocalizedMessage("Veuillez au préalable définir l'objet à supprimer"));		
		
		// Suppression effective
		this.businessDelete(object);	
		
		// Suppression logique dans la liste des données
		this.removeInDatatable(object);
		
		// Message utilisateur
		JSFUtils.addInfoMessage("", this.getLocalizedMessage("Suppression éffectuée avec succès"));
		
		// Navigation vers la vue de liste
		return this.getListViewOutcome();
	}
	
	/**
	 * Retire l'objet du tableau de données
	 * 
	 * @param object Objet à enlever du tableau de données
	 */
	protected void removeInDatatable(T object){
				
		// Si l'objet et la liste des données existent
		if(object != null && this.getDatas() != null)
			// Retrait de l'objet
			this.getDatas().remove(object);	
	}
	
	
	/**
	 * Met à jour un objet dans le tableau de données
	 * 
	 * @param old  Objet à remplacer
	 * @param new_ Objet de remplacement
	 */
	protected void updateInDatatable(T old, T new_){
		
		// Si l'objet à remplacer existe dans la liste
		if(this.getDatas() != null && this.getDatas().contains(old)){
			
			// Index de l'objet à remplacer
			int index = this.getDatas().indexOf(old);
			
			// Retrait de l'objet à remplacer
			this.getDatas().remove(old);
			
			// Insertion du nouvel objet
			this.getDatas().add(index, new_);		
		}		
	}	


	/**
	 * Vérifie les données issues de l'édition (création ou modification)
	 * @param object Objet édité
	 * @throws BusinessValidationException Exception de validation levé
	 */
	protected void checkEditionData(T object) throws BusinessValidationException {
		
		// Implémentation par défaut vide
	}
	
	
	/**
	 * Action de suppression
	 * @param event Evènement
	 * @return Navigation vers la vue appropriée
	 */
	public  String delete(){

		// Trace
		System.out.println("AbstractCRUDViewManager.delete()");

		try{
			
			// Suppression effective
			return this.delete(this.getCurrentObject());			
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}	
		
		return null;
	}	
	
	
	/**
	 * Action de suppression
	 * @param event Evènement
	 */
	public  void delete(ActionEvent event){

		// Trace
		System.out.println("AbstractCRUDViewManager.delete()");

		try{
			
			// Suppression effective
			this.delete(this.getCurrentObject());			
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}				
	}	
	
	
	/**
	 * Méthode de sélection
	 * 
	 * @param object Objet sélectionné
	 */
	public abstract String select(Object object);
	
	/**
	 * Action de sélection 
	 * 
	 * @return Navigation vers la vue appropriée
	 */
	public String select(){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.select()");
		
		try{
			// Sélection effective
			return this.select(this.getCurrentObject());
		}
		catch(Exception e){				
			// Gestion de l'exception
			this.handleException(e);
		}
		
		return null;
	}
	
	
	/**
	 * Action de sélection 
	 * 
	 * @param event Evènement levé
	 */
	public void select(ActionEvent event){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.select()");
		
		try{
			// Sélection effective
			this.select(this.getCurrentObject());
		}
		catch(Exception e){				
			// Gestion de l'exception
			this.handleException(e);
		}
	}
	
	/**
	 * Méthode en charge de la gestion des données sélectionnées
	 * 
	 * @param data Données sélectionnées
	 */
	protected void handleSelectionData(Object data){
		
		// Implémentation  par défaut
	}
	

	/**
	 * Méthode de prévisualisation 
	 * 
	 * @param object Objet devant être prévisualisé
	 * @return La navigation vers la vue appropriée
	 */
	protected String preview(T object){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.preview()");
		
		throw new RuntimeException("Méthode pas encore implémentée");		
	}
	
	/**
	 * Action de prévisualisation
	 * 
	 * @return La navigation vers la vue de appropriée
	 */
	public String preview(){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.preview()");
		
		try{
			
			// Prévisualisation effective
			return this.preview(this.getCurrentObject());				
		}
		catch(Exception e){
			// Gestion de l'exception			
			this.handleException(e);
		}
		
		return null;
	}
	
	
	/**
	 * Action de prévisualisation
	 * 
	 * @param event Evènement
	 */
	public void preview(ActionEvent event){
		
		// Trace
		System.out.println("AbstractCRUDViewManager.preview(event)");
		
		try{
			
			// Prévisualisation effective
			this.preview(this.getCurrentObject());				
		}
		catch(Exception e){			
			// Gestion de l'exception
			this.handleException(e);
		}	
	}
	
	
	/**
	 * Action de (ré)initialisation des critères de recherche
	 */
	public void initializeCriteria(){
		// Trace
		System.out.println("AbstractCRUDViewManager.initializeCriteria()");
		
		// Initialisation des critérias
		this.criteriaObject = this.getNewParameterizedTypeInstance();
	}
	
	/**
	 * TODO Reviser l'implémentation par défaut
	 * Action de recherche des données
	 */
	public abstract  void search();
		
	/**
	 * Recherche des données à base d'une position de début et d'un nombre maximal
	 * 
	 * @param offset Index de début de la pagination 
	 * @param rows Nombre maximal de lignes(tuples) à rechercher		
	 * @return Liste des données trouvées
	 */
	protected abstract List<T> businessSearchByCriteria(int offset, int rows);
	
	/**
	 * Comptage (métier) du nombre de données sur la base de critères
	 * @return
	 */
	protected abstract  int businessCountByCriteria();
	
	/**
	 * Enregistrement (métier) des données en création
	 * @param object Objet à créer
	 * @return Objet créé
	 */
	protected abstract T businessSave(T object);

	/**
	 * Enregistrement (métier) des données en modification
	 * @param object Objet à modifier
	 * @return Objet modifié
	 */
	protected abstract T businessUpdate(T object);
	
	
	/**
	 * Suppression (métier) des données
	 * @param object Objet à supprimer
	 */
	protected abstract void businessDelete(T object);
	
	
	/**
	 * Retourne une nouvelle instance à jour de l'objet en paramètre <br/>
	 * Par défaut c'est l'objet passé en paramètre qui est retourné
	 * @param object Objet à raffraichir
	 * @return Nouvelle instance raffraichie
	 */
	protected T reload(T object){
		return object;
	}
	
	/**
	 * Retourne une nouvelle instance du type en paramètre de la classe
	 * @return Nouvelle instance du <code>T</code> en paramètre de la classe
	 */
	protected T getNewParameterizedTypeInstance(){
		
		// Instance
		T _instance = null;
		
		try{
			// Nouvelle instance
			_instance =  this.parameterizedType.newInstance();
		}
		catch(Exception e){
			// Trace de l'exception
			e.printStackTrace();
		}
		
		return _instance;
	}
	
	/**
	 * Retourne une nouvelle instance d'objet pour la vue de création
	 * @return Une nouvelle instance
	 */
	protected T getCreationTemplateInstance(){
		return this.getNewParameterizedTypeInstance();
	}
	
	/**
	 * Retourne à partir d'un modèle, une nouvelle instance d'objet pour la vue de création
	 * @param model Modèle à copier
	 * @return Une nouvelle instance
	 */
	protected T getCreationTemplateInstance(T model){
		return model;
	}
	
	
	/**
	 * Ferme la vue de liste
	 * @return Une règle de navigation dynamique vers une autre vue
	 */
	public String closeListView(){
				
		// Par défaut navigation vers la vue d'acceuil de l'application
		return this.getApplicationHomeViewOutcome();
	}
	
	/**
	 * Ouvre la vue de liste
	 * 
	 * @return La navigation vers la vue de liste
	 */
	public String openListView(){
				
		// Navigation vers la vue de liste
		return this.getListViewOutcome();
	}
	
	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.web.jsf.core.view.manager.AbstractViewManager#openDefaultView()
	 */
	@Override
	public String openDefaultView() {

		// Par défaut ouverture de la vue de liste
		return this.openListView();
	}	
	
	/**
	 * Retourne la valeur de la propriété currentObject
	 * @return La propriété currentObject
	 */
	public T getCurrentObject() {
		return currentObject;	
	}

	/**
	 * Met à jour la propriété currentObject
	 * @param currentObject Nouvelle valeur de la propriété currentObject
	 */
	public void setCurrentObject(T currentObject) {
		this.currentObject = currentObject;
	}

	/**
	 * Retourne la valeur de la propriété editedObject
	 * @return La propriété editedObject
	 */
	public T getEditedObject() {
		return editedObject;
	}

	/**
	 * Met à jour la propriété editedObject
	 * @param editedObject Nouvelle valeur de la propriété editedObject
	 */
	public void setEditedObject(T editionObject) {
		this.editedObject = editionObject;
	}

	/**
	 * Retourne la valeur de la propriété criteriaObject
	 * @return La propriété criteriaObject
	 */
	public T getCriteriaObject() {
		return criteriaObject;
	}

	/**
	 * Met à jour la propriété criteriaObject
	 * @param criteriaObject Nouvelle valeur de la propriété criteriaObject
	 */
	public void setCriteriaObject(T criteriaObject) {
		this.criteriaObject = criteriaObject;
	}

	/**
	 * Retourne la valeur de la propriété selectedObject
	 * @return La propriété selectedObject
	 */
	public T getSelectedObject() {
		return selectedObject;
	}


	/**
	 * Met à jour la propriété selectedObject
	 * @param selectedObject Nouvelle valeur de la propriété selectedObject
	 */
	public void setSelectedObject(T selectedObject) {
		
		this.selectedObject = selectedObject;	
		
		// Mise à jour de l'objet courant
		this.setCurrentObject(selectedObject); 				
	}
	
	/**
	 * Retourne la valeur de la propriété datas
	 * @return La propriété datas
	 */
	public List<T> getDatas() {
		return datas;
	}


	/**
	 * Met à jour la propriété datas
	 * @param datas Nouvelle valeur de la propriété datas
	 */
	@SuppressWarnings("unchecked")
	public void setDatas(List<T> datas) {
		this.datas = (datas != null)? datas : Collections.EMPTY_LIST;
	}


	/**
	 * Retourne la valeur de la propriété dataTable
	 * @return La propriété dataTable
	 */
	public UIData getDataTable() {
		return dataTable;
	}


	/**
	 * Retourne la valeur de la propriété dataTableModel
	 * @return La propriété dataTableModel
	 */
	public DataModel<T> getDataTableModel() {
		return dataTableModel;
	}


	/**
	 * Met à jour la propriété dataTableModel
	 * @param dataTableModel Nouvelle valeur de la propriété dataTableModel
	 */
	public void setDataTableModel(DataModel<T> dataTableModel) {
		this.dataTableModel = dataTableModel;
	}


	/**
	 * Met à jour la propriété dataTable
	 * @param dataTable Nouvelle valeur de la propriété dataTable
	 */
	public void setDataTable(UIData dataTable) {
		System.out.println("AbstractCRUDViewManager.setDataTable() : " + dataTable);
		this.dataTable = dataTable;
	}

	
	/**
	 * Retourne la valeur de la propriété multiViewPageMode
	 * @return La propriété multiViewPageMode
	 */
	public String getMultiViewPageMode() {
		return multiViewPageMode;
	}

	/**
	 * Met à jour la propriété multiViewPageMode
	 * @param multiViewPageMode Nouvelle valeur de la propriété multiViewPageMode
	 */
	public void setMultiViewPageMode(String multiViewPageMode) {
		this.multiViewPageMode = multiViewPageMode;
	}

//	/**
//	 * Retourne la valeur de la propriété multiViewDialog
//	 * @return La propriété multiViewDialog
//	 */
//	public boolean isMultiViewDialog() {
//		return multiViewDialog;
//	}

//	/**
//	 * Met à jour la propriété multiViewDialog
//	 * @param multiViewDialog Nouvelle valeur de la propriété multiViewDialog
//	 */
//	public void setMultiViewDialog(boolean multiViewDialog) {
//		this.multiViewDialog = multiViewDialog;
//	}
	
	
	/* (non-Javadoc)
	 * @see net.leadware.tools.ui.web.jsf.core.view.manager.AbstractViewManager#closeView()
	 */
	@Override
	public String closeView() {
		
		return this.closeListView();
	}

	/**
	 * Retourne la règle de navigation vers la vue de création
	 * @return Par défaut la règle de navigation vers la vue d'édition
	 */
	public String getCreationViewOutcome(){
		return getEditionViewOutcome();
	}
	
	/**
	 * Retourne la règle de navigation vers la vue de modification
	 * @return Par défaut la règle de navigation vers la vue d'édition
	 */
	public String getModificationViewOutcome(){
		return getEditionViewOutcome();
	}
	
	/**
	 * Retourne la règle de navigation vers la vue d'edition (création / modification)
	 * @return Règle de navigation
	 */
	public String getEditionViewOutcome(){
		return ViewType.buildDefaultViewName(this.getViewIdentifier(), ViewType.EDITION);
	}
	
	/**
	 * Retourne la règle de navigation vers la vue de consultation
	 * @return
	 */
	public String getDetailsViewOutcome(){
		return ViewType.buildDefaultViewName(this.getViewIdentifier(), ViewType.DETAILS);
	}
	
	
	/**
	 * Retourne le nom de la vue modal panel
	 * @return
	 */
	public String getDialogViewOutcome(){
		return ViewType.buildDefaultViewName(this.getViewIdentifier(), ViewType.DIALOG);
	}
	
	/**
	 * Retourne la règle de navigation vers la vue de liste
	 * @return
	 */
	public String getListViewOutcome(){
		return ViewType.buildDefaultViewName(this.getViewIdentifier(), ViewType.LIST);
	}
		
}
