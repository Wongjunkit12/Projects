{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
module LambdaParser where

import Parser
import Data.Lambda
import Data.Builder
import Data.Char (isDigit, isLower)

-- You can add more imports if you need them

-- Remember that you can (and should) define your own functions, types, and
-- parser combinators. Each of the implementations for the functions below
-- should be fairly short and concise.


{-|
    Part 1
-}

-- | Exercise 1

-- BNF GRAMMAR
-- <lambdaP> ::= <longLambda>
--            |  <shortLambda>

-- <longLambda> ::= <repeatedLong>
--               |  <longLambdaBrackets>
-- <repeatedLong> ::= <longLambdaBrackets> <longLambda>

-- <longLambdaBrackets> ::= "(" <longLambdaExpr> ")"

-- <longLambdaExpr> ::= <lambdaSymbol> <variables> <dot> <repeatVariables>
--                   |  <lambdaSymbol> <variables> <dot> <bracket>
--                   |  <lambdaSymbol> <variables> <dot> <longLambda>

-- <shortLambda> ::= <shortLamdaExpr>
--                |  <shortLambdaBrackets>
--                |  <repeatedShortExpr>
--                |  <repeatedShortBrac>

-- <repeatedShortExpr> ::= <shortLamdaExpr> <shortLambda>

-- <repeatedShortBrac> ::= <shortLambdaBrackets> <shortLambda>

-- <shortLambdaBrackets> ::= "(" <shortLamdaExpr> ")"

-- <shortLamdaExpr> ::= <lambdaSymbol> <repeatVariables> <dot> <repeatVariables>
--                   |  <lambdaSymbol> <repeatVariables> <dot> <bracket>

-- <bracket> ::= <repeatedVarBracketVar>
--            |  <repeatedBracketsVariable>
--            |  <varBracketVar>
--            |  <bracketsVariable> 
--            |  <repeatVariables>

-- <repeatedVarBracketVar> ::= <varBracketVar> <bracket>

-- <varBracketVar> ::= <repeatVariables> <bracketsVariable>

-- <repeatedBracketsVariable> ::= <bracketsVariable> <bracket>

-- <bracketsVariable> ::= "(" <repeatVariables> ")"

-- <lambdaSymbol> ::= "λ"

-- <variables> ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i"
--              |  "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r"
--              |  "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"

-- <repeatVariables> ::= <variables> 
--                    |  <variables> <repeatVariables>

-- <dot> ::= "."


-- The Long Lambda parser. WIll either called longLambdaBrackets for only (λx.x) or repeatedLong in case of trailing lambda
-- expresions, eg: (λx.x)(λx.x)
longLambda :: Parser Builder
longLambda = do
  _ <- spaces
  repeatedLong ||| longLambdaBrackets

-- In case of any trailing lambda expression, this parser will parse for it by calling itself recursively and parsing each
-- lambda expression within the brackets.
-- Will apply all the lambda expressions together with the `ap` operator.
repeatedLong :: Parser Builder
repeatedLong = do
  bracketExpr <- longLambdaBrackets
  recurseLam <- longLambda
  pure $ bracketExpr `ap` recurseLam

-- For lambda expressions wrapped in brackets. Eg: (λx.xx).
-- Will parse the opening bracket before parsing the lambda expression inside via calling longLambdaExpr. 
-- Parse the closing bracket. If valid, return the Builder.
longLambdaBrackets :: Parser Builder
longLambdaBrackets = do
  _ <- is '('
  _ <- spaces
  expr <- longLambdaExpr
  _ <- spaces
  _ <- is ')'
  pure expr

-- The expression of the whole Lambda within the brackets. Eg: (λx.xx). 
-- Parse the single variable after the λ symbol.
-- The variable after the λ symbol is placed after 'lam'. 
-- Then call restVar to find the rest of the expression after the '.'.
-- The restVar has 4 alternate options, longLambdaBrackets means it's a recursive function for more lambda expressions.
-- bracket if the variables is x(xx) or (x) or (x)x, etc. repeatVariables is for xx and builderToParser is just for x.
-- This will all be combined together to form one Builder and returned.
longLambdaExpr :: Parser Builder
longLambdaExpr = do
  _ <- lambdaSymbol
  _ <- spaces
  arguement <- variables
  _ <- spaces
  _ <- dot
  _ <- spaces
  restVar <- longLambdaBrackets ||| bracket ||| repeatVariables
  pure $ lam arguement restVar

-- The short lambda Parser. Will call shortLamdaExpr for a basic shortLambda. Eg: λx.xx. 
-- repeatedShortExpr is for any  trailing short lambda expression. 
-- shortLambdaBrackets are for lambdas encased in brackets (λx.xx).
-- repeatedShortBrac is for trailing bracketed short lambda expressions. Eg: (λx.xx)(λy.yy).
shortLambda :: Parser Builder
shortLambda = do
  _ <- spaces
  repeatedShortBrac ||| shortLambdaBrackets ||| repeatedShortExpr ||| shortLamdaExpr

-- In case of any trailing lambda expression, this parser will parse for it by calling itself recursively and parsing each
-- lambda expression within the brackets. 
-- Will apply all the lambda expressions together with the `ap` operator.
repeatedShortExpr :: Parser Builder
repeatedShortExpr = do
  shortExpr <- shortLamdaExpr
  recurseShort <- shortLambda
  pure $ shortExpr `ap` recurseShort

-- The same as repeatedShortExpr but for lambda encased with brackets. Eg: (λx.xx)(λy.yy). 
-- Will apply both (λx.xx) `ap` (λy.yy) to get final expression.
repeatedShortBrac :: Parser Builder
repeatedShortBrac = do
  shortBrac <- shortLambdaBrackets
  recurseShort <- shortLambda
  pure $ shortBrac `ap` recurseShort

-- The expression of the whole Short Lambda . Parse one or more variables after the λ symbol. 
-- The repeatLambda is used to find 0 or more trailing variables. T
-- The variable after the λ symbol is placed after 'lam'. Then call restVar to find the rest of the expression after the '.'. 
-- The restVar has 3 alternate options. shortLambdaBrackets means it's a recursive function for more short lambda expressions.
-- bracket if the variables is x(xx) or (x) or (x)x, etc. repeatVariables is for xx.
-- This will all be combined together to form one Builder and returned.
shortLamdaExpr :: Parser Builder
shortLamdaExpr = do
  _ <- lambdaSymbol
  _ <- spaces
  var <- variables
  _ <- spaces
  arguements <- repeatLambda
  _ <- spaces
  _ <- dot
  _ <- spaces
  restVar <- shortLambdaBrackets ||| bracket ||| repeatVariables
  _ <- spaces
  -- lamBuilder is to construct each lam for the arguement. Eg: λxyz.x will be (lam 'x' (lam 'y' (lam 'x')))
  -- This is done by mapping and partially applying lam to each character in the string of arguements.
  -- Then, foldl is used to reduce the list of Builders into one Builder. Compose each lam to become lam 'x' . lan 'y' . lan 'z'
  -- return the (lam 'x' $ lam 'y' $ lam 'z' (term 'x')) by combining it.
  let
    lamBuilder = lam <$> arguements         -- fmap and partially apply lam to each character in a string.
    applyBuilders = foldl (.) (lam var) lamBuilder  -- Apply . to each lam 'x' and reduce it to one Builder.
  pure $ applyBuilders restVar

-- For short lambda expressions within brackets. Eg: (λx.xx).
-- Will parse the opening bracket before parsing the lambda expression inside via calling shortLamdaExpr. 
-- Parse the closing bracket. If valid, return the Builder.
shortLambdaBrackets :: Parser Builder
shortLambdaBrackets = do
  _ <- is '('
  _ <- spaces
  expr <- shortLamdaExpr
  _ <- spaces
  _ <- is ')'
  pure expr

-- For variables after the dot. If they have a bracket eg: (λx.(xx)), call breacketsVariable. If there are trailing brackets,
-- like (λx.(xx)(xx)) call repeatedBracketsVariable. If there are variables before the bracketm eg: (λx.x(xx)) call
-- varBracketVar. If there are repeating or alternating varBracketVar, eg:  eg: (λx.x(xx)x(xx)) call repeatedVarBracketVar.
bracket :: Parser Builder
bracket = repeatedVarBracketVar ||| repeatedBracketsVariable ||| varBracketVar ||| bracketsVariable ||| repeatVariables

-- Will parse for variable then brackets then the variables within the brackets. Then will call the bracket parser for
-- any repetitions of brckets. Will apply the varBracket to the rest of the Builder returned from the bracket call.
repeatedVarBracketVar :: Parser Builder
repeatedVarBracketVar = do
  varBracket <- varBracketVar
  recurseBracket <- bracket
  pure $ varBracket `ap` recurseBracket

-- If the variables after the dot has a bracket. Eg: (λx.x(xx)). Will parse the variables outside the brackets first and convert
-- it to a Builder. Then call the bracketsVariable to parse the variables within the bracket.
varBracketVar :: Parser Builder
varBracketVar = do
  var <- repeatVariables
  _ <- spaces
  ap var <$> bracketsVariable   -- Apply the repeated variables and the bracketed variables together.

-- Same thing as repeatedVarBracket except will repeat for brackets instead of varBrackets.
repeatedBracketsVariable :: Parser Builder
repeatedBracketsVariable = do
  bracketsVar <- bracketsVariable
  recurseBracket <- bracket
  pure $ bracketsVar `ap` recurseBracket

-- If there are brackets surrounding the variables. Parse for the opening and closing brackets and the call the repeatVariables
-- function to constuct the Builder.
bracketsVariable :: Parser Builder
bracketsVariable = do
  _ <- is '('
  _ <- spaces
  var <- repeatVariables
  _ <- spaces
  _ <- is ')'
  pure var

-- If the character is a lambda symbol.
lambdaSymbol :: Parser Char
lambdaSymbol = is 'λ'

-- If the variables are any of the below from [a-z]
variables :: Parser Char
variables = satisfy isLower

-- For repeating or trailing variables in the arguement. Will parse 0 or more variables.
repeatLambda :: Parser String
repeatLambda = repeatVariablesAux ||| pure ""

-- A function that will parse through a string of repeating variables, while ignoring spaces in between. Call itself recursively,
-- to continue parsing until no more variables is reached. Return emtpy string when reached. 
repeatVariablesAux :: Parser String
repeatVariablesAux = do
  var <- variables
  _ <- spaces
  restVariables <- repeatVariablesAux ||| pure ""
  pure $ var : restVariables

-- A function if there are multiple variables. Eg: (λx.xx). The function will parse for xx after the '.' and turn it into Builders.
-- First, use list1 variables which will parse one or more of the variables [a-z]. Then once all variables are validm
-- Then, map each variable in the string to 'term' to make it into a builder. termList = [term 'x', term 'x'].
-- Next, use foldl ap to reduce the list of Builders to one Builder. We will use the `ap` function to apply one builder to the
-- other. (ap (term 'x') (term 'x')) is returned following the example above.
repeatVariables :: Parser Builder
repeatVariables = do
  var <- variables
  _ <- spaces
  restVar <- repeatVariablesAux ||| pure ""
  -- Map 'term' to each variable to construct Builders. Then use foldl ap to partially apply each Builder with ap to reduce
  -- the list of Builders into one Builder.
  let
    termList = variableBuilder <$> restVar            -- fmap each character in a string to convert it to term 'x' builder
    applyBuilder = foldl ap (variableBuilder var) termList    -- Reduce the whole Builder into one using `ap`.
  pure applyBuilder

-- If the character is a fullstop or a dot.
dot :: Parser Char
dot = is '.'

-- Helper functions used in our Lambda Parsers.
-- Helper function used to convert our character into a Builder data type for ter,s. eg: x -> term 'x'.
variableBuilder :: Char -> Builder
variableBuilder = term

-- Helper function used to convert our character into a Builder data type for lambda. eg: x -> lam 'x'.
lambdaBuilder :: Char -> Builder -> Builder
lambdaBuilder = lam

-- Helper function used to convert one character into a Builder data type, eg: x -> term 'x' and then convert/wrap it
-- into a Parser.
builderToParser :: Parser Builder
builderToParser = do
  variableBuilder <$> variables   -- Will parse variables first and then apply the char with variableBuilder to construct the
                                  -- term 'variable'

-- | Parses a string representing a lambda calculus expression in long form
--
-- >>> parse longLambdaP "(λx.xx)"
-- Result >< \x.xx
--
-- >>> parse longLambdaP "(λx.(λy.xy(xx)))"
-- Result >< \xy.xy(xx)
--
-- >>> parse longLambdaP "(λx(λy.x))"
-- UnexpectedChar '('

-- Calls the longLambda parser to Parse valid Long Lambda Expressions. 
-- Will bind the Parser Builder returned from longLambda an anonymous function. 
-- The Builder is put through a build to turn it into a Lambda type before using pure to wrap it 
-- in a Parser to become Parser Lambda.
longLambdaP :: Parser Lambda
longLambdaP = longLambda >>= \lambda -> pure $ build lambda

-- | Parses a string representing a lambda calculus expression in short form
--
-- >>> parse shortLambdaP "λx.xx"
-- Result >< \x.xx
--
-- >>> parse shortLambdaP "λxy.xy(xx)"
-- Result >< \xy.xy(xx)
--
-- >>> parse shortLambdaP "λx.x(λy.yy)"
-- Result >< \x.x\y.yy
--
-- >>> parse shortLambdaP "(λx.x)(λy.yy)"
-- Result >< (\x.x)\y.yy
--
-- >>> parse shortLambdaP "λxyz"
-- UnexpectedEof

-- Calls the shortLambda parser to Parse valid short Lambda Expressions. 
-- shortLambdaP is also able to parse longLambda expressions.
-- Will bind the Parser Builder returned from shortLambda an anonymous function. 
-- The Builder is put through a build to turn it into a Lambda type before using pure to wrap it in a Parser 
-- to become a Parser Lambda.
shortLambdaP :: Parser Lambda
shortLambdaP = shortLambda >>= \lambda -> pure $ build lambda

-- | Parses a string representing a lambda calculus expression in short or long form
-- >>> parse lambdaP "λx.xx"
-- Result >< \x.xx
--
-- >>> parse lambdaP "(λx.xx)"
-- Result >< \x.xx
--
-- >>> parse lambdaP "λx..x"
-- UnexpectedChar '.'
--

-- Either calls longLambdaP or shortLambdaP to parse it.
-- If neither works, will return an error as input is not a valid Lambda expression.
lambdaP :: Parser Lambda
lambdaP = longLambdaP ||| shortLambdaP

{-|
    Part 2
-}

-- | Exercise 1

-- Parser for the string True. Will create the Builder λt_.t for True in their church encoding.
trueP :: Parser Builder
trueP = do
  _ <- spaces
  _ <- string "True"
  pure trues

-- Parser for the string False. Will create the Builder λ_f.f for False in their church encoding.
falseP :: Parser Builder
falseP = do
  _ <- spaces
  _ <- string "False"
  pure falses

-- Parser for the string if. Will parse for string "if", and either a logic expression or a True/False/bracket expression after it.
-- Then parses a "then" with another logic expression or a True/False/bracket expression before parsing an "else" 
-- Can also parse comparison or arithmetic expressions in the if statement.
-- followed by logic expression or a True/False/bracket expression.
-- Apply each expr together with the ifs to construct the output.
ifP :: Parser Builder
ifP = do
  _ <- spaces
  _ <- string "if"
  _ <- list1 space
  expr1 <- (compBrac ||| chainComparison) ||| logicExpr ||| booleanExpr
  _ <- list1 space
  _ <- string "then"
  _ <- list1 space
  expr2 <- (compBrac ||| chainComparison) ||| logicExpr ||| booleanExpr
  _ <- list1 space
  _ <- string "else"
  _ <- list1 space
  expr3 <- (compBrac ||| chainComparison) ||| logicExpr ||| booleanExpr
  pure $ ifs `ap` expr1 `ap` expr2 `ap` expr3

-- Parser for and operator. Creates the builder λxy. if x  y False for And in their church encoding.
andP :: Parser Builder
andP = do
  _ <- string "and"
  pure ands

-- Parser for or operator. Creates the builder λxy. if x True y for Or in their church encoding.
orP :: Parser Builder
orP = do
  _ <- string "or"
  pure ors

-- Parser for not operator. Creates the builder λx. if x False True for Not in their church encoding.
notP :: Parser Builder
notP = do
  _ <- spaces
  _ <- string "not"
  pure nots

-- The logical expression, calls orExpr first as or has the lowest precedence.
logicExpr :: Parser Builder
logicExpr =  orExpr

-- The or expression, chains andExpr all separated by 'or' operators.
orExpr :: Parser Builder
orExpr = chainB andExpr orP

-- The and expression, chains baseExpr all separated by 'and' operators.
andExpr :: Parser Builder
andExpr = chainB baseExpr andP

-- The not expression, calls ifExpr, if that fails calls negateExpr which is 1 or more nots followed by a logical expression
-- or boolean expression.
baseExpr :: Parser Builder
baseExpr = ifExpr ||| booleanExpr ||| negateExpr

-- For if statements, call the ifP to parse if statements and return the statement in then or else block based on condition.
ifExpr :: Parser Builder
ifExpr = ifP

-- An expression with any amount of repeating nots in front. Eg: not not False.
-- Will create a list of nots and the expression at the end, foldr to reduce the list of Builders into a Builder.
negateExpr :: Parser Builder
negateExpr = do
  negated <- list1 notP
  expr <- booleanExpr
  let
    negatedExpr = foldr ap expr negated
  pure negatedExpr

-- A boolean expression. Will either be True or False or a logical Expression within brackets.
-- Will be called last as brackets has the highest precedence.
booleanExpr :: Parser Builder
booleanExpr = (do
  _ <- spaces
  _ <- is '('
  _ <- spaces
  expr <- logicExpr
  _ <- spaces
  _ <- is ')'
  pure expr) |||
  trueP ||| falseP

-- Helper Functions to construct the Builder for logical equivalences in their church encoding.
-- If statements in church encoding.
ifs :: Builder
ifs = lam 'b' $ lam 't' $ lam 'f' $ (term 'b' `ap` term 't') `ap` term 'f'

-- True in church encoding.
trues :: Builder
trues = boolToLam True

-- False in church encoding.
falses :: Builder
falses = boolToLam False

-- Not in church encoding.
nots :: Builder
nots = 'x' `lam` ((ifs `ap` term 'x') `ap` falses) `ap` trues

-- And in church encoding.
ands :: Builder
ands = lam 'x' $ lam 'y' $ ((ifs `ap` term 'x') `ap` term 'y') `ap` falses

-- Or in church encoding.
ors :: Builder
ors = lam 'x' $ lam 'y' $ ((ifs `ap` term 'x') `ap` trues) `ap` term 'y'

-- The chainB operator taken from Tutorial Week 11.
-- Parses p 0 or more times all separated by an 'op'.
-- chainB is for Builders only and only for booleans.
-- Will enforce spaces between each boolean and logical operator.
chainB :: Parser Builder -> Parser Builder -> Parser Builder
chainB p op = p >>= rest
 where
  rest a =
    (do
        _ <- list1 space
        f <- op
        _ <- list1 space
        b <- p
        rest (f `ap` a `ap` b)
      )
      ||| pure a


-- IMPORTANT: The church encoding for boolean constructs can be found here -> https://tgdwyer.github.io/lambdacalculus/#church-encodings

-- | Parse a logical expression and returns in lambda calculus
-- >>> lamToBool <$> parse logicP "True and False"
-- Result >< Just False
--
-- >>> lamToBool <$> parse logicP "True and False or not False and True"
-- Result >< Just True
--
-- >>> lamToBool <$> parse logicP "not not not False"
-- Result >< Just True
--
-- >>> parse logicP "True and False"
-- Result >< (\xy.(\btf.btf)xy\_f.f)(\t_.t)\_f.f
--
-- >>> parse logicP "not False"
-- Result >< (\x.(\btf.btf)x(\_f.f)\t_.t)\_f.f
-- >>> lamToBool <$> parse logicP "if True and not False then True or True else False"
-- Result >< Just True

-- Will first remove any beginning whitespaces the call logicChain to parse a list of chained boolean expressions
-- Eg: True and False and True or False. logicChain will chain the two expressions separated by 'and'
-- If that fails, call logicExpr instead.
-- logicList will be a list of Builders as list1 will parse 1 or more. Foldr1 to reduce the logicList to one Builder.
logicP :: Parser Lambda
logicP = logicExpr >>= \logic -> pure $ build logic


-- | Exercise 2

-- Parses whether that character is a digit.
digit :: Parser Char
digit = satisfy isDigit

-- Parsers numbers. Will parse 1 or more digits and then use read to convert the string into integer.
-- Then call the numberBuilder to convert the numInt into a Parser Builder.
numberP :: Parser Builder
numberP = do
  _ <- spaces
  lstNumber <- list1 digit
  let
    numInt = read lstNumber
  numberBuilder numInt

-- Parses the plus symbol operator and returns the add Builder in church encoding form.
addP :: Parser Builder
addP = do
  _ <- spaces
  _ <- is '+'
  pure add

-- Parses the minus symbol operator and returns the minus Builder in church encoding form..
minusP :: Parser Builder
minusP = do
  _ <- spaces
  _ <- is '-'
  pure minus

-- Parses the multiply symbol operator and returns the multiply Builder in church encoding form..
multiplyP :: Parser Builder
multiplyP = do
  _ <- spaces
  _ <- is '*'
  pure multiply

--  Parses the exponent symbol operator and returns the exponent Builder in church encoding form..
exponentP :: Parser Builder
exponentP = do
  _ <- spaces
  _ <- string "**"
  pure LambdaParser.exp

-- Either a mathematical expression encased within brackets or a number.
-- Will be called last as brackets have the highest precedence.
terms :: Parser Builder
terms = (do
  _ <- spaces
  _ <- is '('
  expr <- allExpr
  _ <- is ')'
  pure expr) |||
  numberP

-- A function to parse an arithmetic operator ('+', '-', '*', '**')
arithmeticOp :: Parser Builder
arithmeticOp = exponentP ||| multiplyP ||| addP ||| minusP

-- A basic expression parser (Can only do addition and subtraction), call the addExprbasic to parse the basic arithmetic expressions.
basicExpr :: Parser Builder
basicExpr = addExprBasic

-- A basic parser which will only parse for numbers separated by either the '+' or '-' symbols.
addExprBasic :: Parser Builder
addExprBasic = chain numberP (addP ||| minusP)

-- All mathematical expressions. Will call addBasic first to do addition or subtraction as they have the lowest precedence.
allExpr :: Parser Builder
allExpr = addBasic

-- The addition expression. Will chain mulExpr either separated by '+' or '-'.
addBasic :: Parser Builder
addBasic = chain mulExpr (addP ||| minusP)

-- The multiplication expression. Will chain mulExpr separated by '*'
mulExpr :: Parser Builder
mulExpr = chain expExpr multiplyP

-- The exponent expression. Will chain mulExpr separated by "**"
expExpr :: Parser Builder
expExpr = chain terms exponentP

-- Helper Functions to construct the Builder for arithmetic operations in their church encoding.
-- Convert the numbers from Int to Builder.
numberBuilder :: Int -> Parser Builder
numberBuilder = pure . intToLam

-- Succ in Builder form
succ :: Builder
succ = lam 'n' $ lam 'f' $ lam 'x' $ term 'f' `ap` (term 'n' `ap` term 'f' `ap` term 'x')

-- Pred in Builder form.
pred :: Builder
pred = lam 'n' $ lam 'f' $ lam 'x' $ term 'n' `ap` lam 'g' (lam 'h' $ term 'h' `ap` (term 'g' `ap` term 'f')) `ap` lam 'u' (term 'x') `ap` lam 'u' (term 'u')

-- Add in Builder form.
add :: Builder
add = 'x' `lam` 'y' `lam` term 'y' `ap` LambdaParser.succ `ap` term 'x'

-- Minus in Builder form.
minus :: Builder
minus = 'x' `lam` 'y' `lam` term 'y' `ap` LambdaParser.pred `ap` term 'x'

-- Multiply in Builder form.
multiply :: Builder
multiply = 'x' `lam` 'y' `lam` 'f' `lam` term 'x' `ap` (term 'y' `ap` term 'f')

-- Exp in Builder form.
exp :: Builder
exp = 'x' `lam` 'y' `lam` term 'y' `ap` term 'x'

-- General chain function that will ignore all whitespaces between p and op.
chain :: Parser Builder -> Parser Builder -> Parser Builder
chain p op = p >>= rest
 where
  rest a =
    (do
        _ <- spaces
        f <- op
        _ <- spaces
        b <- p
        rest (f `ap` a `ap` b)
      )
      ||| pure a


-- | The church encoding for arithmetic operations are given below (with x and y being church numerals)

-- | x + y = add = λxy.y succ x
-- | x - y = minus = λxy.y pred x
-- | x * y = multiply = λxyf.x(yf)
-- | x ** y = exp = λxy.yx

-- | The helper functions you'll need are:
-- | succ = λnfx.f(nfx)
-- | pred = λnfx.n(λgh.h(gf))(λu.x)(λu.u)
-- | Note since we haven't encoded negative numbers pred 0 == 0, and m - n (where n > m) = 0

-- | Parse simple arithmetic expressions involving + - and natural numbers into lambda calculus
-- >>> lamToInt <$> parse basicArithmeticP "5 + 4"
-- Result >< Just 9
--
-- >>> lamToInt <$> parse basicArithmeticP "5 + 9 - 3 + 2"
-- Result >< Just 13
basicArithmeticP :: Parser Lambda
basicArithmeticP = basicExpr >>= \basic -> pure $ build basic

-- | Parse arithmetic expressions involving + - * ** () and natural numbers into lambda calculus
-- >>> lamToInt <$> parse arithmeticP "5 + 9 * 3 - 2**3"
-- Result >< Just 24
--
-- >>> lamToInt <$> parse arithmeticP "100 - 4 * 2**(4-1)"
-- Result >< Just 68
arithmeticP :: Parser Lambda
arithmeticP = allExpr >>= \complex -> pure $ build complex


-- | Exercise 3
-- Parser for greater than (<) symbol.
greaterThanP :: Parser Builder
greaterThanP = do
  _ <- spaces
  _ <- is '>'
  pure greaterThan

-- Parser for lesser than (>) symbol.
lesserThanP :: Parser Builder
lesserThanP = do
  _ <- spaces
  _ <- is '<'
  pure lesserThan

-- Parser for greater than (>=) symbol.
greaterThanEqualP :: Parser Builder
greaterThanEqualP = do
  _ <- spaces
  _ <- string ">="
  pure greaterThanEqual

-- Parser for lesser than (<=) symbol.
lesserThanEqualP :: Parser Builder
lesserThanEqualP = do
  _ <- spaces
  _ <- string "<="
  pure lesserThanEqual

-- Parser for lesser than (==) symbol.
equalP :: Parser Builder
equalP = do
  _ <- spaces
  _ <- string "=="
  pure equal

-- Parser for lesser than (!=) symbol.
notEqualP :: Parser Builder
notEqualP = do
  _ <- spaces
  _ <- string "!="
  pure notEqual

-- Parsers the operators between each expression in the chain.
-- Will parse (>=, <=, >, <, ==, !=), 'and' and 'or'
comparisonOp :: Parser Builder
comparisonOp = greaterThanEqualP ||| lesserThanEqualP |||
               greaterThanP ||| lesserThanP ||| equalP |||
               notEqualP ||| andP ||| orP

-- Will parse for expressions within brackets.
compBrac :: Parser Builder
compBrac = do
  _ <- spaces
  _ <- is '('
  _ <- spaces
  expr <- compBrac ||| chainComparison
  _ <- spaces
  _ <- is ')'
  pure expr

-- Chain arimethic expressions all separated by a comparisonOp
chainComparison :: Parser Builder
chainComparison = chain allExpr comparisonOp

-- Will either chain brackets comparisons or comparisons expressions.
chainCompExpr :: Parser Builder
chainCompExpr = do
  _ <- spaces
  chain compBrac comparisonOp ||| chainComparison

-- Helper functions to construct our comparisons with church encoding.
-- Church encoding for greater than (>)
greaterThan :: Builder
greaterThan = 'm' `lam` 'n' `lam` ands `ap` (greaterThanEqual `ap` term 'm' `ap` term 'n') `ap`
  (nots `ap` (equal `ap` term 'm' `ap` term 'n'))

-- Church encoding for greater than (<)
lesserThan :: Builder
lesserThan = 'm' `lam` 'n' `lam` ands `ap` (lesserThanEqual `ap` term 'm' `ap` term 'n') `ap`
  (nots `ap` (equal `ap` term 'm' `ap` term 'n'))

-- Church encoding for less than equal (>=)
greaterThanEqual :: Builder
greaterThanEqual = 'm' `lam` 'n' `lam` (nots `ap` (lesserThan `ap` term 'm' `ap` term 'n'))

-- Church encoding for less than equal (<=)
lesserThanEqual :: Builder
lesserThanEqual = 'm' `lam` 'n' `lam` isZero `ap` (minus `ap` term 'm' `ap` term 'n')

-- Church encoding for equal (==).
equal :: Builder
equal = 'm' `lam` 'n' `lam` ands `ap` ((lesserThanEqual `ap` term 'm' `ap` term 'n') `ap` (lesserThanEqual `ap` term 'n' `ap` term 'm'))

-- Church encoding for not equal (!=).
notEqual :: Builder
notEqual = 'm' `lam` 'n' `lam` (nots `ap` (equal `ap` term 'm' `ap` term 'n'))

-- isZero to check for the condition is zero.
isZero :: Builder
isZero = 'n' `lam` term 'n' `ap` ('x' `lam` falses) `ap` trues

-- | The church encoding for comparison operations are given below (with x and y being church numerals)

-- | x <= y = LEQ = λmn.isZero (minus m n)
-- | x == y = EQ = λmn.and (LEQ m n) (LEQ n m)
-- | The helper function you'll need is:
-- | isZero = λn.n(λx.False)True

-- >>> lamToBool <$> parse complexCalcP "9 - 2 <= 3 + 6"
-- Result >< Just True
--
-- >>> lamToBool <$> parse complexCalcP "15 - 2 * 2 != 2**3 + 3 or 5 * 3 + 1 < 9"
-- Result >< Just False
complexCalcP :: Parser Lambda
complexCalcP = chainCompExpr >>= \comparison -> pure $ build comparison


{-|
    Part 3
-}

-- | Exercise 1

-- Parses empty lists while ignoring any whitespaces outside and within it.
-- Will either parse "[]" or "null" and both are treated as empty lists.
emptyListP :: Parser Builder
emptyListP = (do
  _ <- is '['
  _ <- spaces
  _ <- is ']'
  pure emptyList) ||| (do
  _ <- string "null"
  pure emptyList)

-- Apply cons to the variable which is either a comparison expression., number/arithmetic expression or a boolean expression.
listVar :: Parser Builder
listVar = do
  _ <- spaces
  listsParser ||| (compBrac ||| chainComparison) ||| allExpr ||| logicExpr

-- This function is used for cases where there are more than 1 variable within a list.
-- Will parse for the variable and then a comma after it.
repeatedContent :: Parser Builder
repeatedContent = do
  var <- listVar
  _ <- spaces
  _ <- is ','
  pure var

-- Will cons the two inputs together.
-- Eg cons 1 [0] will output [1, 0]
consVar :: Builder -> Builder -> Builder
consVar = ap . (cons `ap`)

-- Parses list of arbitrary length.
-- Will call the repeatedContent to continue parsing all variables within the list separated by a ','.
-- As restContent will be a list of Builders, foldr it with the ap as function to use and the var as the initial value.
-- Apply the contentBuilder to the empty list church encoding at the end.
varsList :: Parser Builder
varsList = do
  _ <- is '['
  _ <- spaces
  restContent <- list1 (repeatedContent ||| listVar)
  _ <-spaces
  _ <- is ']'
  -- Foldr by cons it with the valriables on the right. Eg: [1, 2, 3] will first cons an empty list with 3. cons 3 []
  -- Then it will cons 2 to [3] to become [2, 3].
  -- lastly it will cons 1 to [2, 3] to get [1, 2, 3]
  let
    contentBuilder = foldr consVar emptyList restContent
  pure contentBuilder

-- Parses either an empty list/null, a list with fixed length 1 or a lists of arbitrary length.
listsParser :: Parser Builder
listsParser = do
  _ <- spaces
  varsList ||| emptyListP

-- A parser which will parse "isNull" and then parse for an empty list or a null and apply isNull to the list to construct the
-- church encoding.
isNullP :: Parser Builder
isNullP = do
  _ <- string "isNull"
  pure isNull

-- A parser which will parse "head", then parses for a list of arbitrary length and then apply the head church encoding to it.
headP :: Parser Builder
headP = do
  _ <- string "head"
  pure LambdaParser.head

-- A parser which will parse "tail", then parses for a list of arbitrary length and then apply the tail church encoding to it.
tailP :: Parser Builder
tailP = do
  _ <- string "tail" ||| string "rest"
  pure LambdaParser.tail

-- A parser which will parse for an empty list or a null and apply cons function to the list.
consP :: Parser Builder
consP = do
  _ <- string "cons"
  pure cons

-- Parses a list of Operators.
listOperators :: Parser Builder
listOperators = do
  _ <- list1 space
  isNullP ||| headP ||| tailP

-- Will parse one cons var only. Eg: cons 1 [] = [1]
oneCons :: Parser Builder
oneCons = do
  cons1 <- consP
  _ <- list1 space
  var1 <- listVar
  pure $ cons1 `ap` var1

-- Parsing 1 or more cons funtions. Eg: cons 1 cons 1 cons 1.
-- Will apply each cons with the variable that comes after it.
repeatedCons :: Parser Builder
repeatedCons = do
  _ <- list1 space
  cons1 <- consP
  _ <- list1 space
  var1 <- listVar
  pure $ cons1 `ap` var1

-- Will parses one or more operations performed on lists (head rest [1, 2, 3]).
-- A minimum on one space is enforced between each operator and the list.
-- Will use list listOperators for zero or more list operators (isNull, head or tail).
-- Or will use list repeatedCons for repeating cons.
-- Foldr is used to apply each list operator to the list. As it's right associative, the most right operator will be applied
-- to the list first. Then the second most right and so on.
-- Eventually, the list of list operators will be reduced down to one builder and returned.
listOpExpr :: Parser Builder
listOpExpr = do
  _ <- spaces
  ops1 <- oneCons ||| consP ||| isNullP ||| headP ||| tailP
  ops2 <- list (repeatedCons ||| listOperators)
  _ <- list1 space
  lists <- listsParser
  let
    opsBuilder = foldr ap lists ops2
  pure $ ops1 `ap` opsBuilder

-- Helper functions use in our list parsing using church encoding
-- Church encoding for empty list.
emptyList :: Builder
emptyList = 'c' `lam` 'n' `lam` term 'n'

-- Church encoding for isNull function.
isNull :: Builder
isNull = 'l' `lam` term 'l' `ap` ('h' `lam` 't' `lam` falses) `ap` trues

-- Church encoding for cons function.
cons :: Builder
cons = 'h' `lam` 't' `lam` 'c' `lam` 'n' `lam` term 'c' `ap` term 'h' `ap` (term 't' `ap` term 'c' `ap` term 'n')

-- Church encoding for head function.
head :: Builder
head = 'l' `lam` term 'l' `ap` ('h' `lam` 't' `lam` term 'h') `ap` falses

-- Church encoding for tail function.
tail :: Builder
tail = 'l' `lam` 'c' `lam` 'n' `lam` term 'l' `ap`
  ('h' `lam` 't' `lam` 'g' `lam` term 'g' `ap` term 'h' `ap` (term 't' `ap` term 'c')) `ap`
  ('t' `lam` term 'n') `ap` ('h' `lam` 't' `lam` term 't')


-- | The church encoding for list constructs are given below
-- | [] = null = λcn.n
-- | isNull = λl.l(λht.False) True
-- | cons = λhtcn.ch(tcn)
-- | head = λl.l(λht.h) False
-- | tail = λlcn.l(λhtg.gh(tc))(λt.n)(λht.t)
--
-- >>> parse listP "[]"
-- Result >< \cn.n
--
-- >>> parse listP "[True]"
-- Result >< (\htcn.ch(tcn))(\xy.x)\cn.n
--
-- >>> parse listP "[0, 0]"
-- Result >< (\htcn.ch(tcn))(\fx.x)((\htcn.ch(tcn))(\fx.x)\cn.n)
--
-- >>> parse listP "[0, 0"
-- UnexpectedEof
listP :: Parser Lambda
listP = listsParser >>= \listLambda -> pure $ build listLambda

-- >>> lamToBool <$> parse listOpP "head [True, False, True, False, False]"
-- Result >< Just True
--
-- >>> lamToBool <$> parse listOpP "head rest [True, False, True, False, False]"
-- Result >< Just False
--
-- >>> lamToBool <$> parse listOpP "isNull []"
-- Result >< Just True
--
-- >>> lamToBool <$> parse listOpP "isNull [1, 2, 3]"
-- Result >< Just False
listOpP :: Parser Lambda
listOpP = listOpExpr >>= \listOps -> pure $ build listOps


-- | Exercise 2

-- | Implement your function(s) of choice below!
-- | Factorial
-- | Fibbonaci
-- | Foldr

-- Parses for the string "factorial" followed by one or more spaces. Then parse for a number.
-- Apply the y combinator and the factorial church encoding to the number.
-- This will output the factorial of the input number n.
factP :: Parser Builder
factP = do
  _ <- spaces
  _ <- string "factorial"
  _ <- list1 space
  num <- numberP
  pure $ yCombinator `ap` factorial `ap` num

-- Helper Functions used in our factorial.
-- The factorial function in church encoding form.
factorial :: Builder
factorial = 'a' `lam` 'n' `lam` (isZero `ap` term 'n') `ap` one `ap` (multiply `ap` term 'n' `ap` (term 'a' `ap` (LambdaParser.pred `ap` term 'n')))

-- Y combinator in church encoding.
yCombinator :: Builder
yCombinator = 'f' `lam` ('x' `lam` term 'f' `ap` (term 'x' `ap` term 'x')) `ap` ('x' `lam` term 'f' `ap` (term 'x' `ap` term 'x'))


-- | Parse a number n to calculate the factorial of that number n,
-- | The output of this would be the factorial of n in lambda calculus.
--
-- Church encoding.
-- λan.(isZero n) (1) (multiply n (a (pred n)))
--
-- >> parse factorialP "factorial 4"
-- Result >< (\f.(\x.f(xx))\x.f(xx))(\an.(\b.b(\x_f.f)\t_.t)n(\f.f)((\xyf.x(yf))n(a((\bfx.b(\gh.h(gf))(\u.x)\u.u)n))))\fx.f(f(f(fx)))
-- 
-- >> lamToInt <$> parse factorialP "factorial 8"
-- Result >< Just 40320
--
-- >> lamToInt <$> parse factorialP "factorial 2"
-- Result >< Just 2
--
-- >> parse factorialP "factorial a"
-- UnexpectedChar 'a'
factorialP :: Parser Lambda
factorialP = factP >>= \fact -> pure $ build fact


-- A recurse function fibLoop which accepts 3 inputs, a b and n.
-- a b are accumulators and have the church encoding for 0 and 1 respectively initially. n is the input number for the Fibbonaci.
-- Base case of the recursion if n is <= 1. Will call itself with a being b, and b being the addition of a + b from previous call.
-- Decrement n each time by one to construct the sequence
-- Return the Fibbonaci Number of n when base case is reached.
recurseFib :: Builder -> Builder
recurseFib = fibLoop zero one
  -- A local function to recursively build up the Fibbonaci Number.
  where
    fibLoop a b n
      | lamToBoolF $ build $ lesserThanEqual `ap` n `ap` one = b
      | otherwise = fibLoop b (add `ap` a `ap` b) (minus `ap` n `ap` one)

-- Parses a single number and convert it into it's church encoding.
-- Then, call recurseFib to recursively create the Fibbonaci sequence and find the Fibbonaci number from n.
fibbonaci :: Parser Builder
fibbonaci = do
  _ <- spaces
  _ <- string "fibbonaci"
  _ <- list1 space
  num <- numberP
  let
    fibExpr = recurseFib num
  pure fibExpr

-- Helper functions used in our Fibonacci.
-- The number zero in church encoding form.
zero :: Builder
zero = intToLam 0

-- The number one in church encoding form.
one :: Builder
one = intToLam 1

-- The number two in church encoding form.
two :: Builder
two = intToLam 2

-- Converts the lambda expression to a Boolean.
lamToBoolF :: Lambda -> Bool
lamToBoolF = go . normal
  where
    go (LamAp (LamShow _) l)            = go l
    go (LamAbs _ (LamAbs _ (LamVar i)))
      | i <= 1    = i == 1


-- | Parse a number n to construct a fibbonaci sequence from it and returning a Fibonacci Number
-- | The output of this would be a lambda expression of the Fibonacci Number.
--
-- Church encoding.
-- λfn.(if (EQ n (λf.f)) (λf.f) (if (isZero n) (λfx.x) (plus (f(minus n λf.f)) (f(minus n λfx.f(fx))))
--
-- >> parse fibbonaciP "fibbonaci 3"
-- Result >< (\xy.y(\nfa.f(nfa))x)(\f.f)((\xy.y(\nfa.f(nfa))x)(\fx.x)\f.f)
-- 
-- >> lamToInt <$> parse fibbonaciP "fibbonaci 7"
-- Result >< Just 13
--
-- >> lamToInt <$> parse fibbonaciP "fibbonaci 10"
-- Result >< Just 55
--
-- >> parse fibbonaciP "a"
-- UnexpectedChar 'a'
fibbonaciP :: Parser Lambda
fibbonaciP = fibbonaci >>= \fib -> pure $ build fib


-- Will parse a list of arbitrary length and returns the list of Builders where each index is a element of the list in its
-- church encoding form.
foldrVarsList :: Parser [Builder]
foldrVarsList = do
  _ <- is '['
  _ <- spaces
  restContent <- list1 (repeatedContent ||| listVar)
  _ <-spaces
  _ <- is ']'
  pure restContent

-- Will parse a specific string "foldr" which will signify the start of the function.
-- After a space will be a function used to apply to all the variables within the list. In this case, the function could be
-- arithmetic operator ('+', '-', '*', '**') or a logical connective (`and`, `or`).
-- Parses an initial value, which can be er comparison (3 < 4), an arithmetic expression (3 + 4) or a boolean expression
-- (not, True, False).
-- Then it will parse for a list.
foldrList :: Parser Builder
foldrList = do
  _ <- spaces
  _ <- string "foldr"
  _ <- list1 space
  function <- arithmeticOp ||| andP ||| orP
  _ <- list1 space
  initialVar <- (compBrac ||| chainComparison) ||| allExpr ||| logicExpr
  _ <- list1 space
  lists <- foldrVarsList ||| list1 emptyListP
  -- Apply the foldr with the applyFunction method to apply the function to each and every variable in the list to reduce it
  -- to one value.
  let
    applyFunc = foldr (applyFunction function) initialVar lists
  pure applyFunc

-- Helper Functions used for our Foldr Parser.
-- Apply the function with the other input.
-- Eg: applyFunction addP \f.f, this will partially apply + to 1. So (+1) is the output.
applyFunction :: Builder -> Builder -> Builder -> Builder
applyFunction = (ap .) . ap
-- applyFunction f a b = f `ap` a `ap` b


-- | Parse a foldr function which parses a function, an initial value and a list.
-- | The function can either be an arithmetic operator ('+', '-', '*', '**') or a logical connective (`and`, `or`)
-- | The output of this would be a lambda expression of the Fibonacci Number.
--
-- >> parse foldrP "foldr + 0 []"
-- Result >< (\xy.y(\nfa.f(nfa))x)(\cn.n)\fx.x
-- 
-- >> lamToInt <$>  lamToInt <$>  parse foldrP "foldr * 2 [1, (1**2), 4 - 1]"
-- Result >< Just 6
--
-- >> lamToBool <$>  parse foldrP "foldr and True [True, not False, not (True and False)]"
-- Result >< Just True
--
-- lamToBool <$>  parse foldrP "foldr or if 1 < 2 then True else False [3 > 5, not False, 4 == 4]"  
-- Result >< Just True
--
-- >> parse foldrP "foldr + 1 1"  
-- UnexpectedChar '1'
foldrP :: Parser Lambda
foldrP = foldrList >>= \foldRight -> pure $ build foldRight