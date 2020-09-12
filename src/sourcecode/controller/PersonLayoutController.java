package sourcecode.controller;

import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import sourcecode.MainApp;

import sourcecode.model.Person;
import sourcecode.model.DAO;

/**
 * FXML Controller class
 *
 * @author Fábio Augusto Rodrigues
 */
public class PersonLayoutController implements Initializable {


    @FXML
    private JFXComboBox<String> attributeList;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<Person> personTable;

    @FXML
    private TableColumn<Person, String> columnName;

    @FXML
    private TableColumn<Person, String> columnAddress;

    @FXML
    private TableColumn<Person, String> columnEmail;

    @FXML
    private TableColumn<Person, String> columnNumber;

    @FXML
    private TableColumn<Person, String> columnBirthday;
    
    @FXML
    private Label lblNote;
    
    @FXML
    private Label lblError;
    
    // Lista de Person
    private List<Person> listPerson = new ArrayList();
    
    // ObservableLisr de Person
    private ObservableList<Person> observableListPerson;
    
    // Istância da MainApp
    MainApp mainApp;
   
    // == Métodos =================================================
    
    /**
    *  Abre uma nova view para cadastrar uma nova Person
    */
    @FXML
    void actionRegister(ActionEvent event) {
        if (mainApp.showPersonEditDialog("Register", null)){
            loadPerson(true);
        }
    }
   
    /**
    *   Abre uma nova view para editar um Person
    */
    @FXML
    void actionUpdate(ActionEvent event) {
        // Caso o usuário não tenha selecionado um registro, o valor de index será -1
        // Por isso, aqui, verificamos se o index é maior que -1. Caso seja, chamamos 
        // o PersonEditDialogController que vai tratar da atualização dos dados e tals
        if (personTable.getSelectionModel().getSelectedIndex() > -1){
            if (mainApp.showPersonEditDialog("Update", personTable.getSelectionModel().getSelectedItem()));
            
            // Caso o usuário tenha sido alterado com sucesso, carregamos novamente a tela
            loadPerson(true);
        }else{
            // Se o usuário não selecionou um registro, informamos um erro ao usuário
            if (listPerson.isEmpty()){
                alert("Erro", "Não há registros para editar", "Cadastre uma nova pessoa", Alert.AlertType.ERROR);
            }else{
                alert("Erro", "Selecione um registro", "Para editar precisa selecionar um registro da tabela", Alert.AlertType.ERROR);
            }
        }
    }
    
    /**
    *   Deleta o registro selecionado na tabela
    */
    @FXML
    void actionDelete(ActionEvent event) {
        if (personTable.getSelectionModel().getSelectedIndex() > -1){
       
            /*
                Primeiramente, criamos um alert para o usuário confirmar que quer excluir
                tal Person
            */
            Alert dialogoExe = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType btnYes = new ButtonType("Yes");
            ButtonType btnNoAnswer = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            dialogoExe.setTitle("Atenção!");
            dialogoExe.setHeaderText("Informe se você quer deletar");
            dialogoExe.setContentText("Você quer deletar " + personTable.getSelectionModel().getSelectedItem().getName() + "?");
            dialogoExe.getButtonTypes().setAll(btnYes, btnNoAnswer);
            
            dialogoExe.showAndWait().ifPresent(b -> {
                if (b == btnYes) {
                    DAO.getInstance().delete(personTable.getSelectionModel().getSelectedItem());
                    loadPerson(true);
                }
            });
           
        }else{ 
            // Se o usuário não selecionou um registro, informamos um erro ao usuário
            if (listPerson.isEmpty()){
                alert("Erro", null, "Não há registros para deletar", Alert.AlertType.ERROR);
            }else{
                alert("Erro", "Selecione um registro", "Para deletar precisa selecionar um registro da tabela", Alert.AlertType.ERROR);
            }
        }
    }
    
    /**
    *   Pesquisa por um usuário a partir do valor inserido no txtSearch
    */
    @FXML
    void actionSearch(ActionEvent event) {
        try{
            // Se o usuáro selecionou "Show everyone" no combobox e apertou enter/botão
            // a lista será carregada novamente
            if (attributeList.getValue().equals("Show everyone")){
                loadPerson(true);
            }else{
                
                // ArrayList que vai armazenar as Person que encontrar
                ArrayList<Person> people = new ArrayList();
                
                /*
                    Quando o usuário for pesquisar um registro, primeiro ele seleciona o tipo de
                    coluna no combobox para ter uma referência, e aqui, no código abaixo 
                    fazemos a estrutura que irá selecionar o registro pelo tipo de coluna
                */
                switch (attributeList.getValue()) {
                    case "ID":
                        people.add(DAO.getInstance().findById(Integer.parseInt(txtSearch.getText())));
                        break;
                    case "Name":
                        for (int i = 0; i < listPerson.size(); i++){
                            if (txtSearch.getText().equalsIgnoreCase(listPerson.get(i).getName())){
                                people.add(listPerson.get(i));
                            }
                        }   break;
                    case "Address":
                        for (int i = 0; i < listPerson.size(); i++){
                            if (txtSearch.getText().equalsIgnoreCase(listPerson.get(i).getAddress())){
                                people.add(listPerson.get(i));
                            }
                        }   break;
                    case "E-mail":
                        for (int i = 0; i < listPerson.size(); i++){
                            if (txtSearch.getText().equalsIgnoreCase(listPerson.get(i).getEmail())){
                                people.add(listPerson.get(i));
                            }
                        }   break;
                    case "Birthday":
                        for (int i = 0; i < listPerson.size(); i++){
                            if (txtSearch.getText().equalsIgnoreCase(listPerson.get(i).getBirthday())){
                                people.add(listPerson.get(i));
                            }
                        }   break;
                    case "Number":
                        for (int i = 0; i < listPerson.size(); i++){
                            if (txtSearch.getText().equalsIgnoreCase(listPerson.get(i).getNumber())){
                                people.add(listPerson.get(i));
                            }
                        }   break;
                    default:
                        break;
                }

                loadPerson(people);
            }
        }catch(NumberFormatException ime){
            lblError.setText("Insira o tipo de valor correto!!");
        }catch(NullPointerException npe){
            lblError.setText("Insira algum valor");
        }
    }
    
