(defproject game-of-life "0.1.0"
  :description "A Clojure implementation of Conway's Game of Life."
  :url "http://github.com/edthrn/clojure-game-of-life"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :main game-of-life.core
  :aot [game-of-life.core])
