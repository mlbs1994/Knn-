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
public class ColunaVertebral 
{
    public double c1;
    public double c2;
    public double c3;
    public double c4;
    public double c5;
    public double c6;
    public String classe;

    public ColunaVertebral(double c1, double c2, double c3, double c4, double c5, double c6) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
        this.c5 = c5;
        this.c6 = c6;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
    
    
    
    
}
