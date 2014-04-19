package com.github.underscore;

public abstract class Template<T> implements Function1<T, String> {
    private final String template;
    public Template(final String template) {
        this.template = template;
    }

}
