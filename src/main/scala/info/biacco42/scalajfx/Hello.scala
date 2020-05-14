package info.biacco42.scalajfx

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

/**
 * Created by Biacco42 on 2020/03/23
 */

object Main extends App {
  Application.launch(classOf[HelloFX], args: _*)
}

class HelloFX extends Application {
  override def start(stage: Stage): Unit = {
    val javaVersion = System.getProperty("java.version")
    val javafxVersion = System.getProperty("javafx.version")
    val l = new Label(s"Hello, JavaFX $javafxVersion running on Java $javaVersion")
    val scene = new Scene(new StackPane(l), 640, 480)
    stage.setScene(scene)
    stage.show
  }
}
