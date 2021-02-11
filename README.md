# Conway's Game of Life

A Clojure implementation of John Conway's [Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).

This is my very first Clojure project, please expect it to be shitty.

## Usage

You must have a JVM (at least version 8) installed on your machine. The JRE is enough.

- Clone this repo and `cd` to the root dir

  ```bash
  git clone https://github.com/edthrn/clojure-game-of-life.git
  cd clojure-game-of-life
  ```
- Compile the source to a portable `JAR` file. Example (using [Leiningen](https://github.com/technomancy/leiningen))

  ```bash
  lein ubjerjar
  ```

- Run the game

  ```bash
  java -jar target/uberjar/game-of-life-0.1.0-standalone.jar
  ```

## To be fixed

- **Poor code style**

  Despite reading https://guide.clojure.style I'm struggling to enforce a consistent formatting, most notably for line breaks and indentation.

- **Un-logic order of functions make it difficult to read**

  For the program to be easily readable, it'd be better to have the "higher-level" functions such as `-main` or `play` first, and then the actual mechanics. However, doing so make the compiling process to fail because of unknown reference.
  Apparently, using `declare` beforehand [should be avoided](https://guide.clojure.style/#forward-references).


## License

Published under the [MIT license](https://en.wikipedia.org/wiki/MIT_License). Refer to the `LICENSE` file for further details.
