package com.reciproci.loyalty.tier.exceptions;
public class TierValidationException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public TierValidationException(String message) {
       super(message);
   }
}

