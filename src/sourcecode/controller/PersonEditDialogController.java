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

    // == Atributos  ==========================================
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
    
    // == Métodos acessaveis pelo JavaFX =======================
    
    /*
    * Cadastra/Registra uma nova Person
    */
    @FXML
    private void handleNewPerson(ActionEvent event) {
        

        // Verificamos se todos os dados estão corretos para serem inseridos no banco
        if (isInputValid()){             
            
            // Primaramente verificamos se será Cadadastro ou Atualização
            if (getTitle().equalsIgnoreCase("Register")){
                
                // Resgatamos todos os dados do formulário
                Person personx = new Person();
                personx.setName(txtName.getText());
                personx.setBirthday(DateUtil.formatDate(txtBirthday.getValue()));
                personx.setEmail(txtEmail.getText());
                personx.setNote(txtNote.getText());
                personx.setAddress(txtAddress.getText());
                personx.setNumber(txtNumber.getText()); 
                
                // Por fim, persistimos os dados
                DAO.getInstance().persist(personx);     
            }else{  
                // Resgatamos todos os dados do formulário
                person.setName(txtName.getText());
                person.setBirthday(DateUtil.formatDate(txtBirthday.getValue()));
                person.setEmail(txtEmail.getText());
                person.setNote(txtNote.getText());
                person.setAddress(txtAddress.getText());
                person.setNumber(txtNumber.getText()); 

                // E por fim, alteramos no banco de dados
                DAO.getInstance().merge(person);
            }
            
            this.okClicked = true;
            dialogStage.close();
        }      
    }

    /**
    * Sai da tela de cadastro
    */
    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }
    
    
    /**
     * Inicializa a classe do controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    
    // == Métodos da classe ======================================
    
    /**
    * Verifica se os dados inseridos no formulário estão inseridos corretamente
    */
    public boolean isInputValid(){
        
        String errorMessage = "";
        
        if (txtName.getText() == null || txtName.getText().length() == 0){
            errorMessage += "Nome inválido!\n";
        }
        
        if (txtBirthday.getValue() == null){
            errorMessage += "Data de nascimento inválido!\n";
        }else{
            if (!DateUtil.checkAge(txtBirthday.getValue(), 16)){
                errorMessage += "Menores de 16 anos não podem ser cadastrados!\n";
            }
        }
        
        if (txtEmail.getText() == null || txtEmail.getText().length() == 0){
            errorMessage += "E-mail inválido!\n";
        }
        if (txtNumber.getText() == null || txtNumber.getText().length() == 0){
            errorMessage += "Número inválido!\n";
        }
        if (txtAddress.getText() == null || txtAddress.getText().length() == 0){
            errorMessage += "Endereço inválido!\n";
        }
        
        
        if (errorMessage.length() == 0){
            return true;
        }else{
            // Mostra a mensagem de erro.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos Inválidos");
            alert.setHeaderText("Por favor, corrija os campos inválidos");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

    }
    
    
    // Getters e setters
    public String getTitle() {
        return title;
    }

    /**
    * Inserindo o título na panel superior
    */
    public void setTitle(String title) {
        this.title = title;
        
        if (getTitle().equalsIgnoreCase("Register")){
            lblTitulo.setText("Register a new Person");
        }else{
            lblTitulo.setText("Update registration data");
        }
    }
    
    /**
     * Define o palco deste dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Retorna true se o usuário clicar OK,caso contrário false.
     * 
     * @return 
     */
    public boolean isOkClicked() {
        return okClicked;
    }
 
    /**
    * Define a Person da seção
    * @param person
    */
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
    
    // Retorna a Person da seção
    public Person getPerson(){
        return person;
    }
}
