class Sample {
    foo : Int;
    main(): Object {
        case foo of
            b : String => {
                out_string("test");
                abort();
                0;
            };
        esac
    };
};
