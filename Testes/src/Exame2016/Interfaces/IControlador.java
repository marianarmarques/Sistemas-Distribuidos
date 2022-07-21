package Exame2016.Interfaces;

import Exame2016.Questao;

public interface IControlador {
    void adiciona(String pergunta, String resposta);
    Questao obtem(int id) throws InterruptedException;
}
