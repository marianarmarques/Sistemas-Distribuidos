package Teste2022.Interfaces;

import Teste2022.Exceptions.CabineNaoExistenteException;
import Teste2022.Exceptions.EleitorNaoRegistadoException;

public interface IVotacao {
    boolean verifica(int identidade) throws EleitorNaoRegistadoException;
    int esperaPorCabine() throws InterruptedException;
    void vota(int escolha);
    void desocupaCabine(int i) throws CabineNaoExistenteException;
    int vencedor();
}
