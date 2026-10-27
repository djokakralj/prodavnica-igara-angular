package com.djole.prodavnica.dto;

import java.io.Serializable;

public class StatusDTO implements DomainDTO, Serializable {

    private Long id;
    private String status;

    public StatusDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
