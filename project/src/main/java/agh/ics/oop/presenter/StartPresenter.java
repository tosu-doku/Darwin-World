package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.JungleMap;
import agh.ics.oop.model.util.SimulationConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartPresenter {

    @FXML private TextField mapWidthField;
    @FXML private TextField mapHeightField;
    @FXML private CheckBox fomoBoolean;
    @FXML private TextField startEnergyField;
    @FXML private TextField limitEnergyField;
    @FXML private TextField healthyEnergyField;
    @FXML private TextField moveCostField;
    @FXML private TextField reproductionCostField;
    @FXML private TextField grassEnergyField;
    @FXML private TextField animalAmountField;
    @FXML private TextField startGrassAmountField;
    @FXML private TextField grassAmountField;
    @FXML private TextField genotypeLength;
    @FXML private TextField maxMutationsField;
    @FXML private TextField fomoRadiusInsideField;
    @FXML private TextField fomoRadiusOutsideField;
    @FXML private TextField animalsToTriggerField;

    @FXML
    public void onStartClicked() {
        try {
            SimulationConfig config = new SimulationConfig(
                    Integer.parseInt(mapWidthField.getText()),
                    Integer.parseInt(mapHeightField.getText()),
                    Integer.parseInt(animalAmountField.getText()),
                    Integer.parseInt(startGrassAmountField.getText()),
                    Integer.parseInt(genotypeLength.getText()),
                    Integer.parseInt(grassEnergyField.getText()),
                    Integer.parseInt(startEnergyField.getText()),
                    Integer.parseInt(limitEnergyField.getText()),
                    Integer.parseInt(moveCostField.getText()),
                    Integer.parseInt(healthyEnergyField.getText()),
                    Integer.parseInt(reproductionCostField.getText()),
                    fomoBoolean.isSelected(),
                    Integer.parseInt(maxMutationsField.getText()),
                    Integer.parseInt(grassAmountField.getText()),
                    Integer.parseInt(fomoRadiusInsideField.getText()),
                    Integer.parseInt(fomoRadiusOutsideField.getText()),
                    Integer.parseInt(animalsToTriggerField.getText())
            );

            showSimulationWindow(config);

        } catch (NumberFormatException e) {
            showError("Make sure that all inputs are filled");
        } catch (IllegalArgumentException e) {
            showError("Parameter Error: " + e.getMessage());
        } catch (IOException e) {
            showError("Failed to load the simulation.");
            e.printStackTrace();
        }
    }

    private void showSimulationWindow(SimulationConfig config) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();
        JungleMap map = new JungleMap(config);

        map.registerListener(presenter);
        presenter.setWorldMap(map);
        presenter.setStartEnergy(config.ENERGY_LIMIT());

        Stage stage = new Stage();
        stage.setTitle("Darwin World");
        stage.setScene(new Scene(viewRoot));
        stage.show();

        Simulation simulation = new Simulation(map,config);
        presenter.setThreadController(simulation);
        presenter.setStatsProvider(simulation);
        List<Simulation> simulations_list = new ArrayList<>();
        simulations_list.add(simulation);
        SimulationEngine engine = new SimulationEngine(simulations_list);
        engine.runAsync();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Configuration Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}