package com.mostovoy_company.services;


public interface MainService {
    default void initNewSession(){

    }
    void addExperimentsDescription(int count, int size, double probability);
    void consume(ConsumeProperties consumeProperties);
}
