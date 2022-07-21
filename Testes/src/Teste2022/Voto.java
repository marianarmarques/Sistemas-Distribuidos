package Teste2022;

public class Voto {
    private int escolha;
    private int contador=0;

    public Voto(int escolha) {
        this.escolha=escolha;
    }

    public int getEscolha() {
        return escolha;
    }

    public int getContador() {
        return this.contador;
    }

    public int incVoto() {
        return this.contador++;
    }
}
