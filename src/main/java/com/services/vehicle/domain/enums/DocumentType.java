package com.services.vehicle.domain.enums;

public enum DocumentType {
    SOAT(true, true),
    TECNO(true, true),
    TARJETA_PROPIEDAD(false, true),
    CIRCULACION_PERMITIDA(true, false),
    SEGURO(true, false),
    OTRO(false, false);

    private final boolean expirable;
    private final boolean required;

    DocumentType(boolean expirable, boolean required) {
        this.expirable = expirable;
        this.required = required;
    }

    public boolean isExpirable() {
        return expirable;
    }

    public boolean isRequired() {
        return required;
    }
}
