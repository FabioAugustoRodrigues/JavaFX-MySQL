package sourcecode;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sourcecode.controller.PersonEditDialogController;

import sourcecode.model.Person;

import sourcecode.controller.PersonLayoutController;
import sourcecode.controller.RootLayoutController;

/**
 * classe Main
 * @author Fábio Augusto Rodrigues 
 */
public class MainApp extends Application {

    private Stage primaryStage;
    
    private BorderPane rootLayout; 
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        
        this.primaryStage.setTitle("CRUD JavaFX");
        
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/informacao-pessoal.png")));

        if (initRootLayout()){ 
            showPersonLayout();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro inesperado");
            alert.setContentText("Ocorreu um erro ao tentar iniciar o programa");
            alert.showAndWait();
        }
        
    }

    /*
    * Inicia o Layout principal - RootLayout.fxml
    */
    public boolean initRootLayout(){
        
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("view/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) fxmlLoader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            RootLayoutController controller = fxmlLoader.getController();
            controller.setMainApp(this);
            
            primaryStage.show();
        }catch(IOException e){
            return false;
        }
        
        return true;
    }
    
    // == SCREENS ================================================================
    
    /*
    * Seta no Layout Principal (RootLayout.fxml) a tela de crud de Person
    */
    public boolean showPersonLayout(){
        try { 
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/fxml/PersonLayout.fxml"));

            AnchorPane personLayout = (AnchorPane) loader.load();
            
            rootLayout.setCenter(personLayout);
  
            PersonLayoutController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            return false;
        }
        
        return true;
    }
    
    /*
    * Seta no Layout Principal (RootLayout.fxml) a tela de estatísticas
    */
    public boolean showStatitics(){
        try { 
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/fxml/StatiticsLayout.fxml"));
            
            AnchorPane personLayout = (AnchorPane) loader.load();

            rootLayout.setCenter(null);
            rootLayout.setCenter(personLayout);
  
            // StatiticsController controller = loader.getController();
            // controller.setMainApp(this);

        } catch (IOException e) {
            return false;
        }
        
        return true;
    }
    
    /*
    * Seta no Layout Principal (RootLayout.fxml) a tela de configurações
    */
    public boolean showSettings(){
        try { 
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/fxml/SettingsLayout.fxml"));
            
            AnchorPane personLayout = (AnchorPane) loader.load();
            
            rootLayout.setCenter(null);
            rootLayout.setCenter(personLayout);
  
            // StatiticsController controller = loader.getController();
            // controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /*
    * Edita/Cadastra nova Person
    */
    public boolean showPersonEditDialog(String title, Person person){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/fxml/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
          
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/adicionar.png")));

            PersonEditDialogController controller = loader.getController();
            controller.setTitle(title);
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);
            
            dialogStage.showAndWait();
            
            return controller.isOkClicked();
        }catch(IOException e){
            return false;
        }
       
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
