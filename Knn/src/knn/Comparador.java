/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import knn.*;
import java.util.Comparator;


public class Comparador implements Comparator<Vizinho>{

    @Override
    public int compare(Vizinho v1, Vizinho v2) {
        if(v1.getDistancia() > v2.getDistancia())
        {
            return 1;
        }
        else if(v1.getDistancia() < v2.getDistancia())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
 
}
