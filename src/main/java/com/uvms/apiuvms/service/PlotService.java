package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Plots;
import com.uvms.apiuvms.repository.PlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlotService {

    @Autowired
    private PlotRepository plotRepository;

    public List<Plots> getAllPlots() {
        return plotRepository.findAll();
    }

    public Optional<Plots> getPlotById(Integer id) {
        return plotRepository.findById(id);
    }

    public Plots savePlot(Plots plot) {
        return plotRepository.save(plot);
    }

    public void deletePlotById(Integer id) {
        plotRepository.deleteById(id);
    }

    // Additional methods for business logic
    public List<Plots> getAvailablePlots() {
        return plotRepository.findAll().stream()
                .filter(Plots::getIsAvailable)
                .toList();
    }
}