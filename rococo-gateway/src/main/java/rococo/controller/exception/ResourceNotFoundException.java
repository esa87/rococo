package rococo.controller.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(UUID artistId) {
        super("Resource not found with ID: " + artistId);
    }
}