    /**
    * Limpa a mensagem de erro - caso ela esteja lá - quando o usuário digitar algo
    * no formulário
    */
    @FXML
    void keyPressed(KeyEvent event) {
        // Limpando a mensagem de erro
        lblError.setText("");
            
    }
    
    /** 
    * Se o valor selecionado no Combobox for "Show everyone" ele atualiza a tabela
    */
    @FXML
    void actionCombobox(ActionEvent event) {
        if (attributeList.getValue().equals("Show everyone")){
            loadPerson(true);
        }
    }
    
    /**
     * Inicializa o método initialize. Funciona como um construtor
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Carregamos os dados do banco de dados para a tabela
        loadPerson(false);
        
        // Carregamos os dados no combobox
        loadCombobox();
        
        try{
            // Detecta mudanças de seleção e mostra os detalhes da pessoa quando houver mudança.
            personTable.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showNote(newValue));
            
            // Configures the textfield
            loadAutoComplete();
            
        }catch(NullPointerException npe){
            lblNote.setText("");
        }
    }    
    
    /**
    * Resgata os dados do banco de dados e insere na tabela
    */
    public boolean loadPerson(boolean cleanTable){
        
        // LIMPA A TABELA CASO HAJA ALGO NELA
        // INSERE OS TIPOS DE DADOS PARA AS COLUNAS
        // RECEBE OS DADOS DO BANCO DE DADOS
        // INSERE OS DADOS NA TABELA
        
        try{
            
            // Limpa a tabela
            if (cleanTable){
                cleanTable();
            }
            
            // Definimos os tipos de atributos para as colunas
            definingColumn();
            
            // Inserimos uma lista de pessoas na listPersons
            setListPerson(DAO.getInstance().findAll());

            // Por fim, populamos a tabela
            observableListPerson = FXCollections.observableArrayList(listPerson);
            personTable.setItems(observableListPerson);
            
        }catch(Exception e){
            alert("Erro", null, "Ocorreu um erro ao resgatar os dados", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    
    /** 
    * Recebe uma lista de Person e a insere na tabela
    */
    public void loadPerson(ArrayList<Person> arrayListPerson){
         try{
            cleanTable();
            observableListPerson = FXCollections.observableArrayList(arrayListPerson);
            personTable.setItems(observableListPerson);
        }catch(Exception e){
            alert("Erro", null, "Ocorreu um erro ao resgatar os dados", Alert.AlertType.ERROR);
        }
    }
    
    /**
    *   Define os tipos de atributo de cada colunaS
    */
    public void definingColumn(){
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        columnBirthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
    }

    /**
    * Limpa a tabela
    */
    private void cleanTable(){
        personTable.getItems().clear();
    }
    
    /**
    * Mostra um alerta
    */
    private void alert(String titulo, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    /**
    *  Carrega os tipos de itens no combobox para realizar a pesquisa
    */
    public void loadCombobox(){
        
        List<String> values = new ArrayList();
     
        values.add("Show everyone");
        values.add("ID");
        values.add("Name");
        values.add("Address");
        values.add("Birthday");
        values.add("E-mail");
        values.add("Number");
        
        ObservableList<String> obsValues = FXCollections.observableArrayList(values);
        attributeList.setItems(obsValues);
    }
    
    /**
    * Configures the textfield to receive suggestions
    */
    public void loadAutoComplete() {
        
        // Variables for autosuggestion :)
        AutoCompletionBinding<String> acb;
        Set<String> ps;
        
        ArrayList<String> values = new ArrayList();
        for (int i = 0; i < listPerson.size(); i++){
            values.add(listPerson.get(i).getName());
            values.add(listPerson.get(i).getAddress());
            values.add(listPerson.get(i).getEmail());
            values.add(listPerson.get(i).getBirthday());
            values.add(listPerson.get(i).getNumber());
        }
        
        
        String[] _possibleSuggestions = values.toArray(new String[0]);
        ps = new HashSet<>(Arrays.asList(_possibleSuggestions));
        TextFields.bindAutoCompletion(txtSearch, _possibleSuggestions);
    }
    
    /**
    * Mostra as observações da Person
    */
    public void showNote(Person person){
        lblNote.setText(person.getNote());
    }
    
    // Getters e setters
    public List<Person> getListPerson() {
        return listPerson;
    }

    public void setListPerson(List<Person> listPerson) {
        this.listPerson = listPerson;
    }
    
    
    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
   
}
