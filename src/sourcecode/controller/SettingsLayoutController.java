package sourcecode.controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Fábio Augusto Rodrigues
 */
public class SettingsLayoutController implements Initializable {

    @FXML
    private JFXComboBox<String> combobox;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCombobox();
    }    
    
    public void loadCombobox(){
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
