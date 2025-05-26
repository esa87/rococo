package rococo.config;


import com.google.protobuf.ServiceException;
import io.grpc.Status;
import jakarta.persistence.EntityNotFoundException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;


@GrpcAdvice
public class GrpcExceptionHandlerMy {

    @GrpcExceptionHandler(ServiceException.class)
    public Status handleServiceException(ServiceException e) {
        return Status.INTERNAL.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(EntityNotFoundException.class)
    public Status handleNotFound(EntityNotFoundException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status handleBadRequest(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }
}
