(ns game-of-life.core
  (:require [clojure.string :as string])
  (:gen-class))

(def random (java.util.Random.))
(def width 50)
(def height 50)
(def refresh-rate 2)  ;; 2 frames/sec

;; A cell is just a boolean value for now.
(def alive true)
(def dead false)

(defn alive? [cell] cell)

(defn get-cell-repr [cell] (if (alive? cell) "⬛" "⬜"))

(defn get-grid-repr
  "Get a string representation of the given grid."
  [grid]
  (string/join
   "\n"
   (for
    [y (range height)]
    (string/join
     (for
      [x (range width)]
      (get-cell-repr (get-in grid [x y])))))))

(defn display [grid] (println (get-grid-repr grid) "\n"))

(defn generate-booleans [n] (repeatedly n #(. random nextBoolean)))

(defn count-alive-cells [cells] (reduce + (for [c cells :when (alive? c)] 1)))

(defn get-cells-in-moore-neighborhood
  "Get all cells in given cell's Moore neighborhood, excluding cell itself."
  [grid x y]
  (let [steps [-1 0 1]]
    (for
     [dx steps dy steps :when (not (= [dx dy] [0 0]))] ;; exclude cell itself
     (get-in grid [(+ x dx) (+ y dy)]))))

(defn get-alive-neighbors-grid
  "Given a grid, return a matrix counting alive neighbors for each cell."
  [grid]
  (vec (for [x (range width)] (vec (for [y (range height)]
                                        (count-alive-cells
                                         (get-cells-in-moore-neighborhood
                                          grid x y)))))))

(defn get-cell-evolution
  "Get the next cell's state, either dead or alive."
  [cell alive-neighbors-count]
  (let [living? (alive? cell)]
    (cond (and living? (or (< alive-neighbors-count 2)
                           (> alive-neighbors-count 3))) dead
          (and (not living?) (= alive-neighbors-count 3)) alive
          :else living?))) ;; in any other case, cell state doesn't change

(defn get-next-grid
  "Build a new grid composed of alive and dead cells.

  If an existing grid is given, returns the evolution based on Conway's rules.
  Otherwise, returns a randomly created grid.
  "
  ([] (vec (repeatedly width #(vec (generate-booleans height)))))
  ([previous-grid]
  (let [alive-neighbors-grid (get-alive-neighbors-grid previous-grid)]
    (vec (for
          [x (range width)]
          (vec (for
                [y (range height)]
                (get-cell-evolution
                  (get-in previous-grid [x y])
                  (get-in alive-neighbors-grid [x y])))))))))

(defn play
  [grid]
  (Thread/sleep (/ 1000 refresh-rate))
  (display grid)
  (get-next-grid grid))

(defn -main [] (doall (iterate play (get-next-grid))))
