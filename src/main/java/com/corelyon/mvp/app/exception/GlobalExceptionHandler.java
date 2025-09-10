package com.corelyon.mvp.app.exception;

import com.corelyon.mvp.app.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        List<String> details = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            details.add(fieldName + ": " + errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Erro de validação nos dados fornecidos",
            "VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI(),
            details
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        List<String> details = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            details.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Erro de validação de restrições",
            "CONSTRAINT_VIOLATION",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI(),
            details
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "ILLEGAL_ARGUMENT",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "RESOURCE_NOT_FOUND",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        String message = "Erro de integridade de dados";
        List<String> details = new ArrayList<>();
        
        String errorMessage = ex.getMessage();
        if (errorMessage != null) {
            if (errorMessage.contains("duplicate key") || errorMessage.contains("UNIQUE constraint")) {
                if (errorMessage.contains("cpf") || errorMessage.contains("CPF")) {
                    message = "CPF já cadastrado no sistema";
                    details.add("O CPF fornecido já está sendo utilizado por outro funcionário");
                } else if (errorMessage.contains("email") || errorMessage.contains("EMAIL")) {
                    message = "Email já cadastrado no sistema";
                    details.add("O email fornecido já está sendo utilizado por outro funcionário");
                } else {
                    message = "Registro duplicado encontrado";
                    details.add("Já existe um registro com os mesmos dados únicos");
                }
            } else if (errorMessage.contains("foreign key constraint")) {
                message = "Violação de chave estrangeira";
                details.add("O registro está referenciando um recurso que não existe");
            } else if (errorMessage.contains("not-null constraint")) {
                message = "Campo obrigatório não fornecido";
                details.add("Todos os campos obrigatórios devem ser preenchidos");
            }
        }
        
        if (details.isEmpty()) {
            details.add(ex.getMessage());
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            message,
            "DATA_INTEGRITY_VIOLATION",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI(),
            details
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Formato de dados inválido no corpo da requisição",
            "INVALID_REQUEST_BODY",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        String message = String.format("Parâmetro '%s' deve ser do tipo %s", 
            ex.getName(), ex.getRequiredType().getSimpleName());
        
        ErrorResponse errorResponse = new ErrorResponse(
            message,
            "TYPE_MISMATCH",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        String message = String.format("Parâmetro obrigatório '%s' não foi fornecido", ex.getParameterName());
        
        ErrorResponse errorResponse = new ErrorResponse(
            message,
            "MISSING_PARAMETER",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
        String message = String.format("Método '%s' não é suportado para esta URL", ex.getMethod());
        
        ErrorResponse errorResponse = new ErrorResponse(
            message,
            "METHOD_NOT_SUPPORTED",
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Endpoint não encontrado",
            "NOT_FOUND",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage() != null ? ex.getMessage() : "Erro interno do servidor",
            "RUNTIME_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Erro interno do servidor",
            "INTERNAL_SERVER_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
