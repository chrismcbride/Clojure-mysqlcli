(ns mysqlcli.core
  (:use   [mysqlcli.macros])
  (:require [clojure.java.jdbc :as sql]
            [mysqlcli.prompt :as prompt])
  (:gen-class))

(def- dbName (ref "rjmadmin"))

(defn- get-connection []
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname (str "//192.168.56.10:3306/" @dbName)
   :user "cljruser"
   :password "password"})
  
(defn- run-sql-query [query-string func]
  (sql/with-connection (get-connection)
     (sql/with-query-results rows
       [query-string]
         (func rows))))

(defn- print-select-query [query-string]
  (run-sql-query query-string
    (fn [rows]
      (println (keys (first rows)))
      (doseq [row rows] (println (vals row))))))

(defn- print-select-query-vertical [query-string]
  (run-sql-query query-string
    (fn [rows]
      (doseq [row rows]
        (println (apply str (repeat 60 "-")))
        (doseq [field row]
          (println field))))))

(defn- changeDatabase [newDbNameStr]
  (dosync
    (ref-set dbName newDbNameStr)))

(defmulti execute-input :type)

(defmethod execute-input :CHANGEDB [command]
  (changeDatabase (:value command)))

(defmethod execute-input :QUERY [query]
  (print-select-query (:value query)))

(defn -main [& args]
  (forever (try (execute-input (prompt/get-input-from-user @dbName))
              (catch Exception e 
                (println (str "ERROR: " (.getMessage e)))))))
