package eu.michaeln.helsinkieventbrowser.api;

@FunctionalInterface
public interface CheckedConsumer<TValue, TException extends Exception> {
    void accept(TValue value) throws TException;
}
