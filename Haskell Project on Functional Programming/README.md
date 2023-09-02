## Using the code bundle

`stack build`

Builds the packages.

`stack run`

Builds an executable that runs the main function in app/Main.hs.

`stack test`

Builds the packages and executes doctests on all hs files in the submission folder.

`stack clean --full`

Removes unnecessary build files to reduce bundle size.

## Troubleshooting

`/usr/bin/ld.gold: error: cannot find -lgmp`

Run `sudo apt-get install libgmp3-dev`

## About

### Haskell Parsers
This project was an enriching learning journey for me, as it allowed me to dive deep into the world of parsing and understand how parsers work behind the scenes. I built parsers for various data formats and languages using Haskell, which sharpened my Haskell programming skills and gave me a deep insight into the nuances of parsing techniques and combinators. This project challenged me to think critically and creatively while solving parsing problems, which significantly enhanced my problem-solving abilities. Additionally, it introduced me to the power of functional programming for creating elegant and robust parsers, ultimately broadening my knowledge and skills in both Haskell and parsing concepts. Not to mention adding another language skillset under my belt.
