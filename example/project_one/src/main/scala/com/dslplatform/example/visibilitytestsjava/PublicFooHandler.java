package com.dslplatform.example.visibilitytestsjava;

import com.dslplatform.example.Foo;

import scala.Unit;

public class PublicFooHandler implements Foo<String> {
    @Override
    public void handle(final String t) { }
}
