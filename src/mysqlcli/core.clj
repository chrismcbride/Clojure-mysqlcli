(ns mysqlcli.core
  (:use   [mysqlcli.macros])
  (:require [clojure.java.jdbc :as sql]
            [mysqlcli.printer :as printer]
            [mysqlcli.prompt :as prompt])
  (:gen-class))

(def- dbName (ref "rjmadmin"))

(defn- get-connection-info []
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname (str "//192.168.56.10:3306/" @dbName)
   :user "cljruser"
   :password "password"})
  
(defn- run-sql-query [query-string func]
  (sql/with-connection (get-connection-info)
     (sql/with-query-results rows
       [query-string]
         (func rows))))

(defn- changeDatabase [newDbNameStr]
  (dosync
    (ref-set dbName newDbNameStr)))

(defmulti execute-input :type)

(defmethod execute-input :CHANGEDB [command]
  (changeDatabase (:value command)))

(defmethod execute-input :QUERY [query]
  (let [func (partial run-sql-query (:value query))]
    (if (:vertical query)
      (func printer/print-select-query-vertical)
      (func printer/print-select-query))))

(defn -main [& args]
  (forever (try (execute-input (prompt/get-input-from-user @dbName))
              (catch Exception e 
                (println (str "ERROR: " (.getMessage e)))))))
