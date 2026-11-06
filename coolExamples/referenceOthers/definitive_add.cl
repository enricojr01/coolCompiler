class Main inherits IO {
	a: Int <- 29;
	b: Int <- 85;
	c: Int;

	main(): Int {
		addByFunction(a, b, c)
	};

	main2(): Int {
		addByFunction(3, 4, 5)
	};	

	addByFunction(x: Int, y: Int, z: Int): Int {
		x + y + z
	};

	localIntsOnly(): Int {
		12 + 32	
	};

	localIntsOnlyLongExpression(): Int {
		1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12 + 13 + 14 + 15
	};

	attribIntsOnly(): Int {
		a + b
	};

	insideLet(): Int {
		(let x: Int <- 2, y: Int <- 6, z: Int in (
				c <- x + y	
			)
		)
	};

	mixedLocalInt(): Int {
		c <- 99 + 75
	};

	mixedAttribInts(): Int {
		c <- a + b + 55
	};

	mixedInsideLet(): Int {
		(let r: Int in (
			r <- a + b
		))
	};
};
