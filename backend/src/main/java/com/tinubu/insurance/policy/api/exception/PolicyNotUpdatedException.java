package com.tinubu.insurance.policy.api.exception;


public class PolicyNotUpdatedException extends RuntimeException {

    public PolicyNotUpdatedException() {
        super("Erreur lors de la mise a jour de la police d'assurance ");
    }
}
