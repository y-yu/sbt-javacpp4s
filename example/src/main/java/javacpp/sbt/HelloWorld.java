package javacpp.sbt;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

@Platform(include = {"HelloWorld.hpp"}, link = "HelloWorld")
public class HelloWorld extends Pointer {
    static {
        Loader.load();
    }

    public HelloWorld() {
        allocate();
    }

    public native void allocate();

    public native int printN(int n);
}