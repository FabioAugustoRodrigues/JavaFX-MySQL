package sourcecode.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import sourcecode.MainApp;
import sourcecode.model.DAO;

/**
 * FXML Controller class
 * 
 * @author Fábio Augusto Rodrigues
 */
public class RootLayoutController implements Initializable {
    
    // Istância da MainApp
    MainApp mainApp;
    
    @FXML
    private Button btnPerson;
    
    @FXML
    void showPeople(ActionEvent event) {
        mainApp.showPersonLayout();
    }

    @FXML
    void showStatitics(ActionEvent event)   {
        mainApp.showStatitics();
    }
    
    @FXML
    void showSettings(ActionEvent event) {
        mainApp.showSettings();
    }
    
    @FXML
    void closeApplication(MouseEvent event) {
        try{
            DAO.getInstance().closeEntityManager();
            System.exit(0);
        }catch(Exception e){
            System.out.println("Error when closing the application");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
}
