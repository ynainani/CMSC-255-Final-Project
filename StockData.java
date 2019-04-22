package yeet;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.Company;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType; 


public class StockData extends Application {

	 /* 
	  * Returns String array with Company's name, symbol, current price, peRatio,
	  * and market Cap
	  */ 
	public static String[] Company_Fundamentals(String url) {
	try	{ 
		final Document Data = Jsoup.connect(url).ignoreContentType(true).get();
	 String all_Data = Data.outerHtml(); 
	 all_Data =	  all_Data.substring(all_Data.indexOf('{') + 1, all_Data.lastIndexOf('}'));
	  String company_name = all_Data.substring(all_Data.indexOf("companyName\"") +
	  14, all_Data.indexOf("\",\"calculationPrice")); 
	  String symbol = all_Data.substring(all_Data.indexOf(':') + 2, all_Data.indexOf(",") - 1);
	  String peRatio = all_Data.substring(all_Data.indexOf("peRatio") + 9, all_Data.indexOf(",\"week")); String marketCap =
	  all_Data.substring(all_Data.indexOf("marketCap") + 11, all_Data.indexOf(",\"peRatio")); 
	  String latestPrice = all_Data.substring(all_Data.indexOf("latestPrice") + 13,
	  all_Data.indexOf(",\"latestSource")); 
	  String[] company_data = { company_name, symbol, peRatio, marketCap, latestPrice }; 
	  return company_data; 
	  }
	catch(Exception e) {
		return null;
		}
	}
	
	/*
	 * Returns the company data at a certain time
	 */
	public static String[] Historic_Data(String url) { 
		try { 
			final Document Data  = Jsoup.connect(url).ignoreContentType(true).get(); 
			String all_Data = Data.outerHtml(); all_Data = all_Data.substring(all_Data.indexOf('[') + 1, all_Data.lastIndexOf(']')); String[] company_data = all_Data.split("},");
	  String[] company_data_time = new String[company_data.length]; 
	  for (int i = 0; i < company_data.length; i++) { 
		  String holder = (company_data[i]).substring((company_data[i]).indexOf("date"), (company_data[i]).indexOf("high")); holder = holder.substring(0,
				  holder.indexOf('"')) + holder.substring(holder.indexOf('"') + 1, holder.length() - 2); company_data_time[i] = holder; } 
	  return company_data_time; 
	  }
	  
	  catch (Exception e) { 
		   return null; 
		   }
	  }

	// Creates company object from the indexes of a 5 index String array
	public static Company createCompanyObject(String[] input) {
		Company companyData = new Company(input[0], input[1], input[2], input[3], input[4]);
		return companyData;
	}
	
	// 
	public static String[][] createPriceDate(String[] input) {
		try {
			String[][] priceDate = new String[1259][3];
			int j = 0;
			for (String s : input) {
				String date = s.substring(s.indexOf("date") + 6, s.indexOf("open") - 3);
				String opening = s.substring(s.indexOf("open") + 6, s.indexOf(",\"close"));
				String closing = s.substring(s.indexOf("close") + 7);
				priceDate[j][0] = date;
				priceDate[j][1] = opening;
				priceDate[j][2] = closing;
				j++;
			}
			return priceDate;
		} 
		catch (Exception e) {
			return null;
		}

	}

	Stage window;	
	static Scene scene1; // declares scene 1 and scene 2 (viewing screens)
	Scene scene2;


