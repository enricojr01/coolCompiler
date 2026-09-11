class Term {};
class LambdaListRef {};
class VarList {};
class Lambda {};

class Main inherits Term {
  gen_code(e : Expr) : SELF_TYPE {
    let cl : LambdaListRef <- (new LambdaListRef).reset() in
      {
	while (not (cl.isNil())) loop
	  let e : VarList <- cl.headE(),
	      c : Lambda <- cl.headC(),
	      n : Int <- cl.headN() in
	    {
	      cl.removeHead();
	      c.gen_closure_code(n, e, cl);
	    }
	pool;
	out_string("\n------------------cut here------------------\n");
      }
  };
};
