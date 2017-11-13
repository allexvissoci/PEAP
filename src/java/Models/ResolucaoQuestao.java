/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author allex
 */
public class ResolucaoQuestao {
    private int id;
    private Questao questao;
    private Alternativa alternativaSelecionada;
    private Alternativa alternativaCorreta;
    private ResolucaoProva resolucaoProva;
    private Boolean anulada;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public Alternativa getAlternativaSelecionada() {
        return alternativaSelecionada;
    }

    public void setAlternativaSelecionada(Alternativa alternativaSelecionada) {
        this.alternativaSelecionada = alternativaSelecionada;
    }

    public Alternativa getAlternativaCorreta() {
        return alternativaCorreta;
    }

    public void setAlternativaCorreta(Alternativa alternativaCorreta) {
        this.alternativaCorreta = alternativaCorreta;
    }

    public ResolucaoProva getResolucaoProva() {
        return resolucaoProva;
    }

    public void setResolucaoProva(ResolucaoProva resolucaoProva) {
        this.resolucaoProva = resolucaoProva;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }
    
}
