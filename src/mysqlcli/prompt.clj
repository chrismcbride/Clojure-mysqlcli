(ns mysqlcli.prompt)

(defn- show-prompt [dbName]
  (print (str dbName "> "))
  (flush)
  (read-line))

(defn- parse-input [inputStr]
  (if-let [[_ newDbName] (re-find  #"\\c (\w+)" inputStr)]
    {:type :CHANGEDB, :value newDbName}
    {:type :QUERY, :value inputStr}))

(defn get-input-from-user [dbName]
  (parse-input (show-prompt dbName)))
