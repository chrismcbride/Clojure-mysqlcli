(ns mysqlcli.prompt)

(defn- show-prompt [dbName]
  (print (str dbName "> "))
  (flush)
  (read-line))

(defn- parse-input [inputStr]
  (if-let [[_ newDbName] (re-find  #"\\c (\w+)" inputStr)]
    {:type :CHANGEDB, :value newDbName}
    (if (re-find #"\\g.*$" inputStr)
      {:type :QUERY, :value (clojure.string/replace inputStr #"\\g.*$" ""), :vertical true}
      {:type :QUERY, :value inputStr, :vertical false})))

(defn get-input-from-user [dbName]
  (parse-input (show-prompt dbName)))
