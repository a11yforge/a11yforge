package at.a11yforge.api.exception;

import at.a11yforge.api.fixproposal.FixProposalNotFoundException;
import at.a11yforge.api.page.PageNotFoundException;
import at.a11yforge.api.project.DuplicateProjectNameException;
import at.a11yforge.api.project.ProjectNotFoundException;
import at.a11yforge.api.scan.ScanNotFoundException;
import at.a11yforge.api.user.InvalidCredentialsException;
import at.a11yforge.api.user.UserAlreadyExistsException;
import at.a11yforge.api.user.UserNotFoundException;
import at.a11yforge.api.violation.ViolationNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import at.a11yforge.api.user.PasswordMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // HTTP 404 Not Found
  @ExceptionHandler(ProjectNotFoundException.class)
  public ProblemDetail handleProjectNotFound(ProjectNotFoundException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problem.setTitle("Project not found");
    return problem;
  }

  // HTTP 404 Not Found
  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problem.setTitle("User not found");
    return problem;
  }

  // HTTP 404 Not Found
  @ExceptionHandler(PageNotFoundException.class)
  public ProblemDetail handlePageNotFound(PageNotFoundException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problem.setTitle("Page not found");
    return problem;
  }

  // HTTP 404 Not Found
  @ExceptionHandler(ViolationNotFoundException.class)
  public ProblemDetail handleViolationNotFound(ViolationNotFoundException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problem.setTitle("Violation not found");
    return problem;
  }

  // HTTP 404 Not Found
  @ExceptionHandler(FixProposalNotFoundException.class)
  public ProblemDetail handleFixProposalNotFound(FixProposalNotFoundException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problem.setTitle("FixProposal not found");
    return problem;
  }

  // HTTP 409 Conflict
  @ExceptionHandler(DuplicateProjectNameException.class)
  public ProblemDetail handleDuplicateProjectName(DuplicateProjectNameException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    problem.setTitle("Duplicate project name");
    return problem;
  }

  // HTTP 400 Bad Request
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
    problem.setTitle("Validation error");

    Map<String, String> fieldErrors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
    problem.setProperty("fieldErrors", fieldErrors);

    return problem;
  }

  // HTTP 409 Conflict
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    problem.setTitle("User already exists");
    return problem;
  }

  // HTTP 401 Unauthorized
  @ExceptionHandler(InvalidCredentialsException.class)
  public ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex) {
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    problem.setTitle("Invalid credentials");
    return problem;
  }

  // HTTP 400 Bad Request
  @ExceptionHandler(PasswordMismatchException.class)
  public ProblemDetail handlePasswordMismatch(PasswordMismatchException ex) {
    ProblemDetail problem =
            ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problem.setTitle("Password mismatch");
    return problem;
  }

  // HTTP 500 Internal Server Error
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGeneric(Exception ex) {
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
  }

  @ExceptionHandler(ScanNotFoundException.class)
  public ProblemDetail handleScanNotFound(ScanNotFoundException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problem.setTitle("Scan not found");
    return problem;
  }
}
