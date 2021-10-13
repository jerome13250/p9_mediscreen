package com.mediscreen.mdiabeteassess.exception;

import com.mediscreen.common.exception.BadRequestException;
import com.mediscreen.common.exception.NotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * This class handles Feign error propagation between Microservices.
 * @author jerome
 *
 */
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String invoqueur, Response reponse) {

        if(reponse.status() == 400 ) {
            return new BadRequestException(
                    "Requête incorrecte "
            );
        }

        else if (reponse.status() == 404 ) {
        	
            return new NotFoundException(
                    "Resource non trouvée "
            );
        }

        return defaultErrorDecoder.decode(invoqueur, reponse);
    }

}
