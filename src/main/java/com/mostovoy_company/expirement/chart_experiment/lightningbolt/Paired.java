package com.mostovoy_company.expirement.chart_experiment.lightningbolt;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paired<V, T> {
    @Expose
    private V first;
    @Expose
    private T second;

    @Override
    public String toString() {
        return "{" + "first=" + first + ", second=" + second + '}';
    }
}
