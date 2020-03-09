package company;

import company.expirement.Experiment;
import company.expirement.ExperimentManager;
import company.expirement.Matrix;
import company.paint.Painter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class Controller {

    private ExperimentManager experimentManager = new ExperimentManager();
    private Painter painter = new Painter();

    @FXML
    private ListView<Experiment> experimentListView;

    @FXML
    private AnchorPane grid;

    @FXML
    public TextField experimentNumber;

    @FXML
    public ComboBox<GridSize> gridSize;

    @FXML
    public TextField fillingProbability;

    @FXML
    public ComboBox<FillingType> fillingType;

    @FXML
    public Button applyConfiguration;

    @FXML
    public void initialize() {
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        experimentListView.setOnMouseClicked(item -> {
            Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            Matrix matrix = experimentManager.getMatrix(experiment);
            System.out.println(experiment.getPath() + "\n" + matrix.toString());
            painter.paintCanvas(grid, matrix);
        });
        applyConfiguration.setOnAction(actionEvent -> {
            String txt = experimentNumber.getText();
            int number = Integer.parseInt(txt);
            int size = gridSize.getValue().getValue();
            double probability = Double.parseDouble(fillingProbability.getText());
            experimentListView.setItems(experimentManager.initializeExperiments(number, size, probability));
        });
    }
}