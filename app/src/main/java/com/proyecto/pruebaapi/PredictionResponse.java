package com.proyecto.pruebaapi;

import java.util.List;

public class PredictionResponse {
    private List<List<Double>> prediction;

    // Método para obtener la predicción como un String
    public String getPrediction() {
        if (prediction != null && !prediction.isEmpty()) {
            StringBuilder result = new StringBuilder();
            for (List<Double> innerList : prediction) {
                for (Double value : innerList) {
                    result.append(value.toString()).append(" ");
                }
                result.append("\n");
            }
            return result.toString().trim(); // Convertir la lista a String y devolverla
        }
        return "No hay predicción disponible";
    }
}
