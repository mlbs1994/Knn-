/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author Matheus Levi
 */
public class Knn {

    ArrayList<ColunaVertebral> listaAmostrasTreinamento = new ArrayList();
    ArrayList<ColunaVertebral> listaAmostrasTeste = new ArrayList();
    int[][] matrizConfusao = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    int[] numAcertosDesejadosClasse = {20, 110, 60};
    double taxaAcertoGlobal = 0.0f;
    double taxaAcertoGlobalDH = 0.0f;
    double taxaAcertoGlobalSL = 0.0f;
    double taxaAcertoGlobalNO = 0.0f;
    double taxaAcertoGlobalDE = 0.0f;
    double taxaAcertoGlobalDMis = 0.0f;
    double taxaAcertoGlobalDMan = 0.0f;
    String nomeArquivoTreinamento = "";
    String nomeArquivoTeste = "";
    String distanciaEscolhida = "";
    int kVizinhos = 0;

    public void getDistanciaEuclidiana() {
        ArrayList<Vizinho> vizinhosDistancias;
        int[] indiceVizinhos = new int[this.kVizinhos];
        ArrayList<ColunaVertebral> vizinhos = new ArrayList();
        ColunaVertebral cv;
        double distanciaM;

        for (int i = 0; i < this.listaAmostrasTeste.size(); i++) {
            vizinhosDistancias = new ArrayList<>();

            for (int j = 0; j < this.listaAmostrasTreinamento.size(); j++) {
                distanciaM = Math.sqrt(
                        Math.pow(listaAmostrasTeste.get(i).c1 - listaAmostrasTreinamento.get(j).c1, 2)
                        + Math.pow(listaAmostrasTeste.get(i).c2 - listaAmostrasTreinamento.get(j).c2, 2)
                        + Math.pow(listaAmostrasTeste.get(i).c3 - listaAmostrasTreinamento.get(j).c3, 2)
                        + Math.pow(listaAmostrasTeste.get(i).c4 - listaAmostrasTreinamento.get(j).c4, 2)
                        + Math.pow(listaAmostrasTeste.get(i).c5 - listaAmostrasTreinamento.get(j).c5, 2)
                        + Math.pow(listaAmostrasTeste.get(i).c6 - listaAmostrasTreinamento.get(j).c6, 2)
                );

                vizinhosDistancias.add(new Vizinho(listaAmostrasTreinamento.get(j), distanciaM));

            }

            //Obter os k vizinhos com menor distância
            Collections.sort(vizinhosDistancias, new Comparador());

            int contDH = 0;
            int contSL = 0;
            int contNO = 0;
            int[] contClasses = new int[3];
            String[] classes = {"DH", "SL", "NO"};

            for (int v = 0; v < this.kVizinhos; v++) {
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[0])) {
                    contDH++;
                }
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[1])) {
                    contSL++;
                }
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[2])) {
                    contNO++;
                }
            }

            contClasses[0] = contDH;
            contClasses[1] = contSL;
            contClasses[2] = contNO;

            String classeFrequente = classes[0];
            int indiceClasseFrequente = 0;
            int indiceClasseAmostra = 0;

            for (int t = 0; t < classes.length; t++) {
                if (listaAmostrasTeste.get(i).classe.equals(classes[t])) {
                    indiceClasseAmostra = t;
                }
            }

            for (int u = 1; u < contClasses.length; u++) {
                if (contClasses[u] > contClasses[indiceClasseFrequente]) {
                    indiceClasseFrequente = u;
                }

            }

            //Alimentar a matriz de confusão
            this.matrizConfusao[indiceClasseAmostra][indiceClasseFrequente]++;

        }
    }

    public void getDistanciaManhattan() throws FileNotFoundException, IOException {

        /*
         Dm(p,q) = nEi=1 |pi-qi|
         */
        ArrayList<Vizinho> vizinhosDistancias;
        int[] indiceVizinhos = new int[this.kVizinhos];
        ArrayList<ColunaVertebral> vizinhos = new ArrayList();
        ColunaVertebral cv;
        double distanciaM;

        for (int i = 0; i < this.listaAmostrasTeste.size(); i++) {
            vizinhosDistancias = new ArrayList<>();

            for (int j = 0; j < this.listaAmostrasTreinamento.size(); j++) {
                distanciaM = Math.abs(listaAmostrasTeste.get(i).c1 - listaAmostrasTreinamento.get(j).c1)
                        + Math.abs(listaAmostrasTeste.get(i).c2 - listaAmostrasTreinamento.get(j).c2)
                        + Math.abs(listaAmostrasTeste.get(i).c3 - listaAmostrasTreinamento.get(j).c3)
                        + Math.abs(listaAmostrasTeste.get(i).c4 - listaAmostrasTreinamento.get(j).c4)
                        + Math.abs(listaAmostrasTeste.get(i).c5 - listaAmostrasTreinamento.get(j).c5)
                        + Math.abs(listaAmostrasTeste.get(i).c6 - listaAmostrasTreinamento.get(j).c6);

                /*Math.pow(listaAmostrasTeste.get(i).c1 - listaAmostrasTreinamento.get(j).c1, 2.0); //Eucliaana
                 Math.sqrt(distanciaM); EUCLIDIANA*/
                /*Math.pow(listaAmostrasTeste.get(i).c1 - listaAmostrasTreinamento.get(j).c1, 2.0); //Eucliaana
                 Math.pow(distanciaM,1/x ); MIKOWSKI*/
                vizinhosDistancias.add(new Vizinho(listaAmostrasTreinamento.get(j), distanciaM));

            }

            //Obter os k vizinhos com menor distância
            Collections.sort(vizinhosDistancias, new Comparador());

            int contDH = 0;
            int contSL = 0;
            int contNO = 0;
            int[] contClasses = new int[3];
            String[] classes = {"DH", "SL", "NO"};

            for (int v = 0; v < this.kVizinhos; v++) {
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[0])) {
                    contDH++;
                }
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[1])) {
                    contSL++;
                }
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[2])) {
                    contNO++;
                }
            }

            contClasses[0] = contDH;
            contClasses[1] = contSL;
            contClasses[2] = contNO;

            String classeFrequente = classes[0];
            int indiceClasseFrequente = 0;
            int indiceClasseAmostra = 0;

            for (int t = 0; t < classes.length; t++) {
                if (listaAmostrasTeste.get(i).classe.equals(classes[t])) {
                    indiceClasseAmostra = t;
                }
            }

            for (int u = 1; u < contClasses.length; u++) {
                if (contClasses[u] > contClasses[indiceClasseFrequente]) {
                    indiceClasseFrequente = u;
                }

            }

            //Alimentar a matriz de confusão
            this.matrizConfusao[indiceClasseAmostra][indiceClasseFrequente]++;

        }

    }

    public void getDistanciaMinkowski(double raiz) {
        ArrayList<Vizinho> vizinhosDistancias;
        int[] indiceVizinhos = new int[this.kVizinhos];
        ArrayList<ColunaVertebral> vizinhos = new ArrayList();
        ColunaVertebral cv;
        double distanciaM;

        for (int i = 0; i < this.listaAmostrasTeste.size(); i++) {
            vizinhosDistancias = new ArrayList<>();

            for (int j = 0; j < this.listaAmostrasTreinamento.size(); j++) {
                distanciaM
                        = Math.pow(listaAmostrasTeste.get(i).c1 - listaAmostrasTreinamento.get(j).c1, raiz)
                        + Math.pow(listaAmostrasTeste.get(i).c2 - listaAmostrasTreinamento.get(j).c2, raiz)
                        + Math.pow(listaAmostrasTeste.get(i).c3 - listaAmostrasTreinamento.get(j).c3, raiz)
                        + Math.pow(listaAmostrasTeste.get(i).c4 - listaAmostrasTreinamento.get(j).c4, raiz)
                        + Math.pow(listaAmostrasTeste.get(i).c5 - listaAmostrasTreinamento.get(j).c5, raiz)
                        + Math.pow(listaAmostrasTeste.get(i).c6 - listaAmostrasTreinamento.get(j).c6, raiz);

                distanciaM = Math.pow(distanciaM, 1 / raiz);

                vizinhosDistancias.add(new Vizinho(listaAmostrasTreinamento.get(j), distanciaM));

            }

            //Obter os k vizinhos com menor distância
            Collections.sort(vizinhosDistancias, new Comparador());

            int contDH = 0;
            int contSL = 0;
            int contNO = 0;
            int[] contClasses = new int[3];
            String[] classes = {"DH", "SL", "NO"};

            for (int v = 0; v < this.kVizinhos; v++) {
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[0])) {
                    contDH++;
                }
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[1])) {
                    contSL++;
                }
                if (vizinhosDistancias.get(v).colunaVertebral.getClasse().equals(classes[2])) {
                    contNO++;
                }
            }

            contClasses[0] = contDH;
            contClasses[1] = contSL;
            contClasses[2] = contNO;

            String classeFrequente = classes[0];
            int indiceClasseFrequente = 0;
            int indiceClasseAmostra = 0;

            for (int t = 0; t < classes.length; t++) {
                if (listaAmostrasTeste.get(i).classe.equals(classes[t])) {
                    indiceClasseAmostra = t;
                }
            }

            for (int u = 1; u < contClasses.length; u++) {
                if (contClasses[u] > contClasses[indiceClasseFrequente]) {
                    indiceClasseFrequente = u;
                }

            }

            //Alimentar a matriz de confusão
            this.matrizConfusao[indiceClasseAmostra][indiceClasseFrequente]++;

        }
    }

    public void LerArquivoTreinamento() throws FileNotFoundException, IOException {
        ColunaVertebral cv;

        FileReader file = new FileReader(new File(this.nomeArquivoTreinamento).getAbsoluteFile());
        BufferedReader br = new BufferedReader(file);

        String temp = br.readLine();
        String[] caract = temp.split(" ");
        cv = new ColunaVertebral(Float.parseFloat(caract[0]),
                Float.parseFloat(caract[1]),
                Float.parseFloat(caract[2]),
                Float.parseFloat(caract[3]),
                Float.parseFloat(caract[4]),
                Float.parseFloat(caract[5])
        );
        cv.setClasse(caract[6]);
        this.listaAmostrasTreinamento.add(cv);

        while (temp != null) {
            System.out.println(temp);
            temp = br.readLine();
            if (temp != null) {
                caract = temp.split(" ");
                cv = new ColunaVertebral(Float.parseFloat(caract[0]),
                        Float.parseFloat(caract[1]),
                        Float.parseFloat(caract[2]),
                        Float.parseFloat(caract[3]),
                        Float.parseFloat(caract[4]),
                        Float.parseFloat(caract[5])
                );
                cv.setClasse(caract[6]);
                this.listaAmostrasTreinamento.add(cv);
            }
        }
    }

    public void LerArquivoTeste() throws FileNotFoundException, IOException {
        ColunaVertebral cv;

        //Ler a base de teste
        FileReader file = new FileReader(new File(this.nomeArquivoTeste).getAbsoluteFile());
        BufferedReader br = new BufferedReader(file);

        String temp = br.readLine();
        String[] caract = temp.split(" ");
        cv = new ColunaVertebral(Float.parseFloat(caract[0]),
                Float.parseFloat(caract[1]),
                Float.parseFloat(caract[2]),
                Float.parseFloat(caract[3]),
                Float.parseFloat(caract[4]),
                Float.parseFloat(caract[5])
        );
        cv.setClasse(caract[6]);
        this.listaAmostrasTeste.add(cv);

        while (temp != null) {
            System.out.println(temp);
            temp = br.readLine();
            if (temp != null) {
                caract = temp.split(" ");
                cv = new ColunaVertebral(Float.parseFloat(caract[0]),
                        Float.parseFloat(caract[1]),
                        Float.parseFloat(caract[2]),
                        Float.parseFloat(caract[3]),
                        Float.parseFloat(caract[4]),
                        Float.parseFloat(caract[5])
                );
                cv.setClasse(caract[6]);
                this.listaAmostrasTeste.add(cv);
            }
        }
    }

    private void imprimirAmostrasTreinamento() {
        for (int i = 0; i < this.listaAmostrasTreinamento.size(); i++) {
            System.out.println("Coluna Vertebral " + (i + 1));
            System.out.println("C1 =" + this.listaAmostrasTreinamento.get(i).c1);
            System.out.println("C2 =" + this.listaAmostrasTreinamento.get(i).c2);
            System.out.println("C3 =" + this.listaAmostrasTreinamento.get(i).c3);
            System.out.println("C4 =" + this.listaAmostrasTreinamento.get(i).c4);
            System.out.println("C5 =" + this.listaAmostrasTreinamento.get(i).c5);
            System.out.println("C6 =" + this.listaAmostrasTreinamento.get(i).c6);
            System.out.println("Classe =" + this.listaAmostrasTreinamento.get(i).classe);
            System.out.println("\n");
        }
    }

    private void imprimirAmostrasTeste() {
        for (int i = 0; i < this.listaAmostrasTeste.size(); i++) {
            System.out.println("Coluna Vertebral " + (i + 1));
            System.out.println("C1 =" + this.listaAmostrasTeste.get(i).c1);
            System.out.println("C2 =" + this.listaAmostrasTeste.get(i).c2);
            System.out.println("C3 =" + this.listaAmostrasTeste.get(i).c3);
            System.out.println("C4 =" + this.listaAmostrasTeste.get(i).c4);
            System.out.println("C5 =" + this.listaAmostrasTeste.get(i).c5);
            System.out.println("C6 =" + this.listaAmostrasTeste.get(i).c6);
            System.out.println("Classe =" + this.listaAmostrasTeste.get(i).classe);
            System.out.println("\n");
        }
    }

    private void imprimirMatrizConfusao() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(this.matrizConfusao[i][j] + " ");
            }

            System.out.println("");
        }
    }

    private void obterTaxaAcerto(int metodoDistancia) {
        double somaAcertos = 0.0d;

        for (int i = 0; i < 3; i++) {
            somaAcertos += this.matrizConfusao[i][i];
        }
        
        System.out.printf("Taxa de Acerto = %.2f %n" , ((somaAcertos / 190) * 100));
        
        this.taxaAcertoGlobal += ((somaAcertos / 190) * 100);
        
        switch(metodoDistancia)
        {
            case 1:
            this.taxaAcertoGlobalDE += (somaAcertos / 190) * 100;    
            break;
                
            case 2:
            this.taxaAcertoGlobalDMis += (somaAcertos / 190) * 100;    
            break;
                
            case 3:
             this.taxaAcertoGlobalDMan += (somaAcertos / 190) * 100;    
            break;
                
        }
    }

    private void obterTaxaAcertoDH() {
        double acertosDH = this.matrizConfusao[0][0];

        System.out.printf("Taxa de Acerto DH = %.2f %n", ((acertosDH / 20) * 100));
        
        this.taxaAcertoGlobalDH += ((acertosDH / 20) * 100);
    }

    private void obterTaxaAcertoSL() {
        double acertosSL = this.matrizConfusao[1][1];

        System.out.printf("Taxa de Acerto SL = %.2f %n", ((acertosSL / 110) * 100));
        
        this.taxaAcertoGlobalSL += ((acertosSL / 110) * 100);
    }

    private void obterTaxaAcertoNO() {
        double acertosNO = this.matrizConfusao[2][2];

        System.out.printf("Taxa de Acerto NO = %.2f %n", ((acertosNO / 60) * 100));
        
        this.taxaAcertoGlobalNO += ((acertosNO / 60) * 100);
    }
    
    private double obterTaxaAcertoGlobal()
    {
        return (this.taxaAcertoGlobal/18);
    }

    private double obterTaxaAcertoGlobalDH()
    {
        return (this.taxaAcertoGlobalDH/18);
    }

    private double obterTaxaAcertoGlobalSL()
    {
        return (this.taxaAcertoGlobalSL/18);
    }

    private double obterTaxaAcertoGlobalNO()
    {
        return (this.taxaAcertoGlobalNO/18);
    }

    private double obterTaxaAcertoGlobalDE()
    {
        return (this.taxaAcertoGlobalDE/6);
    }

    private double obterTaxaAcertoGlobalDMin()
    {
        return (this.taxaAcertoGlobalDMis/6);
    }

    private double obterTaxaAcertoGlobalDMan()
    {
        return (this.taxaAcertoGlobalDMan/6);
    }

    private void zerarMatrizConfusao() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.matrizConfusao[i][j] = 0;
            }
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Knn knn = new Knn();

        ColunaVertebral cv;

        Scanner input = new Scanner(System.in);

        System.out.println("Digite o nome do arquivo com a base de treinamento (incluindo a extensão)");
        knn.nomeArquivoTreinamento = input.nextLine();

        System.out.println("Digite o nome do arquivo com a base de teste (incluindo a extensão)");
        knn.nomeArquivoTeste = input.nextLine();

        System.out.println("\nTreinamento: " + new File(knn.nomeArquivoTreinamento).getAbsoluteFile());
        knn.LerArquivoTreinamento();

        System.out.println("\n-----------------------------------------------------------------------\n");

        System.out.println("Teste: " + new File(knn.nomeArquivoTeste).getAbsoluteFile());
        knn.LerArquivoTeste();

        System.out.println("\n\n");

        /*System.out.println("Imprimindo as amostras da base de treinamento...\n");
        knn.imprimirAmostrasTreinamento();
        System.out.println("\nImprimindo as amostras da base de teste...\n");
        knn.imprimirAmostrasTeste();*/

        /*System.out.println("\nSelecione o tipo de distância a ser calculada\n"
         + "1)Euclidiana\n2)Mikowski\n3)Manhattan");
         knn.distanciaEscolhida = input.nextLine();
        
         System.out.println("Defina a quantidade de vizinhos (k)");
         knn.kVizinhos = input.nextInt();*/
        
        System.out.println("Calculando as distâncias...\n");
        System.out.println("Imprimindo a matriz de confusão...\n");
        
        knn.kVizinhos = 1;
        for (int i = 1; i <= 11; i = i + 2) {
            System.out.println("Distância Euclidiana");
            System.out.println("k = " + knn.kVizinhos);
            knn.getDistanciaEuclidiana();
            knn.kVizinhos = i + 2;
            knn.imprimirMatrizConfusao();
            knn.obterTaxaAcerto(1);
            knn.obterTaxaAcertoDH();
            knn.obterTaxaAcertoSL();
            knn.obterTaxaAcertoNO();
            knn.zerarMatrizConfusao();
            System.out.println("\n");
        }

        knn.kVizinhos = 1;
        for (int i = 1; i <= 11; i = i + 2) {
            System.out.println("Distância de Minkowski");
            System.out.println("k = " + knn.kVizinhos);
            System.out.println("Raiz = 4");
            knn.getDistanciaMinkowski(4.0d);
            knn.kVizinhos = i + 2;
            knn.imprimirMatrizConfusao();
            knn.obterTaxaAcerto(2);
            knn.obterTaxaAcertoDH();
            knn.obterTaxaAcertoSL();
            knn.obterTaxaAcertoNO();
            knn.zerarMatrizConfusao();
            System.out.println("\n");
        }

        knn.kVizinhos = 1;
        for (int i = 1; i <= 11; i = i + 2) {
            System.out.println("Distância de Manhattan");
            System.out.println("k = " + knn.kVizinhos);
            knn.getDistanciaManhattan();
            knn.kVizinhos = i + 2;
            knn.imprimirMatrizConfusao();
            knn.obterTaxaAcerto(3);
            knn.obterTaxaAcertoDH();
            knn.obterTaxaAcertoSL();
            knn.obterTaxaAcertoNO();
            knn.zerarMatrizConfusao();
            System.out.println("\n");
        }
        
        System.out.println("DAS CLASSES");
        System.out.printf("TAXA DE ACERTO GLOBAL CLASSE DH = %.2f %n",knn.obterTaxaAcertoGlobalDH());
        System.out.printf("TAXA DE ACERTO GLOBAL CLASSE SL = %.2f %n",knn.obterTaxaAcertoGlobalSL());
        System.out.printf("TAXA DE ACERTO GLOBAL CLASSE NO = %.2f %n",knn.obterTaxaAcertoGlobalNO());
        System.out.println("DOS MÉTODOS MATEMÁTICOS DE CALCULAR DISTÂNCIA");
        System.out.printf("TAXA DE ACERTO GLOBAL DISTÂNCIA EUCLIDIANA = %.2f %n",knn.obterTaxaAcertoGlobalDE());
        System.out.printf("TAXA DE ACERTO GLOBAL DISTÂNCIA DE MIKOWSKI = %.2f %n",knn.obterTaxaAcertoGlobalDMin());
        System.out.printf("TAXA DE ACERTO GLOBAL DISTÂNCIA DE MANHATTAN = %.2f %n",knn.obterTaxaAcertoGlobalDMan());
        System.out.println("\nDA PRECISÃO TOTAL");
        System.out.printf("TAXA DE ACERTO GLOBAL = %.2f %n",knn.obterTaxaAcertoGlobal());
    }

}
