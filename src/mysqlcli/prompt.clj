(ns mysqlcli.prompt
  (:require [mysqlcli.datatypes :as datatype]))

(defn- show-prompt [dbName]
  (print (str dbName "> "))
  (flush)
  (read-line))

(defn- parse-input [inputStr] ;this function is temporarily gross
  (if-let [[_ newDbName] (re-find  #"^\\c (\w+)" inputStr)]
    (datatype/map->Command {:cmdType :CHANGEDB, :string newDbName})
    (if (re-find #"\\g\s*$" inputStr)
      (datatype/map->Query {:string (clojure.string/replace inputStr #"\\g\s*$" ""), :isVertical true})
      (datatype/map->Query {:string inputStr, :isVertical false}))))

(defn get-input-from-user [dbName]
  (parse-input (show-prompt dbName)))
