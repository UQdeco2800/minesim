package minesim.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.*;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * It is what it is...
 * @author GenevieveAsh
 *
 */
public class InstructionsController {

	@FXML
	private Button iClose;

	@FXML
	private VBox iBox;

	@FXML
	private TabPane iTabPane;

	@FXML
	private HBox buttons;

	@FXML
	private StackPane instructionsPane;

	@FXML
	private Tab iTabStart;

	@FXML
	private Tab iTabMining;
	
	@FXML
	private Tab iTabNighttime;

	@FXML
	private Tab iTabMarketplace;


	@FXML
	private TextArea iTextArea2;

	@FXML
	private TextArea iTextArea1;

	@FXML
	void close(ActionEvent event) {
		instructionsPane.setVisible(false);
	}

//	@FXML
//	SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
//	@FXML
//	void loadTextOne(ActionEvent event) {
//		try {
//			iTextArea1.clear();
//			String str;
//			FileReader reader = new FileReader("../../text1.txt" );
//			BufferedReader br = new BufferedReader(reader);
//			while ((str = br.readLine()) != null) {
//				iTextArea1.appendText(str);
//			}
//			br.close();
//			iTextArea1.requestFocus();
//		} catch(Exception e2) { 
//			System.out.println(e2); 
//		}
//	}
//
//	@FXML
//	void loadTextTwo(ActionEvent event) {
//		try {
//			iTextArea2.clear();
//			String str;
//			FileReader reader = new FileReader("../../text2.txt" );
//			BufferedReader br = new BufferedReader(reader);
//			while ((str = br.readLine()) != null) {
//				iTextArea2.appendText(str);
//			}
//			br.close();
//			iTextArea2.requestFocus();
//		} catch(Exception e2) { 
//			System.out.println(e2); 
//		}
//	}
}
