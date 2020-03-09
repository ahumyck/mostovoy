package company;

import company.expirement.Experiment;
import company.expirement.ExperimentManager;
import company.entity.Matrix;
import company.filling.FillingType;
import company.filling.RandomFillingType;
import company.filling.TestFillingType;
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
    public Label probabilityLabel;

    @FXML
    public TextField experimentNumber;

    @FXML
    public ComboBox<GridSize> gridSize;

    @FXML
    public TextField fillingProbability;

    @FXML
    public ComboBox<FillingType> fillingTypes;

    @FXML
    public Button applyConfiguration;

    @FXML
    public void initialize() {
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(FXCollections.observableArrayList(new RandomFillingType(), new TestFillingType()));
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        experimentListView.setOnMouseClicked(item -> {
            Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            Matrix matrix = experimentManager.getMatrix(experiment);
            System.out.println(experiment.getPath() + "\n" + matrix.toString());
            painter.paintCanvas(grid, matrix);
        });
        fillingProbability.setVisible(false);
        probabilityLabel.setVisible(false);
        fillingTypes.setOnAction(event -> {
            if (fillingTypes.getValue() instanceof RandomFillingType) {
                fillingProbability.setVisible(true);
                probabilityLabel.setVisible(true);
            } else {
                fillingProbability.setVisible(false);
                probabilityLabel.setVisible(false);
            }
        });
        applyConfiguration.setOnAction(actionEvent -> {
            String txt = experimentNumber.getText();
            int number = Integer.parseInt(txt);
            int size = gridSize.getValue().getValue();
            double probability = Double.parseDouble(fillingProbability.getText());
            FillingType fillingType = fillingTypes.getValue();
            if (fillingType instanceof RandomFillingType) {
                ((RandomFillingType) fillingType).setPercolationProbability(probability);
            }
            experimentListView.setItems(experimentManager.initializeExperiments(number, size, fillingType));
        });
    }
}