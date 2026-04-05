package com.tinubu.insurance.policy.api.exception;


public class PolicyNotFoundException extends RuntimeException {

    public PolicyNotFoundException(Integer id) {
        super("Police d'assurance introuvable avec l'identifiant : " + id);
    }
}
