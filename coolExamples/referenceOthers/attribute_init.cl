class Main inherits IO {
	a: String <- "Hello, World!";
	b: String <- "Goodbye, World!";
	main(): SELF_TYPE {
		{
			out_string(a);
			out_string(b);
		}
  	};
};
