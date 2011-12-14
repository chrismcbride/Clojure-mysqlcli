(ns mysqlcli.core
  (:use   [mysqlcli.macros])
  (:require [clojure.java.jdbc :as sql]
            [mysqlcli.printer :as printer]
            [mysqlcli.prompt :as prompt]))

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

(defmulti execute-input class)

(defmethod execute-input mysqlcli.datatypes.Command [command]
  (changeDatabase (:string command)))

(defmethod execute-input mysqlcli.datatypes.Query [query]
  (let [func (partial run-sql-query (:string query))]
    (if (:isVertical query)
      (func printer/print-select-query-vertical)
      (func printer/print-select-query))))

(defn -main [& args]
  (forever (try (execute-input (prompt/get-input-from-user @dbName))
              (catch Exception e 
                (println (str "ERROR: " (.getMessage e)))))))
