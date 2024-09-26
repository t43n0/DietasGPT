package com.dam.dietasgpt;

import java.util.List;

public class ContextoRequest {
    private List<Message> contexto;

    public ContextoRequest(List<Message> contexto) {
        this.contexto = contexto;
    }

    public List<Message> getContexto() {
        return contexto;
    }

    public void setContexto(List<Message> contexto) {
        this.contexto = contexto;
    }
}
