class OtherAdd {
	x: Int <- 54;
	y: Int <- 29;
	
	add(): Int {
		x + y
	};
};

class Main {
	a: Int <- 14;
	b: Int <- 15;
	c: OtherAdd <- (new OtherAdd);
	
	main(): SELF_TYPE {
		{
			a + b;
			c.add();
			self;
		}
	};
};

