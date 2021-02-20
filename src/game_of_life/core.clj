(ns game-of-life.core
  (:require [clojure.string :as string])
  (:gen-class))

(def alive true)
(def dead false)
(defn alive? [cell] cell)
(def width 100)
(def height 50)
(def refresh-rate 24)  ;; 24 frames/sec

(defn cell-repr [cell] (if (alive? cell) "⬛" "⬜"))

(defn grid-repr
  "Get a string representation of the given grid."
  [grid]
  (string/join (for [x (range width)]
                    (string/join "\n" (for [y (range height)]
                                           (cell-repr (get-in grid [x y])))))))

(defn display [grid] (println (get-grid-repr grid) "\n"))

(defn count-alive-cells [cells] (reduce + (for [c cells :when (alive? c)] 1)))

(defn moore-neighborhood
  "Return all cells in Moore neighborhood, excluding cell itself."
  [grid x y]
  (let [steps [-1 0 1]]
   (for [dx steps dy steps :when (not (= [dx dy] [0 0]))] ;; exclude this cell
        (get-in grid [(+ x dx) (+ y dy)]))))

(defn alive-neighbors-grid
  "Given a grid, return a matrix counting alive neighbors for each cell."
  [grid]
  (into [] (for [x (range width)]
                (into [] (for [y (range height)]
                              (count-alive-cells
                               (moore-neighborhood grid x y)))))))

(defn cell-evolution
  "Get the next cell's state, either dead or alive."
  [cell n-neighbors]
  (let [is-alive (alive? cell)]
    (cond (and is-alive (or (< n-neighbors 2)
                            (> n-neighbors 3))) dead
          (and (not is-alive) (= n-neighbors 3)) alive
          :else is-alive))) ;; in any other case, cell state doesn't change

(defn new-grid
  "Build a new grid composed of alive and dead cells.

  If an existing grid is given, returns the evolution based on Conway's rules.
  Otherwise, returns a randomly created grid.
  "
  ([]
  (into [] (for [_ (range width)]
                (into [] (for [_ (range height)]
                                 (rand-nth [alive dead]))))))
  ([grid]
  (let [neighbors-grid (alive-neighbors-grid grid)]
    (into [] (for [x (range width)]
                  (into [] (for [y (range height)]
                                (cell-evolution
                                 (get-in grid [x y])
                                 (get-in neighbors-grid [x y])))))))))

(defn play
  [grid]
  (Thread/sleep (/ 1000 refresh-rate))
  (display grid)
  (new-grid grid))

(defn -main [] (doall (iterate play (new-grid))))

