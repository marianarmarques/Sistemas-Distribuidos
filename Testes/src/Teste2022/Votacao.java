package Teste2022;

import Teste2022.Exceptions.CabineNaoExistenteException;
import Teste2022.Exceptions.EleitorNaoRegistadoException;
import Teste2022.Interfaces.IVotacao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Votacao implements IVotacao {
    private HashMap<Integer, Eleitor> eleitores;
    private HashMap<Integer, Cabine> cabines;

    private HashMap<Integer, Voto> votos;
    private ReentrantLock lock = new ReentrantLock();
    private Condition cabineNaoDisponivel = lock.newCondition();

    public Votacao() {
        this.eleitores = new HashMap<>();
        this.cabines = new HashMap<>();
        this.votos = new HashMap<>();

        //pré-população
        this.eleitores.put(1, new Eleitor(1));
        this.eleitores.put(2, new Eleitor(2));
        this.eleitores.put(3, new Eleitor(3));
        this.eleitores.put(4, new Eleitor(4));
        this.eleitores.put(5, new Eleitor(5));

        this.cabines.put(1, new Cabine(1));
        this.cabines.put(2, new Cabine(2));

        this.votos.put(1, new Voto(1));
        this.votos.put(2, new Voto(2));
    }

    @Override
    public boolean verifica(int identidade) throws EleitorNaoRegistadoException {
        try {
            lock.lock();

            if(!this.eleitores.containsKey(identidade)) throw new EleitorNaoRegistadoException("Eleitor não registado.");

            if(!this.eleitores.get(identidade).isJaVotou()) {
                this.eleitores.get(identidade).setJaVotou(true);
                return true;
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public int esperaPorCabine() throws InterruptedException {
        try {
            this.lock.lock();

            Cabine c = verificaDisponibilidade();
            if(c!=null) {
                c.setDisponivel(false);
                return c.getId();
            }

            while(c==null) {
                this.cabineNaoDisponivel.await();
                c = verificaDisponibilidade();
            }
            return c.getId();
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public void vota(int escolha) {
        try {
            this.lock.lock();

            for(Map.Entry<Integer, Voto> votos : this.votos.entrySet()) {
                if(votos.getKey().equals(escolha)) votos.getValue().incVoto();
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public void desocupaCabine(int i) throws CabineNaoExistenteException {
        try {
            this.lock.lock();

            if(!this.cabines.containsKey(i)) throw new CabineNaoExistenteException("Cabine não existente.");

            this.cabines.get(i).setDisponivel(true);
            this.cabineNaoDisponivel.signalAll();
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public int vencedor() {
        int max = -1;
        Voto v = null;


        for(Map.Entry<Integer, Voto> votos: this.votos.entrySet()) {
            if(votos.getValue().getContador()>max) {
                max = votos.getValue().getContador();
                v = votos.getValue();
            }
        }
        return v!=null ? v.getEscolha() : -1;
    }

    public Cabine verificaDisponibilidade() {
        for(Map.Entry<Integer, Cabine> cabines : this.cabines.entrySet()) {
            if(cabines.getValue().isDisponivel()) return cabines.getValue();
        }
        return null;
    }
}
