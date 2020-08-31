package basic.container;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class TilePaneExample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TilePane tilePane = new TilePane();
		tilePane.setPrefTileHeight(100);
		tilePane.setPrefTileWidth(100);

		ImageView[] ary = new ImageView[5];
		for (int i = 0; i < 5; i++) {
			ImageView iv = new ImageView();
			iv.setImage(new Image("/images/fruit" + (i + 1) + ".jpg"));
			ary[i] = iv;
			tilePane.getChildren().add(ary[i]);
		}

		Scene scene = new Scene(tilePane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("TilePane 예제");
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
