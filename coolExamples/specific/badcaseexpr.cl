class Foo {
    a : Razz <- case self of
        n : Razz => (new Bar);
        n : Foo => (new Razz);
        n : Bar => n;
    esac;
};
