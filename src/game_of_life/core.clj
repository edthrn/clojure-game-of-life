(ns game-of-life.core
  (:gen-class))
(require '[clojure.string :as string])

(declare
  play
  display
  new-grid
  cell-repr
  grid-repr
  cell-evolution
  generate-booleans
  alive-neighbors-matrix
  cell-moore-neighborhood
  alive-cells-in-neighborhood)

(def random (java.util.Random.))
(def width 50)
(def height 50)
(def alive true)
(def dead false)
(def refresh-rate 2)  ;; 2 frames/sec

(defn -main [] (doall (iterate play (new-grid))))

(defn play
  [grid]
  (Thread/sleep (/ 1000 refresh-rate))
  (display grid)
  (new-grid grid))

(defn display [grid] (println (grid-repr grid) "\n"))

(defn new-grid
  "Build a new grid composed of alive and dead cells.

  If an existing grid is given, returns the evolution based on Conway's rules.
  Otherwise, returns a randomly created grid.
  "
  ([] (vec (repeatedly width #(vec (generate-booleans height)))))
  ([old-grid]
    (let [alive-neighbors-grid (alive-neighbors-matrix old-grid)]
    (vec (for [x (range width)]
      (vec (for [y (range height)]
              (cell-evolution
                (get-in old-grid [x y])
                (get-in alive-neighbors-grid [x y])))))))))

(defn generate-booleans [n] (repeatedly n #(. random nextBoolean)))

(defn alive-neighbors-matrix
  "Given a grid, return a matrix counting alive neighbors for each cell."
  [grid]
  (vec
    (for [x (range width)]
         (vec (for [y (range height)]
                   (alive-cells-in-neighborhood (cell-moore-neighborhood grid x y)))))))

(defn cell-moore-neighborhood
  "Get all cells in given cell's Moore neighborhood, excluding cell itself."
  [grid x y]
  (let [steps [-1 0 1]] steps
    (for
      [dx steps dy steps :when (not (= [dx dy] [0 0]))] ; exclude cell itself
      (get-in grid [(+ x dx) (+ y dy)]))))

(defn alive-cells-in-neighborhood
  [neighbors]
  (reduce + (for [neighbor neighbors :when (true? neighbor)] 1)))

(defn cell-evolution
  "Get the next cell's state, either dead or alive."
  [alive? alive-neighbors-count]
  (cond
    (and alive? (or (< alive-neighbors-count 2) (> alive-neighbors-count 3))) dead
    (and (not alive?) (= alive-neighbors-count 3)) alive
    :else alive?)) ; the cell keeps its previous state

(defn grid-repr
  "Get a string representation of the given grid."
  [grid]
  (string/join
    "\n"
    (for [y (range height)]
      (string/join
        ""
        (for [x (range width)] (cell-repr (get-in grid [x y])))))))

(defn cell-repr [alive?] (if alive? "⬛" "⬜"))
