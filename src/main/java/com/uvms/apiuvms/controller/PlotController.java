package com.uvms.apiuvms.controller;

import com.uvms.apiuvms.entity.Plots;
import com.uvms.apiuvms.service.PlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/plots")
public class PlotController {

    @Autowired
    private PlotService plotService;

    @GetMapping
    public List<Plots> getAllPlots() {
        return plotService.getAllPlots();
    }

    @GetMapping("/available")
    public List<Plots> getAvailablePlots() {
        return plotService.getAvailablePlots();
    }

    @GetMapping("/{plot_id}")
    public ResponseEntity<Plots> getPlotById(@PathVariable Integer plot_id) {
        Optional<Plots> plot = plotService.getPlotById(plot_id);
        return plot.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Plots createPlot(@RequestBody Plots plot) {
        return plotService.savePlot(plot);
    }

    @PutMapping("/{plot_id}")
    public ResponseEntity<Plots> updatePlot(@PathVariable Integer plot_id,
                                            @RequestBody Plots plotDetails) {
        Optional<Plots> plotOptional = plotService.getPlotById(plot_id);

        if (plotOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Plots plot = plotOptional.get();
        plot.setPlotNumber(plotDetails.getPlotNumber());
        plot.setLocationDescription(plotDetails.getLocationDescription());
        plot.setIsAvailable(plotDetails.getIsAvailable());

        Plots updatedPlot = plotService.savePlot(plot);
        return ResponseEntity.ok(updatedPlot);
    }

    @DeleteMapping("/{plot_id}")
    public void deletePlot(@PathVariable Integer plot_id) {
        plotService.deletePlotById(plot_id);
    }
}