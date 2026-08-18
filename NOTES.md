# ABOUT

Just a collection of random stuff I learn while working on this project because
my dumb ass will forget this stuff at some point and I'd like to not have to
fumble around in the dark if I ever take a break and come back.

# Operator Precedence

In ANTLR4 operator precedence is defined by the order of alternative subrules
within a rule.

I noticed that re-organizing the rules to match the precedence specified in the
COOL manual (such as with the <-, @, and . operators), fixed some of the issues
I had getting `case` statements to parse correctly.

Not sure if this was a fluke or not.

# SEMICOLON Token + closing curly brackets

ANTLR seems to generate internal tokens based on the literals present in the
rules.

Given the rule:

```
coolClass:   CLASS TYPE (INHERITS TYPE)? '{' feature* '};';
```

There appears to be a token designated `T__2` in the globals when running in the
debugger and this is causing certain expressions not to parse correctly because
what should be separate `}` and `;` end up being taken as a single token.

The solution seems to be changing the rule so that the closing bracket and the
semicolon are two separate characters:

```
coolClass:   CLASS TYPE (INHERITS TYPE)? '{' feature* '}'';';
```

# Switching to IntelliJ IDEA

So VSCodium with the Maven and Java plugins just would not install JUnit
properly, so I got fed up and switched to IntelliJ IDEA. I noticed a couple of
things:

I ticked a setting in the VSCode Java extension to dump .classpath and
.factorypath into the project root not knowing that this would cause the
whole folder to be discovered as an Eclipse project. I already have a `pom.xml`
file and am set on Maven, so this was a surprise to me.

I also had to mark the `./target/generated-sources/antlr4` folder as a generated
sources root to get the IDE's auto- import functionality to work correctly.
It would otherwise default to using the fully-qualified class name instead
of putting an import at the top of the file.

It's also worth noting that you shouldn't install ALL the ANTLR4 plugins (my
mistake) because they aren't compatible with one another and will cause the
IDE to crash on startup.

The ANTLR4 plugin has the ability to generate the parser and lexer code, but I
am going to keep using the ANTLR Maven Plugin, since I already have it working
and it's doing just fine.