	public void start(Stage primaryStage) throws Exception {
		// set window equal to primaryStage to simplify future usage
		window = primaryStage;
		/*
		 * finds the bounds of the screen and stores it to a rectangle2D variable. this
		 * variable is used to set the bounds of the window to the screen's dimension
		 */
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		String title = "Stock Market Fundamental Analysis";
		
		primaryStage.setTitle(title); // set title
		GridPane grid = new GridPane(); // creating a grid so children can be placed anywhere
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		// set and format title of scene 1
		Text Title = new Text(title);
		Title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		grid.add(Title, 0, 0, 2, 1);

		Label tickerNum = new Label("Enter Ticker Symbol"); // set title of label
		grid.add(tickerNum, 0, 1, 1, 2);

		// add new text field for symbol
		TextField symbol = new TextField();
		symbol.setPromptText("Ticker Symbol");
		grid.add(symbol, 1, 2);

		// create, define, and format a button to go to the next scene
		Button go = new Button("Go");
		HBox hbGo = new HBox(10);
		hbGo.setAlignment(Pos.BOTTOM_RIGHT);
		hbGo.getChildren().add(go);
		grid.add(hbGo, 1, 4);
		
		// warning screen to inform user to enter a valid ticker number if user inputs an invalid ticker number 
		Alert a = new Alert(AlertType.WARNING);
		go.setOnAction(e -> {
			if(!symbol.getText().equals("") ) {
				try {
				final String ticker = symbol.getText();				
				final Scene scene2 = makeScene2(primaryStage, screenBounds, scene1, ticker);
				primaryStage.setScene(scene2);
				symbol.setText("");
				primaryStage.setX(screenBounds.getMinX());
				primaryStage.setY(screenBounds.getMinY());
				}
				
				catch(NullPointerException n) {
					a.setContentText("Enter a valid Ticker");
					a.setX((screenBounds.getWidth() - window.getWidth()) / 2);
					a.setY((screenBounds.getHeight() - window.getHeight()) / 2);
					a.show();
				}
			}
	
		});
		
		// sets layout of scene 1
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(15, 15, 15, 15));
		layout.getChildren().addAll(grid);
		int width = 400;
		int height = 225;
		scene1 = new Scene(layout, width, height);

