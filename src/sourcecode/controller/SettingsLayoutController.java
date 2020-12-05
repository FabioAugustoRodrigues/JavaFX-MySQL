package sourcecode.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import sourcecode.model.DAO;
import sourcecode.model.Person;

import sourcecode.util.SendEmail;

/**
 * FXML Controller class
 *
 * @author Fábio Augusto Rodrigues
 */
public class SettingsLayoutController implements Initializable {

    @FXML
    private JFXComboBox<String> combobox;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private ComboBox<String> cbReceiver;
    
    @FXML
    private RadioButton rdAll;
    
    @FXML
    private TextArea txtContent;
    
    @FXML
    private JFXRadioButton rdAttach;
    
    @FXML
    private TextField txtSubject;
   
    private List<String> emails;
    private String attach = null;
    
    @FXML
    public void attachFile(ActionEvent event) {
        if (rdAttach.isSelected()){
    
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir Arquivo");

            File file =  fileChooser.showOpenDialog(stage);

            if (file != null){
                
                String nameFile = "";
                char[] fileC = (file+"").toCharArray();
                for (int i = 0; i < (file+"").length(); i++){
                    if (i == 27){
                        nameFile += "...";
                        break;
                    }
                    nameFile += fileC[i];
                }
                attach = file.toString();

                rdAttach.setText(nameFile.toString());
            }else{
                attach = null;
                rdAttach.setSelected(false);
            }
        }else{
            attach = null;
            rdAttach.setText("");
        }
    }
    
    @FXML
    public void sendEmail(ActionEvent event) {

        if (isInputValid()){

            if (rdAll.isSelected()){

                if (emails.isEmpty() || emails == null){
                    alert("Error", "No emails", "You have to register at least one person to send email!",
                            Alert.AlertType.INFORMATION);
                }else{
                    String emailsX = emails.toString().replace("[", "").replace("]", "");
 
                    if (SendEmail.sendEmailAll(txtEmail.getText(), txtPassword.getText(), emailsX, 
                        txtSubject.getText(), txtContent.getText(), attach)){
                        alert("Ready", null, "Sent with success!",
                                Alert.AlertType.INFORMATION);
                        clearFieldsEmail();
                    }else{
                        alert("Error", null, "There was an error sending the email", Alert.AlertType.ERROR);
                    }
                }
            }else{
                if(SendEmail.sendEmail(txtEmail.getText(), txtPassword.getText(), cbReceiver.getValue(), 
                    txtSubject.getText(), txtContent.getText(), attach)){
                    alert("Ready", null, "Sent with success!",
                            Alert.AlertType.INFORMATION);
                    clearFieldsEmail();
                }else{
                    alert("Error", null, "There was an error sending the email", Alert.AlertType.ERROR);
                }
            }
        }
    }
    
    @FXML
    public void selectAll(ActionEvent event) {
        if (rdAll.isSelected()){
            cbReceiver.setValue(null);
            cbReceiver.setPromptText("All e-mails are selected");
            cbReceiver.setDisable(true);
        }else{
            cbReceiver.setPromptText("");
            cbReceiver.setDisable(false);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadComboboxDesign();
        loadComboboxEmail();
    }    
    
    private void alert(String titulo, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    public boolean isInputValid(){
        
        String errorMessage = "";
        
        if (txtEmail.getText() == null || txtEmail.getText().length() == 0){
            errorMessage += "Invalid email!\n";
        }
        if (cbReceiver.getValue() == null || cbReceiver.getValue().length() == 0){
            errorMessage += "Invalid email from receiver!\n";
        }
        
        
        if (errorMessage.length() == 0){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please, correct the fields!");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
    
    public void clearFieldsEmail(){
        txtEmail.setText("");
        txtSubject.setText("");
        cbReceiver.setValue("");
        txtContent.setText("");
        attach = null;
        rdAttach.setText("");
        rdAll.setSelected(false);
        rdAttach.setSelected(false);
        txtPassword.setText("");
    }
    
    public void loadComboboxEmail(){
        emails = new ArrayList();
        
        List<Person> persons = DAO.getInstance().findAll();
        
        if (persons != null){
            for (int i = 0; i < persons.size(); i++){
                emails.add(persons.get(i).getEmail());
            }

            ObservableList<String> obsValues = FXCollections.observableArrayList(emails);
            cbReceiver.setItems(obsValues);
        }
    }
    
    public void loadComboboxDesign(){
        List<String> values = new ArrayList();
     
        values.add("Dark theme");
        values.add("Red theme");
        values.add("Blue theme");
        values.add("Default theme");
        values.add("Birthday");
        
        ObservableList<String> obsValues = FXCollections.observableArrayList(values);
        combobox.setItems(obsValues);
    }
    
}
