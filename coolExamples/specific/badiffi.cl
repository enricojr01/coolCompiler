class A2I {
    (*
    i2c is the inverse of c2i.
    *)
    i2c(i : Int) : String {
        { abort(); ""; }  -- the "" is needed to satisfy the typchecker
    };
};
