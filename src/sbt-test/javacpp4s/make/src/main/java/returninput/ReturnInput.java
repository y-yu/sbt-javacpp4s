package returninput;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

@Platform(include = {"ReturnInput.hpp"}, link = "ReturnInput")
public class ReturnInput extends Pointer {
    static {
        Loader.load();
    }

    public ReturnInput() {
        allocate();
    }

    public native void allocate();

    public native int id(int n);
}
