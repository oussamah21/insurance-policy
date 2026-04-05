package com.tinubu.insurance.policy.api.exception;


public class PolicyNotCreatedException extends RuntimeException {

    public PolicyNotCreatedException() {
        super("Erreur lors de la creation de la police d'assurance ");
    }
}
