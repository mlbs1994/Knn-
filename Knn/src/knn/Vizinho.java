/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

/**
 *
 * @author Matheus Levi
 */
public class Vizinho
{
    ColunaVertebral colunaVertebral;
    double distancia;

    public Vizinho(ColunaVertebral colunaVertebral, double distancia) {
        this.colunaVertebral = colunaVertebral;
        this.distancia = distancia;
    }

    public ColunaVertebral getColunaVertebral() {
        return colunaVertebral;
    }

    public double getDistancia() {
        return distancia;
    }
    
    
}
