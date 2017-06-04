package eu.michaeln.helsinkieventbrowser;

public interface ErrorNotifier<T> {
    void notify(T warning);
}
