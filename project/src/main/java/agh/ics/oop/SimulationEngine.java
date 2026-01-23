package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SimulationEngine {
    private final List<Simulation> simulations_list;
    private final List<Thread> threads_list = new ArrayList<>();
    private ExecutorService executor_service;
    public SimulationEngine(List<Simulation> simulations_list){
        this.simulations_list=simulations_list;
    }

    public void runSync(){
        simulations_list.forEach(Simulation::run);
    }

    public void runAsync(){
        simulations_list.forEach(simulation -> {
            Thread simulation_thread = new Thread(simulation);
            threads_list.add(simulation_thread);
            simulation_thread.start();
        });
    }
}