		// sets scene to scene 1 and shows the window
		primaryStage.setX((screenBounds.getWidth() - width) / 2);
		primaryStage.setY((screenBounds.getHeight() - height) / 2);
		primaryStage.setScene(scene1);
		window.show();
	}
	
	//main method to launch args
	public static void main(String[] args) throws CloneNotSupportedException {
		launch(args);		 
	}

	/**
	 * returns a chart after being passed all of the parameters of the data required
	 * for the chart
	 * 
	 * @param data
	 * @param current_date
	 * @param title
	 * @param Xlabel
	 * @param Ylabel
	 * @param width
	 * @param height
	 * @return chart with the above parameters
	 */
	
	public static LineChart<String, Number> createChart(ArrayList<Double> data, ArrayList<String> current_date,
			String title, String Xlabel, String Ylabel, Rectangle2D screenBounds) {

		 // create the axes
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();

		 // label axes
		xAxis.setLabel(Xlabel);
		yAxis.setLabel(Ylabel);

		/*
		 * create the chart itself that will be returned by this method, set its title
		 */
		final LineChart<String, Number> chart = new LineChart<String, Number>(xAxis, yAxis);
		chart.setTitle(title);
		/*
		 * create series, which the data will be added to, and then the series will be
		 * added to the axes, then added to the chart
		 */
		XYChart.Series Series = new XYChart.Series();

		/*
		 * populate series with the data in the ArrayList passed to this method
		 */
		for (int i = 0; i < data.size(); i++) {
			Series.getData().add(new XYChart.Data(current_date.get(i), data.get(i)));
		}
		// add the series to the chart
		chart.getData().add(Series);
		// hide the legend of the chart (just takes up extra space on the screen, only 1
		// series per chart
		chart.setLegendVisible(false);

		/*
		 * set the size of the chart to the specified input
		 */
		chart.setPrefWidth(screenBounds.getWidth());
		chart.setPrefHeight(screenBounds.getHeight() * 0.43);

		/*
		 * return the lineChart
		 */
		return chart;
	}

	/**
	 * This method calculates % change from an opening and closing value arraylist
	 * it returns a % daily change arraylist
	 * 
	 * @param opening
	 * @param closing
	 * @return
	 */
	public static ArrayList<Double> calcDailyChange(ArrayList<Double> opening, ArrayList<Double> closing) {
		ArrayList<Double> change = new ArrayList<>();
		/*
		 * iterates through the arraylists and calculates % change with the following
		 * formula (closing - opening)/ 100
		 */
		for (int i = 0; i < closing.size(); i++) {
			double perChange = (((Double) closing.get(i)).doubleValue() - ((Double) opening.get(i)).doubleValue())
					/ 100;
			change.add(i, perChange);
		}
		return change;
	}

	// creates method to display data
	public static void displayData(GridPane grid, String[] values) {
		final int staticSize = 25;
		final int valuesSize = 20;
		
		// creates and formats name label 
		Label name = new Label("Name");
		grid.add(name, 0, 2, 1, 1);
		name.setFont(Font.font("Times New Roman", FontWeight.BOLD, staticSize));
		Label nVal = new Label(values[0]);
		grid.add(nVal, 0, 3, 1, 1);
		nVal.setFont(Font.font(valuesSize));
		
		// creates and formats symbol label
		Label symbol = new Label("Symbol");
		grid.add(symbol, 1, 2, 1, 1);
		symbol.setFont(Font.font("Times New Roman", FontWeight.BOLD, staticSize));
		Label sVal = new Label(values[1]);
		grid.add(sVal, 1, 3, 1, 1);
		sVal.setFont(Font.font(valuesSize));
		
		// creates and formats current price label
		Label price = new Label("Current Price");
		grid.add(price, 2, 2, 1, 1);
		price.setFont(Font.font("Times New Roman", FontWeight.BOLD, staticSize));
		Label pVal = new Label(values[2]);
		grid.add(pVal, 2, 3, 1, 1);
		pVal.setFont(Font.font(valuesSize));
		
		// creates and formats p/e ratio label
		Label pe = new Label("P/E Ratio");
		grid.add(pe, 3, 2, 1, 1);
		pe.setFont(Font.font("Times New Roman", FontWeight.BOLD, staticSize));
		Label peVal = new Label(values[3]);
		grid.add(peVal, 3, 3, 1, 1);
		peVal.setFont(Font.font(valuesSize));
		
		//creates and formats market cap label
		Label cap = new Label("Market Cap");
		grid.add(cap, 4, 2, 1, 1);
		cap.setFont(Font.font("Times New Roman", FontWeight.BOLD, staticSize));
		Label cVal = new Label(values[4]);
		grid.add(cVal, 4, 3, 1, 1);
		cVal.setFont(Font.font(valuesSize));
		
		//creates and formats stock quality label
		Label buy = new Label("Stock Quality");
		grid.add(buy, 5, 2, 1, 1);
		buy.setFont(Font.font("Times New Roman", FontWeight.BOLD, staticSize));

		// displays "GOOD" "OKAY" or "BAD" depending on the if the user should buy into the stock now
		if (Double.parseDouble(values[3]) > 25) {
			Label bVal = new Label("GOOD");
			grid.add(bVal, 5, 3, 1, 1);
			bVal.setFont(Font.font(valuesSize));
			bVal.setTextFill(Color.web("#4bf442"));
		} 
		else if(Double.parseDouble(values[3]) > 15) {
			Label bVal = new Label("OKAY");
			grid.add(bVal, 5, 3, 1, 1);
			bVal.setFont(Font.font(valuesSize));
			bVal.setTextFill(Color.web("#a85ed6"));
		}
		else {
			Label bVal = new Label("BAD");
			grid.add(bVal, 5, 3, 1, 1);
			bVal.setFont(Font.font(valuesSize));
			bVal.setTextFill(Color.web("#e24b2d"));
		}

	}
	

	 // method to make second screen that displays stock data
	public static Scene makeScene2(Stage window, Rectangle2D screenBounds, Scene scene1, String ticker) {
		// get the real data from createPriceDate
		ArrayList<Double> current_opening = new ArrayList<Double>();
		ArrayList<Double> current_closing = new ArrayList<Double>();
		ArrayList<String> current_date = new ArrayList<String>();
		String url_fundamentals = "";
		String url_Historic_data = "";
		String historic = "";
		try {
			url_fundamentals = "https://cloud.iexapis.com/beta/stock/" + ticker.toLowerCase()
					+ "/quote?token=sk_873a126d55ed4d14bd53badd0d08f547";
			url_Historic_data = "https://cloud.iexapis.com/beta/stock/" + ticker.toLowerCase()
					+ "/chart/5y?token=sk_873a126d55ed4d14bd53badd0d08f547";
			String[][] history = createPriceDate(Historic_Data(url_Historic_data));
			
			for (String[] n: history) {
				current_date.add(n[0]);
				current_opening.add(Double.parseDouble(n[1]));
				current_closing.add(Double.parseDouble(n[2]));
			}
		} 
		catch (Exception e) {
			
		}
		
		
		// create an ArrayList that calculates and tabulates the daily percent change in
		// the data w/a for loop
		ArrayList<Double> current_change = calcDailyChange(current_opening, current_closing);
		
		Company current_Company = createCompanyObject(Company_Fundamentals(url_fundamentals));
		
		// create a companyData string
		String[] companyData = { current_Company.getName(), current_Company.getTicker(),
		current_Company.getLatestPrice(), current_Company.getpeRatio(), current_Company.marketCap()};

		// create 2 LineCharts that represent the closing chart and the change chart
		final LineChart<String, Number> closingChart = createChart(current_closing, current_date,
		"Closing Prices over the past 5 years", "Date", "Price", screenBounds);

		final LineChart<String, Number> changeChart = createChart(current_change, current_date,
		"Percent Daily Change over the past 5 years", "Date", "Percent Change", screenBounds);
				
		// create a new GridPane for the new scene
		GridPane grid2 = new GridPane();
		grid2.setAlignment(Pos.CENTER);
		grid2.setHgap(10);
		grid2.setVgap(10);
		grid2.setPadding(new Insets(15, 5, 15, 5));
		/*
		 * creates 8 columns of equal width (12.5% of horizontal
		 * area) and 4 rows (45% and 45% for the charts, 2 of 5% for the buttons and
		 * data) in the gridpane.
		 */
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(45);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(45);
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(5);
		RowConstraints row4 = new RowConstraints();
		row4.setPercentHeight(5);
		grid2.getRowConstraints().addAll(row1, row2, row3, row4);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(12.5);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(12.5);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(12.5);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(12.5);
		ColumnConstraints col5 = new ColumnConstraints();
		col5.setPercentWidth(12.5);
		ColumnConstraints col6 = new ColumnConstraints();
		col6.setPercentWidth(12.5);
		ColumnConstraints col7 = new ColumnConstraints();
		col7.setPercentWidth(12.5);
		ColumnConstraints col8 = new ColumnConstraints();
		col8.setPercentWidth(12.5);
		grid2.getColumnConstraints().addAll(col1, col2, col3, col4, col5, col6, col7, col8);

		// add changeChart and closingChart to gridpane2
		grid2.add(closingChart, 0, 0, 8, 1);
		grid2.add(changeChart, 0, 1, 8, 1);

		// add the rest of the data to the gridPane
		displayData(grid2, companyData);

		// create a back button to go back to the earlier window
		Button back = new Button("Go Back");
		back.setOnAction(e -> {
			window.setScene(scene1);
			window.setX((screenBounds.getWidth() - window.getWidth()) / 2);
			window.setY((screenBounds.getHeight() - window.getHeight()) / 2);
		});
		grid2.add(back, 6, 2, 1, 2);
		
		// creates button to allow user to exit the program
		Button exit = new Button("Exit");
		exit.setOnAction(e -> window.close());
		grid2.add(exit, 7, 2, 1, 2);
		return new Scene(grid2, screenBounds.getWidth(), screenBounds.getWidth());
		
	}
}
