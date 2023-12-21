package by.javaguru.mapper;

public interface Mapper<T, F> {
    T mapFrom(F f);
}
