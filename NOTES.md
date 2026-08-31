# ABOUT

Just a collection of random stuff I learn while working on this project because
my dumb ass will forget this stuff at some point, and I'd like to not have to
fumble around in the dark if I ever take a break and come back. Latest notes are
at the top.

### Linking the chain

With symbols right now I have it set up such that only the nodes that actually 
introduce symbols will have a symbol table, and each one is chained to the one
higher up in the program.

My concern is that this won't work for deeply nested expressions like `let` and
`case`.

Theoretically they can show up anywhere an expression is allowed, which is a lot
of places right now, and I'm not sure I completely capture that when recursing
deep into a tree.

### Type Environments

Capture type information about the FREE VARIABLES in the program. Only worry
about the FREE ones for now and we can use them to derive the types of expressions
later.

### Interface Consistency

So I realize I haven't been perfectly consistent with interfaces across the
various AST nodes and the supporting data structures like SymbolTable, etc.

I'd like to fix that at some point, so I'm going to keep a list:

The AST nodes will either use .getName() or .getIdentifier() to retrieve a
CoolIdentifier. I don't know which to standardize around honestly.

SymbolTable has a .hasSymbol() and a .getSymbol() method but only a 
.getParent() method and checks are done with `s.getParent() == null`, this 
strikes me as a bit inconsistent.

### AstVisitor

I realized at some point that it would be better to have a visitor specifically
for the AST, one that would stand alongside the Cool Visitor class provided by
ANTLR.

So I've rigged one up, and I am using it to generate the symbol tables. It 
basically modifies the AST in-place, which I'm sure won't come back to bite me
later.

I think this design is much cleaner and more in line with the idea that 
compilers are just tree-parsers.

Now back to type checking.

### Design Issues

The further along I get the more I question my earlier design choices. Since I'm
not under any time pressure I'd like to take some time to revisit the way I'm
handling the inheritance tree and symbol tables.

For one, I placed a "parent" field on the CoolClass, meant to hold a reference
to another CoolClass. I'm not currently using it, but I'm thinking that maybe
I should start.

Another thought I had was to maybe consider basing future work off of the
inheritance tree because my intuition tells me that it might be easier to work
with compared to the AST. The classes are already organized in the correct 
hierarchy making it easier to traverse properly. It's also a lot easier to 
traverse because it's a proper tree and not a loose collection of objects. 

If I go the second option, I would probably rig up another class that will
walk the tree and fill out symbol tables and such, because right now that
functionality is haphazardly placed into the AstBuilder, and I feel like that's
a bad idea, even if it does work.

By doing this, there will be a nice natural flow of data from one object to
another:

- input file is fed to the lexer,
- lexer output is converted to a token stream
- token stream fed to a parser
- parser output fed to AstBuilder
- AstBuilder output fed to ClassTreeBuilder, currently housed under the 
  SemanticAnalyzer class, but I'd like it to be standalone for consistency's
  sake.

And finally, ClassTreeBuilder outputs a ClassTree, which will be passed into 
future components for processing.

### Type Checking Notes
The notes call for 3 "type environments" that each store mappings from some
entity to their types, for the purposes of type inference.

O - maps object ids to types
M - maps methods to types
C - is the current class, for the purposes of the SELF_TYPE

Each type environment is passed down the tree, from parent to child. They are
like symbol tables but for types. I am not sure if I should be creating something
that stands alongside a symbol table, or modifying the symbol table to include
a type.

Seems like the big difference is that whereas the symbol table is used to 
determine scope, the type environment determines type. I guess in that sense 
it's probably OK that they're separate objects.

### Reusing the Visitor Interface 
So the AstBuilder class at this point does two things - it builds out the AST
and while traversing builds up the symbol tables at each scope.

Next up on the list of things to do is to start building out the type checking
mechanism and I see two ways forward - shove this functionality into the 
AstBuilder class somehow, or create another mechanism that goes around and
modifies the AST with type data.

The first one seems sensible but I'm against shoving too much functionality
into the AstBuilder, I'm pretty sure I need the symbol table complete to do 
type checking.

Second option is my preferred, but the visitor interface functions take in rule 
contexts and returns `CoolBaseNode`. I could always take CoolBaseNode and make it
a subclass of ANTLR's rule contexts but that seems like a bad hack. There's also
nothing stopping me from modifying the CoolBaseNode passed into each function and
returning it again, or even just returning null, but that also seems like a bad
hack.

I suppose I'll re-watch the sections on type checking to see if there's something
I missed.

### Do Let and Case Create Their Own Scopes? 
It kinda makes sense that they would. But I think I'm already in a position to
handle that since `CoolExpr` is the base class for all expressions including
`CoolLet` and `CoolCase`. 

### Symbol Tables pt. N
Thank goodness for Discord, I asked about building the symbol table in one of
the channels I'm in, and one of the guys there mentioned being able to use
the visitor pattern to get it done.

