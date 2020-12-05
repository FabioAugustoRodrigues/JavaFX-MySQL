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
    
    private List<Person> listPerson = new ArrayList();
    
    private ObservableList<Person> observableListPerson;
    
    MainApp mainApp;
   
    @FXML
    void actionRegister(ActionEvent event) {
        if (mainApp.showPersonEditDialog("Register", null)){
            loadPerson(true);
        }
    }
   
    @FXML
    void actionUpdate(ActionEvent event) {
        if (personTable.getSelectionModel().getSelectedIndex() > -1){
            if (mainApp.showPersonEditDialog("Update", personTable.getSelectionModel().getSelectedItem()));
            
            loadPerson(true);
        }else{
            if (listPerson.isEmpty()){
                alert("Error", "Não há registros para editar", "Register a new person", Alert.AlertType.ERROR);
            }else{
                alert("Error", "Select a record", "To edit you need to select a record from the table", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    void actionDelete(ActionEvent event) {
        if (personTable.getSelectionModel().getSelectedIndex() > -1){
       
            Alert dialogoExe = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType btnYes = new ButtonType("Yes");
            ButtonType btnNoAnswer = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            dialogoExe.setTitle("Attention!");
            dialogoExe.setHeaderText("Inform if you want to delete");
            dialogoExe.setContentText("Delete " + personTable.getSelectionModel().getSelectedItem().getName() + "?");
            dialogoExe.getButtonTypes().setAll(btnYes, btnNoAnswer);
            
            dialogoExe.showAndWait().ifPresent(b -> {
                if (b == btnYes) {
                    DAO.getInstance().delete(personTable.getSelectionModel().getSelectedItem());
                    loadPerson(true);
                }
            });
           
        }else{ 
            if (listPerson.isEmpty()){
                alert("Error", null, "There are no records to delete", Alert.AlertType.ERROR);
            }else{
                alert("Error", "Select a record", "To delete you need to select a record from the table", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    void actionSearch(ActionEvent event) {
        try{
            if (attributeList.getValue().equals("Show everyone")){
                loadPerson(true);
            }else{
                
                ArrayList<Person> people = new ArrayList();
                
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
            lblError.setText("Enter the valid value type");
        }catch(NullPointerException npe){
            lblError.setText("Enter some value");
        }
    }
    
    @FXML
    void keyPressed(KeyEvent event) {
        lblError.setText("");
            
    }
    
    @FXML
    void actionCombobox(ActionEvent event) {
        if (attributeList.getValue().equals("Show everyone")){
            loadPerson(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loadPerson(false);
        loadCombobox();
        
        try{
            personTable.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showNote(newValue));
            
            loadAutoComplete();
            
        }catch(NullPointerException npe){
            lblNote.setText("");
        }
    }    
    
    public boolean loadPerson(boolean cleanTable){
        
        try{

            if (cleanTable){
                cleanTable();
            }
            
            definingColumn();
        
            setListPerson(DAO.getInstance().findAll());

            observableListPerson = FXCollections.observableArrayList(listPerson);
            personTable.setItems(observableListPerson);
            
        }catch(Exception e){
            alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    
    public void loadPerson(ArrayList<Person> arrayListPerson){
         try{
            cleanTable();
            observableListPerson = FXCollections.observableArrayList(arrayListPerson);
            personTable.setItems(observableListPerson);
        }catch(Exception e){
            alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
        }
    }
    
    public void definingColumn(){
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        columnBirthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
    }

    private void cleanTable(){
        personTable.getItems().clear();
    }
    
    private void alert(String titulo, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
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
    
    public void showNote(Person person){
        lblNote.setText(person.getNote());
    }
    
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
