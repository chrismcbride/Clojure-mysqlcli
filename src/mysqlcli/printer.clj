(ns mysqlcli.printer)

(defn print-select-query [rows]
  (println (keys (first rows)))
  (doseq [row rows] (println (vals row))))

(defn print-select-query-vertical [rows]
  (doseq [row rows]
    (println (apply str (repeat 60 "-")))
    (doseq [field row]
      (println field))))
