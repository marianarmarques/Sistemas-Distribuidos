package Teste2022;

public class Cabine {
    private int id;
    private boolean disponivel;

    public Cabine(int id) {
        this.id=id;
        this.disponivel = true;
    }

    public int getId() {
        return id;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
