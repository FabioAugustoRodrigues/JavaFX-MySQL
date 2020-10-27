package sourcecode.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

import sourcecode.model.Person;
import sourcecode.util.DateUtil;
import sourcecode.model.DAO;

/**
 * FXML Controller class
 * 
 * @author Fábio Augusto Rodrigues
 */
public class StatiticsController implements Initializable {
    
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private CategoryAxis xAxis;
    
    private ObservableList<String> monthNames = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May",
                           "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        monthNames.addAll(Arrays.asList(months));
        
        xAxis.setCategories(monthNames);
        
        setPersonData(DAO.getInstance().findAll());
    }    
    
    /**
    * Insere os dados no gráfico - meses em que mais tem aniversário
    */
    public void setPersonData(List<Person> persons){
        
        int[] monthCounter = new int[12];
        for (Person p: persons){
            int month = DateUtil.formatDate(p.getBirthday()).getMonthValue()-1;
            monthCounter[month]++;
        }
        
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }
        
        barChart.getData().add(series);
       
    }

}