To quote, "compilers are basically tree traversal machines".

Look at how nicely this all falls into place:

I have a Visitor interface generated by ANTLR4.

I have an AstBuilder class that implements the visitor interface to generate
an AST for a program

Completely independently, I figured out that I should put symbol tables on the 
AST nodes.

But within the `expr` only `let` and `case` expressions introduce new symbols but
the rule is recursive so you basically need to walk the AST to build up the
symbol tables.

So the solution is to rig up the AstBuilder to also take care of the symbol 
tables!

To make it easier to fetch them from symbol tables I added one to the base
`CoolExpr`, with getter / setter and the plan is to just check at the top-level
`visitMethodDefinition()` call to see if there are any symbol tables in the
result of `visitExpression()` and just pull those up into the `CoolMethod`
symbol table. 

The end result is that `CoolProgram`, `CoolClass`, and `CoolMethod` each have
a symbol table that is also correctly scoped. I suppose that I would need a way
to resolve identifiers, but I don't think that'd be too hard to figure out.

(A stack might work - push the symbols onto a stack and then just iterate through
it or something).

### Rebuilding the Symbol Table Builder

Since I changed the inheritance graph to an actual tree (and renamed it to 
ClassTree) I now have to re-do the SymbolTable builder.

For one I need to decide if I want to stick to the original plan of "injecting"
the symbol tables into the AST, or if I want to build off my new class tree.

### Revisiting the Inheritance Graph

So the adjacency list ended up not working out due to me adding Symbol Tables
to some of the base classes.

In doing so, I inadvertently imposed the requirement that we be able to traverse
backwards through a graph from "leaf" to root. The initial implementation was
basically one-way, and worked fine initially, but I could not find a clean way
to perform the "reverse" operation - that is, given a node on the graph trace
a path back to the "root". The adjacency list was a 
`HashMap<CoolIdentifier, LinkedList<CoolClass>>` and the neighbors for any given
identifier were considered "equal" when there was, in fact, an actual hierarchy
that needed to be respected.

I ended up going back to my first design which was an N-tree. I guess I was so
caught up with solving cycle detection that it came at the expense of everything
else.

### Symbol Tables and Stacks

Worked on Symbol Tables today, the coursework said to use a stack, but I opted to
try a different method. I thought about it for a while and realized that I don't
need an explicit stack because the inheritance graph already lays out the
classes in the right order. 

So all that's left for me to do is to a. create a SymbolTable that can hold the
stuff, and then make sure all the relevant classes have their own SymbolTable.

But which classes are relevant? So looking through the Cool manual and my notes,

- Classes and their methods are global to the program. 
- Attributes are local to their class.
- Method parameters are local to the method.
- Identifiers declared in `let` and `case` expressions are local to the method
  they occur in.

So that gives me a grand total of 4 classes that I need to add Symbol Tables to:

- CoolProgram (global)
- CoolClass
- CoolFormal
- CoolMethod

The inheritance graph will act as a "stack" of sorts because any given class `N`
basically form a straight line from `Object` to `N`, as only single inheritance
is allowed.

```
Object: [IO, Int, String, Bool, A, B]
A: []
B: [C] 
C: [] 

So the chain for class B (and C) is:

Object -> B -> C;

And for A its:

Object -> A;
```

All I would need to do now is to add a method to the InheritanceGraph that
generates a linked list for any class `N` such that it contains all the 
`CoolClass` instances that form its inheritance chain. Later, when it's time
to evaluate expressions and such I can just follow the chain back to look up
identifiers.

### Weekly Check-in Aug 12 - Aug 19

- began construction of semantic analysis modules / components
- fixed numerous errors with parser / lexer. parser + lexer now halt on error
  instead of trying to recover.
- fixed annoying bug with string literals not being parsed correctly, causing
  entire portions of the program to be incorrect
- adjusted parser so that '};' is not consumed as a single token, but two
  separate ones.
- inheritance graph is now fully functional, with cycle detection mechanism in
  place.

### Cycle Detection

Turns out this is easier than I thought - given a valid adjacency list, simply
traverse depth-first, and keep track of all the nodes visited in some kind of
list. Check each node as you come across it, and if its present in the list you
have a cycle.

Self-cycles, i.e. `class A inherits A {};` can be checked when constructing
the graph, but will throw the same exception as the cycle detector.

### Inheritance Graph Design Notes

The first semantic analysis problem I decided to tackle was that of the
inheritance graph. Specifically I needed to verify that all the classes defined
in a COOL program follow the inheritance rules laid out in the manual:

- classes you're inheriting from must exist
- classes can only inherit from one above it in the class hierarchy
- inheritance cannot be cyclic
- only single inheritance is allowed
- the root of the inheritance graph is Object, and if no parent is specified in
  a class definition, then its parent is Object.
