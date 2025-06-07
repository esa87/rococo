package rococo.service.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import rococo.controller.exception.ResourceNotFoundException;
import rococo.controller.exception.ValidationException;

import java.util.UUID;
import java.util.concurrent.CompletionException;

public class GrpcExceptionUtil {


    public static RuntimeException convertGrpcException(CompletionException e) {
        return convertGrpcException(e, null);
    }

    public static RuntimeException convertGrpcException(CompletionException e, UUID entityId) {
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof StatusRuntimeException grpcEx) {
                return convertStatusException(grpcEx, entityId);
            }
            cause = cause.getCause();
        }
        return new RuntimeException("Unexpected error", e.getCause());
    }

    private static RuntimeException convertStatusException(StatusRuntimeException grpcEx, UUID entityId) {
        Status.Code code = grpcEx.getStatus().getCode();
        String description = grpcEx.getStatus().getDescription();

        return switch (code) {
            case NOT_FOUND -> new ResourceNotFoundException(entityId);
            case INVALID_ARGUMENT -> new ValidationException(description != null ? description : "Invalid request");
            case UNAVAILABLE -> new RuntimeException("Service unavailable");
            default -> new RuntimeException("Service error: " + (description != null ? description : code.toString()));
        };
    }
}
