package org.peng.dblook;


import dbhelp.DataSet;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @param
 * @version 1.0
 * @Description 住界面
 * @Author pengweitao 2022/4/28 下午11:05
 * @exception
 * @return
 */

public class DBfcontroller implements Initializable {
    @FXML
    private ToolBar toolBar;
    @FXML
    private AnchorPane pane,pane1,pane2;
    @FXML
    private ScrollPane spane1;
    @FXML
    private Button b_dbsource;
    @FXML
    private Button b_go;
    @FXML
    private Button b_notebook;
    @FXML
    private SplitPane splitPane;
    @FXML
    private TextArea t_u;
    @FXML
    private TextArea t_d;
    @FXML
    private TableView table_rs;

    public void dbsource_clicked(ActionEvent event) {
        // todo 选择数据源
        Parent root = null;
        Stage primaryStage = new Stage();

        try {
            URL url = getClass().getClassLoader().getResource("dbsource.fxml");
            root = FXMLLoader.load(url);

            //root = loader.load();
            primaryStage.setTitle("database look");
            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        toolBar.prefWidthProperty().bind(pane.widthProperty());
        splitPane.prefWidthProperty().bind(pane.widthProperty());
        splitPane.prefHeightProperty().bind(pane.heightProperty());
        t_u.prefWidthProperty().bind(pane1.widthProperty());
        t_u.prefHeightProperty().bind(pane1.heightProperty());
//        spane1.prefWidthProperty().bind(pane.widthProperty());
//        spane1.prefHeightProperty().bind(pane.heightProperty());
        pane2.prefWidthProperty().bind(spane1.widthProperty());
        pane2.prefHeightProperty().bind(spane1.heightProperty());
        t_d.prefWidthProperty().bind(pane2.widthProperty());
        t_d.prefHeightProperty().bind(pane2.heightProperty());
        table_rs.prefHeightProperty().bind(pane2.heightProperty());
        table_rs.prefWidthProperty().bind(pane2.widthProperty());
        table_rs.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        table_rs.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(oldValue + " : " + newValue);
            }
        });
        b_go.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bgo_clicked();
            }
        });
        b_dbsource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBfcontroller.this.dbsource_clicked(event);
            }
        });
        b_notebook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bnotebook_clicked();
            }
        });

    }

    private void bnotebook_clicked() {
        //org.peng.dblook.NoteDia(null,true)
        org.peng.dblook.NoteDia.main();
    }

    // 执行sql语句
    private void bgo_clicked() {
        showDialog("sa.fxml");
        // todo 执行sql语句
        String t_sql = this.t_u.getSelectedText();
        if (t_sql == null || t_sql.isEmpty()) {
            String msg = "sql语句没有发现";
            showText(msg);
            return;
        }
        System.out.println(t_sql);

        DataSet ds = Common.dataBase.query(t_sql);
        List dsls = ds.generateList();

        // 显示到table里
        showtable(ds);



        // 分析sql语句,非阻塞
        this.explain(t_sql);
    }

    /**
     * 将dataset数据显示到table里
     *
     * @return
     */
    private void showtable(DataSet ds) {
        ObservableList<Map> data = FXCollections.observableArrayList();
        ;
        TableColumn t_col[];
        List<String> colNames = ds.getColNameSet();
        t_col = new TableColumn[colNames.size()];
        for (int i = 0; i < t_col.length; i++) {
            String colName = colNames.get(i);
            t_col[i] = new TableColumn(colName);
            t_col[i].setCellValueFactory(new MapValueFactory<String>(colName));
        }

        // 将ds中的数据写入ObservableList
        List<Map<String, Object>> rss = ds.getDataTable(); // 源头
        for (int i = 0; i < rss.size(); i++) {
            data.add(rss.get(i));
        }

        this.table_rs.getColumns().addAll(t_col);
        this.table_rs.setItems(data);
        this.table_rs.setVisible(true);
        this.t_d.setVisible(false);


    }

    private void showText(String msg){
        // todo 在textView中现实信息
        this.t_d.setText(msg);
        this.t_d.setVisible(true);
        this.table_rs.setVisible(false);
    }
    // sql语句分析
    private void explain(String sql){
        // 使用explain分析mysql的语句，结果显示在
    }

    // 现实fxl
    private void showDialog(String surl){
        URL url = getClass().getClassLoader().getResource(surl);
        this.showDialog(url);
    }
    private void showDialog(URL url){
        Parent root = null;
        Stage primaryStage = new Stage();

        try {
            //URL url = getClass().getClassLoader().getResource("dbsource.fxml");
            root = FXMLLoader.load(url);

            //root = loader.load();
            primaryStage.setTitle("database look");
            //primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.setScene(new Scene(root));
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ToolBar getToolBar() {
        return toolBar;
    }

    public void setToolBar(ToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public AnchorPane getPane() {
        return pane;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }
}