- you can inherit from built-in class IO, but not redefine it.
- you cannot inherit from, nor redefine, the built-in class String
- you cannot inherit from, nor redefine, the built-in class Int
- you cannot inherit from, nor redefine, the built-in class Bool

Given all these requirements it seemed like a directed graph, unweighted, was
the most appropriate data structure for the job. And the best way to implement
this directed graph was an adjacency list.

I didn't see it at first but the way to model a directed graph with an adjacency
list is to only put neighbors in one of the keys and not the other.

For example, given two class definitions:

```
class A {};
class B inherits A {};
```

The graph would be drawn out like so:

```
Object -> A -> B
```

And internally the hashmap would look something like:

```
Object: [A,]
A: [B,]
B: []
```

Note that while B is technically adjacent to A, it is not present in B's list.
This ensures that you can only travel one direction down the graph from any
given node.

### Syntax Errors vs Semantic Errors

I decided to start with the inheritance graph part of the semantic analysis
because it seemed like the easiest thing to do and I honestly don't know any
better at this point.

It's forced me to think hard about the kinds of errors that can occur during
compilation and how / where they should be handled.

Class identifiers being incorrect aren't semantic errors, they're syntax errors.

But classes inheriting from undefined classes or "invalid" classes* are
definitely semantic errors. Same with classes being defined twice.

At first blush they looked the same, but you've really got to look at the
problems to see the difference.

### Common Lexical Structures

Another nice section from the book `The Definitive ANTLR 4 Reference`, copied
here for posterity.

| Rule                    | Description                                                                                                                 |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| ('a'..'z' \| 'A'..'Z')+ | Match one or more upper / lowercase letters (for identifiers).                                                              |
| [a-zA-Z]+               | Same as above, but in a different notation.                                                                                 |
| ('0'..'9')+             | Matches one or more digits.                                                                                                 |
| [0-9]+                  | Same as above, but in a different notation.                                                                                 | 
| '"' .*? '"'             | For matching string literals - matches any character between two ".                                                         |
| '\\"' \| '\\\\'         | Matches a quote character, or two '\' characters. Used in conjunction with the above to match basically any string literal. |

### ANTLR Core Notation

I was looking for an ANTLR cheat sheet but couldn't find one, so I'm pulling
this from the book `The Definitive ANTLR 4 Reference`.

| Syntax                | Description                                 |
|-----------------------|---------------------------------------------|
| x                     | Match token, rule reference, or subrule `x` |
| x y ... z             | Match a sequence of rule elements.          |
| (...\|...\|...)       | Subrule with multiple alternatives.         |
| x?                    | Match `x` or skip it.                       |
| x*                    | Match x zero or more times.                 |
| x+                    | Match x one or more times.                  |
| r: ... ;              | Define rule `r`                             |
| r: ... \| ... \| ...; | Define rule `r` with multiple alternatives. |

### Switching to IntelliJ IDEA

So VSCodium with the Maven and Java plugins just would not install JUnit
properly, so I got fed up and switched to IntelliJ IDEA. I noticed a couple of
things:

I ticked a setting in the VSCode Java extension to dump .classpath and
.factorypath into the project root not knowing that this would cause the
whole folder to be discovered as an Eclipse project. I already have a `pom.xml`
file and am set on Maven, so this was a surprise to me.

I also had to mark the `./target/generated-sources/antlr4` folder as a generated
sources root to get the IDE's auto-import functionality to work correctly.
It would otherwise default to using the fully-qualified class name instead
of putting an import at the top of the file.

It's also worth noting that you shouldn't install ALL the ANTLR4 plugins (my
mistake) because they aren't compatible with one another and will cause the
IDE to crash on startup.

The ANTLR4 plugin has the ability to generate the parser and lexer code, but I
am going to keep using the ANTLR Maven Plugin, since I already have it working
and it's doing just fine.

### SEMICOLON Token + closing curly brackets

ANTLR seems to generate internal tokens based on the literals present in the
rules.

Given the rule:

```
coolClass:   CLASS TYPE (INHERITS TYPE)? '{' feature* '};';
```

There appears to be a token designated `T__2` in the globals, whose contents are
`};` when running in the debugger and this is causing certain expressions not to
parse correctly because what should be separate `}` and `;` end up being taken
as a single token.

The solution seems to be changing the rule so that the closing bracket and the
semicolon are two separate characters:

```
coolClass:   CLASS TYPE (INHERITS TYPE)? '{' feature* '}'';';
```

### Operator Precedence

In ANTLR4 operator precedence is defined by the order of alternative subrules
within a rule. This means that any language that explicitly defines operator
precedence can be modelled simply by ordering the subrules correctly

I noticed that re-organizing the rules to match the precedence specified in the
COOL manual (such as with the <-, @, and . operators), fixed some of the issues
I was having getting `case` statements to parse correctly.

Not sure if this was a fluke or not.
