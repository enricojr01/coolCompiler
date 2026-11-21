class Temp {
	a: Int <- 42;
	get_a(): Int {
		a
	};
};

class Main {
	a: Int <- 3;
	b: Int <- 4;
	c: Int <- 6;

	main(): Int {
		(let x: Int in (
			x <- 5 + 5
		))
	};

	let_test(): Int {
		(let x: Int, y: Int in (
			x <- y + b
		))
	};
	
	let_test_inst(): Int {
		(let x: Temp <- new Temp in (
			a <- x.get_a() + 27
		))
	};

	let_test_deep(): Int {
		(let d: Int in (
			let e: Int in (
				let f: Int in (
					let g: Int in (
						let h: Int in (
							let i: Int in (
								let j: Int in (
									let k: Int in (
										5				
		)))))))))
	};
	
};
