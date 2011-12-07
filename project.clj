(defproject mysqlcli "1.0.0-SNAPSHOT"
  :description "a better mysql cli"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [org.clojure/java.jdbc "0.0.7"]]
  :main mysqlcli.core)
