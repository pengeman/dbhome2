package org.peng.dblook;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SaController implements Initializable {
    @FXML
    TableView table_1;
    @FXML
    Button b_1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableView tab = table_1;

        TableColumn nameColumn = new TableColumn("firstname");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));

        TableColumn surnameColumn = new TableColumn("secname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("secname"));

        tab.getColumns().clear();
        tab.getColumns().addAll(nameColumn, surnameColumn);

        Person person = new Person();
        person.setFirstname("1111");
        person.setSecname("tttttt");

        tab.getItems().addAll(person);

        b_1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Person person = new Person();
                person.setFirstname("1111");
                person.setSecname("tttttt");
                tab.getItems().addAll(person);
            }
        });
    }
}
