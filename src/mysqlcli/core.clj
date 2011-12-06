(ns mysqlcli.core
  (:require [clojure.java.jdbc :as sql]))

(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname "//192.168.56.10:3306/rjmadmin"
         :user "cljruser"
         :password "password"})

(defn sql-query [^String query-string func]
  (sql/with-connection db
     (sql/with-query-results rows
       [query-string]
         (func rows))))

(defn select-query [^String query-string]
  (sql-query query-string
    (fn [rows]
      (println (keys (first rows)))
      (doseq [row rows] (println (vals row))))))

(defn select-query-vertical [^String query-string]
  (sql-query query-string
    (fn [rows]
      (doseq [row rows]
        (println (apply str (repeat 60 "-")))
        (doseq [field row]
          (println field))))))
