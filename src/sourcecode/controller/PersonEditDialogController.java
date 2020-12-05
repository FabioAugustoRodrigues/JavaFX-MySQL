package sourcecode.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import sourcecode.model.Person;
import sourcecode.model.DAO;

import sourcecode.util.DateUtil;

/**
 * FXML Controller class
 *
 * @author Fábio Augusto Rodrigues
 */
public class PersonEditDialogController implements Initializable {

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXDatePicker txtBirthday;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtNumber;

    @FXML
    private TextArea txtNote;
    
    @FXML
    private Label lblTitulo;
    
    private String title;
    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;
    private String insertionType;
    
    @FXML
    private void handleNewPerson(ActionEvent event) {
        
        if (isInputValid()){             
            
            if (getTitle().equalsIgnoreCase("Register")){
                
                Person personx = new Person();
                personx.setName(txtName.getText());
                personx.setBirthday(DateUtil.formatDate(txtBirthday.getValue()));
                personx.setEmail(txtEmail.getText());
                personx.setNote(txtNote.getText());
                personx.setAddress(txtAddress.getText());
                personx.setNumber(txtNumber.getText()); 
                
                DAO.getInstance().persist(personx);     
            }else{  
                person.setName(txtName.getText());
                person.setBirthday(DateUtil.formatDate(txtBirthday.getValue()));
                person.setEmail(txtEmail.getText());
                person.setNote(txtNote.getText());
                person.setAddress(txtAddress.getText());
                person.setNumber(txtNumber.getText()); 

                DAO.getInstance().merge(person);
            }
            
            this.okClicked = true;
            dialogStage.close();
        }      
    }

    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  

    public boolean isInputValid(){
        
        String errorMessage = "";
        
        if (txtName.getText() == null || txtName.getText().length() == 0){
            errorMessage += "Invalid name!\n";
        }
        
        if (txtBirthday.getValue() == null){
            errorMessage += "Invalid date of birth!\n";
        }else{
            if (!DateUtil.checkAge(txtBirthday.getValue(), 16)){
                errorMessage += "Children under 16 years old cannot be registered!\n";
            }
        }
        
        if (txtEmail.getText() == null || txtEmail.getText().length() == 0){
            errorMessage += "Invalid email!\n";
        }
        if (txtNumber.getText() == null || txtNumber.getText().length() == 0){
            errorMessage += "Invalid number!\n";
        }
        if (txtAddress.getText() == null || txtAddress.getText().length() == 0){
            errorMessage += "Invalid address!\n";
        }
        
        
        if (errorMessage.length() == 0){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct the invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

    }
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        
        if (getTitle().equalsIgnoreCase("Register")){
            lblTitulo.setText("Register a new Person");
        }else{
            lblTitulo.setText("Update registration data");
        }
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
 
    public void setPerson(Person person){
        this.person = person;
        
        if (person != null){
            txtName.setText(person.getName());
            txtBirthday.setValue(DateUtil.formatDate(person.getBirthday()));
            txtEmail.setText(person.getEmail());
            txtNote.setText(person.getNote());
            txtAddress.setText(person.getAddress());
            txtNumber.setText(person.getNumber());
        }
    }
    
    public Person getPerson(){
        return person;
    }
}
