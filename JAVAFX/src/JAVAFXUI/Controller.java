/**
 * Sample Skeleton for 'ETTJAVAFX.fxml' Controller Class
 */

package JAVAFXUI;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.xml.bind.JAXBException;


public class Controller {

    JavaFxUI ui = new JavaFxUI();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="fileChooserButton"
    private Button fileChooserButton; // Value injected by FXMLLoader

    @FXML // fx:id="loadFileTextBox"
    private TextField loadFileTextBox; // Value injected by FXMLLoader

    @FXML // fx:id="exceptionMessageTextBox"
    private Label exceptionMessageTextBox; // Value injected by FXMLLoader

    @FXML
    void OnFileChooser(ActionEvent event)
    {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            loadFileTextBox.setText("File: " + selectedFile.getName());
            try
            {
                 ui.loadInfoFromXmlFile(selectedFile);
                 exceptionMessageTextBox.setText("The xml file was loaded");
            }
            catch (JAXBException e)
            {
                exceptionMessageTextBox.setText("Error with generating data from xml file");
            }
            catch (FileNotFoundException e)
            {
                exceptionMessageTextBox.setText("File not found");
            }
            catch (Exception e)
            {
                exceptionMessageTextBox.setText(e.getMessage());/////
            }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert loadFileTextBox != null : "fx:id=\"loadFileTextBox\" was not injected: check your FXML file 'ettjavafx.fxml'.";
        assert fileChooserButton != null : "fx:id=\"fileChooserButton\" was not injected: check your FXML file 'ettjavafx.fxml'.";
        assert exceptionMessageTextBox != null : "fx:id=\"exceptionMessageTextBox\" was not injected: check your FXML file 'ettjavafx.fxml'.";
    }
}
