package Exame2016;

import Exame2016.Interfaces.IControlador;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Controlador implements IControlador {

    private HashMap<Integer, Questao> questoes;
    private ReentrantLock lockControlador;
    private Condition naoDisponivel;

    public Controlador() {
        this.questoes = new HashMap<>();
        this.lockControlador = new ReentrantLock();
        this.naoDisponivel = lockControlador.newCondition();

        //pré população
        adiciona("Quantos continentes existem?", "6");
        adiciona("Quantos elementos químicos a tabela periódica possui?", "118");
        adiciona("A Assembleia da República é composta por quantos deputados?", "230");
        adiciona("Em que país nasce o rio Tejo?", "Espanha");
    }

    @Override
    public void adiciona(String pergunta, String resposta) {
        try {
            this.lockControlador.lock();

            Questao q = new Questao(pergunta, resposta);
            questoes.putIfAbsent(q.id(), q);
            this.naoDisponivel.signalAll();
        }
        finally {
            this.lockControlador.unlock();
        }
    }

    public Questao verificaDisponibilidade(int id) {
        Questao q = null;

        for (Map.Entry<Integer, Questao> questoes : this.questoes.entrySet()) {
            if (questoes.getKey() > id && !questoes.getValue().isRespondida() && questoes.getValue().getTentativas() <= 10) {
                q = questoes.getValue();
            }
        }
        return q;
    }
    @Override
    public Questao obtem(int id) throws InterruptedException {
        try {
            this.lockControlador.lock();
            Questao q = verificaDisponibilidade(id);

            if(q!=null) return verificaDisponibilidade(id);

            while (q==null) {
                this.naoDisponivel.await();
                q = verificaDisponibilidade(id);
            }
            return q;
        }
        finally {
            this.lockControlador.unlock();
        }
    }
}
