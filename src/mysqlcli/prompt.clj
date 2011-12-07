(ns mysqlcli.prompt)

(defn show-prompt []
  (print "> ")
  (flush)
  (read-line))
