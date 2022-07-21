package Teste2022;

public class Eleitor {

    private int identidade;
    private boolean jaVotou;

    private int voto;

    public Eleitor(int identidade) {
        this.identidade = identidade;
        this.jaVotou = false;
        this.voto = 0;
    }

    public int getIdentidade() {
        return identidade;
    }

    public boolean isJaVotou() {
        return jaVotou;
    }

    public void setJaVotou(boolean jaVotou) {
        this.jaVotou = jaVotou;
    }
}
