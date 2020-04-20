package com.mostovoy_company.expirement.lightningbolt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paired<V,T>{
    private V first;
    private T second;

    @Override
    public String toString() {
        return "{" + "first=" + first + ", second=" + second + '}';
    }
}
