package Teste2016.Interfaces;

import Teste2016.Controlador;
import Teste2016.Questao;

public interface IControlador {
    void adiciona(String pergunta, String resposta);
    Questao obtem(int id) throws InterruptedException;
}
