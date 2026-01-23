package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationStats;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


import java.util.Optional;

public class SimulationPresenter implements MapChangeListener {
    private WorldMap map;
    private int startEnergy;
    private Simulation simulation;
    private boolean isPaused = false;

    @FXML private Label moveInfoLabel;
    @FXML private Canvas mapGrid;
    @FXML private Button pauseButton;
    @FXML private Button stopButton;
    @FXML private Label dayCountLabel;
    @FXML private Label animalCountLabel;
    @FXML private Label grassCountLabel;
    @FXML private Label freeFieldsLabel;
    @FXML private Label avgEnergyLabel;
    @FXML private Label avgLifespanLabel;

    private static final Color STEP_COLOR = Color.LIGHTYELLOW;
    private static final Color JUNGLE_COLOR = Color.web("#228B22");

    public void setWorldMap(WorldMap map) {
        this.map = map;
    }

    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }

    public void setSimulation(Simulation sim){
        this.simulation = sim;
    }

    private void clearGrid(GraphicsContext graphics) {
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, mapGrid.getWidth(), mapGrid.getHeight());
    }


    private void redrawGrid() {
        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        clearGrid(graphics);

        Boundary boundary = map.getMapBoundary();
        Vector2d lower = boundary.lowerBoundary();
        Vector2d upper = boundary.higherBoundary();

        int cols = upper.x() - lower.x() + 1;
        int rows = upper.y() - lower.y() + 1;

        double cellSize = Math.min(
                mapGrid.getWidth() / (cols),
                mapGrid.getHeight() / (rows)
        );

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Vector2d currentPos = new Vector2d(lower.x() + i, upper.y() - j);

                if (isJungle(currentPos)) {
                    graphics.setFill(JUNGLE_COLOR);
                } else {
                    graphics.setFill(STEP_COLOR);
                }

                double x = i * cellSize;
                double y = j * cellSize;
                graphics.fillRect(x, y, cellSize, cellSize);
            }
        }

        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(1);
        for (int i = 0; i <= cols; i++) {
            graphics.strokeLine(i * cellSize, 0, i * cellSize, rows * cellSize);
        }
        for (int j = 0; j <= rows; j++) {
            graphics.strokeLine(0, j * cellSize, cols * cellSize, j * cellSize);
        }


        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Vector2d currentPos = new Vector2d(lower.x() + i, upper.y() - j);

                Optional<WorldElement> obj = map.objectAt(currentPos);

                if (obj.isPresent()) {
                    double x = i * cellSize;
                    double y = j * cellSize;

                    WorldElement element = obj.get();
                    if (element instanceof Animal animal) {
                        drawAnimal(graphics, animal, x, y, cellSize);
                    } else {
                        graphics.setFill(Color.GREEN);
                        graphics.fillRect(x, y, cellSize, cellSize);
                    }
                }
            }
        }
    }

    private void drawAnimal(GraphicsContext graphics, Animal animal, double x, double y, double size) {
        graphics.setFill(getEnergyColor(animal.getEnergy()));
        double radius = size * 0.4;
        graphics.fillOval(x + (size - radius * 2) / 2, y + (size - radius * 2) / 2, radius * 2, radius * 2);
    }

    private Color getEnergyColor(int energy) {
        if (startEnergy <= 0) return Color.BLACK;
        float health = (float) energy / startEnergy;

        if (health <= 0) return Color.BLACK;
        if (health < 0.2) return Color.RED;
        if (health < 0.5) return Color.ORANGE;
        if (health < 0.75) return Color.YELLOW;
        return Color.LIGHTGREEN;
    }

    private boolean isJungle(Vector2d pos) {
        return map.isInJungle(pos);
    }

    public void drawMap() {
        redrawGrid();
    }

    private void updateStatsUI(SimulationStats stats) {
        dayCountLabel.setText("Day: " + simulation.getDay());
        animalCountLabel.setText("Animals: " + stats.animalCount());
        grassCountLabel.setText("Grass: " + stats.grassCount());
        freeFieldsLabel.setText("Vacant cell: " + stats.freeFieldCount());
        avgEnergyLabel.setText(String.format("Avg Energy: %.2f", stats.avgEnergy()));
        avgLifespanLabel.setText(String.format("Avg Lifespan: %.2f", stats.avgLifespan()));
    }

    @Override
    public void mapChanged(WorldMap world_map, String message) {
        Platform.runLater(() -> {
            drawMap();
            SimulationStats stats = map.getStats();
            updateStatsUI(stats);
        });
    }

    @FXML
    public void onPauseButtonClicked() {
        if (simulation == null) return;

        if (isPaused) {
            simulation.resume();
            pauseButton.setText("Pause");
            isPaused = false;
        } else {
            simulation.pause();
            pauseButton.setText("Resume");
            isPaused = true;
        }
    }

    @FXML
    public void onStopButtonClicked() {
        if (simulation != null) {
            simulation.stop();
        }
    }
}

