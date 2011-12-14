(ns mysqlcli.datatypes)

(defrecord Query [queryType string isVertical])

(defrecord Command [cmdType string])
