package basic.control.chart;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import basic.common.ConnectionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;

public class ChartController implements Initializable {
	@FXML
	private PieChart pieChart;
	@FXML
	private BarChart barChart;
	@FXML
	private AreaChart areaChart;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// PieChart 데이터 설정
//		pieChart.setData(FXCollections.observableArrayList(
//				new PieChart.Data("AWT", 10), new PieChart.Data("Swing", 30),
//				new PieChart.Data("SWT", 25), new PieChart.Data("JavaFx", 35)));

		// 우선 인스턴스만 생성하고 하나씩 데이터 삽입
		ObservableList<Data> list = FXCollections.observableArrayList();
		list.add(new PieChart.Data("AWT", 10));
		list.add(new PieChart.Data("Swing", 30));
		list.add(new PieChart.Data("SWT", 25));
		list.add(new PieChart.Data("JavaFx", 35));

		pieChart.setData(list);

		// 시리즈 생성
		XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
		XYChart.Series<String, Integer> series2 = new XYChart.Series<>();

		series1.setName("남자");
		series1.setData(getSeries1());

		series2.setName("여자");
		series2.setData(getSeries2());

		// 시리즈를 차트에 추가
		barChart.getData().add(series1);
		barChart.getData().add(series2);

		XYChart.Series<String, Integer> series3 = new XYChart.Series<>();
		XYChart.Series<String, Integer> series4 = new XYChart.Series<>();

		series3.setName("평균온도");
		series3.setData(getSeries3());

		series4.setName("COVID19");
		series4.setData(getSeries4());

		areaChart.getData().add(series3);
//		areaChart.getData().add(series4);
	}

	public ObservableList<XYChart.Data<String, Integer>> getSeries1() {
		ObservableList<XYChart.Data<String, Integer>> list = FXCollections.observableArrayList();
		list.add(new XYChart.Data<String, Integer>("2015", 70));
		list.add(new XYChart.Data<String, Integer>("2016", 40));
		list.add(new XYChart.Data<String, Integer>("2017", 50));
		list.add(new XYChart.Data<String, Integer>("2018", 30));
		return list;
	}

	public ObservableList<XYChart.Data<String, Integer>> getSeries2() {
		ObservableList<XYChart.Data<String, Integer>> list = FXCollections.observableArrayList();
		list.add(new XYChart.Data<String, Integer>("2015", 30));
		list.add(new XYChart.Data<String, Integer>("2016", 60));
		list.add(new XYChart.Data<String, Integer>("2017", 50));
		list.add(new XYChart.Data<String, Integer>("2018", 60));
		return list;
	}

	public ObservableList<XYChart.Data<String, Integer>> getSeries3() {
		Connection conn = ConnectionDB.getDB();
		String sql = "select * from receipt";
		ObservableList<XYChart.Data<String, Integer>> list = FXCollections.observableArrayList();

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new XYChart.Data<>(rs.getString("receipt_month"), rs.getInt("receipt_qty")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ObservableList<XYChart.Data<String, Integer>> getSeries4() {
		ObservableList<XYChart.Data<String, Integer>> list = FXCollections.observableArrayList();
		list.add(new XYChart.Data<String, Integer>("09", 13));
		list.add(new XYChart.Data<String, Integer>("10", 6));
		list.add(new XYChart.Data<String, Integer>("11", 22));
		list.add(new XYChart.Data<String, Integer>("12", 19));
		return list;
	}
}
