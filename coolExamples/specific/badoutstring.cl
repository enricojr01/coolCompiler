class Main {
    print_self() : SELF_TYPE {
        {
            out_string("\\");
            arg.print_self();
            out_string(".");
            body.print_self();
            self;
        }
    };
};