package Exame2016;

import Exame2016.Interfaces.IQuestao;

public class Questao implements IQuestao {

    private static int id = 0;
    private boolean respondida;
    private int tentativas;
    private String pergunta;
    private String resposta;
    public Questao(String pergunta, String resposta) {
        incId();
        this.respondida = false;
        this.tentativas = 0;
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    @Override
    public String responde(String resposta) {
        if (!this.respondida) {
            if (this.resposta.equals(resposta)) {
                this.respondida = true;
                return "C";
            } else {
                this.tentativas++;
                return "E";
            }
        } else {
            return "R";
        }
    }

    public String charToString(String resposta) {
        String r = null;
        switch (this.responde(resposta)) {
            case "R":
                r = "Respondida";
                break;
            case "C":
                r = "Certa";
                break;
            case "E":
                r = "Errada";
                break;
        }
        return r;
    }

    public int incId() {
        return id++;
    }

    @Override
    public int id() {
        return id;
    }

    public boolean isRespondida() {
        return respondida;
    }

    public int getTentativas() {
        return tentativas;
    }

    public String getPergunta() {
        return pergunta;
    }

    public String getResposta() {
        return resposta;
    }

}
