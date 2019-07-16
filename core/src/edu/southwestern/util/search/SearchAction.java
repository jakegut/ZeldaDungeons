package edu.southwestern.util.search;

public interface SearchAction {
    // Must implement equals
    boolean equals(Object other);

    void update();
}
