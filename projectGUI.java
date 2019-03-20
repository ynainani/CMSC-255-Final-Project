package projectGUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class projectGUI extends Application {
	Stage window;
	Scene scene1, scene2;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
		primaryStage.setTitle("Stock Market Fundamental Analysis");
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scene1Title = new Text("Stock Market Fundamental Analysis");
        scene1Title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        grid.add(scene1Title, 0, 0, 2, 1);

        Label tickerNum = new Label("Enter Ticker Number:");
        grid.add(tickerNum, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Button btn1 = new Button("Go");
        HBox hbBtn1 = new HBox(10);
        hbBtn1.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn1.getChildren().add(btn1);
        grid.add(hbBtn1, 1, 4);
        btn1.setOnAction(e -> window.setScene(scene2));
        
        
        window.setTitle("AAPL pricing 2019 March");					//defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Months");
        
        final LineChart<Number,Number> chart = 						//creating the chart
                new LineChart<Number,Number>(xAxis,yAxis);
                
        chart.setTitle("Stock Monitoring, 2010");					//defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio"); 							//populating the series with data
        int rand1;
        for(int i = 1; i<= 12; i++) {
        	rand1 = (int)(Math.random() * 226);
        	series.getData().add(new XYChart.Data(i, rand1));
        }
     
        scene2 = new Scene(chart, 800, 600);
        chart.getData().add(series);
        
       /* Button btn2 = new Button("Go Back");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(btn2);
        chart.add(hbBtn2, 10, 10);
        btn2.setOnAction(e -> window.setScene(scene1));
        
        */
        
   
        Scene scene1 = new Scene(grid, 800, 600);
        window.setScene(scene1);
        window.show();
    }
}