package pro.gravit.launcher.client.gui.scenes.renewal;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pro.gravit.launcher.client.gui.JavaFXApplication;
import pro.gravit.launcher.client.gui.scenes.AbstractScene;
import pro.gravit.launcher.client.gui.helper.LookupHelper;
import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import pro.gravit.launcher.client.gui.scenes.login.LoginScene;
import pro.gravit.launcher.client.gui.service.AuthService;
import javafx.animation.RotateTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.scene.shape.Arc;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.ArcType;


public class RenewalScene extends AbstractScene {

    private Circle circle1;
    private Circle circle2;
    private Circle circle3;
    private Circle circle4;
    private Circle circle5;
    private LoginScene loginScene;
    private final AuthService authService = new AuthService(application);
    private Arc spinner;

    public RenewalScene(JavaFXApplication application) {
        super("scenes/renewal/renewal.fxml", application);
    }

    @Override
    public void doInit() throws Exception {
        spinner = LookupHelper.lookup(layout, "#spinner");

        spinner.setType(ArcType.OPEN);
        spinner.setStrokeWidth(5);
        spinner.setStroke(Color.RED);
        spinner.setFill(null);

        // Запускаємо анімацію круга
        animateCircle();
    }

    private void animateCircle() {
        // Створюємо анімацію для зміни кута заповнення круга
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(spinner.lengthProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(spinner.lengthProperty(), 360))
        );

        // Налаштовуємо параметри анімації
        timeline.setCycleCount(2); // 5 повторень
        timeline.setAutoReverse(false); // Автоматичне зворотне відтворення (відключено)

        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Перехід на іншу сцену після закінчення анімації
                Platform.runLater(() -> {
                    try {
                        application.setMainScene(application.gui.loginScene);
                    } catch (Exception e) {
                        // Обробка виключення (можливо, виведення стеку виклику або інші дії)
                        e.printStackTrace();
                    }
                });
            }
        });

        // Починаємо анімацію
        timeline.play();
    }





    
  

    @Override
    public String getName() {
        return "renewal";
    }

    @Override
    public void reset() {
        // Повернення сцени до початкового стану, якщо необхідно.
    }
}
