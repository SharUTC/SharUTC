package fr.utc.lo23.sharutc.ui.custom;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class DynamicGridPane extends GridPane {

    private static final int DEFAULT_COL_SIZE = 100;
    private static final double DEFAULT_H_GAP = 10;
    private static final double DEFAULT_V_GAP = 10;
    private IntegerProperty mColumsNumber;
    private ArrayList<String> mList;

    //TODO abstraction of the List as well as the
    public DynamicGridPane(ReadOnlyDoubleProperty sceneW) {
        super();

        this.setAlignment(Pos.CENTER);
        this.setHgap(DEFAULT_H_GAP);
        this.setVgap(DEFAULT_V_GAP);

        //Dummy init
        mList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) mList.add("JABBERWOCKY " + i);

        mColumsNumber = new SimpleIntegerProperty(1);
        mColumsNumber.bind(sceneW.divide(200));
        mColumsNumber.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                draw();
            }
        });
        draw();
    }

    private void draw() {
        final int col = mColumsNumber.intValue();
        if (col != 0) {
            this.getChildren().clear();
            final int row = mList.size() / col;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    this.add(new Button(mList.get(i * col + j)), j, i);
                }
            }
        }
    }
}
