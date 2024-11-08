package com.carlosdev.doctohtml.converter;

import java.time.LocalDate;

public class AtoLegislativoDTO {

    private String epigrafe;
    private String ementa;
    private String localPublicacao;
    private LocalDate dataPublicacao;
    private String categoria;
    private String subcategoria;

    public String getEpigrafe() {
        return epigrafe;
    }

    public void setEpigrafe(String epigrafe) {
        this.epigrafe = epigrafe;
    }

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    public String getLocalPublicacao() {
        return localPublicacao;
    }

    public void setLocalPublicacao(String localPublicacao) {
        this.localPublicacao = localPublicacao;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }
}
