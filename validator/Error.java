package by.javaguru.validator;

import lombok.Value;

@Value(staticConstructor = "of")
public class Error {
    String code, message;
}
