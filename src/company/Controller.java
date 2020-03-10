package company;

import company.expirement.Experiment;
import company.expirement.ExperimentManager;
import company.entity.Matrix;
import company.filling.*;
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
    public Label gridSizeLabel;
    @FXML
    public ComboBox<FillingTypeV2> fillingTypes;

    @FXML
    public Button applyConfiguration;

    @FXML
    public void initialize() {
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(FXCollections.observableArrayList(new RandomFillingType(), new MaltTestFillingType("Мальтийский крест")));
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        experimentListView.setOnMouseClicked(item -> {
            Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            Matrix matrix = experimentManager.getMatrix(experiment);
            System.out.println(experiment.getPath() + "\n" + matrix.toString());
            painter.paintCanvas(grid, matrix);
        });
        fillingProbability.setVisible(false);
        probabilityLabel.setVisible(false);
        gridSize.setVisible(false);
        gridSizeLabel.setVisible(false);
        fillingTypes.setOnAction(event -> {
            if (fillingTypes.getValue() instanceof RandomFillingType) {
                fillingProbability.setVisible(true);
                probabilityLabel.setVisible(true);
                gridSize.setVisible(true);
                gridSizeLabel.setVisible(true);
            } else if (fillingTypes.getValue() instanceof CustomTestFillingType) {
                gridSize.setVisible(false);
                gridSizeLabel.setVisible(false);
                fillingProbability.setVisible(false);
                probabilityLabel.setVisible(false);
            }
        });
        applyConfiguration.setOnAction(actionEvent -> {
            String txt = experimentNumber.getText();
            int number = Integer.parseInt(txt);
            FillingTypeV2 fillingType = fillingTypes.getValue();
            if (fillingType instanceof RandomFillingType) {
                int size = gridSize.getValue().getValue();
                double probability = Double.parseDouble(fillingProbability.getText());
                ((RandomFillingType) fillingType).setPercolationProbability(probability);
                ((RandomFillingType) fillingType).setSize(size);
                experimentListView.setItems(experimentManager.initializeExperiments(number, fillingType));
            } else if (fillingType instanceof MaltTestFillingType) {
                experimentListView.setItems(experimentManager.initializeExperiments(number, fillingType));
            }
        });
    }
}